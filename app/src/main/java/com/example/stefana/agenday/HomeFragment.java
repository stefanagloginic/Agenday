package com.example.stefana.agenday;


import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {
    //request code for launching resolution request
    private static final int REQUEST_RESOLVE_ERROR = 1002;
    //TAG for fragment
    private static final String DIALOG_ERROR = "dialog_error";
    //bool track if error is being resolved currently
    private boolean resolvingConnectionError = false;
    //a tag to identify boolean resolvingConnectionError from Bundle
    private static final String STATE_RESOLVING_CONNECTION_ERROR = "resolving_error";


    private RecyclerView recyclerView;
    private MyAdapter adapter;
    private Context context;
    private GoogleApiClient googleApiClient;


    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //need to save boolean resolvingConnectionError state
        //when user rotates phone and orientation changes
        outState.putBoolean(STATE_RESOLVING_CONNECTION_ERROR, resolvingConnectionError);

    }

    @Override
    public void onAttach(Context context) {
        //make sure fragment is attached before trying to access context
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        //first grab anything from savedInstanceStateBundle
        //in this case resolvingConnectionError's State
        if(savedInstanceState != null){
            resolvingConnectionError = savedInstanceState.getBoolean(STATE_RESOLVING_CONNECTION_ERROR);
        }

        //going to create an instance of the googleapiclient
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(context)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }


        View view = inflater.inflate(R.layout.fragment_home, container, false);
        //now I will create the lists for the home schedule
        //a recyclerView requires a layout and an adapter.

        recyclerView = (RecyclerView) view.findViewById(R.id.rv);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context)); //might want to use itemanimator soon (will allow swipe feature)

        //access drawable to create divider on below cardviews
        Drawable dividerDrawable = ContextCompat.getDrawable(getContext(), R.drawable.dividerdrawable);

        //crate a decorator and add it to recyclerview
        RecyclerView.ItemDecoration dividerItemDecoration = new DividerItemDecoration(dividerDrawable);
        recyclerView.addItemDecoration(dividerItemDecoration);

        return view;
    }

    @Override
    public void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    private void initializeData(ArrayList<FakeCard> fakecards) {
        fakecards.add(new FakeCard("Coldplay playing in CA, LA on Monday", "Concert"));
        fakecards.add(new FakeCard("Enjoy our Greek cuisine in San Fran", "Greek Restaurant"));
        fakecards.add(new FakeCard("Torture yourself at the stairs of hell in Culver City", "Hike"));
        fakecards.add(new FakeCard("Enjoy paradise", "Vacation"));

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        //make sure you have access to coarse location
        if (ContextCompat.checkSelfPermission(context,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
            if (lastLocation != null) {
                //run a search for items that should be on cardview
                ArrayList<FakeCard> fakecard = new ArrayList<>();
                initializeData(fakecard);

                fakecard.add(new FakeCard("Latitude: " + Double.toString(lastLocation.getLatitude())
                        , "Longitude: " + Double.toString(lastLocation.getLongitude())));

                adapter = new MyAdapter(fakecard);
                recyclerView.setAdapter(adapter);

            } else {
                //googleapi could not get user location
                //inflate LocationDeniedFragment
            }
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        if(resolvingConnectionError){
            //if the application is already trying to resolve error dont want loop
            return;
        }else if(connectionResult.hasResolution()){
            //if this error has a resolution  then allow proper procedure
            try {
                resolvingConnectionError = true;
                connectionResult.startResolutionForResult(getActivity(), REQUEST_RESOLVE_ERROR);
            } catch (IntentSender.SendIntentException e) {
                // There was an error with the resolution intent. Try again.
                googleApiClient.connect();
            }
        }else{
            //display error message
            showErrorDialog(connectionResult.getErrorCode());
            resolvingConnectionError = true;
        }


    }

    private void showErrorDialog(int errorCode) {
        //build dialog for error message
        ErrorDialogFragment dialogFragment = new ErrorDialogFragment();
        // Pass the error that should be displayed
        Bundle args = new Bundle();
        args.putInt(DIALOG_ERROR, errorCode);
        dialogFragment.setArguments(args);
        dialogFragment.show(getChildFragmentManager() ,"errordialog");
    }

    public void onErrorDialogDismissed(){
        resolvingConnectionError = false;
    }


    public static class ErrorDialogFragment extends DialogFragment {

        public ErrorDialogFragment(){}

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            int errorCode = this.getArguments().getInt(DIALOG_ERROR);
            return GoogleApiAvailability.getInstance().getErrorDialog(
                    this.getActivity(), errorCode, REQUEST_RESOLVE_ERROR);
        }

        @Override
        public void onDismiss(DialogInterface dialog) {
            super.onDismiss(dialog);
            ((HomeFragment)getParentFragment()).onErrorDialogDismissed();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_RESOLVE_ERROR) {
            resolvingConnectionError = false;
            if (resultCode == getActivity().RESULT_OK) {
                // Make sure the app is not already connected or attempting to connect
                if (!googleApiClient.isConnecting() &&
                        !googleApiClient.isConnected()) {
                    googleApiClient.connect();
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}

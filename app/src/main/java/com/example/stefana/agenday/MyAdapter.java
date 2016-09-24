package com.example.stefana.agenday;

import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Stefana on 9/12/2016.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    public List<FakeCard> fakecard; //give access to list of events

    public MyAdapter(List<FakeCard> fakecard){
        this.fakecard = fakecard; //in constructor bind adapter to list of events (aka cards)
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //later i can set the views size parameters etc....(aesthetic of the card)
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false));
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) { //here i am just
        holder.description.setText(fakecard.get(position).description);
        holder.title.setText(fakecard.get(position).title);
       // holder.rating.setText(fakecard.get(position).rating);

        holder.image.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                showPopupMenu(holder.image, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return fakecard.size(); //change this to amount of cards (events) currently present
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    //ViewHolder objects contain the info from the card to preclude repeated findViewById lookups
    public static class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
       // TextView rating;
        TextView description;
        public ImageView image;

        public ViewHolder(View v){
            super(v);
            title = (TextView) v.findViewById(R.id.title); //need to have a title in the XML...
            //rating = (TextView) v.findViewById(R.id.rating);
            description = (TextView) v.findViewById(R.id.description);
            //obtaining the overflow button
            image = (ImageButton) v.findViewById(R.id.imageButton);
        }
    }

    private void showPopupMenu(View view, int position){
        //create the menu and inflater...then display
        PopupMenu popup = new PopupMenu(view.getContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.overflow_menu, popup.getMenu());

        //when something within the menu is clicked..defines what happens next
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener(){
            @Override
            public boolean onMenuItemClick(MenuItem item){
                switch(item.getItemId()) {
                    case R.id.add_to_schedule:
                        return true;
                }
                return false;
            }
        });
        popup.show();
    }
}

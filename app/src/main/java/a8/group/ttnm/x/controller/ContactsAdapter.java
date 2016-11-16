package a8.group.ttnm.x.controller;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;
import java.util.zip.Inflater;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.model.Contact;

/**
 * Created by LittleDragon on 11/15/2016.
 */

public class ContactsAdapter extends RecyclerView.Adapter<ContactsAdapter.ViewHolder> {

    private final static int FADE_DURATION = 500 ;// in milliseconds
    // Store a member variable for the contacts
    private List<Contact> listContacts ;
    // Store the context for easy access
    private Context mContext ;
    private Context getContext(){
        return mContext ;
    }

    public ContactsAdapter(Context mContext , List<Contact> listContacts){
        this.mContext = mContext ;
        this.listContacts = listContacts ;
    }

    // Usually involves inflating a layout from XML and returning the holder
    @Override
    public ContactsAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = LayoutInflater.from(context);

        View contactView = inflater.inflate(R.layout.item_contacts,parent,false);
        ViewHolder viewHolder = new ViewHolder(contactView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(ContactsAdapter.ViewHolder holder, int position) {
        // Get the data model based on position
        Contact contact = listContacts.get(position);

        //set image
        ImageView profileContact = holder.profileContact ;
        profileContact.setImageURI(contact.getUriImageContact());
        Log.d("HUYNH Adapter : " , contact.getUriImageContact().toString());

        //set name
        TextView nameContact = holder.nameContact ;
        nameContact.setText(contact.getNameContact());

        //set Number
        TextView numberContact = holder.numberContact ;
        numberContact.setText(contact.getNumberContact());

        //setFadeAnimation(holder.itemView);


    }

    private void setFadeAnimation(View view) {
        AlphaAnimation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(FADE_DURATION);
        view.startAnimation(anim);
    }

    @Override
    public int getItemCount() {
        return listContacts.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        public ImageView profileContact ;
        public TextView nameContact ;
        public TextView numberContact ;

        public ViewHolder(View itemView){
            super(itemView);
            profileContact = (ImageView)itemView.findViewById(R.id.listview_image);
            nameContact = (TextView) itemView.findViewById(R.id.listview_item_title);
            numberContact = (TextView) itemView.findViewById(R.id.listview_item_description);
        }

    }
}

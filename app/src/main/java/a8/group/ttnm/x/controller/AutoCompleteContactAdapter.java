package a8.group.ttnm.x.controller;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.model.Contact;

/**
 * Created by LittleDragon on 11/16/2016.
 */

public class AutoCompleteContactAdapter extends ArrayAdapter<Contact>{
    Context context ;
    int resource ;
    List<Contact> contacts , tempContacts , suggestion ;

    public AutoCompleteContactAdapter(Context context , int resource ,List<Contact> contacts){
        super(context,resource , contacts);
        this.contacts = contacts ;
        this.context = context ;
        this.resource = resource ;

        // tạo mảng mới ko tham chiếu
        tempContacts = new ArrayList<Contact>(contacts);
        suggestion = new ArrayList<Contact>();

    }

    @Override
    public View getView(int position , View convertView , ViewGroup parentView){
        Log.d("HUYNH","begin get view");
        View view = convertView ;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_contacts,parentView,false);
        }
        Contact contact = contacts.get(position);
        if(contact != null){
            ImageView profileContact = (ImageView)view.findViewById(R.id.listview_image);
            TextView nameContact = (TextView)view.findViewById(R.id.listview_item_title);
            TextView numberContact = (TextView)view.findViewById(R.id.listview_item_description);

            profileContact.setImageURI(contact.getUriImageContact());
            nameContact.setText(contact.getNameContact());
            numberContact.setText(contact.getNumberContact());
        }
        Log.d("HUYNH","end get view");
        return view ;
    }

    @Override
    public Filter getFilter(){
       return contactsFilter ;
    }
    Filter contactsFilter = new Filter() {

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            String str = ((Contact) resultValue).getNameContact();
            return str;
        }

        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            if (constraint != null) {
                suggestion.clear();
                for (Contact contact : contacts) {
                    if(constraint.toString().equals(" ")) suggestion.add(contact);
                    else if (contact.getNameContact().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestion.add(contact);
                    }
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = suggestion;
                filterResults.count = suggestion.size();
                return filterResults;
            } else {
                return new FilterResults();
            }
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            List<Contact> filterList = (ArrayList<Contact>) results.values;
            if (results != null && results.count > 0) {
                clear();
                for (Contact contact : filterList) {
                    add(contact);
                    notifyDataSetChanged();
                }
            }
        }
    };

}

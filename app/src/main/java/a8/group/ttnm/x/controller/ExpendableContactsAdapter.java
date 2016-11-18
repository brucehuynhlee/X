package a8.group.ttnm.x.controller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ExpandListOption;

/**
 * Created by LittleDragon on 11/16/2016.
 */

public class ExpendableContactsAdapter extends BaseExpandableListAdapter{

    private ArrayList<ExpandListOption> listOptions ;
    private Context mContext ;
    private List<Contact> listContacts ;

    public ExpendableContactsAdapter(Context mContext , List<Contact> listContacts){
        this.mContext = mContext ;
        this.listContacts = listContacts ;
        listOptions = new ArrayList<ExpandListOption>();
        listOptions.add(new ExpandListOption(Unity.getUriToDrawable(mContext,android.R.drawable.ic_menu_call),"Gọi"));
        listOptions.add(new ExpandListOption(Unity.getUriToDrawable(mContext,android.R.drawable.ic_menu_info_details),"Xem chi tiết"));
        listOptions.add(new ExpandListOption(Unity.getUriToDrawable(mContext,android.R.drawable.ic_menu_edit),"Sửa liên hệ"));
        listOptions.add(new ExpandListOption(Unity.getUriToDrawable(mContext,android.R.drawable.ic_menu_delete),"Xóa liên hệ"));
    }

    @Override
    public int getGroupCount() {
        return listContacts.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        //fix children later
        return listOptions.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return listContacts.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        //fix children later
        return listOptions.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        //fix children later
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        Contact contact = (Contact)getGroup(groupPosition);
        if(convertView == null){
            LayoutInflater layoutInflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.item_contacts,null);

        }
        if(contact != null){
            ImageView profileContact = (ImageView)convertView.findViewById(R.id.listview_image);
            TextView nameContact = (TextView)convertView.findViewById(R.id.listview_item_title);
            TextView numberContact = (TextView)convertView.findViewById(R.id.listview_item_description);

            profileContact.setImageURI(contact.getUriImageContact());
            nameContact.setText(contact.getNameContact());
            numberContact.setText(contact.getNumberContact());
        }
        return convertView ;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_child_contacts,null);

        }
        ExpandListOption option = listOptions.get(childPosition);
        ImageView image = (ImageView)convertView.findViewById(R.id.imageOption);
        TextView txtNumber = (TextView)convertView.findViewById(R.id.txtOptionContact);
        image.setImageURI(option.getUriImage());
        txtNumber.setText(option.getOption());
        return convertView ;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}

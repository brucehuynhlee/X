package a8.group.ttnm.x.view;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ExpandableListView;
import java.util.List;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.ExpendableContactsAdapter;
import a8.group.ttnm.x.model.Contact;
import a8.group.ttnm.x.model.ContactsFactory;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ContactsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ContactsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private static final String TAG = "LIST_CONTACTS";
    private static final int ADD_CONTACTS = 100;
    private static final int EDIT_CONTACTS = 101;
    private static final int MENU_CALL = 0;
    private static final int MENU_EDIT = 1;
    private static final int MENU_DELETE = 2;
    private static final String[] menuItem = {"Call","Edit","Delete"};
    private FloatingActionButton btnContact ;
    private OnFragmentInteractionListener mListener;
    //ListView listContacts ;
    ExpandableListView listContacts ;
    ContactsFactory contactsFactory ;
    //ContactsAdapter contactsAdapter ;
    ExpendableContactsAdapter contactsAdapter ;
    List<Contact> listDataContact ;




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }


    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contacts, container, false);
        //listContacts = (ListView) view.findViewById(R.id.listContacts);
        listContacts = (ExpandableListView) view.findViewById(R.id.listContacts);
        DisplayMetrics metrics = new DisplayMetrics();
        this.getActivity().getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        listContacts.setIndicatorBounds(width - GetPixelFromDips(50), width - GetPixelFromDips(10));

        listDataContact = ContactsFactory.getInstanceContactsFactory(this.getContext()).contact ;
        contactsAdapter = new ExpendableContactsAdapter(this.getContext(),listDataContact);
        listContacts.setAdapter(contactsAdapter);
        listContacts.setOnChildClickListener(newChildClick);

        btnContact = (FloatingActionButton) view.findViewById(R.id.fabContacts);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditContact.class);
                startActivityForResult(intent,ADD_CONTACTS);
            }
        });


        registerForContextMenu(listContacts);
        return view;
    }

    //method to expand all groups
    private void expandAll() {
        int count = contactsAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listContacts.expandGroup(i);
        }
    }

    //method to collapse all groups
    private void collapseAll() {
        int count = contactsAdapter.getGroupCount();
        for (int i = 0; i < count; i++){
            listContacts.collapseGroup(i);
        }
    }

    ExpandableListView.OnChildClickListener newChildClick = new ExpandableListView.OnChildClickListener() {
        @Override
        public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
            switchMenu(groupPosition,childPosition);
            return  true ;
        }
    };

    public int GetPixelFromDips(float pixels) {
        // Get the screen's density scale
        final float scale = getResources().getDisplayMetrics().density;
        // Convert the dps to pixels, based on density scale
        return (int) (pixels * scale + 0.5f);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == ADD_CONTACTS) {
            if(resultCode == Activity.RESULT_OK){
                contactsAdapter.notifyDataSetChanged();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        if(v.getId() == R.id.listContacts){
            ExpandableListView.ExpandableListContextMenuInfo infoAdapter = (ExpandableListView.ExpandableListContextMenuInfo) menuInfo ;
            menu.setHeaderTitle(listDataContact.get((int)infoAdapter.packedPosition).getNameContact());
            for (int i = 0; i < menuItem.length ; i++) {
                menu.add(Menu.NONE, i, i, menuItem[i]);
            }
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        ExpandableListView.ExpandableListContextMenuInfo infoAdapter = (ExpandableListView.ExpandableListContextMenuInfo) menuItem.getMenuInfo() ;
        int indexMenuItem = menuItem.getItemId();
        int indexContact = (int)infoAdapter.packedPosition ;
        switchMenu(indexContact,indexMenuItem);
        return true;
    }

    private void switchMenu(int indexContact,int indexMenuItem){
        Intent intent ;
        switch (indexMenuItem){
            case MENU_CALL:
                intent = new Intent(Intent.ACTION_DIAL,Uri.parse("Tel:"+listDataContact.get(indexContact).getNumberContact()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                this.getActivity().startActivity(intent);
            case MENU_EDIT:
                intent = new Intent(this.getContext(),EditContact.class);
                intent.putExtra("contact",listDataContact.get(indexContact));
                startActivity(intent);
                break;
            case MENU_DELETE:
                final int index = indexContact;
                new AlertDialog.Builder(this.getContext())
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setTitle("Liên hệ : " + listDataContact.get(indexContact).getNameContact() + "-" + listDataContact.get(indexContact).getNumberContact())
                        .setMessage("Bạn muốn xóa liên hệ này không?")
                        .setPositiveButton("Có", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                listDataContact.remove(index);
                                contactsAdapter.notifyDataSetChanged();
                                collapseAll();
                            }
                        })
                        .setNegativeButton("Không", null)
                        .show();
                break;
        }
    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {

        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}

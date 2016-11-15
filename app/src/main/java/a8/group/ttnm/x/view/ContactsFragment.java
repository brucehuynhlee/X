package a8.group.ttnm.x.view;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.main;
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
    private static final int MENU_EDIT = 0;
    private static final int MENU_DELETE = 1;
    private static final String[] menuItem = {"Edit","Delete"};
    private FloatingActionButton btnContact ;
    private OnFragmentInteractionListener mListener;
    ListView listContacts ;
    SimpleAdapter simpleAdapter;
    List<HashMap<String, String>> aList ;
    ContactsFactory contactsFactory ;

    int[] listviewImage = new int[]{
            R.mipmap.profile, R.mipmap.profile,R.mipmap.profile, R.mipmap.profile,
            R.mipmap.profile,R.mipmap.profile, R.mipmap.profile, R.mipmap.profile,

    };

    public void init(){
        aList = new ArrayList<HashMap<String, String>>();
        contactsFactory = ContactsFactory.getInstanceContactsFactory() ;
        for (int i = 0; i < contactsFactory.contact.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("profile_contact", Integer.toString(listviewImage[i]));
            hm.put("name_contact", contactsFactory.contact.get(i).getNameContact());
            hm.put("number_contact", contactsFactory.contact.get(i).getNumberContact());
            aList.add(hm);
        }

        String[] from = {"profile_contact", "name_contact", "number_contact"};
        int[] to = {R.id.listview_image, R.id.listview_item_title, R.id.listview_item_description};

        simpleAdapter = new SimpleAdapter(getActivity(), aList, R.layout.item_contacts, from, to);
        listContacts.setAdapter(simpleAdapter);
        listContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String uri = "tel:" + contactsFactory.contact.get(position).getNumberContact();
                Intent callIntent = new Intent(Intent.ACTION_CALL, Uri.parse(uri));
                startActivity(callIntent);
            }
        });
        registerForContextMenu(listContacts);
    }


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
        listContacts = (ListView) view.findViewById(R.id.listContacts);
        btnContact = (FloatingActionButton) view.findViewById(R.id.fabContacts);
        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),EditContact.class);
                startActivity(intent);
            }
        });

        // init all
        init();
        return view;
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo){
        if(v.getId() == R.id.listContacts){
            AdapterView.AdapterContextMenuInfo infoAdapter = (AdapterView.AdapterContextMenuInfo) menuInfo ;
            menu.setHeaderTitle(contactsFactory.contact.get(infoAdapter.position).getNameContact());
            //Toast.makeText(this.getActivity(),listviewTitle[infoAdapter.position],Toast.LENGTH_LONG).show();
            for (int i = 0; i < menuItem.length ; i++) {
                menu.add(Menu.NONE, i, i, menuItem[i]);
            }
        }

    }
    @Override
    public boolean onContextItemSelected(MenuItem menuItem){
        AdapterView.AdapterContextMenuInfo infoItem = (AdapterView.AdapterContextMenuInfo)menuItem.getMenuInfo();
        int indexMenuItem = menuItem.getItemId();
        int indexContact = infoItem.position ;
        switch (indexMenuItem){
            case MENU_EDIT:
                break;
            case MENU_DELETE:
                //Toast.makeText(this.getActivity(),"show",Toast.LENGTH_LONG).show();
                aList.remove(indexContact);
                simpleAdapter.notifyDataSetChanged();
                break;
        }
        return true;
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

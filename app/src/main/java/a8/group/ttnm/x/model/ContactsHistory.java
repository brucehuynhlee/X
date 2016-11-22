package a8.group.ttnm.x.model;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.Test.Unity;

/**
 * Created by LittleDragon on 11/20/2016.
 */

public class ContactsHistory {
    public ArrayList<Contact> historyContacts ;
    private static ContactsHistory me = null ;
    public ContactsHistory(Context context){
        historyContacts = new ArrayList<Contact>();
        Uri image = Unity.getUriToDrawable(context, R.mipmap.profile);
    }
    public static ContactsHistory getInstanceContactsFavorite(Context context){
        if(me == null){
            me = new ContactsHistory(context);
        }
        return me ;
    }
    public Contact checkContact(String str){
        for(int i = 0 ; i < historyContacts.size() ; i++){
            if(historyContacts.get(i).getNumberContact().equals(str)) return historyContacts.get(i);
        }
        return null;
    }
    public void removeContact(Contact contact){
        for(int i = 0 ; i < historyContacts.size() ; i++){
            if(historyContacts.get(i).compareTo(contact) == 0) {
                historyContacts.remove(i);
            }
        }

    }
}

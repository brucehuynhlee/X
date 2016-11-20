package a8.group.ttnm.x.model;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.Test.Unity;

/**
 * Created by LittleDragon on 11/18/2016.
 */

public class ContactsFavorite {
    public ArrayList<Contact> favoriteContacts ;
    private static ContactsFavorite me = null ;
    public ContactsFavorite(Context context){
        favoriteContacts = new ArrayList<Contact>();
        Uri image = Unity.getUriToDrawable(context, R.mipmap.profile);
    }
    public static ContactsFavorite getInstanceContactsFavorite(Context context){
        if(me == null){
            me = new ContactsFavorite(context);
        }
        return me ;
    }
    public boolean checkContact(Contact contact){
        for(int i = 0 ; i < favoriteContacts.size() ; i++){
             if(favoriteContacts.get(i).compareTo(contact) == 0) return true;
        }
        return false;
    }
    public void removeContact(Contact contact){
        for(int i = 0 ; i < favoriteContacts.size() ; i++){
            if(favoriteContacts.get(i).compareTo(contact) == 0) {
                favoriteContacts.remove(i);
            }
        }

    }
}

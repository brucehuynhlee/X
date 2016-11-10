package a8.group.ttnm.x.model;

import android.net.Uri;

/**
 * Created by LittleDragon on 11/11/2016.
 */

public class Contact {

    public Contact(){

    }
    public Contact(int idContact, String nameContact, String numberContact, String addressContact, String emailContact, String groupContact , Uri uri) {
        this.idContact = idContact;
        this.nameContact = nameContact;
        this.numberContact = numberContact;
        this.addressContact = addressContact;
        this.emailContact = emailContact;
        this.groupContact = groupContact;
        this.uriImageContact = uri ;
    }

    private int idContact ;
    private String nameContact ;
    private String numberContact ;
    private String addressContact ;
    private String emailContact ;
    private String groupContact ;
    private Uri uriImageContact ;



    public Uri getUriImageContact() {
        return uriImageContact;
    }

    public void setUriImageContact(Uri uriImageContact) {
        this.uriImageContact = uriImageContact;
    }


    public int getIdContact() {
        return idContact;
    }

    public void setIdContact(int idContact) {
        this.idContact = idContact;
    }

    public String getNameContact() {
        return nameContact;
    }

    public void setNameContact(String nameContact) {
        this.nameContact = nameContact;
    }

    public String getNumberContact() {
        return numberContact;
    }

    public void setNumberContact(String numberContact) {
        this.numberContact = numberContact;
    }

    public String getAddressContact() {
        return addressContact;
    }

    public void setAddressContact(String addressContact) {
        this.addressContact = addressContact;
    }

    public String getEmailContact() {
        return emailContact;
    }

    public void setEmailContact(String emailContact) {
        this.emailContact = emailContact;
    }

    public String getGroupContact() {
        return groupContact;
    }

    public void setGroupContact(String groupContact) {
        this.groupContact = groupContact;
    }
}

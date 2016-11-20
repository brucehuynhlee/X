package a8.group.ttnm.x.model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by LittleDragon on 11/11/2016.
 */

public class Contact implements Parcelable,Comparable<Contact>{

    private int idContact ;
    private String nameContact ;
    private String numberContact ;
    private String addressContact ;
    private String emailContact ;
    private String groupContact ;
    private Uri uriImageContact ;

    public Contact(){}
    public Contact(int idContact, String nameContact, String numberContact, String addressContact, String emailContact, String groupContact , Uri uri) {
        this.idContact = idContact;
        this.nameContact = nameContact;
        this.numberContact = numberContact;
        this.addressContact = addressContact;
        this.emailContact = emailContact;
        this.groupContact = groupContact;
        this.uriImageContact = uri ;
    }


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

    @Override
    public int describeContents() {
        return 0;
    }


    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(idContact);
        dest.writeString(nameContact);
        dest.writeString(numberContact);
        dest.writeString(addressContact);
        dest.writeString(emailContact);
        dest.writeString(groupContact);
        dest.writeString(uriImageContact.toString());

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Parcelable.Creator<Contact> CREATOR = new Parcelable.Creator<Contact>() {
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        public Contact[] newArray(int size) {
            return new Contact[size] ;
        }
    };

    private Contact(Parcel dest){
        idContact = dest.readInt();
        nameContact = dest.readString();
        numberContact = dest.readString();
        addressContact = dest.readString();
        emailContact = dest.readString();
        groupContact = dest.readString();
        uriImageContact = Uri.parse(dest.readString());
    }

    @Override
    public String toString(){
        return getNameContact();
    }

    @Override
    public int compareTo(Contact contact) {
        return getNameContact().compareTo(contact.getNameContact()) ;
    }


}

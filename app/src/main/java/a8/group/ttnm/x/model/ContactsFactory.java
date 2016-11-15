package a8.group.ttnm.x.model;

import java.util.ArrayList;

/**
 * Created by LittleDragon on 11/11/2016.
 */

public class ContactsFactory {
    public ArrayList<Contact> contact ;
    private static ContactsFactory me = null ;
    public ContactsFactory(){
        contact = new ArrayList<Contact>();
        contact.add(new Contact(1,"Nguyễn Văn A","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",null));
        contact.add(new Contact(1,"Nguyễn Văn B","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",null));
        contact.add(new Contact(1,"Nguyễn Văn C","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",null));
        contact.add(new Contact(1,"Nguyễn Văn D","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",null));

    }
    public static ContactsFactory getInstanceContactsFactory(){
        if(me == null){
            me = new ContactsFactory();
        }
        return me ;
    }
}

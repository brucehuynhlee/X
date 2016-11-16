package a8.group.ttnm.x.model;

import android.content.Context;
import android.net.Uri;

import java.util.ArrayList;

import a8.group.ttnm.x.R;
import a8.group.ttnm.x.controller.Unity;

/**
 * Created by LittleDragon on 11/11/2016.
 */

public class ContactsFactory {
    public ArrayList<Contact> contact ;
    private static ContactsFactory me = null ;
    public ContactsFactory(Context context){
        contact = new ArrayList<Contact>();
        Uri image = Unity.getUriToDrawable(context,R.mipmap.profile);
        contact.add(new Contact(1,"Nguyễn Văn A","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng", image));
        contact.add(new Contact(2,"Nguyễn Văn B","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(3,"Nguyễn Văn C","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(4,"Nguyễn Văn D","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(5,"Nguyễn Văn E","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng", image));
        contact.add(new Contact(6,"Nguyễn Văn F","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(7,"Nguyễn Văn G","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(8,"Nguyễn Văn H","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(9,"Nguyễn Văn I","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng", image));
        contact.add(new Contact(10,"Nguyễn Văn K","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(11,"Nguyễn Văn L","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));
        contact.add(new Contact(12,"Nguyễn Văn M","01674953329","Hoàng Mai - Hà Nội","ng@gmail.com","Khách hàng",image));

    }
    public static ContactsFactory getInstanceContactsFactory(Context context){
        if(me == null){
            me = new ContactsFactory(context);
        }
        return me ;
    }
}

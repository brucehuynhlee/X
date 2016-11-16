package a8.group.ttnm.x.model;

import android.net.Uri;

/**
 * Created by LittleDragon on 11/16/2016.
 */

public class ExpandListOption {
    private Uri uriImage ;
    private String option ;

    public ExpandListOption(Uri uriImage , String option){
        this.uriImage = uriImage ;
        this.option = option ;
    }
    public Uri getUriImage() {
        return uriImage;
    }

    public void setUriImage(Uri uriImage) {
        this.uriImage = uriImage;
    }

    public String getOption() {
        return option;
    }

    public void setOption(String option) {
        this.option = option;
    }
}

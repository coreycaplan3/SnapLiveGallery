package caplaninnovations.com.livesnapgallery.models;

import android.os.Parcel;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

/**
 * Created by Corey on 4/30/2017.
 * Project: LiveSnapGallery
 * <p></p>
 * Purpose of Class:
 */
public class Snap extends RealmObject {

    public static final String COL_URL = "url";
    public static final String COL_DATE = "dateAdded";
    public static final String COL_IS_DELETED = "isDeleted";

    @PrimaryKey
    private String url;
    private long dateAdded;
    private String from;
    private boolean isDeleted;

    public static ArrayList<Snap> getDummyList() {
        String url;
        ArrayList<Snap> snaps = new ArrayList<>();
        long time = Calendar.getInstance().getTimeInMillis();
        String from = "Corey Caplan";

        url = "https://images.pexels.com/photos/59523/pexels-photo-59523.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time, from));

        url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnfpzoD-_HTIGH37ncMqaYiqjOI4MrXSPSTCyAHbJdSsc6O9vP";
        snaps.add(new Snap(url, time, from));

        url = "https://images.pexels.com/photos/39317/chihuahua-dog-puppy-cute-39317.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time, from));

        url = "https://images.pexels.com/photos/97863/pexels-photo-97863.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time, from));

        url = "https://images.pexels.com/photos/71581/animal-pet-dog-puppy-71581.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time, from));

        return snaps;
    }

    public Snap() {
    }

    private Snap(String url, long dateAdded, String from) {
        this.url = url;
        this.dateAdded = dateAdded;
        this.from = from;
        this.isDeleted = false;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }
}

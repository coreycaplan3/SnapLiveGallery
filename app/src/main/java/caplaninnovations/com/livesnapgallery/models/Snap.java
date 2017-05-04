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

    @PrimaryKey
    private String url;
    private long dateAdded;

    @SuppressWarnings("WeakerAccess")
    protected Snap(Parcel in) {
        url = in.readString();
        dateAdded = in.readLong();
    }

    public static ArrayList<Snap> getDummyList() {
        String url;
        ArrayList<Snap> snaps = new ArrayList<>();
        long time = Calendar.getInstance().getTimeInMillis();

        url = "https://images.pexels.com/photos/59523/pexels-photo-59523.jpeg?w=1260&h=750&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time));

        url = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcSnfpzoD-_HTIGH37ncMqaYiqjOI4MrXSPSTCyAHbJdSsc6O9vP";
        snaps.add(new Snap(url, time));

        url = "https://images.pexels.com/photos/39317/chihuahua-dog-puppy-cute-39317.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time));

        url = "https://images.pexels.com/photos/97863/pexels-photo-97863.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time));

        url = "https://images.pexels.com/photos/71581/animal-pet-dog-puppy-71581.jpeg?w=940&h=650&auto=compress&cs=tinysrgb";
        snaps.add(new Snap(url, time));

        return snaps;
    }

    public Snap() {
    }

    private Snap(String url, long dateAdded) {
        this.url = url;
        this.dateAdded = dateAdded;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String mUrl) {
        this.url = mUrl;
    }

    public long getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(long mDateAdded) {
        this.dateAdded = mDateAdded;
    }

}

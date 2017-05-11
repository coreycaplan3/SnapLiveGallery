package caplaninnovations.com.livesnapgallery.database;

import caplaninnovations.com.livesnapgallery.models.Snap;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import io.realm.Sort;

/**
 * Created by Corey Caplan on 5/11/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */

public class RealmWrapper {

    public static Realm getInstance() {
        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        return Realm.getInstance(configuration);
    }

    public static RealmResults<Snap> getSnaps(Realm realm) {
        return realm.where(Snap.class)
                .equalTo(Snap.COL_IS_DELETED, false)
                .findAll()
                .sort(Snap.COL_DATE, Sort.DESCENDING);
    }

    public static RealmResults<Snap> getSnaps(Realm realm, String[] snapKeys) {
        return realm.where(Snap.class)
                .equalTo(Snap.COL_IS_DELETED, false)
                .in(Snap.COL_URL, snapKeys)
                .findAll()
                .sort(Snap.COL_DATE, Sort.DESCENDING);
    }

    public static Snap getSnap(Realm realm, String url) {
        return realm.where(Snap.class)
                .equalTo(Snap.COL_URL, url)
                .findFirst();
    }

}

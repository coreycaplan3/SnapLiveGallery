package caplaninnovations.com.livesnapgallery.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;

import butterknife.BindView;
import butterknife.OnClick;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.recyclers.PictureRecyclerAdapter;
import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

/**
 * Created by Corey on 4/30/2017.
 * Project: LiveSnapGallery
 * <p></p>
 * Purpose of Class:
 */
public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RealmResults<Snap> snaps = getRealm()
                .where(Snap.class)
                .findAll();

        if(snaps.size() == 0) {
            getRealm().executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {
                    realm.insert(Snap.getDummyList());
                }
            });
        }

        RecyclerView.Adapter adapter = new PictureRecyclerAdapter(snaps);
        mRecyclerView.setAdapter(adapter);

        int span;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                span = 5;
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                span = 3;
                break;
            case Configuration.SCREENLAYOUT_SIZE_NORMAL:
            case Configuration.SCREENLAYOUT_SIZE_SMALL:
                span = 1;
                break;
            default:
                Log.e(TAG, "onCreate: Invalid Screen size, found: " + screenSize);
                span = 0;
                break;
        }
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(span, StaggeredGridLayoutManager.VERTICAL));
    }

    @Override
    int getContentView() {
        return R.layout.activity_main;
    }

    @OnClick(R.id.fab)
    public void onClickStartSlideshow() {
        Intent intent = SlideShowActivity.createIntent(null);
        startActivity(intent);
    }

}

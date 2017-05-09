package caplaninnovations.com.livesnapgallery.activities;

import android.app.ActivityOptions;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.util.Pair;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;

import com.android.volley.Response;
import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;

import butterknife.BindView;
import butterknife.OnClick;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.networking.FeedNetwork;
import caplaninnovations.com.livesnapgallery.recyclers.PictureRecyclerAdapter;
import caplaninnovations.com.livesnapgallery.recyclers.PictureRecyclerAdapter.OnSnapClickListener;
import caplaninnovations.com.livesnapgallery.utilities.JsonExtractor;
import caplaninnovations.com.livesnapgallery.utilities.UiUtility;
import io.realm.Realm;
import io.realm.RealmResults;

/**
 * Created by Corey on 4/30/2017.
 * Project: LiveSnapGallery
 * <p></p>
 * Purpose of Class:
 */
public class MainActivity extends BaseActivity implements OnSnapClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSwipeRefreshLayout.setOnRefreshListener(/* This class implements the interface */ this);

        RealmResults<Snap> snaps = getRealm()
                .where(Snap.class)
                .findAll()
                .sort(Snap.COL_DATE);

        RecyclerView.Adapter adapter = new PictureRecyclerAdapter(snaps,
                /* This class implements the interface */ this);
        mRecyclerView.setAdapter(adapter);

        int span;
        int screenSize = getResources().getConfiguration().screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch (screenSize) {
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
    protected int getContentView() {
        return R.layout.activity_main;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_activity_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_refresh) {
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onRefresh() {
        new FeedNetwork().getPhotosFromNetwork(new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(final JSONArray response) {
                getRealm().executeTransaction(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JsonExtractor extractor = new JsonExtractor(response.getJSONObject(i));


                                String url = extractor.getString("url");

                                if (url != null && !url.contains(".mp4")) {
                                    Snap snap = new Snap();
                                    snap.setUrl(url);
                                    snap.setDateAdded(extractor.getLong("date"));
                                    snap.setFrom(extractor.getString("from"));
                                    realm.insertOrUpdate(snap);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mSwipeRefreshLayout.setRefreshing(false);
                UiUtility.snackbar(getRootView(), R.string.error_no_connection, R.string.retry, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRefresh();
                    }
                });
            }
        });
    }

    @Override
    public final void onSnapClick(Snap snap, Pair<View, String> dateTextView, Pair<View, String> imageView) {
        View statusBar = findViewById(android.R.id.statusBarBackground);
        Pair<View, String> sharedStatusBar = Pair.create(statusBar, Window.STATUS_BAR_BACKGROUND_TRANSITION_NAME);

        String appBarTransitionName = getString(R.string.toolbar_transition);
        View appBarLayout = findViewById(R.id.app_bar);
        Pair<View, String> sharedToolbar = new Pair<>(appBarLayout, appBarTransitionName);

        ActivityOptions activityOptions =
                ActivityOptions.makeSceneTransitionAnimation(this, dateTextView, imageView,
                        sharedStatusBar, sharedToolbar);

        Intent intent = PhotoDetailsActivity.createIntent(snap.getUrl());
        startActivity(intent, activityOptions.toBundle());
    }

    @Override
    public void onSnapLongClick(Snap snap) {
        final String url = snap.getUrl();
        new AlertDialog.Builder(this)
                .setTitle("Delete Snap?")
                .setMessage("Are you sure you want to delete this snap?")
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        getRealm().executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.where(Snap.class)
                                        .equalTo(Snap.COL_URL, url)
                                        .findFirst()
                                        .deleteFromRealm();
                            }
                        });
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create()
                .show();
    }

    @OnClick(R.id.fab)
    public void onClickStartSlideshow() {
        Intent intent = SlideShowActivity.createIntent(null);
        startActivity(intent);
    }

}

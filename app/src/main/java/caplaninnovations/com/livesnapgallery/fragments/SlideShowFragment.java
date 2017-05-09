package caplaninnovations.com.livesnapgallery.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.utilities.DateUtility;
import io.realm.Realm;
import io.realm.RealmConfiguration;
import it.sephiroth.android.library.imagezoom.ImageViewTouch;

/**
 * Created by Corey Caplan on 5/2/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */

public class SlideShowFragment extends Fragment {

    private static final String KEY_SNAP = "SNAP";

    private String mSnapKey;

    @BindView(R.id.slide_show_image)
    ImageViewTouch mImageViewTouch;

    @BindView(R.id.details_date)
    TextView mDateTextView;

    @BindView(R.id.details_from)
    TextView mFromTextView;


    private Unbinder mUnbinder;

    public static SlideShowFragment newInstance(String snapKey) {
        Bundle bundle = new Bundle();
        bundle.putString(KEY_SNAP, snapKey);
        SlideShowFragment fragment = new SlideShowFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSnapKey = savedInstanceState.getString(KEY_SNAP);
        } else {
            mSnapKey = getArguments().getString(KEY_SNAP);
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_slide_show, container, false);
        mUnbinder = ButterKnife.bind(this, view);

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm realm = Realm.getInstance(configuration);
        Snap snap = realm.where(Snap.class)
                .equalTo("url", mSnapKey)
                .findFirst();

        Glide.with(this)
                .load(snap.getUrl())
                .fitCenter()
                .placeholder(R.mipmap.ic_launcher_round)
                .error(R.drawable.ic_cloud_off_black_48dp)
                .into(mImageViewTouch);

        mFromTextView.setText(snap.getFrom());

        mDateTextView.setText(DateUtility.getDateForUi(snap.getDateAdded()));

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SNAP, mSnapKey);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (mUnbinder != null) {
            mUnbinder.unbind();
            mUnbinder = null;
        }
    }
}

package caplaninnovations.com.livesnapgallery.activities;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.utilities.DateUtility;

/**
 * Created by Corey Caplan on 5/4/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */
public class PhotoDetailsActivity extends BaseActivity {

    private static final String KEY_SNAP = "SNAP";

    private String mSnap;

    @BindView(R.id.details_image)
    ImageView mImageView;

    @BindView(R.id.details_date)
    TextView mDateTextView;

    @BindView(R.id.details_from)
    TextView mFromTextView;

    public static Intent createIntent(String snapKey) {
        Intent intent = new Intent(BaseActivity.getGlobalContext(), PhotoDetailsActivity.class);
        intent.putExtra(KEY_SNAP, snapKey);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setEnterTransition(null);
        getWindow().setExitTransition(null);

        if (savedInstanceState != null) {
            mSnap = savedInstanceState.getString(KEY_SNAP);
        } else {
            mSnap = getIntent().getStringExtra(KEY_SNAP);
        }

        Snap snap = getRealm()
                .where(Snap.class)
                .equalTo(Snap.COL_URL, mSnap)
                .findFirst();

        Drawable errorDrawable = getResources().getDrawable(R.drawable.ic_cloud_off_black_48dp, null);
        errorDrawable.setTint(Color.WHITE);

        disableScrollFlags();

        Glide.with(this)
                .load(snap.getUrl())
                .placeholder(R.mipmap.ic_launcher_round)
                .error(errorDrawable)
                .fitCenter()
                .crossFade()
                .into(mImageView);

        mDateTextView.setText(DateUtility.getDateForUi(snap.getDateAdded()));
        mFromTextView.setText(snap.getFrom());
    }

    @Override
    protected int getContentView() {
        return R.layout.activity_photo_details;
    }

    @Override
    public boolean onSupportNavigateUp() {
        finishAfterTransition();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(KEY_SNAP, mSnap);
    }
}

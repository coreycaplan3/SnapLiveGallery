package caplaninnovations.com.livesnapgallery.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.fragments.SlideShowFragment;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.utilities.AnimationUtility;
import caplaninnovations.com.livesnapgallery.utilities.SharedPreferenceUtility;
import io.realm.RealmResults;

/**
 * Created by Corey Caplan on 5/2/17.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class:
 */
public class SlideShowActivity extends BaseActivity {

    private static final long ANIMATION_DURATION = 300L;
    private static final long TASK_DELAY = 3000L;
    private static final long TASK_DURATION = SharedPreferenceUtility.getSlideShowTaskDuration();
    private static final String KEY_SNAPS = "SNAPS";

    private SlideShowAdapter mSlideShowAdapter;
    private Handler mSlideShowHandler;

    @BindView(R.id.view_pager)
    ViewPager mViewPager;

    private ArrayList<String> mSnapKeys;

    public static Intent createIntent(ArrayList<String> snapKeys) {
        Intent intent = new Intent(BaseActivity.getGlobalContext(), SlideShowActivity.class);
        intent.putStringArrayListExtra(KEY_SNAPS, snapKeys);
        return intent;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            mSnapKeys = savedInstanceState.getStringArrayList(KEY_SNAPS);
        } else {
            mSnapKeys = getIntent().getStringArrayListExtra(KEY_SNAPS);
        }

        RealmResults<Snap> mSnaps;
        if (mSnapKeys == null) {
            mSnaps = getRealm().where(Snap.class)
                    .findAll();
        } else {
            mSnaps = getRealm().where(Snap.class)
                    .in(Snap.COL_URL, (String[]) mSnapKeys.toArray())
                    .findAll();
        }

        mSlideShowHandler = new Handler();

        mSlideShowAdapter = new SlideShowAdapter(getSupportFragmentManager(), mSnaps);
        mViewPager.setAdapter(mSlideShowAdapter);

        scheduleSlideShow();
    }

    @Override
    int getContentView() {
        return R.layout.activity_slide_show;
    }

    private void scheduleSlideShow() {
        final Runnable update = new Runnable() {

            @Override
            public void run() {
                int nextPage = mViewPager.getCurrentItem() + 1;

                if (nextPage == mSlideShowAdapter.getCount()) {
                    Animator animator = AnimationUtility.getCircularHide(mViewPager);
                    animator.addListener(new AnimatorListenerAdapter() {
                        @Override
                        public void onAnimationEnd(Animator animation) {
                            mViewPager.setCurrentItem(0, false);

                            AnimationUtility.getCircularReveal(mViewPager)
                                    .start();
                        }
                    });
                    animator.start();
                } else {
                    mViewPager.setCurrentItem(nextPage, true);
                }
            }

        };


        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                mSlideShowHandler.post(update);
            }
        }, TASK_DELAY, TASK_DURATION);
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putStringArrayList(KEY_SNAPS, mSnapKeys);
    }

    private static final class SlideShowAdapter extends FragmentStatePagerAdapter {

        private RealmResults<Snap> mSnaps;

        private SlideShowAdapter(FragmentManager fragmentManager, RealmResults<Snap> snaps) {
            super(fragmentManager);
            mSnaps = snaps;
        }

        @Override
        public Fragment getItem(int position) {
            return SlideShowFragment.newInstance(mSnaps.get(position).getUrl());
        }

        @Override
        public int getCount() {
            return mSnaps.size();
        }

    }

}

package caplaninnovations.com.livesnapgallery.utilities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.util.Property;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;

import java.util.ArrayList;

import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.activities.BaseActivity;

/**
 * Created by Corey on 6/4/2016.
 * Project: SnapLiveGallery
 * <p></p>
 * Purpose of Class: A utility class used to make working with animations more efficient.
 */
@SuppressWarnings("unused")
public final class AnimationUtility {

    private static final long ANIMATION_DURATION_SHORT = BaseActivity.getGlobalContext()
            .getResources()
            .getInteger(R.integer.activity_transition_length_very_short);

    private AnimationUtility() {
    }

    private static Interpolator fastOutSlowIn;
    private static Interpolator fastOutLinearIn;
    private static Interpolator linearOutSlowIn;

    public static Interpolator getFastOutSlowInInterpolator(Context context) {
        if (fastOutSlowIn == null) {
            fastOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_slow_in);
        }
        return fastOutSlowIn;
    }

    public static Interpolator getFastOutLinearInInterpolator(Context context) {
        if (fastOutLinearIn == null) {
            fastOutLinearIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.fast_out_linear_in);
        }
        return fastOutLinearIn;
    }

    public static Interpolator getLinearOutSlowInInterpolator(Context context) {
        if (linearOutSlowIn == null) {
            linearOutSlowIn = AnimationUtils.loadInterpolator(context,
                    android.R.interpolator.linear_out_slow_in);
        }
        return linearOutSlowIn;
    }

    @Nullable
    public static Animator getCircularReveal(final View view) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float finalRadius = (float) Math.hypot(cx, cy);

        if(view.isAttachedToWindow()) {
            Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, 0, finalRadius);

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationStart(Animator animation) {
                    super.onAnimationStart(animation);
                    view.setVisibility(View.VISIBLE);
                }
            });

            animator.setDuration(ANIMATION_DURATION_SHORT);

            return animator;
        } else {
            return null;
        }
    }

    @Nullable
    public static Animator getCircularHide(final View view) {
        int cx = view.getWidth() / 2;
        int cy = view.getHeight() / 2;

        float initialRadius = (float) Math.hypot(cx, cy);

        if(view.isAttachedToWindow()) {
            Animator animator = ViewAnimationUtils.createCircularReveal(view, cx, cy, initialRadius, 0);

            animator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    view.setVisibility(View.INVISIBLE);
                }
            });

            animator.setDuration(ANIMATION_DURATION_SHORT);

            return animator;
        } else {
            return null;
        }
    }

}

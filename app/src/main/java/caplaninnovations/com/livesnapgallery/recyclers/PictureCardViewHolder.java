package caplaninnovations.com.livesnapgallery.recyclers;

import android.content.Context;
import android.content.res.Configuration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import butterknife.BindView;
import butterknife.ButterKnife;
import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import caplaninnovations.com.livesnapgallery.utilities.DateUtility;
import caplaninnovations.com.livesnapgallery.utilities.UiUtility;

/**
 * Created by Corey on 4/30/2017.
 * Project: LiveSnapGallery
 * <p></p>
 * Purpose of Class:
 */

class PictureCardViewHolder extends RecyclerView.ViewHolder {

    interface OnSnapClickListenerInternal {

        void onSnapClickInternal(int position, Pair<View, String> dateTextView,
                                 Pair<View, String> imageView);

        void onSnapLongClickInternal(int position);
    }

    @SuppressWarnings("unused")
    private static final String TAG = "TAG";

    @BindView(R.id.picture_view_holder_image)
    ImageView mImageView;

    @BindView(R.id.picture_view_holder_date_text)
    TextView mDateTextView;

    @BindView(R.id.picture_view_holder_from_text)
    TextView mFromTextView;


    PictureCardViewHolder(View itemView, final OnSnapClickListenerInternal listenerInternal) {
        super(itemView);
        ButterKnife.bind(this, itemView);

        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View baseView = view;
                while(baseView.getParent() != null && baseView.getParent() instanceof View) {
                    baseView = (View) baseView.getParent();
                }

                Context context = view.getContext();

                String dateTransitionName = context.getString(R.string.date_transition);
                Pair<View, String> sharedDate = new Pair<View, String>(mDateTextView, dateTransitionName);

                String imageTransitionName = context.getString(R.string.transition_shared_image);
                Pair<View, String> sharedImage = new Pair<View, String>(mImageView, imageTransitionName);

                //noinspection unchecked
                listenerInternal.onSnapClickInternal(getAdapterPosition(), sharedDate, sharedImage);
            }
        });

        itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                listenerInternal.onSnapLongClickInternal(getAdapterPosition());
                return false;
            }
        });
    }

    void bind(Snap snap) {
        Context context = itemView.getContext();

        float aspectRatio = UiUtility.getScreenHeight() / UiUtility.getScreenWidth();

        int width = UiUtility.getScreenWidth() - UiUtility.dpToPx(32);
        int height = (int) (width * aspectRatio);

        int screenSize = context.getResources()
                .getConfiguration()
                .screenLayout & Configuration.SCREENLAYOUT_SIZE_MASK;
        switch(screenSize) {
            case Configuration.SCREENLAYOUT_SIZE_XLARGE:
                width = width / 5;
                height = (int) (width * aspectRatio);
                break;
            case Configuration.SCREENLAYOUT_SIZE_LARGE:
                width = width / 3;
                height = (int) (width * aspectRatio);
                break;
        }

        Log.e(TAG, "bind: " + snap.getUrl());

        Glide.with(context)
                .load(snap.getUrl())
                .placeholder(context.getResources().getDrawable(R.mipmap.ic_launcher_round, null))
                .fitCenter()
                .override(width, height)
                .error(R.drawable.ic_cloud_off_black_48dp)
                .crossFade()
                .into(mImageView);

        mDateTextView.setText(DateUtility.getDateForUi(snap.getDateAdded()));
        mFromTextView.setText(snap.getFrom());
    }

}

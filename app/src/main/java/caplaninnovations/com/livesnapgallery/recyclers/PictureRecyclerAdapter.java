package caplaninnovations.com.livesnapgallery.recyclers;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import caplaninnovations.com.livesnapgallery.R;
import caplaninnovations.com.livesnapgallery.models.Snap;
import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmChangeListener;
import io.realm.RealmResults;

/**
 * Created by Corey on 4/30/2017.
 * Project: LiveSnapGallery
 * <p></p>
 * Purpose of Class:
 */

public class PictureRecyclerAdapter extends RecyclerView.Adapter<PictureCardViewHolder> implements
        PictureCardViewHolder.OnSnapClickListenerInternal {

    public interface OnSnapClickListener {

        void onSnapClick(Snap snap, Pair<View, String> dateTextView, Pair<View, String> imageView);

        void onSnapLongClick(Snap snap);
    }

    @NonNull
    private final RealmResults<Snap> mSnapRealmList;
    private OnSnapClickListener mListener;

    public PictureRecyclerAdapter(@NonNull RealmResults<Snap> snapRealmList,
                                  OnSnapClickListener listener) {
        mSnapRealmList = snapRealmList;
        this.mListener = listener;

        mSnapRealmList.addChangeListener(new OrderedRealmCollectionChangeListener<RealmResults<Snap>>() {
            @Override
            public void onChange(RealmResults<Snap> collection, OrderedCollectionChangeSet changeSet) {
                // For deletions, the adapter has to be notified in reverse order.
                OrderedCollectionChangeSet.Range[] deletions = changeSet.getDeletionRanges();
                for (int i = deletions.length - 1; i >= 0; i--) {
                    OrderedCollectionChangeSet.Range range = deletions[i];
                    notifyItemRangeRemoved(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] insertions = changeSet.getInsertionRanges();
                for (OrderedCollectionChangeSet.Range range : insertions) {
                    notifyItemRangeInserted(range.startIndex, range.length);
                }

                OrderedCollectionChangeSet.Range[] modifications = changeSet.getChangeRanges();
                for (OrderedCollectionChangeSet.Range range : modifications) {
                    notifyItemRangeChanged(range.startIndex, range.length);
                }
            }
        });
    }

    @Override
    public PictureCardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.picture_view_holder, parent, false);
        return new PictureCardViewHolder(view, /* This class implements the interface */ this);
    }

    @Override
    public void onBindViewHolder(PictureCardViewHolder holder, int position) {
        holder.bind(mSnapRealmList.get(position));
    }

    @Override
    public void onSnapClickInternal(int position, Pair<View, String> dateTextView,
                                          Pair<View, String> imageView) {
        // Pass the snap to the activity so it can properly respond to the click event
        mListener.onSnapClick(mSnapRealmList.get(position), dateTextView, imageView);
    }

    @Override
    public void onSnapLongClickInternal(int position) {
        // Pass the snap to the activity so it can properly respond to the click event
        mListener.onSnapLongClick(mSnapRealmList.get(position));
    }

    @Override
    public int getItemCount() {
        return mSnapRealmList.size();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);
        mListener = null;
    }
}

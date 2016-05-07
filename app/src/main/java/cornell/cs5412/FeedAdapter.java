package cornell.cs5412;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import cornell.cs5412.MiscHelpers.*;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.FeedEventViewHolder> {
    public FeedEvent[] events;

    public static class FeedEventViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public RelativeLayout layout;
        public FeedEventViewHolder(RelativeLayout layout) {
            super(layout);
            this.layout = layout;
        }
    }

    public FeedAdapter(FeedEvent[] events) {
        this.events = events;
    }

    @Override
    public FeedEventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RelativeLayout layout = new RelativeLayout(parent.getContext());
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        layout.setLayoutParams(layoutParams);
        layout.setPadding(MiscHelpers.pixToDp(parent.getContext(), 10),
                MiscHelpers.pixToDp(parent.getContext(), 10),
                MiscHelpers.pixToDp(parent.getContext(), 10),
                MiscHelpers.pixToDp(parent.getContext(), 10));
        return new FeedEventViewHolder(layout);
    }

    @Override
    public void onBindViewHolder(FeedEventViewHolder holder, int position) {
        holder.layout.removeAllViews();
        holder.layout.addView(events[position].titleBox);
        holder.layout.addView(events[position].dtlBox);
        holder.layout.addView(events[position].previewBox);
        holder.layout.addView(events[position].rsvpBox);
        holder.layout.addView(events[position].rsvpCountBox);
        holder.layout.addView(events[position].creatorBox);
    }

    @Override
    public int getItemCount() {
        return events.length;
    }
}

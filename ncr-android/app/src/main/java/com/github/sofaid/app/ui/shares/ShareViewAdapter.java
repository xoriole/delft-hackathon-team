package com.github.sofaid.app.ui.shares;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.github.sofaid.app.R;

import java.util.List;

public class ShareViewAdapter extends RecyclerView.Adapter<ShareViewAdapter.ViewHolder> {

    private List<ShareView> mData;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private SparseArray<ViewHolder> holders;


    // data is passed into the constructor
    public ShareViewAdapter(Context context, List<ShareView> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
        holders = new SparseArray<>();
    }

    @NonNull
    @Override
    public ShareViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.shares_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ShareViewAdapter.ViewHolder holder, int position) {
        holder.updateData(mData.get(position));
        holders.append(position, holder);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public ViewHolder getViewHolder(int position) { return holders.get(position);}

    public ShareView getItem(int position) {
        return mData.get(position);
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public Button shareButton;
        public TextView emojiView;
        public TextView idView;

        ViewHolder(View itemView) {
            super(itemView);
            shareButton = itemView.findViewById(R.id.share_button);
            emojiView = itemView.findViewById(R.id.share_emoji);
            idView = itemView.findViewById(R.id.shareId);
            itemView.setOnClickListener(this);
        }

        public void updateData(ShareView shareView){
            if (shareView.isActive()){
                shareButton.setText("SHARE");
                shareButton.setAlpha((float) 1.0);
            } else {
                shareButton.setText("SHARED");
                shareButton.setAlpha((float) 0.4);
            }
            emojiView.setText(shareView.getEmoji());
            String text = "ShareView " + shareView.getShareId().toString();
            idView.setText(text);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // allows clicks events to be caught
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

}

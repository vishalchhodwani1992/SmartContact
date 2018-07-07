package com.androiderstack.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiderstack.custom_view.StyledTextView;
import com.androiderstack.item.BlockContacts;
import com.androiderstack.item.IMPContacts;
import com.androiderstack.smartcontacts.R;

import java.util.List;

/**
 * Created by vishalchhodwani on 10/8/17.
 */
public class AllContactsRecyclerAdapter extends RecyclerView.Adapter<AllContactsRecyclerAdapter.FeedViewHolder> {

    private final String TAG = "IMPContactRecyclerViewAdapter";

    Context context;
    List<?> feedList;

    OnItemClickListener itemClickListener;

    public AllContactsRecyclerAdapter(Context context, List<?> feedList) {
        this.context = context;
        this.feedList = feedList;
    }

    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    public class FeedViewHolder extends RecyclerView.ViewHolder {

        CardView cardView;
        public StyledTextView numberTv, nameTv;


        public FeedViewHolder(View itemView) {
            super(itemView);

            numberTv = (StyledTextView) itemView.findViewById(R.id.contactItem_numberTv);
            nameTv = (StyledTextView) itemView.findViewById(R.id.contactItem_nameTv);
            cardView = (CardView) itemView.findViewById(R.id.card_view);
        }
    }

    @Override
    public FeedViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_recycler_item, parent, false);


        return new FeedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final FeedViewHolder holder, final int position) {
        if (feedList.get(position) instanceof BlockContacts) {
            holder.nameTv.setText(((BlockContacts) feedList.get(position)).getName());
            holder.numberTv.setText(((BlockContacts) feedList.get(position)).getNumber());
        } else {
            holder.nameTv.setText(((IMPContacts) feedList.get(position)).getName());
            holder.numberTv.setText(((IMPContacts) feedList.get(position)).getNumber());
        }

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {

                itemClickListener.onItemClick(holder.cardView, position);

                return false;
            }
        });
    }

    @Override
    public int getItemCount() {

        return feedList.size();
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}

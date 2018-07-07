package com.androiderstack.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.androiderstack.custom_view.StyledTextView;
import com.androiderstack.item.BlockContacts;
import com.androiderstack.smartcontacts.R;

import java.util.List;

/**
 * Created by vishalchhodwani on 10/8/17.
 */
public class BlockContactRecyclerViewAdapter extends RecyclerView.Adapter<BlockContactRecyclerViewAdapter.FeedViewHolder> {

    private final String TAG = "BlockContactRecyclerViewAdapter";

    Context context;
    List<BlockContacts> feedList;
    private int position;

    public BlockContactRecyclerViewAdapter(Context context, List<BlockContacts> feedList) {
        this.context = context;
        this.feedList = feedList;
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
    public void onBindViewHolder(final FeedViewHolder holder, final int position)
    {
        holder.nameTv.setText((feedList.get(position)).getName());
        holder.numberTv.setText((feedList.get(position)).getNumber());

        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(position);
                return false;
            }
        });

    }

    @Override
    public int getItemCount() {

        return feedList.size();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

}

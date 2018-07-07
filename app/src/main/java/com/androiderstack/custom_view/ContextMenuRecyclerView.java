package com.androiderstack.custom_view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.ContextMenu;
import android.view.View;

/**
 * Created by vishalchhodwani on 10/8/17.
 */
public class ContextMenuRecyclerView extends RecyclerView {

    private RecyclerViewContextMenuInfo mContextMenuInfo;

    public ContextMenuRecyclerView(Context context) {
        super(context);
    }

    public ContextMenuRecyclerView(Context context, RecyclerViewContextMenuInfo mContextMenuInfo) {
        super(context);
        this.mContextMenuInfo = mContextMenuInfo;
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs, RecyclerViewContextMenuInfo mContextMenuInfo) {
        super(context, attrs);
        this.mContextMenuInfo = mContextMenuInfo;
    }

    public ContextMenuRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle, RecyclerViewContextMenuInfo mContextMenuInfo) {
        super(context, attrs, defStyle);
        this.mContextMenuInfo = mContextMenuInfo;
    }

    @Override
    protected ContextMenu.ContextMenuInfo getContextMenuInfo() {
        return mContextMenuInfo;
    }

    @Override
    public boolean showContextMenuForChild(View originalView) {
        final int longPressPosition = getChildPosition(originalView);
        if (longPressPosition >= 0) {
            final long longPressId = getAdapter().getItemId(longPressPosition);
            mContextMenuInfo = new RecyclerViewContextMenuInfo(longPressPosition, longPressId);
            return super.showContextMenuForChild(originalView);
        }
        return false;
    }

    public static class RecyclerViewContextMenuInfo implements ContextMenu.ContextMenuInfo {

        public RecyclerViewContextMenuInfo(int position, long id) {
            this.position = position;
            this.id = id;
        }

        final public int position;
        final public long id;
    }
}
package com.mjacksi.rojprints.SimpleAlbum;

import android.content.Context;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.DimenRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

// todo: implementing this class to make the spacing in recycler view perfect!
public class ItemOffsetDecoration extends RecyclerView.ItemDecoration {

    private int mItemOffset;

    public ItemOffsetDecoration(int itemOffset) {
        mItemOffset = itemOffset;
    }

    public ItemOffsetDecoration(@NonNull Context context, @DimenRes int itemOffsetId) {
        this(context.getResources().getDimensionPixelSize(itemOffsetId));
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        int itemPosition = ((RecyclerView.LayoutParams) view.getLayoutParams()).getViewAdapterPosition();
//
//        if (itemPosition % 2 == 0) {
//            outRect.set(mItemOffset, mItemOffset, 0, mItemOffset);
//
//        } else {
//            outRect.set(0, mItemOffset, mItemOffset, mItemOffset);
//
//        }
//
//        if (parent.getChildLayoutPosition(view) == 0) {
//            outRect.top = mItemOffset;
//        } else {
//            outRect.top = 0;
//        }
    }
}




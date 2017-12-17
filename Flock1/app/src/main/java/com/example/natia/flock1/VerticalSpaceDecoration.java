package com.example.natia.flock1;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by napti on 12/12/2017.
 */

public class VerticalSpaceDecoration extends RecyclerView.ItemDecoration {
    private int Space;

    public VerticalSpaceDecoration(int Space){
        this.Space = Space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = Space;
        outRect.bottom = Space;
        outRect.right = Space;

        if(parent.getChildLayoutPosition(view) == 0){
            outRect.top = Space;
        }
    }
}

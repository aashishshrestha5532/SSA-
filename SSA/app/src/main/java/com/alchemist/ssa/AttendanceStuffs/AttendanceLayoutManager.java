package com.alchemist.ssa.AttendanceStuffs;

import android.content.Context;
import android.graphics.PointF;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;


public class AttendanceLayoutManager extends GridLayoutManager {

    private Context context;
    private AttendanceInterface attendanceInterface;
    private boolean present;

    public AttendanceLayoutManager(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    public AttendanceLayoutManager(Context context, int spanCount) {
        super(context, spanCount);
    }

    public AttendanceLayoutManager(Context context, int spanCount, int orientation, boolean reverseLayout) {
        super(context, spanCount, orientation, reverseLayout);
        this.context=context;
    }

    public void addOnScrollCompleteListener(AttendanceInterface attendanceInterface) {
        this.attendanceInterface=attendanceInterface;
    }

    public void setPresent(boolean present) {
        this.present=present;
    }

    @Override
    public void smoothScrollToPosition(RecyclerView recyclerView, RecyclerView.State state, int position) {
        LinearSmoothScroller linearSmoothScroller=new LinearSmoothScroller(context) {
            @Nullable
            @Override
            public PointF computeScrollVectorForPosition(int targetPosition) {
                return super.computeScrollVectorForPosition(targetPosition);
            }

            @Override
            protected void onStop() {
                super.onStop();
                attendanceInterface.scrollComplete(present);
            }
        };
        linearSmoothScroller.setTargetPosition(position);
        startSmoothScroll(linearSmoothScroller);


    }
}

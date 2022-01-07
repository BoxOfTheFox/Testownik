package com.example.testownik.ui.title

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs

class ArchiveSwipeActionCallback :
    ItemTouchHelper.SimpleCallback(
        0,
        ItemTouchHelper.LEFT
    ) {

    interface ArchiveViewHolder {
        /**
         * A view from the view holder which should be translated for swipe events.
         */
        val swipeableView: View

        /**
         * Called as a view holder is actively being swiped/rebounded.
         *
         * @param currentSwipePercentage The total percentage the view has been swiped.
         * @param swipeThreshold The percentage needed to consider a swipe as "rebounded"
         *  or "swiped"
         */
        fun onSwipeOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float
        )

        fun onSwiped()
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        return if (viewHolder is BaseSelectionAdapter.ViewHolder)
            makeMovementFlags(0, ItemTouchHelper.START or ItemTouchHelper.END)
        else
            0
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        getDefaultUIUtil().clearView((viewHolder as ArchiveViewHolder).swipeableView)
    }

    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if (viewHolder is ArchiveViewHolder)
            getDefaultUIUtil().onSelected(viewHolder.swipeableView)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        getDefaultUIUtil().onDraw(
            c,
            recyclerView,
            (viewHolder as ArchiveViewHolder).swipeableView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
        (viewHolder as ArchiveViewHolder).onSwipeOffsetChanged(
            abs(dX) / viewHolder.swipeableView.width,
            0.5f
        )
    }

    override fun onChildDrawOver(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder?,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        getDefaultUIUtil().onDrawOver(
            c,
            recyclerView,
            (viewHolder as ArchiveViewHolder).swipeableView,
            dX,
            dY,
            actionState,
            isCurrentlyActive
        )
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as ArchiveViewHolder).onSwiped()
    }
}
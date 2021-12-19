package com.example.testownik.ui.title

import android.graphics.Canvas
import android.view.View
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import timber.log.Timber
import kotlin.math.abs
import kotlin.math.ln

// The 'true' percentage of total swipe distance needed to consider a view as 'swiped'. This
// is used in favor of getSwipeThreshold since that has been overridden to return an impossible
// to reach value.
private const val trueSwipeThreshold = 0.4F

class ArchiveSwipeActionCallback : ItemTouchHelper.SimpleCallback(
    0,
    ItemTouchHelper.LEFT
) {

    interface ArchiveViewHolder{
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
         * @param currentTargetHasMetThresholdOnce Whether or not during a contiguous interaction
         *  with a single view holder, the swipe percentage has ever been greater than the swipe
         *  threshold.
         */
        fun onSwipeOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float,
            currentTargetHasMetThresholdOnce: Boolean
        )

        fun onSwiped()
    }

    // Track the view holder currently being swiped.
    private var currentTargetPosition: Int = -1
    private var currentTargetHasMetThresholdOnce: Boolean = false

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        (viewHolder as ArchiveViewHolder).onSwiped()
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
        if (viewHolder !is ArchiveViewHolder) return
        if (currentTargetPosition != viewHolder.adapterPosition) {
            currentTargetPosition = viewHolder.adapterPosition
            currentTargetHasMetThresholdOnce = false
        }

        val currentSwipePercentage = abs(dX) / viewHolder.itemView.width
        viewHolder.onSwipeOffsetChanged(
            currentSwipePercentage,
            trueSwipeThreshold,
            currentTargetHasMetThresholdOnce
        )
        viewHolder.swipeableView.translationX = dX

        if (currentSwipePercentage >= trueSwipeThreshold &&
            !currentTargetHasMetThresholdOnce) {
            currentTargetHasMetThresholdOnce = true
        }

//        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
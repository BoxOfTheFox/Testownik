package com.example.testownik.ui.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testownik.database.Base
import com.example.testownik.database.BaseWithQuestions
import com.example.testownik.databinding.BaseItemLayoutBinding

class BaseSelectionAdapter(private val listener: BaseSelectionAdapterListener) :
    ListAdapter<BaseWithQuestions, BaseSelectionAdapter.ViewHolder>(BaseSelectionDiffCallback()) {

    interface BaseSelectionAdapterListener {
        fun onBaseArchived(base: Base?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        private val binding: BaseItemLayoutBinding,
        private val listener: BaseSelectionAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root),
        ArchiveSwipeActionCallback.ArchiveViewHolder {

        override val swipeableView = binding.cardView

        override fun onSwipeOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float,
            currentTargetHasMetThresholdOnce: Boolean
        ) {
            // Only alter shape and activation in the forward direction once the swipe
            // threshold has been met. Undoing the swipe would require releasing the item and
            // re-initiating the swipe.
            if (currentTargetHasMetThresholdOnce) return

            // Start the background animation once the threshold is met.
            val thresholdMet = currentSwipePercentage >= swipeThreshold

            binding.root.isActivated = thresholdMet
        }

        override fun onSwiped() {
            listener.onBaseArchived(binding.baseWithQuestions?.base)
        }

        init {
            binding.root.background = ArchiveSwipeActionDrawable(binding.root.context)
        }

        fun bind(item: BaseWithQuestions) {
            binding.baseWithQuestions = item
//            binding.cardView.setOnLongClickListener {
//                it.isSelected = !it.isSelected
//                true
//            }
        }

        companion object {
            fun from(parent: ViewGroup, listener: BaseSelectionAdapterListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BaseItemLayoutBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding, listener)
            }
        }
    }
}

class BaseSelectionDiffCallback : DiffUtil.ItemCallback<BaseWithQuestions>() {
    override fun areItemsTheSame(oldItem: BaseWithQuestions, newItem: BaseWithQuestions): Boolean {
        return oldItem.base.id == newItem.base.id
    }

    override fun areContentsTheSame(
        oldItem: BaseWithQuestions,
        newItem: BaseWithQuestions
    ): Boolean {
        return oldItem == newItem
    }
}
package com.example.testownik.ui.title

import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.selection.ItemDetailsLookup
import androidx.recyclerview.selection.ItemKeyProvider
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testownik.database.BaseWithQuestions
import com.example.testownik.databinding.BaseItemLayoutBinding

class BaseSelectionAdapter(private val listener: BaseSelectionAdapterListener) :
    ListAdapter<BaseWithQuestions, BaseSelectionAdapter.ViewHolder>(BaseSelectionDiffCallback()) {

    var selectionTracker: SelectionTracker<Long>? = null

    fun getPosition(key: Long) = currentList.indexOfFirst { it.base.id == key }

    interface BaseSelectionAdapterListener {
        fun onBaseArchived(baseWithQuestions: BaseWithQuestions?)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent,selectionTracker,listener)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(
        private val binding: BaseItemLayoutBinding,
        private val selectionTracker: SelectionTracker<Long>?,
        private val listener: BaseSelectionAdapterListener
    ) :
        RecyclerView.ViewHolder(binding.root),
        ArchiveSwipeActionCallback.ArchiveViewHolder {

        private val details = Details()

        override val swipeableView = binding.cardView

        override fun onSwipeOffsetChanged(
            currentSwipePercentage: Float,
            swipeThreshold: Float
        ) {
            // Start the background animation once the threshold is met.
            val thresholdMet = currentSwipePercentage >= swipeThreshold

            binding.root.isActivated = thresholdMet
        }

        override fun onSwiped() {
            listener.onBaseArchived(binding.baseWithQuestions)
        }

        init {
            binding.root.background = ArchiveSwipeActionDrawable(binding.root.context)
        }

        fun bind(item: BaseWithQuestions) {
            binding.baseWithQuestions = item
            details.viewHolder = this
            details.key = item.base.id
            bindSelectedState()
        }

        private fun bindSelectedState() {
            binding.cardView.isChecked = selectionTracker?.isSelected(details.selectionKey) ?: false
        }

        fun getItemDetails(): ItemDetailsLookup.ItemDetails<Long> = details

        companion object {
            fun from(parent: ViewGroup, selectionTracker: SelectionTracker<Long>?, listener: BaseSelectionAdapterListener): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BaseItemLayoutBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding, selectionTracker, listener)
            }
        }
    }

    class DetailsLookup(private val recyclerView: RecyclerView): ItemDetailsLookup<Long>() {
        override fun getItemDetails(e: MotionEvent): ItemDetails<Long>? {

            recyclerView.findChildViewUnder(e.x, e.y)?.let { view ->
                val viewHolder = recyclerView.getChildViewHolder(view)
                if (viewHolder is ViewHolder) {
                    return viewHolder.getItemDetails()
                }
            }

            return null
        }

    }

    class KeyProvider(private val recyclerView: RecyclerView) : ItemKeyProvider<Long>(ItemKeyProvider.SCOPE_CACHED) {
        override fun getKey(position: Int): Long {
            return (recyclerView.adapter as BaseSelectionAdapter).getItem(position).base.id
        }
        override fun getPosition(key: Long): Int {
            return (recyclerView.adapter as BaseSelectionAdapter).getPosition(key)
        }
    }

    class Details : ItemDetailsLookup.ItemDetails<Long>() {
        var key: Long? = null
        var viewHolder: ViewHolder? = null

        override fun getPosition(): Int {
            return viewHolder?.adapterPosition ?: -1
        }
        override fun getSelectionKey(): Long? {
            return key
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
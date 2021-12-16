package com.example.testownik.ui.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testownik.database.BaseWithQuestions
import com.example.testownik.databinding.BaseItemLayoutBinding

class BaseSelectionAdapter: ListAdapter<BaseWithQuestions, BaseSelectionAdapter.ViewHolder>(BaseSelectionDiffCallback())  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(private val binding: BaseItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: BaseWithQuestions){
            binding.baseWithQuestions = item

            binding.cardView.setOnLongClickListener {
                it.isSelected = !it.isSelected
                true
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = BaseItemLayoutBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

class BaseSelectionDiffCallback : DiffUtil.ItemCallback<BaseWithQuestions>() {
    override fun areItemsTheSame(oldItem: BaseWithQuestions, newItem: BaseWithQuestions): Boolean {
        return oldItem.base.id == newItem.base.id
    }

    override fun areContentsTheSame(oldItem: BaseWithQuestions, newItem: BaseWithQuestions): Boolean {
        return oldItem == newItem
    }
}
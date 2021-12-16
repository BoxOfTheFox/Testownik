package com.example.testownik.title

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testownik.database.Base
import com.example.testownik.databinding.BaseItemLayoutBinding

class BaseSelectionAdapter: ListAdapter<Base, BaseSelectionAdapter.ViewHolder>(BaseSelectionDiffCallback())  {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder private constructor(val binding: BaseItemLayoutBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(item: Base){
            binding.base = item
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

class BaseSelectionDiffCallback : DiffUtil.ItemCallback<Base>() {
    override fun areItemsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Base, newItem: Base): Boolean {
        return oldItem == newItem
    }
}
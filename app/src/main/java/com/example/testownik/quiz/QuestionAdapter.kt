package com.example.testownik.quiz

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testownik.databinding.QuizButtonBinding

// todo make listener an interface
class QuestionAdapter(private val listener: (String, CheckBox) -> Unit) :
    ListAdapter<String, QuestionAdapter.ViewHolder>(QuestionDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position), listener)
    }

    class ViewHolder private constructor(val binding: QuizButtonBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(answer: String, listener: (String, CheckBox) -> Unit) {
            binding.quizButton.apply {
                text = answer
                isChecked = false
                isSelected = false
                setOnClickListener {
                    listener.invoke(answer, this)
                }
            }
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = QuizButtonBinding.inflate(layoutInflater, parent, false)

                return ViewHolder(binding)
            }
        }
    }
}

// todo check recycled view when right answer becomes wrong
// todo possible optimization - create custom object with isChecked
class QuestionDiffCallback : DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }
}
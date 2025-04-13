package com.interview.ideamanager.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.interview.ideamanager.data.Task
import com.interview.ideamanager.databinding.ItemTaskBinding

class ListTasksAdapter(tasks: List<Task>, private val onTaskClick: (Task) -> Unit) : RecyclerView.Adapter<ListTasksAdapter.ViewHolder>() {
    var tasks: List<Task> = tasks
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    inner class ViewHolder(private val binding: ItemTaskBinding) : RecyclerView.ViewHolder(binding.root) {
        // ViewHolder implementation
        fun bind(task: Task, onTaskClick: (Task) -> Unit) {
            // Bind the task data to the view
            binding.textTitle.text = task.title
            binding.checkboxDone.isChecked = task.isCompleted
            binding.container.setOnClickListener {
                onTaskClick(task)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // Inflate the item layout and create a ViewHolder
        val binding = ItemTaskBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        // Return the number of items in the list
        return tasks.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Bind the data to the ViewHolder
        val task = tasks[position]
        holder.bind(task, onTaskClick)
    }
}
package com.example.studentscheduler

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.studentscheduler.room.Task
import kotlinx.android.synthetic.main.item_task.view.*

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val startTime : TextView = view.startTime
    val finishTime : TextView = view.finishTime
    val textTask : TextView = view.textTask
}

class RecyclerViewAdapter: RecyclerView.Adapter<ViewHolder>() {

    var tasksList = listOf<Task>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_task, parent, false)

        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return tasksList.count()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val taskList = tasksList[position]
        holder.startTime.text = taskList.startTimeTask
        holder.finishTime.text = taskList.finishTimeTask
        holder.textTask.text = taskList.textTask

//        holder.itemView.setOnClickListener {
//            Toast.makeText(, taskList.textTask, Toast.LENGTH_SHORT).show() //какой контекст?!
//        }
    }

//    interface OnItemClickListener {
//        fun onClickListener(task : Task)
//    }
}
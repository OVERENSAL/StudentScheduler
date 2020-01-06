package com.example.studentscheduler

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.studentscheduler.activity.MainActivity
import com.example.studentscheduler.room.Task
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.android.synthetic.main.item_task.view.*
import kotlin.concurrent.thread

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val startTime : TextView = view.startTime
    val finishTime : TextView = view.finishTime
    val textTask : TextView = view.textTask
    val switch : Switch = view.switch1
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
        
        holder.itemView.setOnClickListener {
            MyViewModel().deleteTask(tasksList[position])
            MainActivity().adapter.notifyDataSetChanged()
        }

        //выолнение задачи(затемнение и зачеркивание)
        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
            if (isChecked) {
                holder.startTime.setTextColor(Color.parseColor("#D3D1D1"))
                holder.startTime.setPaintFlags(holder.startTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.finishTime.setTextColor(Color.parseColor("#D3D1D1"))
                holder.finishTime.setPaintFlags(holder.finishTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.textTask.setTextColor(Color.parseColor("#D3D1D1"))
                holder.textTask.setPaintFlags(holder.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            }
            else {
                holder.startTime.setTextColor(Color.parseColor("#202230"))
                holder.startTime.setPaintFlags(holder.startTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.finishTime.setTextColor(Color.parseColor("#202230"))
                holder.finishTime.setPaintFlags(holder.finishTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.textTask.setTextColor(Color.parseColor("#202230"))
                holder.textTask.setPaintFlags(holder.textTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            }
        }
    }
}
package com.example.studentscheduler

import android.graphics.Color
import android.graphics.Paint
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.example.studentscheduler.room.Task
import com.example.studentscheduler.room.TaskDao
import kotlinx.android.synthetic.main.item_task.view.*
import net.igenius.customcheckbox.CustomCheckBox

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val startTime : TextView = view.startTime
    val finishTime : TextView = view.finishTime
    val textTask : TextView = view.textTask
    val checkBox : CustomCheckBox = view.checkbox
    val separator : ImageView = view.imageView
}

class RecyclerViewAdapter: RecyclerView.Adapter<ViewHolder>() {
    var tasksList = mutableListOf<Task>()

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

        holder.itemView.setOnLongClickListener {
            deleteItem(position)
            true
        }

        //сохранение состояния checkbox'a
        holder.checkBox.setOnCheckedChangeListener { checkBox, isChecked ->
            crossOutTask(holder.checkBox, holder, taskList)
            if (checkBox.isChecked) {
                taskList.processed = true
                MyViewModel().updateTask(taskList)
            }
            else {
                taskList.processed = false
                MyViewModel().updateTask(taskList)
            }
        }

        if (taskList.processed == true) {
            holder.checkBox.setChecked(true)
        }
        else
            holder.checkBox.setChecked(false)
    }

    //удаление итема
    private fun deleteItem(position: Int) {
        MyViewModel().deleteTask(tasksList[position])//удаление задачи из room
        tasksList.remove(tasksList[position])//удаление из списка
        notifyItemRemoved(position)//анимация удаления с recyclerview
        notifyItemRangeChanged(position, tasksList.size)//изменение размера списка
    }

    //выполнение задачи(осветление и зачеркивание)
    private fun crossOutTask(checkBox: CustomCheckBox, holder: ViewHolder, taskList: Task) {
        if (checkBox.isChecked) {
            holder.startTime.setTextColor(Color.parseColor("#D3D1D1"))
            holder.startTime.setPaintFlags(holder.startTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.finishTime.setTextColor(Color.parseColor("#D3D1D1"))
            holder.finishTime.setPaintFlags(holder.finishTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.textTask.setTextColor(Color.parseColor("#D3D1D1"))
            holder.textTask.setPaintFlags(holder.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
            holder.separator.setBackgroundResource(R.drawable.gradient_separate_item)
        }
        else {
            holder.startTime.setTextColor(Color.parseColor("#202230"))
            holder.startTime.setPaintFlags(holder.startTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            holder.finishTime.setTextColor(Color.parseColor("#202230"))
            holder.finishTime.setPaintFlags(holder.finishTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            holder.textTask.setTextColor(Color.parseColor("#202230"))
            holder.textTask.setPaintFlags(holder.textTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
            drawByPriority(holder, taskList)
        }
    }

    private fun drawByPriority(holder: ViewHolder, taskList : Task) {
        if (taskList.priority == 0)
            holder.separator.setBackgroundResource(R.drawable.gradient_separate_item)
        if (taskList.priority == 1)
            holder.separator.setBackgroundResource(R.drawable.green_gradient_separate_item)
        if (taskList.priority == 2)
            holder.separator.setBackgroundResource(R.drawable.lyme_gradient_separate_item)
        if (taskList.priority == 3)
            holder.separator.setBackgroundResource(R.drawable.yellow_gradient_separate_item)
        if (taskList.priority == 4)
            holder.separator.setBackgroundResource(R.drawable.orange_gradient_separate_item)
        if (taskList.priority == 5)
            holder.separator.setBackgroundResource(R.drawable.red_gradient_separate_item)
    }
}
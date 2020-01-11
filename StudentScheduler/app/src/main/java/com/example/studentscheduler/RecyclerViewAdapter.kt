package com.example.studentscheduler

import android.graphics.Color
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.studentscheduler.activity.MainActivity
import com.example.studentscheduler.room.Task
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.item_task.*
import kotlinx.android.synthetic.main.item_task.view.*
import org.threeten.bp.ZonedDateTime
import java.text.DateFormat
import java.text.SimpleDateFormat
import kotlin.concurrent.thread

class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val startTime : TextView = view.startTime
    val finishTime : TextView = view.finishTime
    val textTask : TextView = view.textTask
    val switch : Switch = view.switch1
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
//        tasksList = sortByTime(tasksList, holder) //сортировка по времени
        val taskList = tasksList[position]
        holder.startTime.text = taskList.startTimeTask
        holder.finishTime.text = taskList.finishTimeTask
        holder.textTask.text = taskList.textTask

        drawByPriority(holder, taskList)
        
        holder.itemView.setOnLongClickListener {
            deleteItem(position)

            true
        }

        // свайпы, не работают когда свайпаешь непосредственно по списку
//        holder.itemView.setOnTouchListener(object: OnSwipeTouchListener(MainActivity()) {
//            override fun onSwipeLeft() {
//                MainActivity().nextDate(MainActivity().next_date)
//            }
//
//            override fun onSwipeRight() {
//                MainActivity().prevDate(MainActivity().prev_date)
//            }
//        })

        holder.switch.setOnCheckedChangeListener { buttonView, isChecked ->
//            val switch = MainActivity().switch1
//            crossOutTask(switch, holder)// падает если вынести в отдельную функцию
            if (isChecked) {
                holder.startTime.setTextColor(Color.parseColor("#D3D1D1"))
                holder.startTime.setPaintFlags(holder.startTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.finishTime.setTextColor(Color.parseColor("#D3D1D1"))
                holder.finishTime.setPaintFlags(holder.finishTime.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.textTask.setTextColor(Color.parseColor("#D3D1D1"))
                holder.textTask.setPaintFlags(holder.textTask.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                holder.separator.setBackgroundResource(R.drawable.gradient_separate_item)
                MyViewModel().setProcessed(taskList.id)
            }
            else {
                holder.startTime.setTextColor(Color.parseColor("#202230"))
                holder.startTime.setPaintFlags(holder.startTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.finishTime.setTextColor(Color.parseColor("#202230"))
                holder.finishTime.setPaintFlags(holder.finishTime.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                holder.textTask.setTextColor(Color.parseColor("#202230"))
                holder.textTask.setPaintFlags(holder.textTask.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                drawByPriority(holder, taskList)
                MyViewModel().setProcessed(taskList.id)
            }
        }
    }

    //удаление итема
    private fun deleteItem(position: Int) {
        MyViewModel().deleteTask(tasksList[position])//удаление задачи из room
        tasksList.remove(tasksList[position])//удаление из списка
        notifyItemRemoved(position)//анимация удаления с recyclerview
        notifyItemRangeChanged(position, tasksList.size)//изменение размера списка
    }

    //выполнение задачи(затемнение и зачеркивание)
    private fun crossOutTask(switch: Switch, holder: ViewHolder, taskList: Task) {
        if (switch.isChecked) {
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

    private fun sortByTime(tasksList: MutableList<Task>, holder: ViewHolder): MutableList<Task> {
        var sortedTasksList = mutableListOf<Task>()

        return sortedTasksList
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
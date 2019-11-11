package com.example.studentscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*


class MainActivity: AppCompatActivity()
{
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val currentDate = SimpleDateFormat("dd.M.yyyy").format(Date())

        val date: TextView = findViewById(R.id.date)
        date.setText(currentDate)
    }

    fun addTasksActivity(view: View)
    {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

}
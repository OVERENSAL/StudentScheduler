package com.example.studentscheduler

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        val day = currentDate2.plusDays(1)

        AndroidThreeTen.init(this)

        val currentDate2 = ZonedDateTime.now()
        date.text = currentDate2.toString()
    }

  fun prevDate(view: View) {
        val prevDate = SimpleDateFormat("dd.M.yyyy").format(Date())

        date.text = prevDate
    }

    fun nextDate(view: View) {
        val nextDate = SimpleDateFormat("dd.M.yyyy").format(Date())

        date.text = nextDate
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

}
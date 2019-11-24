package com.example.studentscheduler

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import org.threeten.bp.format.DateTimeFormatter
import java.util.*


class MainActivity : AppCompatActivity() {

    var globalDate = ZonedDateTime.now()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        AndroidThreeTen.init(this)
        val currentDate2 = ZonedDateTime.now()
        date.text = currentDate2.format(DateTimeFormatter.ofPattern("dd.M.yyyy"))
    }

    fun prevDate(view: View) {
        val currentDate = globalDate
        val prevDate = currentDate.minusDays(1)
        globalDate = prevDate
        date.text = prevDate.format(DateTimeFormatter.ofPattern("dd.M.yyyy"))
    }

    fun nextDate(view: View) {
        val currentDate = globalDate
        val nextDate = currentDate.plusDays(1)
        globalDate = nextDate
        date.text = nextDate.format(DateTimeFormatter.ofPattern("dd.M.yyyy"))
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

}
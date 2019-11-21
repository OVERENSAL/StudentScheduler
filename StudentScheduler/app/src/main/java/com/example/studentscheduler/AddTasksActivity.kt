package com.example.studentscheduler

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_add_tasks.*
import java.text.SimpleDateFormat
import java.util.*


class AddTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)

        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //автоматически вылазит клавиатура
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )//не убирается, когда сворачиваешь приложение с открытой клавиатурой

        val c = Calendar.getInstance()

        calendar_button.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                calendar_button.text = day.toString() + "." + month.toString() + "." + year.toString()
            }
            DatePickerDialog(
                this,
                R.style.DatePickerTheme,
                dpd,
                c.get(Calendar.YEAR),
                c.get(Calendar.MONTH),
                c.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        startTimeButton.setOnClickListener {
            val tpd = TimePickerDialog.OnTimeSetListener { timePicker, startHour, startMinute ->
                c.set(Calendar.HOUR_OF_DAY, startHour)
                c.set(Calendar.MINUTE, startMinute)
                startTimeButton.text = SimpleDateFormat("HH:mm").format(c.time)
            }
            TimePickerDialog(
                this,
                R.style.DatePickerTheme,
                tpd,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            ).show()
        }

        finishTimeButton.setOnClickListener {
            val tpd = TimePickerDialog.OnTimeSetListener { timePicker, finishHour, finishMinute ->
                c.set(Calendar.HOUR_OF_DAY, finishHour)
                c.set(Calendar.MINUTE, finishMinute)
                finishTimeButton.text = SimpleDateFormat("HH:mm").format(c.time)
            }
            TimePickerDialog(
                this,
                R.style.DatePickerTheme,
                tpd,
                c.get(Calendar.HOUR_OF_DAY),
                c.get(Calendar.MINUTE),
                true
            ).show()
        }
    }
}

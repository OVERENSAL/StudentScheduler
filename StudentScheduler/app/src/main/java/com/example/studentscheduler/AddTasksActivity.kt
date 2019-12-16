package com.example.studentscheduler

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_add_tasks.*
import java.text.SimpleDateFormat
import java.util.*

class AddTasksActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)
        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel

        //кривая инициализация вьюшек
        if (myViewModel.getDate() != "")
            calendar_button.text = myViewModel.getDate()
        if (myViewModel.getStartTime() != "")
            startTimeButton.text = myViewModel.getStartTime()
        if (myViewModel.getFinishTime() != "")
            finishTimeButton.text = myViewModel.getFinishTime()

        val imm =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //автоматически вылазит клавиатура
        imm.toggleSoftInput(
            InputMethodManager.SHOW_FORCED,
            0
        )//не убирается, когда сворачиваешь приложение с открытой клавиатурой

        val c = Calendar.getInstance()
        //появляется датапикер
        calendar_button.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                myViewModel.setDate(day.toString() + "." + (month + 1).toString() + "." + year.toString())
                calendar_button.text = myViewModel.getDate()
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
        //появляется таймпикер
        startTimeButton.setOnClickListener {
            val tpd = TimePickerDialog.OnTimeSetListener { timePicker, startHour, startMinute ->
                c.set(Calendar.HOUR_OF_DAY, startHour)
                c.set(Calendar.MINUTE, startMinute)
                myViewModel.setStartTime(SimpleDateFormat("HH:mm").format(c.time))
                startTimeButton.text = myViewModel.getStartTime()
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
        //появляется таймпикер
        finishTimeButton.setOnClickListener {
            val tpd = TimePickerDialog.OnTimeSetListener { timePicker, finishHour, finishMinute ->
                c.set(Calendar.HOUR_OF_DAY, finishHour)
                c.set(Calendar.MINUTE, finishMinute)
                myViewModel.setFinishTime(SimpleDateFormat("HH:mm").format(c.time))
                finishTimeButton.text = myViewModel.getFinishTime()
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

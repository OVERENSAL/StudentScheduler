package com.example.studentscheduler.activity

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.studentscheduler.MyViewModel
import com.example.studentscheduler.R
import com.example.studentscheduler.room.Task
import kotlinx.android.synthetic.main.activity_add_tasks.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.text.SimpleDateFormat
import java.util.*

class AddTasksActivity : AppCompatActivity() {
    private lateinit var myViewModel: MyViewModel
    private lateinit var imm : InputMethodManager
    private lateinit var curr: ZonedDateTime
    private lateinit var lastDate: ZonedDateTime

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)

        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel
        myViewModel.showTaskEvent.observe(this, androidx.lifecycle.Observer { text ->
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        })

        curr = myViewModel.getGlobalDate()
        lastDate = myViewModel.getGlobalDate()

        //принятие текущего числа в calendar_button
        val arg = intent.extras
        val name = arg!!.get("InternalDate")!!.toString()

        //определение полей при пересоздании активити
        if (!(myViewModel.getInternalDate().isEmpty()))
            calendar_button.text = myViewModel.getInternalDate()
        else
            calendar_button.text = name
        if (!(myViewModel.getStartTime().isEmpty()))
            startTimeButton.text = myViewModel.getStartTime()
        if (!(myViewModel.getFinishTime().isEmpty()))
            finishTimeButton.text = myViewModel.getFinishTime()
        ratingBar.setRating(1.0F)

        val c = Calendar.getInstance()
        //появляется датапикер
        calendar_button.setOnClickListener {
            dataPicker(c, calendar_button)
        }
        //появляется таймпикер
        startTimeButton.setOnClickListener {
            timePicker(c, startTimeButton)
        }
        //появляется таймпикер
        finishTimeButton.setOnClickListener {
            timePicker(c, finishTimeButton)
        }
        //появляется датапикер
        select_end_date.setOnClickListener {
            dataPicker(c, select_end_date)
        }

        //добавление задачи в Room
        addTaskButton.setOnClickListener {
            val task = Task(
                id = 0,
                dateTask = calendar_button.text.toString(),
                startTimeTask = startTimeButton.text.toString(),
                finishTimeTask = finishTimeButton.text.toString(),
                textTask = editText.text.toString(),
                priority = ratingBar.rating.toInt(),
                processed = false
            )
            editText.setText("")
            select_end_date.text = "Select end date"
            ratingBar.setRating(1.0F)

            myViewModel.saveTask(task)
            Toast.makeText(applicationContext, curr.plusDays(1).toString(), Toast.LENGTH_SHORT).show()
            if (radioButton1.isChecked) {
                while (curr.isBefore(lastDate)) {
                    Thread.sleep(100) //из за того что сохраняется задача в другом потоке, даты успевают обновиться и
                    curr = curr.plusDays(1)//задачи могут добавляться по нескольку на один день, приостановка спасает
                    task.dateTask = curr.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                    myViewModel.saveTask(task)
                }
            }
            else if (radioButton2.isChecked) {
                while (curr.isBefore(lastDate)) {
                    Thread.sleep(100) //из за того что сохраняется задача в другом потоке, даты успевают обновиться и
                    curr = curr.plusDays(7)//задачи могут добавляться по нескольку на один день, приостановка спасает
                    task.dateTask = curr.format(DateTimeFormatter.ofPattern(DATE_FORMAT))
                    myViewModel.saveTask(task)
                }
            }
            radioGroup.clearCheck()
        }
    }

    private fun dataPicker(c: Calendar, button: Button) {
        val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
            c.set(Calendar.YEAR, year)
            c.set(Calendar.MONTH, month)
            c.set(Calendar.DAY_OF_MONTH, day)
            if (button == calendar_button)
                curr = ZonedDateTime.of(year, month + 1, day, 0, 0, 0,0, ZoneOffset.UTC)
            else
                lastDate = ZonedDateTime.of(year, month + 1, day, 0, 0, 0,0, ZoneOffset.UTC)
            myViewModel.setInternalDate((ZonedDateTime.of(year, month + 1, day, 0, 0, 0,0, ZoneOffset.UTC))
                .format(DateTimeFormatter.ofPattern(DATE_FORMAT)))
            button.text = myViewModel.getInternalDate()
        }
        DatePickerDialog(
            this,
            R.style.MainTheme,
            dpd,
            c.get(Calendar.YEAR),
            c.get(Calendar.MONTH),
            c.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun timePicker(c: Calendar, button: Button) {
        val tpd = TimePickerDialog.OnTimeSetListener { timePicker, startHour, startMinute ->
            c.set(Calendar.HOUR_OF_DAY, startHour)
            c.set(Calendar.MINUTE, startMinute)
            myViewModel.setStartTime(SimpleDateFormat(TIME_FORMAT).format(c.time))
            button.text = myViewModel.getStartTime()
        }
        TimePickerDialog(
            this,
            R.style.MainTheme,
            tpd,
            c.get(Calendar.HOUR_OF_DAY),
            c.get(Calendar.MINUTE),
            true
        ).show()
    }

    //принудительное скрытие клавиатуры
    override fun onPause() {
        super.onPause()

        if (imm.isActive())
            imm.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()

        //отображения клавиатуры принудительно, не работает через запуск с трея
        imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
    }

    companion object {
        const val DATE_FORMAT = "dd.MM.yyyy"
        const val TIME_FORMAT = "HH:mm"

    }
}
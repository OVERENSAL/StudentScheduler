package com.example.studentscheduler.activity

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)

        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel
        myViewModel.showTaskEvent.observe(this, androidx.lifecycle.Observer { text ->
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
        })

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

        val c = Calendar.getInstance()
        //появляется датапикер
        calendar_button.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                myViewModel.setInternalDate((ZonedDateTime.of(year, month + 1, day, 0, 0, 0,0, ZoneOffset.UTC))
                    .format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))) //такая кривая реализация даты, потому DAY_OF_MONTH и MONTH возвращает
                calendar_button.text = myViewModel.getInternalDate()            //число до 10 без 0 впереди, т.е 1, а не 01, как надо
            }                                                                   //в дальнейшем возникли проблемы с отображением записей из комнаты
            DatePickerDialog(                                                   //т.к. дата на запрос передается в формате dd.MM.yyyy,
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

            myViewModel.saveTask(task)
        }
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
}
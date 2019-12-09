package com.example.studentscheduler

import android.app.Application
import android.content.Intent
import android.graphics.Canvas
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.jakewharton.threetenabp.AndroidThreeTen
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZonedDateTime
import java.text.SimpleDateFormat
import org.threeten.bp.format.DateTimeFormatter
import java.util.*

// TODO("Посмотреть способы сохранения состояние экрана после переворота ViewModels или Moxy")
class MainActivity : AppCompatActivity() {

    private lateinit var myViewModel: MyViewModel
    var globalDate: ZonedDateTime = ZonedDateTime.now() //определение глобального времени для прибавления/вычитания даты

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel

        globalDate = myViewModel.getGlobalDate()//получаем значение глобальной даты
        if (myViewModel.getDate() == "") //костыль на отображение даты первый раз
            date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        else
            date.text = myViewModel.getDate()
    }

    fun prevDate(view: View) {
        val currentDate = globalDate
        val prevDate = currentDate.minusDays(1)
        myViewModel.setGlobalDate(prevDate)
        globalDate = myViewModel.getGlobalDate()//сохраняем значение глобальной даты
        date.text = prevDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(prevDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))//сохраняем измененную дату
    }

    fun nextDate(view: View) {
        val currentDate = globalDate
        val nextDate = currentDate.plusDays(1)
        myViewModel.setGlobalDate(nextDate)
        globalDate = myViewModel.getGlobalDate()
        date.text = nextDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(nextDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

    companion object {
        const val TOOLBAR_DATE_FORMAT = "dd.MM.yyyy"
    }
}
package com.example.studentscheduler

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

/*TODO: Задача не отображается при добавлении на текущий день и возврате на MainActivity
 */
class MainActivity : AppCompatActivity() {

    private lateinit var myViewModel: MyViewModel
    private val room : TaskDataBase = CalendarApplication.instance.room
    private val adapter = RecyclerViewAdapter()
    var globalDate: ZonedDateTime = ZonedDateTime.now() //определение глобального времени для прибавления/вычитания дней

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel

        globalDate = myViewModel.getGlobalDate()//получаем значение глобальной даты
        if (myViewModel.getDate() == "") //костыль на отображение даты первый раз
            date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        else
            date.text = myViewModel.getDate()

        recyclerView.adapter = adapter
        getAllTasksByDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))

        val c = Calendar.getInstance()

        calendar_icon.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                globalDate = ZonedDateTime.of(year, month + 1, day, 0, 0, 0, 0, ZoneOffset.UTC )
                myViewModel.setGlobalDate(globalDate)
                date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
                myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))

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
    }

    fun currDate(view: View) {
        globalDate = ZonedDateTime.now()
        myViewModel.setGlobalDate(globalDate)
        date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
        getAllTasksByDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
    }

    fun prevDate(view: View) {
        globalDate = globalDate.minusDays(1)
        myViewModel.setGlobalDate(globalDate)//изменяем глобальную дату во вьюмодели
        date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))//сохраняем измененную дату
        getAllTasksByDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
    }

    fun nextDate(view: View) {
        globalDate = globalDate.plusDays(1)
        myViewModel.setGlobalDate(globalDate)
        date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
        getAllTasksByDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

    fun getAllTasksByDate(date: String) {
        thread {
            adapter.tasksList = room.taskDao().getAllTasksByDate(date)
            runOnUiThread {                     //меняю айтемы recView в UI потоке
                adapter.notifyDataSetChanged()  //иначе вылетает, странно что не сразу
            }
        }
    }

    companion object {
        const val TOOLBAR_DATE_FORMAT = "dd.MM.yyyy"
    }
}
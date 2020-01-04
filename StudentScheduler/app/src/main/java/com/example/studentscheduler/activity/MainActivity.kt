package com.example.studentscheduler.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProviders
import com.example.studentscheduler.*
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread

/*TODO: ГЛАВНАЯ:
        - Убрать кнопки переключения дней, сделать свайпами
        - Изменить формат вывода даты на что-то вроде Friday, January 3
        - Скорректировать итемы для удобного и приятного просмотра задач +-
        - Добавить функцию просмотра, удаления и выполнения(зачеркивание) задачи
        - Сортировать задачи по начальному времени по возрастанию
        - Выполненные задачи перекидывать вниз списка
        ФОРМА ДОБАВЛЕНИЯ:
        - По умолчанию ставить дату текущего или выбранного дня
        - Изначально устанавливать на времени ближайшее последнее время
        - Решить проблемы с исчезанием клавиатуры // не вылазит при запуске с трея, хотя описано в onResume()????
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

        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))

        date.text = myViewModel.getDate() //отображение даты первый раз

//        adapter = RecyclerViewAdapter(object : RecyclerViewAdapter.OnItemClickListener {
//            override fun onClickListener(task: Task) {
//                Toast.makeText(baseContext, "gg", Toast.LENGTH_SHORT).show()
//            }
//        })

        recyclerView.adapter = adapter
        getAllTasksByDate(myViewModel.getDate())

        val c = Calendar.getInstance()

        calendar_icon.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                globalDate = ZonedDateTime.of(year, month + 1, day, 0, 0, 0, 0, ZoneOffset.UTC )
                myViewModel.setGlobalDate(globalDate)
                myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(
                    TOOLBAR_DATE_FORMAT
                )))
                date.text = myViewModel.getDate()
                getAllTasksByDate(myViewModel.getDate())
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
        getAllTasksByDate(myViewModel.getDate())
    }

    fun prevDate(view: View) {
        globalDate = globalDate.minusDays(1)
        myViewModel.setGlobalDate(globalDate)//изменяем глобальную дату во вьюмодели
        date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))//сохраняем измененную дату
        getAllTasksByDate(myViewModel.getDate())
    }

    fun nextDate(view: View) {
        globalDate = globalDate.plusDays(1)
        myViewModel.setGlobalDate(globalDate)
        date.text = globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT))
        myViewModel.setDate(globalDate.format(DateTimeFormatter.ofPattern(TOOLBAR_DATE_FORMAT)))
        getAllTasksByDate(myViewModel.getDate())
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        startActivity(addTasksActivityIntent)
    }

    fun getAllTasksByDate(date: String) {
        thread {
            adapter.tasksList = room.taskDao().getAllTasksByDate(date)
            runOnUiThread {                     //обновляю айтемы recView в UI потоке
                adapter.notifyDataSetChanged()  //иначе вылетает, странно что не сразу
            }
        }
    }

    // Отображение задач при добавлении на текущий день и возврате в MainActivity
    override fun onResume() {
        super.onResume()

        getAllTasksByDate(myViewModel.getDate())
    }

    companion object {
        const val TOOLBAR_DATE_FORMAT = "dd.MM.yyyy"
    }
}
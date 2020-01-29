package com.example.studentscheduler.activity

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModelProviders
import com.example.studentscheduler.*
import com.example.studentscheduler.room.Task
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.time.LocalTime
import java.util.*
import kotlin.concurrent.thread

/*TODO: ГЛАВНАЯ:
        - Сделать возможность добавления постоянных задач
 */

class MainActivity : AppCompatActivity() {

    private lateinit var myViewModel: MyViewModel
    private val room : TaskDataBase = CalendarApplication.instance.room
    private val adapter = RecyclerViewAdapter()
    private var globalDate: ZonedDateTime = ZonedDateTime.now() //определение глобального времени для прибавления/вычитания дней

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        myViewModel = ViewModelProviders.of(this)[MyViewModel::class.java] //определение viewmodel

        globalDate = myViewModel.getGlobalDate() //вытаскиваем глобальную дату из вьюмодели в случае пересоздания активити

        //отображение даты впервые и сохранение состояния
        if (myViewModel.getExternalDate() == "") {
            myViewModel.setInternalDate(
                globalDate.format(
                    DateTimeFormatter.ofPattern(
                        INTERNAL_DATE_FORMAT
                    )
                )
            )
            myViewModel.setExternalDate(
                globalDate.format(
                    DateTimeFormatter.ofPattern(
                        EXTERNAL_DATE_FORMAT
                    )
                )
            )
            date.text = myViewModel.getExternalDate()
        }
        else {
            date.text = myViewModel.getExternalDate()
        }

        recyclerView.adapter = adapter

        val c = Calendar.getInstance()

        calendar_icon.setOnClickListener {
            val dpd = DatePickerDialog.OnDateSetListener { dataPicker, year, month, day ->
                c.set(Calendar.YEAR, year)
                c.set(Calendar.MONTH, month)
                c.set(Calendar.DAY_OF_MONTH, day)
                globalDate = ZonedDateTime.of(year, month + 1, day, 0, 0, 0, 0, ZoneOffset.UTC )
                myViewModel.setGlobalDate(globalDate)
                myViewModel.setInternalDate(globalDate.format(DateTimeFormatter.ofPattern(
                    INTERNAL_DATE_FORMAT
                )))
                myViewModel.setExternalDate(globalDate.format(DateTimeFormatter.ofPattern(
                    EXTERNAL_DATE_FORMAT
                )))
                date.text = myViewModel.getExternalDate()
                getAllTasksByDate(myViewModel.getInternalDate())
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
        date.text = globalDate.format(DateTimeFormatter.ofPattern(EXTERNAL_DATE_FORMAT))
        myViewModel.setInternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            INTERNAL_DATE_FORMAT
        )))
        myViewModel.setExternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            EXTERNAL_DATE_FORMAT
        )))

        getAllTasksByDate(myViewModel.getInternalDate())
    }

    fun prevDate(view: View) {
        globalDate = globalDate.minusDays(1)
        myViewModel.setGlobalDate(globalDate)//изменяем глобальную дату во вьюмодели
        date.text = globalDate.format(DateTimeFormatter.ofPattern(EXTERNAL_DATE_FORMAT))
        myViewModel.setInternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            INTERNAL_DATE_FORMAT
        )))//сохраняем измененную дату
        myViewModel.setExternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            EXTERNAL_DATE_FORMAT
        )))

        getAllTasksByDate(myViewModel.getInternalDate())
    }

    fun nextDate(view: View) {
        globalDate = globalDate.plusDays(1)
        myViewModel.setGlobalDate(globalDate)
        date.text = globalDate.format(DateTimeFormatter.ofPattern(EXTERNAL_DATE_FORMAT))
        myViewModel.setInternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            INTERNAL_DATE_FORMAT
        )))
        myViewModel.setExternalDate(globalDate.format(DateTimeFormatter.ofPattern(
            EXTERNAL_DATE_FORMAT
        )))

        getAllTasksByDate(myViewModel.getInternalDate())
    }

    fun addTasksActivity(view: View) {
        val addTasksActivityIntent = Intent(this, AddTasksActivity::class.java)
        addTasksActivityIntent.putExtra("InternalDate", myViewModel.getInternalDate())//передача текущего числа
        startActivity(addTasksActivityIntent)
    }

    private fun getAllTasksByDate(date: String) {
        thread {
            //берем список задач из комнаты и сортируем, отправляем в адаптер уже отсортированный список
            adapter.tasksList = sortTaskByTime(room.taskDao().getAllTasksByDate(date))
            runOnUiThread {                     //обновляю айтемы recView в UI потоке
                adapter.notifyDataSetChanged()  //иначе вылетает, странно что не сразу
            }
        }
    }

    private fun sortTaskByTime(tasksList: MutableList<Task>): MutableList<Task> {
        val sortedTasksList = mutableListOf<Task>()

        for (i in 0 until tasksList.size) {
            var minTime = LocalTime.parse("23:59")
            var minTaskByTime = tasksList[0]
            for (j in 0 until tasksList.size) {
                val time = LocalTime.parse(tasksList[j].startTimeTask)
                if (minTime.isAfter(time)) {
                    minTime = time
                    minTaskByTime = tasksList[j]
                }
            }
            sortedTasksList.add(minTaskByTime)
            tasksList.remove(minTaskByTime)
        }
        return sortedTasksList
    }

    // Отображение задач при добавлении на текущий день и возврате в MainActivity
    override fun onResume() {
        super.onResume()

        getAllTasksByDate(myViewModel.getInternalDate())
    }

    companion object {
        const val INTERNAL_DATE_FORMAT = "dd.MM.yyyy"
        const val EXTERNAL_DATE_FORMAT = "EEEE, MMMM d"
    }
}
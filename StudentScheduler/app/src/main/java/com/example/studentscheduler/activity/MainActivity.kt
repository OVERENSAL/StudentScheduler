package com.example.studentscheduler.activity

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.studentscheduler.*
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.android.synthetic.main.activity_main.*
import org.threeten.bp.ZoneOffset
import org.threeten.bp.ZonedDateTime
import org.threeten.bp.format.DateTimeFormatter
import java.util.*
import kotlin.concurrent.thread
import androidx.recyclerview.widget.RecyclerView
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import androidx.core.content.ContextCompat.getSystemService
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.widget.Toast


/*TODO: ГЛАВНАЯ:
        - Запилить выполение в комнату
        - Сортировать задачи по начальному времени по возрастанию
        - Выполненные задачи перекидывать вниз списка
        ФОРМА ДОБАВЛЕНИЯ:
        - По умолчанию ставить дату текущего или выбранного дня
        - Изначально устанавливать на времени ближайшее последнее время
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

        //переключение дней свайпами, корректно работает на месте где нет итемов
        //после переключений дней нельзя сразу оперировать итемами, нужно сначала нажать на список
//        recyclerView.setOnTouchListener(object:OnSwipeTouchListener(this@MainActivity) {
//            override fun onSwipeLeft() {
//                nextDate(next_date)
//            }
//
//            override fun onSwipeRight() {
//                prevDate(prev_date)
//            }
//        })
//        ItemTouchHelper(itemTouchHelperCallback).attachToRecyclerView(recyclerView)
    }

//    private val itemTouchHelperCallback: ItemTouchHelper.SimpleCallback =
//        object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
//            override fun onMove(
//                recyclerView: RecyclerView,
//                viewHolder: RecyclerView.ViewHolder,
//                target: RecyclerView.ViewHolder
//            ): Boolean {
//                return false
//            }
//
//            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
//                RecyclerViewAdapter().deleteItem(viewHolder.adapterPosition)
//            }
//        }

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
        startActivity(addTasksActivityIntent)
    }

    private fun getAllTasksByDate(date: String) {
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

        getAllTasksByDate(myViewModel.getInternalDate())
    }

    companion object {
        const val INTERNAL_DATE_FORMAT = "dd.MM.yyyy"
        const val EXTERNAL_DATE_FORMAT = "EEEE, MMMM d"
    }
}
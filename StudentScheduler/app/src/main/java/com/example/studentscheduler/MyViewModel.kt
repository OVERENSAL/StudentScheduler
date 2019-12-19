package com.example.studentscheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class MyViewModel : ViewModel() {
    private var date : String = ""
    private var startTime : String = ""
    private var finishTime : String = ""
    private var globalDate : ZonedDateTime = ZonedDateTime.now()
    private val room : TaskDataBase = CalendarApplication.room

    private val showTaskEvent = SingleLiveEvent<String>()

    fun setDate(date: String) {
        this.date = date
    }

    fun getDate() = date

    fun setGlobalDate(globalDate: ZonedDateTime) {
        this.globalDate = globalDate
    }

    fun getGlobalDate() = globalDate

    fun setStartTime(startTime: String) {
        this.startTime = startTime
    }

    fun getStartTime() = startTime

    fun setFinishTime(finishTime: String) {
        this.finishTime = finishTime
    }

    fun getFinishTime() = finishTime

    fun saveTask(task : Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                room.taskDao().insert(task) //crash
            }

            showTaskEvent.value = "Задача добавлена"
        }
    }
}

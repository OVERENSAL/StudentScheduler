package com.example.studentscheduler

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.studentscheduler.activity.MainActivity
import com.example.studentscheduler.room.Task
import com.example.studentscheduler.room.TaskDataBase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.threeten.bp.ZonedDateTime

class MyViewModel : ViewModel() {
    private var internalDate : String = ""
    private var externalDate : String = ""
    private var startTime : String = ""
    private var finishTime : String = ""
    private var globalDate : ZonedDateTime = ZonedDateTime.now()
    private val room : TaskDataBase = CalendarApplication.instance.room

    val showTaskEvent = SingleLiveEvent<String>()

    fun setExternalDate(externalDate: String) {
        this.externalDate = externalDate
    }

    fun getExternalDate() = externalDate

    fun setInternalDate(internalDate: String) {
        this.internalDate = internalDate
    }

    fun getInternalDate() = internalDate

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
                room.taskDao().insert(task)
            }

            showTaskEvent.value = "Задача добавлена"
        }
    }

    fun deleteTask(task: Task) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                room.taskDao().delete(task)
            }

            showTaskEvent.value = "Задача удалена"
        }
    }
}

package com.example.studentscheduler

import androidx.lifecycle.ViewModel
import org.threeten.bp.ZonedDateTime

class MyViewModel : ViewModel() {
    private var date : String = ""
    private var startTime : String = ""
    private var finishTime : String = ""
    private var globalDate : ZonedDateTime = ZonedDateTime.now()

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
}

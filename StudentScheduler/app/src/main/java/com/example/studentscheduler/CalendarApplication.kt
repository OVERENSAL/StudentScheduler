package com.example.studentscheduler

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

class CalendarApplication : Application() {

    lateinit var room : TaskDataBase

    override fun onCreate() {
        super.onCreate()
        AndroidThreeTen.init(this)

        room =  TaskDataBase.getInstance(applicationContext)!!
        instance = this
    }

    companion object {
        lateinit var instance: CalendarApplication
    }

}

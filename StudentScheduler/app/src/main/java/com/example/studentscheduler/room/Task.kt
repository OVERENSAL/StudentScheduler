package com.example.studentscheduler.room

import androidx.room.*

@Entity
data class Task (
    @PrimaryKey (autoGenerate = true) var id: Long,
    @ColumnInfo(name = "dateTask") var dateTask: String,
    @ColumnInfo(name = "startTimeTask") var startTimeTask: String,
    @ColumnInfo(name = "finishTimeTask") var finishTimeTask: String,
    @ColumnInfo(name = "textTask") var textTask: String,
    @ColumnInfo(name = "priority") var priority: Int? = 0,
    @ColumnInfo(name = "processed") var processed: Boolean? = false
)

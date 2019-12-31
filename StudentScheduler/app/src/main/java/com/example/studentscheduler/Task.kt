package com.example.studentscheduler

import androidx.room.*

@Entity
data class Task (
    @PrimaryKey (autoGenerate = true) var id: Long,
    @ColumnInfo(name = "dateTask") var dateTask: String,
    @ColumnInfo(name = "startTimeTask") var startTimeTask: String,
    @ColumnInfo(name = "finishTimeTask") var finishTimeTask: String,
    @ColumnInfo(name = "textTask") var textTask: String
//    var priority:
)

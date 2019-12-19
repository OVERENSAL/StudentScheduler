package com.example.studentscheduler

import androidx.room.*

@Entity
data class Task (
    @PrimaryKey var id: Int,
    var dateTask: String,
    var startTimeTask: String,
    var finishTimeTask: String,
    var textTask: String
//    var priority:
)

package com.example.studentscheduler

import androidx.room.*

@Entity
class Task {
    var startTimeTask: String = ""
    var finishTimeTask: String = ""
    var textTask: String = ""
//    var priority:
    @PrimaryKey var dateTask: String = ""

}

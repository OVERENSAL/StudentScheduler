package com.example.studentscheduler

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import org.threeten.bp.ZonedDateTime

@Entity
class Task {
//    @PrimaryKey //определиться с ключом
    var startTimeTask: String = ""
    var finishTimeTask: String = ""
    var textTask: String = ""
    var dateTask: ZonedDateTime = ZonedDateTime.now()

}

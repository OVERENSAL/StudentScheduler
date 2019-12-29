package com.example.studentscheduler

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE dateTask LIKE :date")
    fun getAllTasksByDate(date : String): List<Task> //вывод всех задач, у которых дата: date

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task : Task)

    @Delete
    fun delete(task : Task)
}
package com.example.studentscheduler.room

import androidx.room.*

@Dao
interface TaskDao {
    @Query("SELECT * FROM task WHERE dateTask LIKE :date")
    fun getAllTasksByDate(date : String): MutableList<Task> //вывод всех задач, у которых дата: date

    @Query("SELECT * FROM task WHERE id LIKE :id")
    fun getTaskById(id: Long): Task

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(task : Task)

    @Update
    fun update(task: Task)

    @Delete
    fun delete(task: Task)
}
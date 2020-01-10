package com.example.studentscheduler.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = arrayOf(Task::class), version = 4)
abstract class TaskDataBase : RoomDatabase() {

    abstract fun taskDao() : TaskDao

    companion object {
        private var INSTANCE: TaskDataBase? = null

        fun getInstance(context: Context): TaskDataBase? {
            val MIGRATION_3_4 = object: Migration(3, 4) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    database.execSQL("CREATE TABLE TaskTemp('id' INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, " +
                            "'dateTask' TEXT NOT NULL, " +
                            "'startTimeTask' TEXT NOT NULL, " +
                            "'finishTimeTask' TEXT NOT NULL, " +
                            "'textTask' TEXT NOT NULL, " +
                            "'priority' INTEGER, " +
                            "'processed' INTEGER)")
                    database.execSQL("INSERT INTO TaskTemp(id, dateTask, startTimeTask, finishTimeTask, textTask) SELECT id, dateTask, startTimeTask, finishTimeTask, textTask FROM Task")
                    database.execSQL("DROP TABLE Task")
                    database.execSQL("ALTER TABLE 'TaskTemp' RENAME TO 'Task'")
                }
            }
            if (INSTANCE == null) {
                synchronized(TaskDataBase::class) {
                    INSTANCE = Room.databaseBuilder(context,
                        TaskDataBase::class.java,
                        "task.db")
                        .addMigrations(MIGRATION_3_4)
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }
    }
}
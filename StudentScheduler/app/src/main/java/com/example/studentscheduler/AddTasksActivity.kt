package com.example.studentscheduler

import android.app.DatePickerDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.Context.INPUT_METHOD_SERVICE
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.app.ComponentActivity
import androidx.core.app.ComponentActivity.ExtraData
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.view.inputmethod.InputMethodManager
import kotlinx.android.synthetic.main.activity_add_tasks.*
import java.util.*


class AddTasksActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_tasks)

        val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager //автоматически вылазит клавиатура
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)//не убирается, когда сворачиваешь приложение с открытой клавиатурой

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val dayOfMonth = c.get(Calendar.DAY_OF_MONTH)

        calendar_button.setOnClickListener{
            val dpd = DatePickerDialog(this, R.style.DatePickerTheme, DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth -> }, year, month, dayOfMonth)
            dpd.show()
        }
    }
}

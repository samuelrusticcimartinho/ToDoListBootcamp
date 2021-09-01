package br.com.dio.todolist.Ui

import android.app.Activity
import android.nfc.Tag
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.datasource.taskdatasource
import br.com.dio.todolist.extensions.format
import br.com.dio.todolist.extensions.text
import br.com.dio.todolist.model.Task
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.timepicker.MaterialTimePicker
import com.google.android.material.timepicker.TimeFormat
import java.util.*

class addtaskactivity: AppCompatActivity() {

    private lateinit var binding: ActivityAddTaskBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityAddTaskBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (intent.hasExtra(TASK_ID)) {
            val taskId = intent.getIntExtra(TASK_ID, 0)
            taskdatasource.findById(taskId)?. let {
                binding.tilTitle.text = it.title
                binding.tilDate.text = it.date
                binding.tilHour.text = it.hour

            }

        }

        insertListeners()
    }
            private fun insertListeners() {
               binding.tilDate.editText?.setOnClickListener {
                    val datePicker = MaterialDatePicker.Builder.datePicker().build()
                   datePicker.addOnPositiveButtonClickListener{
                       val timeZone = TimeZone.getDefault()
                       val offset = timeZone.getOffset(Date().time) * -1
                       binding.tilDate.text = Date(it + offset).format()
                   }
                   datePicker.show(supportFragmentManager, "Date_picker_TAG")
                }

               binding.tilHour.editText?.setOnClickListener {
                    val timerPicker = MaterialTimePicker.Builder()
                        .setTimeFormat(TimeFormat.CLOCK_24H)
                        .build()

                   timerPicker.addOnPositiveButtonClickListener{
                   val minute = if (timerPicker.minute in 0..9) "0${timerPicker.minute}" else timerPicker.minute
                   val hour = if (timerPicker.hour in 0..9) "0${timerPicker.hour}" else timerPicker.hour

                       binding.tilHour.text = "$hour:$minute"
                   }

                   timerPicker.show(supportFragmentManager, null)
                }

               binding.btnCancel.setOnClickListener {
                    finish()
                }

               binding.btnNewTask.setOnClickListener {
                   val task = Task(
                       title = binding.tilTitle.text,
                       date = binding.tilDate.text,
                       hour = binding.tilHour.text,
                       id = intent.getIntExtra(TASK_ID,0)

                   )
                   taskdatasource.insertTask(task)

                   setResult(Activity.RESULT_OK)
                      finish()
                }
    }
    companion object {
        const val  TASK_ID = "task_id"
    }

}
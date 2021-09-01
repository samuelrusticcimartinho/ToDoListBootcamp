package br.com.dio.todolist

import android.app.Activity
import android.content.Intent
import android.content.res.Configuration
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import br.com.dio.todolist.Ui.TaskListAdapter
import br.com.dio.todolist.Ui.addtaskactivity
import br.com.dio.todolist.databinding.ActivityAddTaskBinding
import br.com.dio.todolist.databinding.ActivityMainBinding
import br.com.dio.todolist.datasource.taskdatasource

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val adapter by lazy { TaskListAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvTask.adapter = adapter
        updateList()

        insertListeners()
        // DATA SOURCE
        // ROOM

    }

    private fun insertListeners() {
        binding.fab.setOnClickListener{
            startActivityForResult(Intent(this, addtaskactivity::class.java), CREATE_NEW_TASK)
        }
        adapter.listenerEdit = {
            val intent = Intent(this, addtaskactivity::class.java)
            intent.putExtra(addtaskactivity.TASK_ID, it.id)
            startActivityForResult(intent, CREATE_NEW_TASK)
        }
        adapter.listenerDelete = {
            taskdatasource.deletetask(it)
            updateList()

        }
}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == CREATE_NEW_TASK && resultCode == Activity.RESULT_OK) updateList()

        }

     private fun updateList() {
         val list = taskdatasource.getList()
            binding.includeEmpty.emptyState.visibility = if (list.isEmpty()) View.VISIBLE
          else View.GONE

         adapter.submitList(list)
     }



    companion object {
        private const val CREATE_NEW_TASK = 1000

    }
}


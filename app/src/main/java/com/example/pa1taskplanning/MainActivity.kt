package com.example.pa1taskplanning

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class MainActivity : AppCompatActivity() {
    private lateinit var buttonLogout: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var user: FirebaseUser
    private lateinit var taskInput: EditText
    private lateinit var addTaskButton: Button
    private lateinit var taskListView: ListView
    private lateinit var taskAdapter: ArrayAdapter<String>
    private var tasks = mutableListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Firebase and UI elements initialization
        auth = FirebaseAuth.getInstance()
        buttonLogout = findViewById(R.id.logoutButton)
        taskInput = findViewById(R.id.taskInput)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskListView = findViewById(R.id.taskListView)
        user = auth.currentUser!!

        // initialize the task list adapter
        taskAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, tasks)
        taskListView.adapter = taskAdapter
        taskListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        addTaskButton.setOnClickListener {
            val task = taskInput.text.toString()
            if (task.isNotBlank() && tasks.size < 10) {
                tasks.add(task)
                taskAdapter.notifyDataSetChanged()
                taskInput.text.clear()
            } else if (tasks.size >= 10) {
                Toast.makeText(this, "Can only add 10 tasks at a time", Toast.LENGTH_SHORT).show()
            }
        }

        taskListView.setOnItemClickListener { _, _, position, _ ->
            val task = tasks[position]
            tasks.removeAt(position)
            taskAdapter.notifyDataSetChanged()

            for (i in 0 until taskListView.count) {
                taskListView.setItemChecked(i, false)
            }

            Snackbar.make(taskListView, "Task deleted", Snackbar.LENGTH_LONG)
                .setAction("UNDO") {
                    tasks.add(position, task)
                    taskAdapter.notifyDataSetChanged()
                }.show()
        }

        buttonLogout.setOnClickListener {
            auth.signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }
    }
}



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
    private val taskList = ArrayList<String>()
    private val maxTasks = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        user = auth.currentUser ?: run {
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
            return
        }

        // Initialize UI elements
        buttonLogout = findViewById(R.id.logoutButton)
        taskInput = findViewById(R.id.taskInput)
        addTaskButton = findViewById(R.id.addTaskButton)
        taskListView = findViewById(R.id.taskListView)

        // Set up task list adapter
        taskAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_multiple_choice, taskList)
        taskListView.adapter = taskAdapter
        taskListView.choiceMode = ListView.CHOICE_MODE_MULTIPLE

        // Set logout button functionality
        buttonLogout.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            val intent = Intent(applicationContext, Login::class.java)
            startActivity(intent)
            finish()
        }

        // Add task button functionality
        addTaskButton.setOnClickListener {
            if (taskList.size >= maxTasks) {
                Toast.makeText(this, "You can only add 10 tasks at a time.", Toast.LENGTH_SHORT).show()
            } else {
                val task = taskInput.text.toString()
                if (task.isNotEmpty()) {
                    taskList.add(task)
                    taskAdapter.notifyDataSetChanged()
                    taskInput.text.clear()
                }
            }
        }
    }
}

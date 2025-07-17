package sv.edu.udb.todolistapp

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainActivity : AppCompatActivity() {

    private lateinit var editText: EditText
    private lateinit var buttonAdd: Button
    private lateinit var listView: ListView

    private val tasks = mutableListOf<Task>()
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editText = findViewById(R.id.editTextTask)
        buttonAdd = findViewById(R.id.buttonAdd)
        listView = findViewById(R.id.listViewTasks)

        adapter = TaskAdapter(this, tasks)
        listView.adapter = adapter

        buttonAdd.setOnClickListener {
            val taskText = editText.text.toString()
            if (taskText.isNotEmpty()) {
                tasks.add(Task(taskText))
                adapter.notifyDataSetChanged()
                editText.text.clear()
            }
        }

        listView.setOnItemClickListener { _, _, position, _ ->
            val task = tasks[position]
            task.isDone = !task.isDone
            adapter.notifyDataSetChanged()
        }

        listView.setOnItemLongClickListener { _, _, position, _ ->
            tasks.removeAt(position)
            adapter.notifyDataSetChanged()
            true
        }
    }

    override fun onPause() {
        super.onPause()
        val prefs = getSharedPreferences("tasks", MODE_PRIVATE)
        val editor = prefs.edit()
        val json = Gson().toJson(tasks)
        editor.putString("taskList", json)
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val prefs = getSharedPreferences("tasks", MODE_PRIVATE)
        val json = prefs.getString("taskList", null)
        if (json != null) {
            val type = object : TypeToken<MutableList<Task>>() {}.type
            val savedTasks: MutableList<Task> = Gson().fromJson(json, type)
            tasks.clear()
            tasks.addAll(savedTasks)
            adapter.notifyDataSetChanged()
        }
    }
}
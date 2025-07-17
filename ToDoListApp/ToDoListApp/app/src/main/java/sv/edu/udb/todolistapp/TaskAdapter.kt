package sv.edu.udb.todolistapp

import android.content.Context
import android.graphics.Paint
import android.view.*
import android.widget.*

class TaskAdapter(private val context: Context, private val tasks: MutableList<Task>) :
    ArrayAdapter<Task>(context, 0, tasks) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val task = tasks[position]
        val view = convertView ?: LayoutInflater.from(context)
            .inflate(android.R.layout.simple_list_item_multiple_choice, parent, false)

        val textView = view.findViewById<TextView>(android.R.id.text1)
        textView.text = task.title

        // Si la tarea está completada, se tachará el texto
        textView.paintFlags = if (task.isDone)
            textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        else
            textView.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()

        val listView = parent as ListView
        listView.setItemChecked(position, task.isDone)

        return view
    }
}
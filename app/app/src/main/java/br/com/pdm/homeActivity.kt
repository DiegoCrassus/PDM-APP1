package br.com.pdm

import android.content.Intent
import android.media.Image
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.ImageButton
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONArray
import org.json.JSONObject

class homeActivity: AppCompatActivity() {
    private val notebookList = ArrayList<NotebookModel>()
    private lateinit var notebookAdapter: NotebookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val name: String? = intent.getStringExtra("name")
        val email: String? = intent.getStringExtra("email")
        Log.d("email", email.toString())
        Log.d("name", name.toString())

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        notebookAdapter = NotebookAdapter(notebookList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = notebookAdapter
        getAllNotebooks(email, -1, name)

        GlobalScope.launch(Dispatchers.IO) {
            val create: Boolean = validateUser(email)
            if (create) {
                createUserNotebook(email, name)
            }
        }

        btnAddNotebook.setOnClickListener {
            val notebook = Intent(this, notebookActivity::class.java)
            notebook.putExtra("name", name)
            notebook.putExtra("email", email)
            notebook.putExtra("create", "true")
            startActivity(notebook)
        }
    }

    fun reload(){
        startActivity(intent)
    }
    private fun getAllNotebooks(email: String?, id_notebook: Int?, name: String?) {
        val responseNotebooks: String? = selectNotebook(email, id_notebook)
        val jsonReponse = JSONObject(responseNotebooks)
        val data = jsonReponse.get("data") as JSONArray
        for (i in 0 until data.length()) {
            val item = data.getJSONArray(i)
            val idNotebook: Int = item.get(0) as Int
            val text: String = item.get(2) as String
            val title: String = item.get(1) as String
            prepareNotebookData(title=title, text=text, id_notebook=idNotebook, email=email, name=name)
        }
    }

    private suspend fun validateUser (email: String?): Boolean {
        val responseValidateUser: String? = email?.let { selectUser(it) }
        val jsonReponse = JSONObject(responseValidateUser)
        Log.d("data", jsonReponse.get("data").toString())
        val validate = jsonReponse.get("data") as JSONArray
        return validate.length() == 0
    }

    private suspend fun createUserNotebook (email: String?, name: String?) {
        Log.d("Debug", "Creating user on notebook database.")
        email?.let { name?.let { it1 -> createUser(it, it1) } }
    }

    internal class NotebookAdapter(private var notebookList: List<NotebookModel>):
        RecyclerView.Adapter<NotebookAdapter.MyViewHolder>() {
        internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var title: TextView = view.findViewById(R.id.title)
            var text: TextView = view.findViewById(R.id.text)
            var delete: ImageButton = view.findViewById(R.id.btnNotebookDelete)

            init {
                itemView.setOnClickListener{
                    val position: Int = adapterPosition
                    val notebook = Intent(itemView.context, notebookActivity::class.java)
                    notebook.putExtra("name", notebookList[position].getName())
                    notebook.putExtra("email", notebookList[position].getEmail())
                    notebook.putExtra("id_notebook", notebookList[position].getId_notebook().toString())
                    notebook.putExtra("title", notebookList[position].getTitle())
                    notebook.putExtra("text", notebookList[position].getText())
                    notebook.putExtra("create", "false")
                    itemView.context.startActivity(notebook)
                }
                delete.setOnClickListener {
                    val position: Int = adapterPosition
                    notebookList[position].getId_notebook()?.let { it1 -> deleteNotebook(email = notebookList[position].getEmail(), id_notebook = it1) }
                    val home = Intent(delete.context, homeActivity::class.java)
                    home.putExtra("name", notebookList[position].getName())
                    home.putExtra("email", notebookList[position].getEmail())
                    delete.context.startActivity(home)
                }
            }
        }
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row, parent, false)
            return MyViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val notebook = notebookList[position]
            holder.title.text = notebook.getTitle()
            holder.text.text = notebook.getText()
        }
        override fun getItemCount(): Int {
            return notebookList.size
        }
    }

    private fun prepareNotebookData(title: String?, text: String?, id_notebook: Int?, email: String?, name: String?) {
        var titleNotebook: String? = title
        if (titleNotebook?.length!! < 1) {
            titleNotebook = "Sem titulo"
            
        } else if (titleNotebook?.length!! > 25) {
            titleNotebook = titleNotebook.chunked(25)[0]

        }
        var textNotebook: String? = text
        if (textNotebook?.length!! > 30) {
            textNotebook = textNotebook.chunked(30)[0]

        }
        val notebook = NotebookModel(title=titleNotebook, text=textNotebook, id_notebook=id_notebook, email=email, name=name)
        notebookList.add(notebook)
        notebookAdapter.notifyDataSetChanged()
    }
}
package br.com.PDM

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.util.Log
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_home.*
import org.json.JSONArray
import org.json.JSONObject

class home: AppCompatActivity() {
    private val notebookList = ArrayList<NotebookModel>()
    private lateinit var notebookAdapter: NotebookAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        val name: String? = intent.getStringExtra("name")
        val email: String? = intent.getStringExtra("email")
        Log.d("email", email.toString())
        Log.d("name", name.toString())

        GlobalScope.launch(Dispatchers.IO) {
            val create = validateUser(email)
            if (create) {
                createUserNotebook(email, name)
            }

            getAllNotebooks(email, null)
        }

//        btnAddNotebook.setOnClickListener {
//            val login = Intent(this, notebook::class.java)
//            login.putExtra("name", name)
//            login.putExtra("email", email)
//            startActivity(login)
//        }

        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)
        notebookAdapter = NotebookAdapter(notebookList)
        val layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.layoutManager = layoutManager
        recyclerView.itemAnimator = DefaultItemAnimator()
        recyclerView.adapter = notebookAdapter
        prepareMovieData()
    }

    private suspend fun createNewNotebook(email: String?, title: String?, text: String?) {
        email?.let { title?.let { it1 -> text?.let { it2 -> createNotebook(it, it1, it2) } } }
    }

    private suspend fun getAllNotebooks(email: String?, id_notebook: Any?) {
        val responseNotebooks: String? = selectNotebook(email, id_notebook)
        val jsonReponse = JSONObject(responseNotebooks)
        Log.d("response", responseNotebooks.toString())
        Log.d("data", jsonReponse.get("data").toString())
        val validate = jsonReponse.get("data") as JSONArray
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





    internal class NotebookAdapter(private var notebookList: List<NotebookModel>) :
        RecyclerView.Adapter<NotebookAdapter.MyViewHolder>() {
        internal inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var title: TextView = view.findViewById(R.id.title)
            var text: TextView = view.findViewById(R.id.text)
        }
        @NonNull
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_row, parent, false)
            return MyViewHolder(itemView)
        }
        override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
            val movie = notebookList[position]
            holder.title.text = movie.getTitle()
            holder.text.text = movie.getText()
        }
        override fun getItemCount(): Int {
            return notebookList.size
        }
    }

    private fun prepareMovieData() {
        var movie = NotebookModel("Mad Max: Fury Road", "Action & Adventure")
        notebookList.add(movie)
        movie = NotebookModel("Inside Out", "Animation, Kids & Family")
        notebookList.add(movie)
        movie = NotebookModel("Star Wars: Episode VII - The Force Awakens", "Action")
        notebookList.add(movie)
        movie = NotebookModel("Shaun the Sheep", "Animation")
        notebookList.add(movie)
        movie = NotebookModel("The Martian", "Science Fiction & Fantasy")
        notebookList.add(movie)
        movie = NotebookModel("Mission: Impossible Rogue Nation", "Action")
        notebookList.add(movie)
        movie = NotebookModel("Up", "Animation")
        notebookList.add(movie)
        movie = NotebookModel("Star Trek", "Science Fiction")
        notebookList.add(movie)
        movie = NotebookModel("The LEGO MovieModel", "Animation")
        notebookList.add(movie)
        movie = NotebookModel("Iron Man", "Action & Adventure")
        notebookList.add(movie)
        movie = NotebookModel("Aliens", "Science Fiction")
        notebookList.add(movie)
        movie = NotebookModel("Chicken Run", "Animation")
        notebookList.add(movie)
        movie = NotebookModel("Back to the Future", "Science Fiction")
        notebookList.add(movie)
        movie = NotebookModel("Raiders of the Lost Ark", "Action & Adventure")
        notebookList.add(movie)
        movie = NotebookModel("Goldfinger", "Action & Adventure")
        notebookList.add(movie)
        movie = NotebookModel("Guardians of the Galaxy", "Science Fiction & Fantasy")
        notebookList.add(movie)
        notebookAdapter.notifyDataSetChanged()
    }
}
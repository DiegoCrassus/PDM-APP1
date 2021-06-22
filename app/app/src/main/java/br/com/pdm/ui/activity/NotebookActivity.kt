package br.com.pdm.ui.activity

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.com.pdm.R
import br.com.pdm.createNotebook
import br.com.pdm.updateNotebook
import kotlinx.android.synthetic.main.notebook.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class notebookActivity : AppCompatActivity() {
    private var title: String? = ""
    private var text: String? = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notebook)
        val name: String? = intent.getStringExtra("name")
        val email: String? = intent.getStringExtra("email")
        val notebookIdNotebook: String? = intent.getStringExtra("id_notebook")
        val notebookTitle: String? = intent.getStringExtra("title")
        val notebookText: String? = intent.getStringExtra("text")
        val create: String? = intent.getStringExtra("create")
        Log.d("id", notebookIdNotebook.toString())
        Log.d("name", name.toString())
        Log.d("email", email.toString())


        titleNotebook.setText(notebookTitle)
        textNotebook.setText(notebookText)

        titleNotebook.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                title = titleNotebook.text.toString()
            }
        })

        textNotebook.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                text = textNotebook.text.toString()
            }
        })


        btnNotebookCancel.setOnClickListener {
            val home = Intent(this, homeActivity::class.java)
            home.putExtra("name", name)
            home.putExtra("email", email)
            startActivity(home)
        }

        btnNotebookAccept.setOnClickListener {
            if (create == "true") {
                GlobalScope.launch(Dispatchers.IO) {
                    newNotebook(email=email, title=title, text=text)
                }
            } else {
                GlobalScope.launch(Dispatchers.IO) {
                    editNotebook(email=email, id_notebook=notebookIdNotebook, title=title, text=text)
                }
            }
            val home = Intent(this, homeActivity::class.java)
            home.putExtra("name", name)
            home.putExtra("email", email)
            startActivity(home)
        }
    }

    private suspend fun newNotebook(email: String?, title: String?, text: String?) {
        email?.let { title?.let { it1 -> text?.let { it2 -> createNotebook(email= it, title= it1, text= it2) } } }
    }
    private suspend fun editNotebook(email: String?, id_notebook: String?, text: String?, title: String?) {
        email?.let { it1 -> title?.let { it2 -> text?.let { it3 -> updateNotebook(email =it1, title =it2, text =it3, id_notebook= id_notebook?.toInt()) } } }
    }
}
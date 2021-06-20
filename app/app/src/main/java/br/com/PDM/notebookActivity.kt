package br.com.PDM

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.notebook.*

class notebookActivity : AppCompatActivity() {
    private var title: String? = null
    private var text: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.notebook)
        val name: String? = intent.getStringExtra("name")
        val email: String? = intent.getStringExtra("email")

        btnNotebookCancel.setOnClickListener {
            val home = Intent(this, homeActivity::class.java)
            home.putExtra("name", name)
            home.putExtra("email", email)
            startActivity(home)
        }

        btnNotebookAccept.setOnClickListener {

        }
    }
}
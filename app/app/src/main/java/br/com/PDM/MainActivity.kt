package br.com.PDM

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Toast.makeText(this, "Nós da Reco Note cuidamos dos seus dados!", Toast.LENGTH_LONG).show()

        btnLogin.setOnClickListener {
            startActivity(Intent(this, signIn::class.java))
        }

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, signUp::class.java))
        }
    }


}
package br.com.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, signIn::class.java))
        }

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, signUp::class.java))
        }

        btCarrossel.setOnClickListener {
            startActivity(Intent(this, MainActivityCarrossel::class.java))
        }
    }

}
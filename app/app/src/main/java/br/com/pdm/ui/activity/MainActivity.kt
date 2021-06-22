package br.com.pdm.ui.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import br.com.pdm.R
import br.com.pdm.interactor.signIn
import br.com.pdm.interactor.signUp
import br.com.pdm.ui.fragments.onboarding.MainActivityCarrossel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btnLogin.setOnClickListener {
            startActivity(Intent(this, br.com.pdm.interactor.signIn::class.java))
        }

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, br.com.pdm.interactor.signUp::class.java))
        }

        btCarrossel.setOnClickListener {
            startActivity(Intent(this, MainActivityCarrossel::class.java))
        }
    }

}
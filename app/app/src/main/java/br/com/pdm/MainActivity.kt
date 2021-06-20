package br.com.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import br.com.pdm.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

//        btInfos.setOnClickListener {
//            startActivity(Intent(this, MainActivityCarrossel::class.java))
//        }
//
////        btnLogin.setOnClickListener {
////            startActivity(Intent(this, signIn::class.java))
////        }
//
//        btnCadastro.setOnClickListener {
//            startActivity(Intent(this, signUp::class.java))
//        }
    }

//    override fun onStart() {
//        super.onStart()
//        Toast.makeText(applicationContext, "onStart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onResume() {
//        super.onResume()
//        Toast.makeText(applicationContext, "onResume", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onPause() {
//        super.onPause()
//        Toast.makeText(applicationContext, "onPause", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onStop() {
//        super.onStop()
//        Toast.makeText(applicationContext, "onStop", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onRestart() {
//        super.onRestart()
//        Toast.makeText(applicationContext, "onRestart", Toast.LENGTH_SHORT).show()
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        Toast.makeText(applicationContext, "onDestroy", Toast.LENGTH_SHORT).show()
//    }

}
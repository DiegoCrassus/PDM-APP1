package br.com.pdm

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import br.com.pdm.R
import kotlinx.android.synthetic.main.activity_splash_screen.*

import kotlinx.coroutines.delay


class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_FULLSCREEN

        setContentView(R.layout.activity_splash_screen)

        iv_logo_ok.alpha = 0f
        iv_logo_ok.animate().setDuration(1500).alpha(1f).withEndAction{
            val i = Intent(this, MainActivity::class.java)
            startActivity(i)
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this, "Nós da Reco Note cuidamos dos seus dados!", Toast.LENGTH_LONG).show()
    }
}
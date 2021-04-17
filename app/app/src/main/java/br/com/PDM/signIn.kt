package br.com.PDM

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.core.content.FileProvider
import java.io.File
import android.os.StrictMode
import kotlinx.android.synthetic.main.activity_sign_in.*


private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class signIn : AppCompatActivity() {
    private var faceSelfie: String? = ""
    private var email: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        val policy: StrictMode.ThreadPolicy? = android.os.StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnTakePictureLogin.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(this, "br.com.PDM.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this@signIn, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }
        btnSendLogin.setOnClickListener {
            btnSendLogin.isEnabled = false
            try {
                val validEmail: Boolean? = validateEmail(email!!)
                Log.d("valid", validEmail.toString())
                if (validEmail == true) {

                    if (faceSelfie!!.isNotEmpty()) {
                        val recognition: Boolean? = faceRecognition(email!!, faceSelfie!!)

                        Log.d("recognition", recognition.toString())
                        if (recognition!!) {
                            startActivity(Intent(this, home::class.java))
                        } else {
                            Toast.makeText(this@signIn, "Falha na autenticação, tente novamente!", Toast.LENGTH_SHORT).show()
                        }

                    } else {
                        Toast.makeText(this@signIn, "É necessário que tire uma selfie!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@signIn, "Houve um problema com este email!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: kotlin.KotlinNullPointerException) {
                btnSendLogin.isEnabled = true
                Toast.makeText(this@signIn, "É necessário um email para efetuar o login!", Toast.LENGTH_SHORT).show()
            } catch (e: java.net.SocketTimeoutException) {
                Toast.makeText(this@signIn, "Backend OFFLINE!", Toast.LENGTH_SHORT).show()
            }
            btnSendLogin.isEnabled = true
        }

        textFieldEmailLogin.addTextChangedListener(object: TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(textFieldEmailLogin.text.toString()).matches()) {
                    email = textFieldEmailLogin.text.toString()
                }
            }

        })
    }
    private fun getPhotoFile(fileName: String): File {
        val storageDirectory = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile(fileName, ".jpg", storageDirectory)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Log.d("image_path", photoFile.absolutePath)
            val bytes = File(photoFile.absolutePath).readBytes()
            val takenImageBase64 = Base64.encodeToString(bytes, Base64.DEFAULT)
            faceSelfie = takenImageBase64

            try {
                val valid: Boolean? = faceDetect(faceSelfie!!)
                Log.d("faceDetect", valid.toString())
                if (!valid!!) {
                    faceSelfie = ""
                }
            } catch (e: java.net.SocketTimeoutException) {
                Toast.makeText(this@signIn, "Backend OFFLINE!", Toast.LENGTH_SHORT).show()
            }

        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }
}
package br.com.pdm.interactor

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
import androidx.core.widget.addTextChangedListener
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.io.File
import android.os.StrictMode
import br.com.pdm.R
import br.com.pdm.faceDetect
import br.com.pdm.faceRecognitionRegister
import br.com.pdm.ui.activity.MainActivity
import br.com.pdm.validateEmail
import org.json.*
import java.util.Timer
import kotlin.concurrent.schedule


private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class signUp : AppCompatActivity() {
    private val listPayload = mutableListOf<String>()
    private var email: String? = null
    private var name: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val policy: StrictMode.ThreadPolicy? = android.os.StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            val fileProvider = FileProvider.getUriForFile(this, "br.com.pdm.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this@signUp, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        btnSend.setOnClickListener {
            try {
                val validEmail = validateEmail(email!!)
                Log.d("valid", validEmail.toString())
                if (validEmail == false && listPayload!!.size == 3) {
                    val success: Boolean? = faceRecognitionRegister(name!!, email!!, listPayload)
                    Log.d("success", success.toString())
                    if (success == true) {
                        Toast.makeText(this@signUp, "Registro concluido com sucesso!", Toast.LENGTH_SHORT).show()
                        Timer("SettingUp", false).schedule(2000) {
                            startActivity(Intent(this@signUp, MainActivity::class.java))
                        }
                    } else {
                        Toast.makeText(this@signUp, "Falha ao tentar registrar, tente novamente!", Toast.LENGTH_SHORT).show()
                        Timer("SettingUp", false).schedule(2000) {
                            startActivity(Intent(this@signUp, MainActivity::class.java))
                        }

                    }
                } else {
                    Toast.makeText(this@signUp, "Houve um problema com este email!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: kotlin.KotlinNullPointerException) {
                Toast.makeText(this@signUp, "Os campos precisam ser preenchidos!", Toast.LENGTH_SHORT).show()
            } catch (e: java.net.SocketTimeoutException) {
                Log.d("Erro", "Backend offline!")
                Toast.makeText(this@signUp, "Backend OFFLINE!", Toast.LENGTH_SHORT).show()
            } catch (e: java.lang.NullPointerException) {
                Toast.makeText(this@signUp, "É necessário um email para efetuar o cadastro!", Toast.LENGTH_SHORT).show()
            }
        }

        textFieldEmail.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(textFieldEmail.text.toString()).matches()) {
                    email = textFieldEmail.text.toString()
                }
            }

        })

        textFieldName.addTextChangedListener {
            name = textFieldName.text.toString()
        }
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
            listPayload.add(takenImageBase64)
            Log.d("LIST_COUNT", listPayload.size.toString())

            when (listPayload.size) {
                1 -> {

                    val validFace: Boolean? = faceDetect(listPayload[0])
                    if (validFace == true) {
                        firstPhoto.isChecked = true
                    } else {
                        listPayload.remove(listPayload[0])
                        Toast.makeText(this@signUp, "Primeira foto falhou, não detectada face na foto!", Toast.LENGTH_SHORT).show()
                    }

                }
                2 -> {
                    val validFace: Boolean? = faceDetect(listPayload[1])
                    if (validFace == true) {
                        secondPhoto.isChecked = true
                    } else {
                        listPayload.remove(listPayload[1])
                        Toast.makeText(this@signUp, "Segunda foto falhou, não detectada face na foto!", Toast.LENGTH_SHORT).show()
                    }

                }
                3 -> {
                    val validFace: Boolean? = faceDetect(listPayload[2])
                    if (validFace == true) {
                        thirdPhoro.isChecked = true
                    } else {
                        listPayload.remove(listPayload[2])
                        Toast.makeText(this@signUp, "Terceira foto falhou, não detectada face na foto!", Toast.LENGTH_SHORT).show()
                    }

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }
}
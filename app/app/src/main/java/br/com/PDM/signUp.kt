package br.com.PDM

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
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
import okhttp3.*
import java.io.File
import java.io.ByteArrayOutputStream
import android.os.StrictMode
import org.json.*
import java.util.Timer
import kotlin.concurrent.schedule


private const val RECOGNITION_TRAIN = "http://192.168.0.61:9002/api/recognition/train"
private const val DETECTION = "http://192.168.0.61:9001/api/detection/"
private const val EMAIL_CHECK = "http://192.168.0.61:9003/api/pdm/POC"
private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class signUp : AppCompatActivity() {
    private val listPayload = mutableListOf<String>()
    private var email: String? = null
    private var name: String? = null
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val policy: StrictMode.ThreadPolicy? = android.os.StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)

        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            var fileProvider = FileProvider.getUriForFile(this, "br.com.PDM.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this@signUp, "Unable to open camera", Toast.LENGTH_SHORT).show()
            }
        }

        btnSend.setOnClickListener {
            val success: Boolean? = faceRecognitionRegister()

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
        }

        textFieldEmail.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                if (android.util.Patterns.EMAIL_ADDRESS.matcher(textFieldEmail.text.toString()).matches()) {

                    email = textFieldEmail.text.toString()
                    val validEmail = validateEmail(email!!)
                    Log.d("valid", validEmail.toString())
                    if (validEmail == true) {
                        btnTakePicture.isEnabled = true
                    } else {
                        Toast.makeText(this@signUp, "Houve um problema com este email!", Toast.LENGTH_SHORT).show()
                    }
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
            val takenImage = BitmapFactory.decodeFile(photoFile.absolutePath)
            val stream = ByteArrayOutputStream()
            takenImage.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            val image = stream.toByteArray()
            val takenImageBase64 = Base64.encodeToString(image, Base64.DEFAULT)
            listPayload.add(takenImageBase64)
            Log.d("LIST_COUNT", listPayload.size.toString())

            when (listPayload.size) {
                1 -> {
                    try {
                        faceDetect(listPayload[0])
                        listPayload.remove(listPayload[0])
                    } catch (e: java.net.ProtocolException) {
                        firstPhoto.isChecked = true
                    }
                }
                2 -> {
                    try {
                        faceDetect(listPayload[0])
                        listPayload.remove(listPayload[0])
                    } catch (e: java.net.ProtocolException) {
                        secondPhoto.isChecked = true
                    }

                }
                3 -> {
                    try {
                        faceDetect(listPayload[0])
                        listPayload.remove(listPayload[0])
                    } catch (e: java.net.ProtocolException) {
                        thirdPhoro.isChecked = true
                        btnTakePicture.isEnabled = false
                        btnSend.isEnabled = true
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun faceRecognitionRegister(): Boolean? {
        if (name == null) {
            name = "User";
        }

        val jsonObj = JSONObject()
        jsonObj.put("name", name)
        jsonObj.put("user", email)
        jsonObj.put("face", listPayload)

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val payload: RequestBody? = RequestBody.create(JSON, jsonObj.toString())
        val request = Request.Builder()
            .header("Connection","close")
            .method("POST", payload)
            .addHeader("content-type", "application/json")
            .url(RECOGNITION_TRAIN)
            .build()
        Log.d("Request", "request created at $DETECTION")

        try {
            val response = client.newCall(request).execute()
            val body = response.body()
            if (body != null) {
                val body = body?.string()
                Log.d("body", body.toString())
            }
        } catch (e: java.net.SocketTimeoutException) {
            Toast.makeText(this@signUp, "Backend offline!", Toast.LENGTH_SHORT).show()
        }
        return false
    }

    private fun faceDetect(imageB64:String) {
        val jsonObj = JSONObject()
        jsonObj.put("image", imageB64)
        val JSON = MediaType.parse("application/json; charset=utf-8")

        val payload: RequestBody? = RequestBody.create(JSON, jsonObj.toString())
        val request = Request.Builder()
            .header("Connection","close")
            .method("POST", payload)
            .addHeader("content-type", "application/json")
            .url(DETECTION)
            .build()
        Log.d("Request", "request created at $DETECTION")

        try {
            val response = client.newCall(request).execute()
            val body = response.body()
            if (body != null) {
                body?.string()
            }
        } catch (e: java.net.SocketTimeoutException) {
            Toast.makeText(this@signUp, "Backend offline!", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validateEmail(email:String): Boolean? {
        val request = Request.Builder()
                .header("User-Agent", "OkHttp Headers.java")
                .addHeader("Accept", "application/json")
                .addHeader("email", email)
                .addHeader("Connection","close")
                .url(EMAIL_CHECK)
                .build()
        Log.d("Request", "request created at $EMAIL_CHECK")
        try {
            val response = client.newCall(request).execute()
            val body = response.body()
            if (body != null) {
                val json = body?.string()
                val obj = JSONObject(json)
                val data = obj.get("data")
                try {
                    if (data.toString().length < 5) {
                        return true
                    } else {
                        return false
                    }
                } catch (e: Exception) {
                    return false
                }
            }
        } catch (e: java.net.SocketTimeoutException) {
            Toast.makeText(this@signUp, "Backend offline!", Toast.LENGTH_SHORT).show()
        }
        return false
    }
}
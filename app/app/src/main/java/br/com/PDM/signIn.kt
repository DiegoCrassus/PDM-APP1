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
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.json.*
import java.util.Timer
import kotlin.concurrent.schedule

private const val RECOGNITION_USE = "http://192.168.0.61:9002/api/recognition/use"
private const val DETECTION = "http://192.168.0.61:9001/api/detection/"
private const val EMAIL_CHECK = "http://192.168.0.61:9003/api/pdm/POC"
private const val FILE_NAME = "photo.jpg"
private const val REQUEST_CODE = 42
private lateinit var photoFile: File

class signIn : AppCompatActivity() {
    private val listPayload = mutableListOf<String>()
    private var email: String? = null
    private var name: String? = null
    private val client = OkHttpClient()

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
                val validEmail = validateEmail(email!!)
                Log.d("valid", validEmail.toString())
                if (validEmail == true) {
                    btnTakePictureLogin.isEnabled = true

                    if (listPayload.size > 0) {
                        faceRecognition()
                    } else {
                        Toast.makeText(this@signIn, "É necessário que tire uma selfie!", Toast.LENGTH_SHORT).show()
                    }

                } else {
                    Toast.makeText(this@signIn, "Houve um problema com este email!", Toast.LENGTH_SHORT).show()
                }
            } catch (e: kotlin.KotlinNullPointerException) {
                btnSendLogin.isEnabled = true
                Toast.makeText(this@signIn, "É necessário um email para efetuar o login!", Toast.LENGTH_SHORT).show()
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
            listPayload.add(takenImageBase64)
            Log.d("LIST_COUNT", listPayload.size.toString())

            try {
                faceDetect(listPayload[0])
                listPayload.remove(listPayload[0])
            } catch (e: java.net.ProtocolException) {
                btnSendLogin.isEnabled = true
                Log.d("face", e.toString())
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }


    }

    private fun faceRecognition(): Boolean? {

        val jsonObj = JSONObject()
        jsonObj.put("user_id", email)
        jsonObj.put("face", listPayload)
        Log.d("payload", jsonObj.toString())

        val JSON = MediaType.parse("application/json; charset=utf-8")
        val payload: RequestBody? = RequestBody.create(JSON, jsonObj.toString())
        val request = Request.Builder()
            .header("Connection","close")
            .method("POST", payload)
            .addHeader("content-type", "application/json")
            .url(RECOGNITION_USE)
            .build()
        Log.d("Request", "request created at $RECOGNITION_USE")

        try {
            val response = client.newCall(request).execute()
            val body = response.body()
            if (body != null) {
                val body = body?.string()
                Log.d("body", body.toString())
            }
        } catch (e: java.net.SocketTimeoutException) {
            Toast.makeText(this@signIn, "Backend offline!", Toast.LENGTH_SHORT).show()
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
            body?.string()
        } catch (e: java.net.SocketTimeoutException) {
            Toast.makeText(this@signIn, "Backend offline!", Toast.LENGTH_SHORT).show()
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
            Toast.makeText(this@signIn, "Backend offline!", Toast.LENGTH_SHORT).show()
        }
//        catch (e: java.net.ProtocolException) {
//            Toast.makeText(this@signIn, "Fail request.", Toast.LENGTH_SHORT).show()
//        }
        return false
    }
}
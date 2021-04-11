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

private const val RECOGNITION_TRAIN = "http://192.168.0.61:9002/api/recognition/use"
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

        btnTakePicture.setOnClickListener {
            val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            photoFile = getPhotoFile(FILE_NAME)

            var fileProvider = FileProvider.getUriForFile(this, "br.com.PDM.fileprovider", photoFile)
            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileProvider)
            if (takePictureIntent.resolveActivity(this.packageManager) != null) {
                startActivityForResult(takePictureIntent, REQUEST_CODE)
            } else {
                Toast.makeText(this, "Unable to open camera", Toast.LENGTH_SHORT).show()
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
                    Log.d("Email", email.toString())
                    val validEmail = validateEmail(email!!)
                    Log.d("valid", validEmail.toString())
                    btnTakePicture.isEnabled = true
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
                    firstPhoto.isChecked = true
                }
                2 -> {
                    secondPhoto.isChecked = true
                }
                3 -> {

                    thirdPhoro.isChecked = true
                    btnTakePicture.isEnabled = false
                    btnSend.isEnabled = true
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }

    }

    private fun faceDetect(imageB64:String) {

    }
    private fun faceRecognition(listImage:List<String>) {
        if (name == null) {
            name = "User";
        }



    }
    private fun validateEmail(email:String): Response? {
        Log.d("Email", email)
        val request = Request.Builder()
            .addHeader("email", email)
            .url(EMAIL_CHECK)
            .build()
        Log.d("Request", "request created at $EMAIL_CHECK")
        val response = async client.newCall(request).execute()
        Log.d("Response", response.toString())
        return response

    }

}
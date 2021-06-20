package br.com.pdm

import android.util.Log
import okhttp3.*
import org.json.*

// Endpoints na azure
private const val RECOGNITION_USE = "http://192.168.101.3:9002/api/recognition/use"
private const val RECOGNITION_TRAIN = "http://192.168.101.3:9002/api/recognition/train"
private const val DETECTION = "http://192.168.101.3:9001/api/detection/"
private const val EMAIL_CHECK = "http://192.168.101.3:9003/api/PDM/app"
// Endpoints local
//private const val RECOGNITION_USE = "http://192.168.0.61:9002/api/recognition/use"
//private const val RECOGNITION_TRAIN = "http://192.168.0.61:9002/api/recognition/train"
//private const val DETECTION = "http://192.168.0.61:9001/api/detection/"
//private const val EMAIL_CHECK = "http://192.168.0.61:9003/api/pdm/POC"

private val client = OkHttpClient()

fun faceRecognitionRegister (name: String, email: String, listPayload: List<String>): Boolean? {
    val jsonObj = JSONObject()
    jsonObj.put("name", name)
    jsonObj.put("user", email)
    jsonObj.put("face", listPayload)
    Log.d("payload", jsonObj.toString())

    val json = MediaType.parse("application/json; charset=utf-8")
    val payload: RequestBody? = RequestBody.create(json, jsonObj.toString())
    val request = Request.Builder()
            .header("Connection","close")
            .method("POST", payload)
            .addHeader("content-type", "application/json")
            .url(RECOGNITION_TRAIN)
            .build()
    Log.d("Request", "request created at $RECOGNITION_TRAIN")
    val response = client.newCall(request).execute()
    Log.d("lengthBody", response.body()?.contentLength().toString())

    return try {
        val bodyFaceRegister = response.peekBody(response.body()!!.contentLength()).string()
        Log.d("body", bodyFaceRegister.toString())
        val objArray = JSONArray(bodyFaceRegister)
        val faceOne = objArray.get(0).toString()
        val statusOne = JSONObject(faceOne).get("status")
        statusOne == "200"

    } catch (e: java.net.ProtocolException) {
        false
    }
}

fun faceRecognition (email: String, faceSelfie: String): JSONObject? {

    val jsonObj = JSONObject()
    jsonObj.put("user_id", email)
    jsonObj.put("image", faceSelfie)
    Log.d("payload", jsonObj.toString())

    val json = MediaType.parse("application/json; charset=utf-8")
    val payload: RequestBody? = RequestBody.create(json, jsonObj.toString())
    val request = Request.Builder()
            .header("Connection","close")
            .method("POST", payload)
            .addHeader("content-type", "application/json")
            .url(RECOGNITION_USE)
            .build()
    Log.d("Request", "request created at $RECOGNITION_USE")
    val response = client.newCall(request).execute()
    Log.d("lengthBody", response.body()?.contentLength().toString())
    try {
        val bodyFaceRecognition = response.peekBody(response.body()!!.contentLength()).string()
        val namesObj = JSONObject(bodyFaceRecognition).get("names") as JSONObject
        Log.d("names", namesObj.get("0").toString())
        val jsonName = namesObj.get("0") as JSONObject
        val jsonArrayName = jsonName.get("names") as JSONArray
        val name = jsonArrayName.get(0)
        val recognition = JSONObject()
        recognition.put("name", name)
        recognition.put("error", false)
        recognition.put("recognised", JSONObject(bodyFaceRecognition).get("recognised") as Boolean?)
        return recognition
    } catch (e: java.net.ProtocolException) {
        val recognition = JSONObject()
        recognition.put("error", true)
        return recognition
    }
}

fun faceDetect (imageB64:String): Boolean? {
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
    return try {
        val response = client.newCall(request).execute()
        Log.d("lengthBody", response.body()?.contentLength().toString())
        val bodyFaceDetector = response.peekBody(response.body()!!.contentLength()).string()
        Log.d("body", bodyFaceDetector.toString())
        val obj = JSONObject(bodyFaceDetector)
        obj.get("have_faces") as Boolean
    } catch (e: java.net.ProtocolException) {
        true
    }
}

fun validateEmail (email:String): Boolean? {
    /*
    Api para verificar se email consta no banco de dados ou não.
    Input:
        email - dado a ser validado.
    return:
        False - se não está cadastrado.
        True -  se está cadastrado.
     */
    val request = Request.Builder()
            .header("User-Agent", "OkHttp Headers.java")
            .header("Accept","application/json")
            .header("Content-Type","application/json")
            .header("Connection","close")
            .addHeader("email", email)
            .url(EMAIL_CHECK)
            .build()

    Log.d("Request", "request created at $EMAIL_CHECK")
    val response = client.newCall(request).execute()
    Log.d("lengthBody", response.body()?.contentLength().toString())
    return try {
        val bodyValidateEmail = response.peekBody(response.body()!!.contentLength()).string()
        val respData = JSONObject(bodyValidateEmail)
        val objArrayData = respData.get("data") as JSONArray
        Log.d("data", objArrayData.toString())
        var valid: Boolean? = false
        for (i in 0 until objArrayData.length()) {
            val objValid = objArrayData.get(i) as JSONObject
            valid = objValid.get("user") == email
        }
        valid
    } catch (e: java.net.ProtocolException) {
        true
    }  catch (e: org.json.JSONException) {
        Log.d("ERROR", "Backend fail!")
        true
    }
}

package br.com.pdm

import android.util.Log
import okhttp3.*
import org.json.JSONObject

//import org.json.*


// Endpoints na azure
//private const val RECOGNITION_USE = "http://13.68.183.62:9002/api/recognition/use"
//private const val RECOGNITION_TRAIN = "http://13.68.183.62:9002/api/recognition/train"
//private const val DETECTION = "http://13.68.183.62:9001/api/detection/"
//private const val EMAIL_CHECK = "http://13.68.183.62:9003/api/pdm/app"
// Endpoints local
private const val NOTEBOOK = "http://192.168.101.3:9004/api/notebook/"
private const val USER = "http://192.168.101.3:9004/api/user/"

private val client = OkHttpClient()

fun selectNotebook (email: String?, id_notebook: Int?): String? {
    val json = MediaType.parse("application/json; charset=utf-8")
    var url = "$NOTEBOOK?email=$email&id_notebook=$id_notebook"
    if (id_notebook != null) {
        if (id_notebook < 0) {
            url = "$NOTEBOOK?email=$email"
        }
    }

    val request = Request.Builder()
            .header("Connection","close")
            .url(url)
            .build()

    Log.d("Request", "request created at $url")
    val response = client.newCall(request).execute()
    return response.body()?.string()
}

fun createNotebook (email: String, title: String, text: String): Boolean? {
    val jsonObj = JSONObject()
    jsonObj.put("texto", text)
    val json = MediaType.parse("application/json; charset=utf-8")
    val payload: RequestBody? = RequestBody.create(json, jsonObj.toString())
    val url = "$NOTEBOOK?email=$email&titulo=$title"

    val request = Request.Builder()
        .header("Connection","close")
        .method("POST", payload)
        .url(url)
        .build()

    Log.d("Request", "request created at $url")
    client.newCall(request).execute()
    return true
}

fun updateNotebook (email: String, id_notebook: Int?, title: String?, text: String?) {
    val jsonObj = JSONObject()
    jsonObj.put("texto", text)
    val json = MediaType.parse("application/json; charset=utf-8")
    val payload: RequestBody? = RequestBody.create(json, jsonObj.toString())
    val url = "$NOTEBOOK?email=$email&id_notebook=$id_notebook&titulo=$title"

    val request = Request.Builder()
        .header("Connection","close")
        .method("PUT", payload)
        .url(url)
        .build()

    Log.d("Request", "request created at $url")
    client.newCall(request).execute()
}

fun deleteNotebook (email: String, id_notebook: Int) {
    val json = MediaType.parse("application/json; charset=utf-8")
    val url = "$NOTEBOOK?email=$email&id_notebook=$id_notebook"

    val request = Request.Builder()
        .header("Connection","close")
        .method("DELETE", null)
        .url(url)
        .build()

    Log.d("Request", "request created at $url")
    client.newCall(request).execute()
}

fun selectUser (email: String): String? {
    val json = MediaType.parse("application/json; charset=utf-8")
    val url = "$USER?email=$email"

    val request = Request.Builder()
        .header("Connection","close")
        .url(url)
        .build()

    Log.d("Request", "request created at $url")
    val response = client.newCall(request).execute()
    return response.body()?.string()
}

fun createUser (email: String, name: String) {
    val jsonObj = JSONObject()
    val json = MediaType.parse("application/json; charset=utf-8")
    val payload: RequestBody? = RequestBody.create(json, jsonObj.toString())
    val url = "$USER?email=$email&nome=$name"

    val request = Request.Builder()
        .header("Connection","close")
        .method("POST", payload)
        .url(url)
        .build()

    Log.d("Request", "request created at $url")
    client.newCall(request).execute()
}

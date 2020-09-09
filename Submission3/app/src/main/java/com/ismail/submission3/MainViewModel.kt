package com.ismail.submission3

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ismail.submission3.entity.User
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONObject
import java.lang.Exception

class MainViewModel : ViewModel(){

    var mutableDataUser = MutableLiveData<ArrayList<User>>()

    fun getSearchUser(context: Context, username: String) {

        val listUser =  ArrayList<User>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/search/users?q=${username}"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            override fun onSuccess(
                statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray
            ) {
                val result = String(responseBody)
                Log.d("OnSuccess:..", result)

                try {
                    val responseObject = JSONObject(result)
                    val items = responseObject.getJSONArray("items")
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val avatar = item.getString("avatar_url")
                        val userLogin = item.getString("login")
                        val user = User(
                            avatar,
                            "",
                            userLogin,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        listUser.add(user)
                    }

                    mutableDataUser.postValue(listUser)
                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error.message}"
                }
                Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
            }

        })
    }


    fun getUser() : LiveData<ArrayList<User>> {
        return mutableDataUser
    }
}
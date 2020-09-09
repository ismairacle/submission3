package com.ismail.submission3

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ismail.submission3.entity.Following
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import org.json.JSONArray
import java.lang.Exception

class FollowingViewModel : ViewModel() {
    private val mutableListFollowing = MutableLiveData<ArrayList<Following>>()

    fun getFollowingList(): LiveData<ArrayList<Following>> {
        return mutableListFollowing
    }

    fun getFollowingData(context: Context, selectedUser: String) {

        val listFollowing = ArrayList<Following>()

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/${selectedUser}/following"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler(){
            override fun onSuccess(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray) {

                val result = String(responseBody)
                Log.d(FollowingFragment.TAG, result)

                try {
                    val items = JSONArray(result)
                    for (i in 0 until items.length()) {
                        val item = items.getJSONObject(i)
                        val avatar_url = item.getString("avatar_url")
                        val username = item.getString("login")
                        val following = Following(
                            avatar_url,
                            "",
                            username,
                            "",
                            "",
                            "",
                            "",
                            ""
                        )
                        listFollowing.add(following)
                    }
                    mutableListFollowing.postValue(listFollowing)

                } catch (e: Exception) {
                    Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()

                }

            }

            override fun onFailure(statusCode: Int, headers: Array<out Header>?, responseBody: ByteArray?, error: Throwable?) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(context,  errorMessage, Toast.LENGTH_SHORT).show()

            }


        })
    }


}
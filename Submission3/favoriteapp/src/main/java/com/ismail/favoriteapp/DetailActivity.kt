package com.ismail.favoriteapp

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ismail.favoriteapp.adapter.SectionsPagerAdapter
import com.ismail.favoriteapp.data.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ismail.favoriteapp.entity.Favorite
import com.ismail.favoriteapp.entity.User
import com.ismail.favoriteapp.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_header.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private var favorite: Favorite? = null

    private lateinit var uriWithUsername: Uri

    companion object {
        const val EXTRA_USER = "extra_user"

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        val actionbar = supportActionBar
        actionbar?.title = ""


        val user = intent.getParcelableExtra(EXTRA_USER) as User
        getUserData(user.username)

        val sectionsPagerAdapter = SectionsPagerAdapter(this, supportFragmentManager)
        sectionsPagerAdapter.username = user.username
        view_pager.adapter = sectionsPagerAdapter
        tabs.setupWithViewPager(view_pager)
        supportActionBar?.elevation = 0f



        uriWithUsername = Uri.parse(CONTENT_URI.toString() + "/" + favorite?.username)
        val cursor = contentResolver.query(uriWithUsername, null, null, null, null)
        if (cursor != null) {
            favorite = MappingHelper.mapCursorToObject(cursor)
            cursor.close()
        }




    }






    private fun getUserData(username: String) {

        showLoading(true)

        val client = AsyncHttpClient()
        val url = "https://api.github.com/users/$username"
        client.addHeader("Authorization", "38bc054d015cda8751711b987ef3e086e2a1ca5e")
        client.addHeader("User-Agent", "request")
        client.get(url, object : AsyncHttpResponseHandler() {
            @SuppressLint("SetTextI18n")
            override fun onSuccess(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray
            ) {

                val result = String(responseBody)

                try {
                    val item = JSONObject(result)
                    Glide.with(this@DetailActivity)
                        .load(item.getString("avatar_url"))
                        .apply(RequestOptions())
                        .into(img_detail_avatar)
                    tv_detail_username.text = "@${item.getString("login")}"
                    tv_detail_name.text = item.getString("name")
                    tv_detail_following.text = item.getString("following")
                    tv_detail_followers.text = item.getString("followers")
                    tv_detail_company.text = item.getString("company")
                    tv_detail_location.text = item.getString("location")
                    tv_detail_repository.text = item.getString("public_repos")

                    showLoading(false)

                } catch (e: Exception) {
                    Toast.makeText(this@DetailActivity, e.message, Toast.LENGTH_SHORT).show()
                    e.printStackTrace()
                }

            }

            override fun onFailure(
                statusCode: Int,
                headers: Array<out Header>?,
                responseBody: ByteArray?,
                error: Throwable?
            ) {
                val errorMessage = when (statusCode) {
                    401 -> "$statusCode : Bad Request"
                    403 -> "$statusCode : Forbidden"
                    404 -> "$statusCode : Not Found"
                    else -> "$statusCode : ${error?.message}"
                }
                Toast.makeText(this@DetailActivity, errorMessage, Toast.LENGTH_SHORT).show()
            }
        })
    }

    fun showLoading(state: Boolean) {
        if (state) {
            detail_progressbar.visibility = View.VISIBLE
        } else {
            detail_progressbar.visibility = View.GONE
        }
    }


}
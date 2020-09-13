package com.ismail.submission3.view.activity

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ismail.submission3.R
import com.ismail.submission3.adapter.SectionsPagerAdapter
import com.ismail.submission3.data.DatabaseContract
import com.ismail.submission3.data.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ismail.submission3.entity.Favorite
import com.ismail.submission3.entity.User
import com.ismail.submission3.helper.MappingHelper
import com.loopj.android.http.AsyncHttpClient
import com.loopj.android.http.AsyncHttpResponseHandler
import cz.msebera.android.httpclient.Header
import kotlinx.android.synthetic.main.activity_detail.*
import kotlinx.android.synthetic.main.content_header.*
import org.json.JSONObject

class DetailActivity : AppCompatActivity() {

    private var favorite: Favorite? = null
    private var position: Int = 0
    private lateinit var uriWithUsername: Uri

    companion object {
        private val TAG = MainActivity::class.java.simpleName
        const val EXTRA_USER = "extra_user"
        const val EXTRA_FAVORITE = "extra_favorite"
        const val EXTRA_POSITION = "extra_position"
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

        var statusFavorite = false
        if (cursor != null) {
            statusFavorite = !statusFavorite
            setStatusFavorite(statusFavorite)
        } else {
            setStatusFavorite(statusFavorite)
        }

        fab_favorite.setOnClickListener {

            if (!statusFavorite) {
                statusFavorite = true
                insertFavoriteToDatabase(user)
                setStatusFavorite(statusFavorite)
            } else {
                contentResolver.delete(uriWithUsername, null, null)
                statusFavorite = false
                Toast.makeText(this, "Data dihapus", Toast.LENGTH_SHORT).show()
                setStatusFavorite(statusFavorite)

            }


        }


    }


    private fun setStatusFavorite(statusFavorite: Boolean) {
        if (statusFavorite) {
            fab_favorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_whitep
                )
            )
        } else {
            fab_favorite.setImageDrawable(
                ContextCompat.getDrawable(
                    this,
                    R.drawable.ic_favorite_border_white
                )
            )
        }
    }

    private fun insertFavoriteToDatabase(user: User) {
        val username = user.username
        val avatar = user.avatar

        favorite?.username = username
        favorite?.avatar = avatar

        val intent = Intent()
        intent.putExtra(EXTRA_FAVORITE, favorite)
        intent.putExtra(EXTRA_POSITION, position)

        val values = ContentValues()
        values.put(DatabaseContract.FavoriteColumns.USERNAME, username)
        values.put(DatabaseContract.FavoriteColumns.AVATAR, avatar)

        contentResolver.insert(CONTENT_URI, values)
        Toast.makeText(this, "Satu item berhasil disimpan", Toast.LENGTH_SHORT).show()
        finish()
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
                Log.d(TAG, result)

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
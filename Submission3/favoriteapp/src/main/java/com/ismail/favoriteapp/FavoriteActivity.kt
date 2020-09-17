package com.ismail.favoriteapp

import android.database.ContentObserver
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.favoriteapp.adapter.FavoriteAppAdapter
import com.ismail.favoriteapp.data.DatabaseContract.FavoriteColumns.Companion.CONTENT_URI
import com.ismail.favoriteapp.entity.Favorite
import com.ismail.favoriteapp.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAppAdapter: FavoriteAppAdapter

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        showFavoriteRecyclerList()

        val handlerThread = HandlerThread("DataObserver")
        handlerThread.start()
        val handler = Handler(handlerThread.looper)
        val myObserver = object : ContentObserver(handler) {
            override fun onChange(self: Boolean) {
                loadFavoritesAsync()
            }
        }
        contentResolver.registerContentObserver(CONTENT_URI, true, myObserver)

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                favoriteAppAdapter.listFavorite = list
            }
        }


    }



    private fun showFavoriteRecyclerList() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        favoriteAppAdapter = FavoriteAppAdapter()
        rv_favorite.adapter = favoriteAppAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAppAdapter.listFavorite)
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
            showLoading(true)
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = contentResolver?.query(CONTENT_URI, null, null, null, null)
                MappingHelper.mapCursorToArrayList(cursor)
            }

            showLoading(false)
            val favorites  = deferredFavorite.await()
            if (favorites.size > 0) {
                favoriteAppAdapter.listFavorite = favorites
            } else {
                favoriteAppAdapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity, getString(R.string.no_data), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            favorite_progressBar.visibility = View.VISIBLE
        } else {
            favorite_progressBar.visibility = View.GONE
        }
    }
}
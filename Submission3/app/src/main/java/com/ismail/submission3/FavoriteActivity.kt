package com.ismail.submission3

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Adapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.submission3.adapter.FavoriteAdapter
import com.ismail.submission3.data.FavoriteHelper
import com.ismail.submission3.entity.Favorite
import com.ismail.submission3.helper.MappingHelper
import kotlinx.android.synthetic.main.activity_favorite.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class FavoriteActivity : AppCompatActivity() {

    private lateinit var favoriteAdapter: FavoriteAdapter
    private lateinit var favoriteHelper: FavoriteHelper

    companion object {
        private const val EXTRA_STATE = "EXTRA_STATE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favorite)

        showFavoriteRecyclerList()

        favoriteHelper = FavoriteHelper.getInstance(applicationContext)
        favoriteHelper.open()

        if (savedInstanceState == null) {
            // proses ambil data
            loadFavoritesAsync()
        } else {
            val list = savedInstanceState.getParcelableArrayList<Favorite>(EXTRA_STATE)
            if (list != null) {
                favoriteAdapter.listFavorite = list
            }
        }


    }

    override fun onDestroy() {
        super.onDestroy()
        favoriteHelper.close()
    }

    private fun showFavoriteRecyclerList() {
        rv_favorite.layoutManager = LinearLayoutManager(this)
        rv_favorite.setHasFixedSize(true)
        favoriteAdapter = FavoriteAdapter()
        rv_favorite.adapter = favoriteAdapter
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelableArrayList(EXTRA_STATE, favoriteAdapter.listFavorite)
    }

    private fun loadFavoritesAsync() {
        GlobalScope.launch(Dispatchers.Main) {
//            progressbar.visibility = View.VISIBLE
            val deferredFavorite = async(Dispatchers.IO) {
                val cursor = favoriteHelper.queryAll()
                MappingHelper.mapCursorToArrayList(cursor)
            }
//            progressbar.visibility = View.INVISIBLE
            val favorites  = deferredFavorite.await()
            if (favorites.size > 0) {
                favoriteAdapter.listFavorite = favorites
            } else {
                favoriteAdapter.listFavorite = ArrayList()
                Toast.makeText(this@FavoriteActivity, "Tidak ada data", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
package com.ismail.submission3.view.activity

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.submission3.R
import com.ismail.submission3.adapter.ListUserAdapter
import com.ismail.submission3.entity.User
import com.ismail.submission3.viewmodel.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*

class HomeActivity : AppCompatActivity() {


    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: ListUserAdapter

    private val list = ArrayList<User>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        showRecyclerList()

        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory())[MainViewModel::class.java]
        showLoading(false)

        viewModel.getUser().observe(this, Observer { User ->
            if (User != null) {
                adapter.setData(User)
                showLoading(false)
            }
        })

        val customSearchView = findViewById<SearchView>(R.id.search_bar)
        val context = applicationContext

        customSearchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                if (query.isNotEmpty()) {
                    viewModel.getSearchUser(context, query)
                    showLoading(true)
                }
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return true
            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.favorite -> {
                val favIntent = Intent(this, FavoriteActivity::class.java)
                startActivity(favIntent)
                true
            }
//            R.id.change_app_language -> {
//                val mIntent = Intent(Settings.ACTION_LOCALE_SETTINGS)
//                startActivity(mIntent)
//                super.onOptionsItemSelected(item)
            R.id.setting_menu -> {
                val settingIntent = Intent(this, SettingsActivity::class.java)
                startActivity(settingIntent)
                true
            }

            else -> true
        }
    }


    private fun showRecyclerList() {
        adapter = ListUserAdapter(list)
        adapter.notifyDataSetChanged()
        rv_users.layoutManager = LinearLayoutManager(this)
        rv_users.adapter = adapter

    }


    private fun showLoading(state: Boolean) {
        if (state) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

}

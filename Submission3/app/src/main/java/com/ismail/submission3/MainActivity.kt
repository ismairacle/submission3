package com.ismail.submission3

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.submission3.adapter.ListUserAdapter
import com.ismail.submission3.entity.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {


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

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.option_menu, menu)

        val searchView = menu.findItem(R.id.search).actionView as SearchView
        val context = applicationContext

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
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

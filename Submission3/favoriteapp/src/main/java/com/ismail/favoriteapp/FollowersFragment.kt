package com.ismail.favoriteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.favoriteapp.adapter.ListFollowersAdapter
import com.ismail.favoriteapp.entity.Followers
import kotlinx.android.synthetic.main.fragment_followers.*

class FollowersFragment : Fragment() {

    companion object {
        private const val ARG_USERNAME_FOLLOWERS = "username"

        fun newInstance(username: String?) : FollowersFragment {
            val fragment = FollowersFragment()
            val bundle = Bundle()

            bundle.putString(ARG_USERNAME_FOLLOWERS, username)
            fragment.arguments = bundle
            return fragment
        }
    }
    private lateinit var adapter: ListFollowersAdapter
    private var listFollowers: ArrayList<Followers> = ArrayList()
    private lateinit var followersViewModel: FollowersViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = ListFollowersAdapter(listFollowers)
        adapter.notifyDataSetChanged()

        followersViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowersViewModel::class.java)


        val username = arguments?.getString(ARG_USERNAME_FOLLOWERS)
        val context  = requireContext()


        if (username != null) {
            followersViewModel.getFollowersData(context, username)
        }

        showRecyclerList()
        showLoading(false)


        followersViewModel.getFollowingList().observe(viewLifecycleOwner, Observer { listFollowers ->
            if (listFollowers != null) {
                adapter.setData(listFollowers)
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_followers, container, false)
    }

    private fun showRecyclerList() {
        rv_users_followers.layoutManager = LinearLayoutManager(activity)
        rv_users_followers.setHasFixedSize(true)
        rv_users_followers.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            followers_progressBar.visibility = View.VISIBLE
        } else {
            followers_progressBar.visibility = View.GONE
        }
    }
}
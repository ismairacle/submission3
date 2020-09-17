package com.ismail.favoriteapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ismail.favoriteapp.adapter.ListFollowingAdapter
import com.ismail.favoriteapp.entity.Following
import kotlinx.android.synthetic.main.fragment_following.*

class FollowingFragment : Fragment() {

    companion object {
        private val ARG_USERNAME = "username"

        fun newInstance(username: String?) : FollowingFragment {
            val fragment = FollowingFragment()
            val bundle = Bundle()

            bundle.putString(ARG_USERNAME, username)
            fragment.arguments = bundle
            return fragment
        }
        val TAG = FollowingFragment::class.java.simpleName
    }
    private lateinit var adapter: ListFollowingAdapter
    private var listFollowing: ArrayList<Following> = ArrayList()
    private lateinit var followingViewModel: FollowingViewModel

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        adapter = ListFollowingAdapter(listFollowing)
        adapter.notifyDataSetChanged()

        followingViewModel = ViewModelProvider(
            this, ViewModelProvider.NewInstanceFactory()
        ).get(FollowingViewModel::class.java)



        val username = arguments?.getString(ARG_USERNAME)
        val context  = requireContext()

        if (username != null) {
            followingViewModel.getFollowingData(context, username)

        }

        showLoading(true)

        showRecyclerList()

        followingViewModel.getFollowingList().observe(viewLifecycleOwner, { listFollowing ->
            if (listFollowing != null) {
                adapter.setData(listFollowing)
                showLoading(false)
            }
        })

    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_following, container, false)
    }

    private fun showRecyclerList() {
        rv_users_following.layoutManager = LinearLayoutManager(activity)

        rv_users_following.setHasFixedSize(true)
        rv_users_following.adapter = adapter
    }

    private fun showLoading(state: Boolean) {
        if (state) {
            following_progressBar.visibility = View.VISIBLE
        } else {
            following_progressBar.visibility = View.GONE
        }
    }
}
package com.ismail.favoriteapp.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ismail.favoriteapp.DetailActivity
import com.ismail.favoriteapp.R
import com.ismail.favoriteapp.entity.Followers
import com.ismail.favoriteapp.entity.User
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListFollowersAdapter(private val dataFollowers: ArrayList<Followers>) : RecyclerView.Adapter<ListFollowersAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<Followers>) {
        dataFollowers.clear()
        dataFollowers.addAll(items)
        notifyDataSetChanged()
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: Followers) {
            with(itemView){
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .into(img_item_avatar)
                tv_item_username.text = user.username

            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int = dataFollowers.size

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataFollowers[position])

        val data = dataFollowers[position]
        holder.itemView.setOnClickListener {
            val user = User(
                data.avatar,
                "",
                data.username,
                "",
                "",
                "",
                "",
                ""
            )
            val intent = Intent(it.context, DetailActivity::class.java)
            intent.putExtra(DetailActivity.EXTRA_USER, user)
            it.context.startActivity(intent)
        }

    }

}
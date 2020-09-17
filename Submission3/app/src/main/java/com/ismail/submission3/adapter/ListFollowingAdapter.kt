package com.ismail.submission3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ismail.submission3.R
import com.ismail.submission3.entity.Following
import com.ismail.submission3.entity.User
import com.ismail.submission3.view.activity.DetailActivity
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListFollowingAdapter(private val dataFollowing: ArrayList<Following>) :
    RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<Following>) {
        dataFollowing.clear()
        dataFollowing.addAll(items)
        notifyDataSetChanged()
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: Following) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(user.avatar)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.person)
                            .error(R.drawable.broken_image))
                    .into(img_item_avatar)
                tv_item_username.text = user.username

            }
        }

    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ListViewHolder {
        val view =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.item_row_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataFollowing.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataFollowing[position])

        val data = dataFollowing[position]
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
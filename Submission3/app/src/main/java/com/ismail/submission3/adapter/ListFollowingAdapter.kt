package com.ismail.submission3.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ismail.submission3.R
import com.ismail.submission3.entity.Following
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListFollowingAdapter(private val dataFollowing: ArrayList<Following>) : RecyclerView.Adapter<ListFollowingAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<Following>) {
        dataFollowing.clear()
        dataFollowing.addAll(items)
        notifyDataSetChanged()
    }


    inner class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: Following) {
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

    override fun getItemCount(): Int {
        return dataFollowing.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataFollowing[position])

    }



}
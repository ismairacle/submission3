package com.ismail.submission3.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ismail.submission3.DetailActivity
import com.ismail.submission3.R
import com.ismail.submission3.entity.User
import kotlinx.android.synthetic.main.item_row_user.view.*

class ListUserAdapter(private val dataUser: ArrayList<User>) : RecyclerView.Adapter<ListUserAdapter.ListViewHolder>() {

    fun setData(items: ArrayList<User>) {
        dataUser.clear()
        dataUser.addAll(items)
        notifyDataSetChanged()
    }

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(user: User) {
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
        return dataUser.size
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.bind(dataUser[position])

        val data = dataUser[position]
            holder.itemView.setOnClickListener {
                val user = User (
                    data.avatar,
                    data.name,
                    data.username,
                    data.company,
                    data.location,
                    data.repository,
                    data.following,
                    data.followers
                )
                val intent = Intent(it.context, DetailActivity::class.java)
                intent.putExtra(DetailActivity.EXTRA_USER, user)
                it.context.startActivity(intent)

        }
    }


}
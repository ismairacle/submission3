package com.ismail.submission3.adapter


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.ismail.submission3.R
import com.ismail.submission3.entity.Favorite
import com.ismail.submission3.entity.User
import com.ismail.submission3.view.activity.DetailActivity
import kotlinx.android.synthetic.main.item_row_user.view.*

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {

    var listFavorite = ArrayList<Favorite>()
        set(listFavorite) {
            if (listFavorite.size > 0) {
                this.listFavorite.clear()
            }
            this.listFavorite.addAll(listFavorite)
            notifyDataSetChanged()
        }

    class FavoriteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(favorite: Favorite) {
            with(itemView) {
                Glide.with(itemView.context)
                    .load(favorite.avatar)
                    .apply(
                        RequestOptions()
                            .placeholder(R.drawable.person)
                            .error(R.drawable.broken_image)
                    )
                    .into(img_item_avatar)
                tv_item_username.text = favorite.username

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavoriteViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_row_user, parent, false)
        return FavoriteViewHolder(view)
    }

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) {
        holder.bind(listFavorite[position])

        val data = listFavorite[position]
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

    override fun getItemCount(): Int = this.listFavorite.size
}
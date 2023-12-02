package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Account.UserFavorite
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Model.FavoriteInfo
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class NotificationAdapter(
    val list: List<FavoriteInfo>,
    val onItemClick:(FavoriteInfo) -> Unit
):RecyclerView.Adapter<NotificationAdapter.NotificationViewHolder>() {
    class NotificationViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val nameF = itemView.findViewById<TextView>(R.id.nameNotifi)
        val timeF = itemView.findViewById<TextView>(R.id.timeNotifi)
        val imgF = itemView.findViewById<ImageView>(R.id.imgNotifi)
        val itemNotifi = itemView.findViewById<LinearLayout>(R.id.itemNotifi)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_notification,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.nameF.setText("Add ${list[position].movieName} movies to the list")
        holder.timeF.setText(convertLongToDateString(list[position].timeAdd).toString())
        Glide.with(holder.imgF)
            .load(Constrain.BASE_URL_IMG + list[position].movieImage)
            .into(holder.imgF)
        holder.itemNotifi.setOnClickListener{
            onItemClick(list[position])
        }
    }
    private fun convertLongToDateString(time: Long): String {
        val date = Date(time)
        val format = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault())
        return format.format(date)
    }
}
package com.example.moviesapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Model.ResultComment
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain.BASE_URL_IMG
import java.text.SimpleDateFormat
import java.util.Locale

class CommentAdapter(val list: List<ResultComment>):RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {
    class CommentViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {
        val userAvt = itemView.findViewById<ImageView>(R.id.img_avt)
        val userName = itemView.findViewById<TextView>(R.id.name_cmt)
        val userTime = itemView.findViewById<TextView>(R.id.time_cmt)
        val userRate = itemView.findViewById<TextView>(R.id.rate_cmt)
        val userContent = itemView.findViewById<TextView>(R.id.content_cmt)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder {
        return  CommentViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_user_cmt,parent,false))
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        if(list[position].author_details.avatar_path != null){
            Glide.with(holder.userAvt)
                .load(BASE_URL_IMG + list[position].author_details.avatar_path)
                .into(holder.userAvt)
        }

        holder.userName.text =  list[position].author_details.username
        holder.userTime.text =  convertIso8601ToCustomFormat(list[position].created_at)
        holder.userContent.text =  list[position].content
        holder.userRate.text =  list[position].author_details.rating.toString()
    }
    private fun convertIso8601ToCustomFormat(iso8601String: String): String {
        val iso8601Format = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val customFormat = SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault())

        try {
            val date = iso8601Format.parse(iso8601String)
            return customFormat.format(date!!)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return ""
    }
}
package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Model.ResultPlaying
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain.BASE_URL_IMG

class NowPlayingAdapter(
    val listPlaying: List<ResultPlaying>,
    val onItemClick:(ResultPlaying) -> Unit
    ):RecyclerView.Adapter<NowPlayingAdapter.NowPlayViewHolder>() {
    class NowPlayViewHolder(itemView: View):RecyclerView.ViewHolder(itemView) {
        val imgPlaying = itemView.findViewById<ImageView>(R.id.imgPlaying)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NowPlayViewHolder {
        return NowPlayViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_playing,parent,false))
    }

    override fun getItemCount(): Int {
        return listPlaying.size
    }

    override fun onBindViewHolder(holder: NowPlayViewHolder, position: Int) {
        Glide.with(holder.imgPlaying)
            .load(BASE_URL_IMG + listPlaying[position].poster_path)
            .into(holder.imgPlaying)
        holder.imgPlaying.setOnClickListener{
            onItemClick(listPlaying[position])
        }
    }
}
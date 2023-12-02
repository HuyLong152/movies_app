package com.example.moviesapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Model.Genre
import com.example.moviesapp.Model.ResultPlaying
import com.example.moviesapp.Model.ResultPopular
import com.example.moviesapp.R
import kotlin.collections.mutableListOf
import com.example.moviesapp.Utils.Constrain

class PopularAdapter(
    val listPopular :List<ResultPopular>,
    val listCate:List<Genre>,
    val onItemClick:(ResultPopular) -> Unit
):RecyclerView.Adapter<PopularAdapter.PopularViewHolder>() {
    lateinit var listCategory :List<Genre>
    init {
        getData()
    }
    class PopularViewHolder(itemView : View):RecyclerView.ViewHolder(itemView) {
        val imgPopular = itemView.findViewById<ImageView>(R.id.imgPopular)
        val namePopular = itemView.findViewById<TextView>(R.id.namePopular)
        val categoryPopular = itemView.findViewById<TextView>(R.id.categoryPopular)
        val votePopular = itemView.findViewById<TextView>(R.id.votePopular)
        val itemPopular = itemView.findViewById<ConstraintLayout>(R.id.itemPopular)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularViewHolder {
        return PopularViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_popular,parent,false))
    }

    override fun getItemCount(): Int {
        return listPopular.size
    }

    fun getData() {
        listCategory = listCate
    }
    override fun onBindViewHolder(holder: PopularViewHolder, position: Int) {
        Glide.with(holder.imgPopular)
            .load(Constrain.BASE_URL_IMG + listPopular[position].poster_path)
            .into(holder.imgPopular)
        holder.namePopular.text = listPopular[position].title
        var txtCat = ""
        var lisCate = mutableListOf<String>()
        for( i in listPopular[position].genre_ids){
            for(j in listCategory){
                if(i == j.id){
                    lisCate.add(j.name)
                }
            }
        }
        txtCat = lisCate.joinToString(separator = ", ")
        holder.categoryPopular.text =txtCat
        holder.votePopular.text =listPopular[position].vote_average.toString()
        holder.itemPopular.setOnClickListener{
            onItemClick(listPopular[position])
        }
    }
}
package com.example.moviesapp.Adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain

class ListFavoriteAdapter(
    var list:ArrayList<DetailAMovie>,
    val onItemClick:(DetailAMovie) -> Unit
    ):RecyclerView.Adapter<ListFavoriteAdapter.ListFavoriteHolder>() {
    class ListFavoriteHolder(itemview:View):RecyclerView.ViewHolder(itemview) {
        val imgSearch = itemView.findViewById<ImageView>(R.id.imgSearch)
        val txtName = itemView.findViewById<TextView>(R.id.searchName)
        val txtRate = itemView.findViewById<TextView>(R.id.searchRate)
        val txtDate = itemView.findViewById<TextView>(R.id.searchDate)
        val txtTime = itemView.findViewById<TextView>(R.id.searchTime)
        val txtGener = itemView.findViewById<TextView>(R.id.searchGener)
        val itemSearch = itemView.findViewById<ConstraintLayout>(R.id.itemSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListFavoriteHolder {
        return ListFavoriteAdapter.ListFavoriteHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ListFavoriteHolder, position: Int) {
        Glide.with(holder.imgSearch)
            .load(Constrain.BASE_URL_IMG + list[position]!!.poster_path)
            .into(holder.imgSearch)
        holder.txtName.text = list[position].title
        holder.txtDate.text = list[position].release_date
        holder.txtRate.text = list[position].vote_average.toString()
        val listCar = mutableListOf<String>()
        for (i in list[position]!!.genres) {
            listCar.add(i.name)
        }
        val txtCate = listCar.joinToString(separator = " . ")
        holder.txtGener.text = txtCate
        holder.txtTime.text = "${list[position].runtime} minutes"
        holder.itemSearch.setOnClickListener{
            onItemClick(list[position])
        }
    }
    public fun loadData(newData: ArrayList<DetailAMovie>) {
        Log.wtf("abc1",list.size.toString())
        list = newData
        Log.wtf("abc1",list.size.toString())
        notifyDataSetChanged()
    }
}
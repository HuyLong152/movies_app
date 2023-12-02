package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.moviesapp.Model.DetailAMovie
import com.example.moviesapp.Model.ResultByName
import com.example.moviesapp.Network.MovieService
import com.example.moviesapp.R
import com.example.moviesapp.Utils.Constrain
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchNameAdapter(
    val list: List<ResultByName>,
    val onItemClick:(ResultByName) ->Unit
) : RecyclerView.Adapter<SearchNameAdapter.SearchNameHolder>() {
    class SearchNameHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgSearch = itemView.findViewById<ImageView>(R.id.imgSearch)
        val txtName = itemView.findViewById<TextView>(R.id.searchName)
        val txtRate = itemView.findViewById<TextView>(R.id.searchRate)
        val txtDate = itemView.findViewById<TextView>(R.id.searchDate)
        val txtTime = itemView.findViewById<TextView>(R.id.searchTime)
        val txtGener = itemView.findViewById<TextView>(R.id.searchGener)
        val itemSearch = itemView.findViewById<ConstraintLayout>(R.id.itemSearch)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchNameHolder {
        return SearchNameHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_search, parent, false)
        )
    }


    override fun onBindViewHolder(holder: SearchNameAdapter.SearchNameHolder, position: Int) {
        val movieService: MovieService by lazy {
            MovieService()
        }

        movieService.api.getDetailById(list[position].id).enqueue(object : Callback<DetailAMovie> {
            override fun onResponse(call: Call<DetailAMovie>, response: Response<DetailAMovie>) {
                val DetailMovie = response.body()
                Glide.with(holder.imgSearch)
                    .load(Constrain.BASE_URL_IMG + DetailMovie!!.poster_path)
                    .into(holder.imgSearch)
                holder.txtName.text = DetailMovie.title
                holder.txtDate.text = DetailMovie.release_date
                holder.txtRate.text = DetailMovie.vote_average.toString()
                val listCar = mutableListOf<String>()
                for (i in DetailMovie!!.genres) {
                    listCar.add(i.name)
                }
                val txtCate = listCar.joinToString(separator = " . ")
                holder.txtGener.text = txtCate
                holder.txtTime.text = "${DetailMovie.runtime} minutes"
            }

            override fun onFailure(call: Call<DetailAMovie>, t: Throwable) {
            }
        })
        holder.itemSearch.setOnClickListener{
            onItemClick(list[position])
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
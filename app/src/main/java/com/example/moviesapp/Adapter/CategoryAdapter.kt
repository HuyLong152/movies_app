package com.example.moviesapp.Adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.moviesapp.Model.Genre
import com.example.moviesapp.R

class CategoryAdapter(
    val listCategory:List<Genre>,
    val onItemClick:(Genre) ->Unit
):RecyclerView.Adapter<CategoryAdapter.CategoryViewHodel>() {
    class CategoryViewHodel(itemView : View):RecyclerView.ViewHolder(itemView) {
        val btnCategory = itemView.findViewById<TextView>(R.id.btnCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHodel {
        return CategoryViewHodel(LayoutInflater.from(parent.context).inflate(R.layout.item_category,parent,false))
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }

    override fun onBindViewHolder(holder: CategoryViewHodel, position: Int) {
        holder.btnCategory.setText(listCategory[position].name)
        holder.btnCategory.setOnClickListener {
            onItemClick(listCategory[position])
        }
    }
}
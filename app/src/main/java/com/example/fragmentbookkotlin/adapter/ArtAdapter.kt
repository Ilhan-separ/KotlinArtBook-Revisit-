package com.example.fragmentbookkotlin.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.example.fragmentbookkotlin.databinding.ActivityMainBinding
import com.example.fragmentbookkotlin.databinding.RecyclerRowBinding
import com.example.fragmentbookkotlin.model.Art
import com.example.fragmentbookkotlin.view.MainFragment
import com.example.fragmentbookkotlin.view.MainFragmentDirections

var selectedList : Art? = null

class ArtAdapter(val artList: List<Art>):RecyclerView.Adapter<ArtAdapter.ArtHolder>() {



    class ArtHolder(val binding: RecyclerRowBinding):RecyclerView.ViewHolder(binding.root){

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArtHolder {
        val binding = RecyclerRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ArtHolder(binding)
    }

    override fun onBindViewHolder(holder: ArtHolder, position: Int) {
        holder.binding.recyclerTextView.text = artList.get(position).artName
        holder.binding.smallImageView.setImageBitmap(artList[position].image)
        holder.itemView.setOnClickListener{
            selectedList = artList.get(position)
            val action = MainFragmentDirections.actionMainFragmentToDetailFragment("card",artList[position].id)
            Navigation.findNavController(it).navigate(action)
        }
    }

    override fun getItemCount(): Int {
        return artList.size
    }

}
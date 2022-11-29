package com.luqmanfajar.story_app.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.databinding.ItemStoriesBinding


class ListStoryAdapter(private val listStory: ArrayList<ListStoryItem>) : RecyclerView.Adapter<ListStoryAdapter.ListViewHolder>() {

    private lateinit var onItemClickCallback: OnItemClickCallback

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback){
        this.onItemClickCallback = onItemClickCallback
    }
    interface OnItemClickCallback {

        fun onItemClicked(data: ListStoryItem)

    }

    class ListViewHolder(var binding: ItemStoriesBinding): RecyclerView.ViewHolder(binding.root) {

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
       val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
       val stories: ListStoryItem = listStory[position]
        Glide.with(holder.itemView.context)
            .load("${stories.photoUrl}")
            .into(holder.binding.ivItemPhoto)
        holder.binding.tvItemName.text = "Nama : "+stories.name
        holder.binding.tvItemCreatedAt.text = "Dibuat pada : "+stories.createdAt
        holder.binding.tvItemDeskripsi.text = "Deskripsi : "+stories.description

        holder.itemView.setOnClickListener{
            onItemClickCallback.onItemClicked(listStory[holder.adapterPosition])

        }
    }

    override fun getItemCount() = listStory.size

}
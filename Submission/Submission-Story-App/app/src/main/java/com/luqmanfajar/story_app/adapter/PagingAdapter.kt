package com.luqmanfajar.story_app.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.luqmanfajar.story_app.api.ListStoryItem
import com.luqmanfajar.story_app.databinding.ItemStoriesBinding
import com.luqmanfajar.story_app.fitur.DetailStoryActivity


class PagingAdapter : PagingDataAdapter<ListStoryItem, PagingAdapter.MyViewHolder>(DIFF_CALLBACK){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = ItemStoriesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val data = getItem(position)
        if (data!=null){
            holder.bind(data)
        }
    }


    class MyViewHolder(private val binding: ItemStoriesBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: ListStoryItem){
            Glide.with(itemView.context)
                .load("${data.photoUrl}")
                .into(binding.ivItemPhoto)
            binding.tvItemName.text = "Nama : "+data.name
            binding.tvItemCreatedAt.text = "Dibuat pada : "+data.createdAt
            binding.tvItemDeskripsi.text = "Deskripsi : "+data.description

            binding.root.setOnClickListener{
                val moveWithObjectIntent = Intent(itemView.context, DetailStoryActivity::class.java )
                moveWithObjectIntent.putExtra(DetailStoryActivity.EXTRA_DETAIL, data)
                val optionsCompat: ActivityOptionsCompat =
                    ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "transisiPhoto"),
                        Pair(binding.tvItemName, "transisiNama"),
                        Pair(binding.tvItemDeskripsi, "transisiDeskripsi"),
                        Pair(binding.tvItemCreatedAt,"transisiTanggal")
                    )
                it.context.startActivity(moveWithObjectIntent, optionsCompat.toBundle())
            }
        }

    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem , newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}
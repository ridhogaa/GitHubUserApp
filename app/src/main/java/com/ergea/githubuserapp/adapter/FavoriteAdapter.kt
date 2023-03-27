package com.ergea.githubuserapp.adapter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ergea.githubuserapp.R
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.databinding.ItemListFavoriteBinding
import com.ergea.githubuserapp.utils.Constants

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class FavoriteAdapter : RecyclerView.Adapter<FavoriteAdapter.FavoriteViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<FavoriteEntity>() {
        override fun areItemsTheSame(
            oldItem: FavoriteEntity,
            newItem: FavoriteEntity
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: FavoriteEntity,
            newItem: FavoriteEntity
        ): Boolean =
            oldItem == newItem
    }

    private lateinit var favClickItemCallback: FavClickItemCallback

    fun itemClicked(favClickItemCallback: FavClickItemCallback) {
        this.favClickItemCallback = favClickItemCallback
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavoriteViewHolder =
        FavoriteViewHolder(
            ItemListFavoriteBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: FavoriteViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    inner class FavoriteViewHolder(private val binding: ItemListFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: FavoriteEntity) {
            binding.username.text = item.login
            Glide.with(itemView).load(item.avatarUrl).into(binding.image)
            Glide.with(itemView).load(item.avatarUrl).into(binding.backdrop)
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(Constants.KEY_USERNAME, item.login)
                }
                it.findNavController().navigate(R.id.detailFragment, bundle)
            }
            binding.icBtnRemove.setOnClickListener {
                favClickItemCallback.removeItem(item)
            }
        }
    }

    interface FavClickItemCallback {
        fun removeItem(favoriteEntity: FavoriteEntity)
    }
}
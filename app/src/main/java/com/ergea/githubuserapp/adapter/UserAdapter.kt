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
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.databinding.ItemListHomeBinding
import com.ergea.githubuserapp.databinding.ItemListHomeBinding.inflate
import com.ergea.githubuserapp.utils.Constants

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class UserAdapter : RecyclerView.Adapter<UserAdapter.ListViewHolder>() {
    private val callback = object : DiffUtil.ItemCallback<ListResponseUserItem>() {
        override fun areItemsTheSame(
            oldItem: ListResponseUserItem,
            newItem: ListResponseUserItem
        ): Boolean =
            oldItem.id == newItem.id

        override fun areContentsTheSame(
            oldItem: ListResponseUserItem,
            newItem: ListResponseUserItem
        ): Boolean =
            oldItem == newItem
    }

    val differ = AsyncListDiffer(this, callback)

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ListViewHolder =
        ListViewHolder(inflate(LayoutInflater.from(parent.context), parent, false))

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) =
        holder.bind(differ.currentList[position])

    override fun getItemCount(): Int = differ.currentList.size

    inner class ListViewHolder(private val binding: ItemListHomeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ListResponseUserItem) {
            binding.username.text = item.login
            binding.linkGithub.text = item.htmlUrl
            Glide.with(itemView).load(item.avatarUrl).into(binding.image)
            binding.root.setOnClickListener {
                val bundle = Bundle().apply {
                    putString(Constants.KEY_USERNAME, item.login)
                }
                it.findNavController().navigate(R.id.detailFragment, bundle)
            }
        }
    }

}
package com.ergea.githubuserapp.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.bumptech.glide.Glide
import com.ergea.githubuserapp.R
import com.ergea.githubuserapp.adapter.SectionsPagerAdapter
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.data.remote.model.DetailResponse
import com.ergea.githubuserapp.databinding.FragmentDetailBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.utils.Constants
import com.ergea.githubuserapp.utils.viewModelFactory
import com.ergea.githubuserapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator
import kotlin.properties.Delegates

class DetailFragment : Fragment() {

    private var _binding: FragmentDetailBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailViewModel by viewModelFactory {
        DetailViewModel(
            ServiceLocator.provideRemoteRepository(),
            ServiceLocator.provideLocalRepository(requireContext())
        )
    }
    private var isFavorite by Delegates.notNull<Boolean>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dataUsername = arguments?.getString(Constants.KEY_USERNAME)!!
        viewModel.getDetail(dataUsername)
        setUpSectionsPager(dataUsername)
        observeData()
        onBack()
    }

    private fun setUpSectionsPager(dataUsername: String) {
        val bundle = Bundle().apply {
            putString(Constants.KEY_USERNAME, dataUsername)
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this, bundle)
        binding.pager.adapter = sectionsPagerAdapter

        TabLayoutMediator(binding.tabLayout, binding.pager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
    }

    private fun observeData() {
        viewModel.snackBar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                activity?.window?.decorView?.rootView?.let { rootView ->
                    Snackbar.make(
                        rootView, msg, Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewModel.detail.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    showLoading(false)
                }
                is Resource.Success -> {
                    showLoading(false)
                    it.payload?.let { data ->
                        bindView(data)
                        selectFavorite(data)
                        data.id?.let { dataId -> viewModel.isFavorite(dataId) }
                    }
                }
                is Resource.Empty -> {
                    showLoading(false)
                }
            }
        }
        viewModel.isFavorite.observe(viewLifecycleOwner) {
            if (it == true) {
                binding.favorite.setImageResource(R.drawable.baseline_favorite_30)
            } else {
                binding.favorite.setImageResource(R.drawable.baseline_favorite_border_30)
            }
            isFavorite = it
        }
    }

    private fun selectFavorite(detailResponse: DetailResponse) {
        binding.favorite.setOnClickListener {
            if (!isFavorite) {
                viewModel.addFavorite(
                    FavoriteEntity(
                        id = detailResponse.id,
                        login = detailResponse.login,
                        avatarUrl = detailResponse.avatarUrl,
                        isFavorite = true
                    )
                )
                binding.favorite.setImageResource(R.drawable.baseline_favorite_30)
                isFavorite = true
            } else {
                viewModel.removeFavorite(
                    FavoriteEntity(
                        id = detailResponse.id,
                        login = detailResponse.login,
                        avatarUrl = detailResponse.avatarUrl,
                        isFavorite = true
                    )
                )
                binding.favorite.setImageResource(R.drawable.baseline_favorite_border_30)
                isFavorite = false
            }
        }
    }

    private fun bindView(it: DetailResponse) {
        binding.tvUsername.text = it.login
        with(binding.layoutDetailContent) {
            Glide.with(this@DetailFragment).load(it.avatarUrl).into(image)
            tvPosting.text = it.publicRepos.toString()
            tvFollowers.text = it.followers.toString()
            tvFollowing.text = it.following.toString()
            if (it.name != null) {
                tvName.isVisible = true
                tvName.text = it.name
            } else tvName.isVisible = false
            if (it.bio != null) {
                tvBio.isVisible = true
                tvBio.text = it.bio
            } else tvBio.isVisible = false
            btnView.setOnClickListener { view ->
                val uri = Uri.parse(it.htmlUrl)
                val intent = Intent(Intent.ACTION_VIEW, uri)
                startActivity(intent)
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutDetailContentShimmer.isVisible = isLoading
        binding.layoutDetailContent.constraintLayout.isVisible = !isLoading
        binding.tabLayout.isVisible = !isLoading
        binding.pager.isVisible = !isLoading
    }

    private fun onBack() {
        binding.backButton.setOnClickListener {
            it.findNavController().navigate(R.id.homeFragment)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1, R.string.tab_text_2
        )
    }
}
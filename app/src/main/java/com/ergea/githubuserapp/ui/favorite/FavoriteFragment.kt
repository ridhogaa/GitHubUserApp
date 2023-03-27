package com.ergea.githubuserapp.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.ergea.githubuserapp.adapter.FavoriteAdapter
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.databinding.FragmentFavoriteBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.utils.viewModelFactory
import com.ergea.githubuserapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FavoriteViewModel by viewModelFactory {
        FavoriteViewModel(ServiceLocator.provideLocalRepository(requireContext()))
    }
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAllFavorites()
        initFavoriteList()
        observeData()
        onBack()
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
        viewModel.favorite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.scrollView.isVisible = false
                    binding.rvFavorite.isVisible = false
                    binding.emptyFavorite.isVisible = false
                }
                is Resource.Error -> {
                    binding.scrollView.isVisible = false
                    binding.rvFavorite.isVisible = false
                    binding.emptyFavorite.isVisible = false
                }
                is Resource.Success -> {
                    binding.scrollView.isVisible = true
                    binding.rvFavorite.isVisible = true
                    binding.emptyFavorite.isVisible = false
                    favoriteAdapter.differ.submitList(it.payload)
                }
                is Resource.Empty -> {
                    binding.scrollView.isVisible = false
                    binding.rvFavorite.isVisible = false
                    binding.emptyFavorite.isVisible = true
                }
            }
        }
    }

    private fun initFavoriteList() {
        favoriteAdapter = FavoriteAdapter()
        binding.rvFavorite.apply {
            adapter = favoriteAdapter
            layoutManager = GridLayoutManager(requireContext(), 2)
            setHasFixedSize(true)
        }
        favoriteAdapter.itemClicked(object : FavoriteAdapter.FavClickItemCallback {
            override fun removeItem(favoriteEntity: FavoriteEntity) {
                viewModel.removeFavorite(favoriteEntity)
                viewModel.getAllFavorites()
            }
        })
    }

    private fun onBack() {
        binding.backButton.setOnClickListener {
            it.findNavController().popBackStack()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
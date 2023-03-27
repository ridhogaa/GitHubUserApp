package com.ergea.githubuserapp.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.ergea.githubuserapp.R
import com.ergea.githubuserapp.adapter.FavoriteAdapter
import com.ergea.githubuserapp.adapter.UserAdapter
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.databinding.FragmentHomeBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.ui.setting.SettingFragment
import com.ergea.githubuserapp.utils.viewModelFactory
import com.ergea.githubuserapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by viewModelFactory {
        HomeViewModel(
            ServiceLocator.provideRemoteRepository(),
            ServiceLocator.provideLocalRepository(requireContext())
        )
    }
    private lateinit var userAdapter: UserAdapter
    private lateinit var favoriteAdapter: FavoriteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getSomeFavoritesUsers()
        searchQuery()
        initUserList()
        initFavoriteList()
        observeData()
        goToSettings()
        goToMoreFavorite()
    }

    private fun goToMoreFavorite() {
        binding.layoutHomeContent.iconMoreFavorite.setOnClickListener {
            it.findNavController().navigate(R.id.action_homeFragment_to_favoriteFragment)
        }
    }

    private fun goToSettings() {
        binding.icBtnSetting.setOnClickListener {
            SettingFragment().show(parentFragmentManager, SettingFragment.TAG)
        }
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
        viewModel.searchResult.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    showLoading(true)
                }
                is Resource.Error -> {
                    showLoading(false)
                }
                is Resource.Success -> {
                    showLoading(false)
                    userAdapter.differ.submitList(it.payload?.items?.toList())
                }
                is Resource.Empty -> {
                    showLoading(false)
                }
            }
        }
        viewModel.favorite.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.layoutHomeContent.rvFavoriteList.isVisible = false
                    binding.layoutHomeContent.emptyFavorite.isVisible = false
                }
                is Resource.Error -> {
                    binding.layoutHomeContent.rvFavoriteList.isVisible = false
                    binding.layoutHomeContent.emptyFavorite.isVisible = false
                }
                is Resource.Success -> {
                    binding.layoutHomeContent.rvFavoriteList.isVisible = true
                    binding.layoutHomeContent.emptyFavorite.isVisible = false
                    favoriteAdapter.differ.submitList(it.payload?.toList())
                }
                is Resource.Empty -> {
                    binding.layoutHomeContent.rvFavoriteList.isVisible = false
                    binding.layoutHomeContent.emptyFavorite.isVisible = true
                }
            }
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.layoutHomeContent.shimmerList.isVisible = isLoading
        binding.layoutHomeContent.rvUserList.isVisible = !isLoading
    }

    private fun initUserList() {
        userAdapter = UserAdapter()
        binding.layoutHomeContent.rvUserList.apply {
            adapter = userAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            setHasFixedSize(true)
        }
    }

    private fun initFavoriteList() {
        favoriteAdapter = FavoriteAdapter()
        binding.layoutHomeContent.rvFavoriteList.apply {
            adapter = favoriteAdapter
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
        }
        favoriteAdapter.itemClicked(object : FavoriteAdapter.FavClickItemCallback {
            override fun removeItem(favoriteEntity: FavoriteEntity) {
                viewModel.removeFavorite(favoriteEntity)
                viewModel.getSomeFavoritesUsers()
            }
        })
    }

    private fun searchQuery() {
        binding.searchView.setIconifiedByDefault(false)
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchUsers(query ?: "")
                binding.layoutHomeContent.shimmerList.startShimmer()
                binding.searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText?.length == 0) {
                    binding.layoutHomeContent.shimmerList.startShimmer()
                    binding.searchView.clearFocus()
                }
                return true
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
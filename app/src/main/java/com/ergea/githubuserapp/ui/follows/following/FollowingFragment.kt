package com.ergea.githubuserapp.ui.follows.following

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ergea.githubuserapp.adapter.UserAdapter
import com.ergea.githubuserapp.databinding.FragmentFollowingBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.utils.Constants
import com.ergea.githubuserapp.utils.viewModelFactory
import com.ergea.githubuserapp.wrapper.Resource
import com.google.android.material.snackbar.Snackbar

class FollowingFragment : Fragment() {

    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: FollowingViewModel by viewModelFactory {
        FollowingViewModel(ServiceLocator.provideRemoteRepository())
    }
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initList()
        observeData()
    }

    private fun observeData() {
        val name = arguments?.getString(Constants.KEY_USERNAME)!!
        viewModel.getFollowing(name)
        viewModel.snackBar.observe(viewLifecycleOwner) {
            it.getContentIfNotHandled()?.let { msg ->
                activity?.window?.decorView?.rootView?.let { rootView ->
                    Snackbar.make(
                        rootView,
                        msg,
                        Snackbar.LENGTH_SHORT
                    ).show()
                }
            }
        }
        viewModel.following.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Loading -> {
                    binding.shimmerList.isVisible = true
                    binding.rvUser.isVisible = false
                    binding.emptyFollowing.isVisible = false
                }
                is Resource.Error -> {
                    binding.shimmerList.isVisible = false
                    binding.rvUser.isVisible = false
                    binding.emptyFollowing.isVisible = false
                }
                is Resource.Success -> {
                    binding.shimmerList.isVisible = false
                    binding.rvUser.isVisible = true
                    binding.emptyFollowing.isVisible = false
                    userAdapter.differ.submitList(it.payload)
                }
                is Resource.Empty -> {
                    binding.shimmerList.isVisible = false
                    binding.rvUser.isVisible = false
                    binding.emptyFollowing.isVisible = true
                }
            }
        }
    }

    private fun initList() {
        userAdapter = UserAdapter()
        binding.apply {
            rvUser.layoutManager = LinearLayoutManager(requireContext())
            rvUser.setHasFixedSize(true)
            rvUser.adapter = userAdapter
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}
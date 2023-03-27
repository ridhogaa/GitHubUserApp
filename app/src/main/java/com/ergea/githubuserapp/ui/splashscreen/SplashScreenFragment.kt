package com.ergea.githubuserapp.ui.splashscreen

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.ergea.githubuserapp.R
import com.ergea.githubuserapp.databinding.FragmentSplashScreenBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.utils.Constants
import com.ergea.githubuserapp.utils.viewModelFactory

@SuppressLint("CustomSplashScreen")
class SplashScreenFragment : Fragment() {

    private var _binding: FragmentSplashScreenBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SplashScreenViewModel by viewModelFactory {
        SplashScreenViewModel(ServiceLocator.provideLocalRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashScreenBinding.inflate(layoutInflater, container, false)
        loadingScreen()
        return binding.root
    }

    private fun loadingScreen() {
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }

        binding.animationView.animate().translationX(Constants.ANIMATION_TRANSLATION_X)
            .setDuration(Constants.DURATION).startDelay = Constants.START_DELAY
        binding.textView.animate().translationX(Constants.TEXT_TRANSLATION_X)
            .setDuration(Constants.DURATION).startDelay = Constants.START_DELAY

        Handler().postDelayed({
            lifecycleScope.launchWhenResumed {
                findNavController().navigate(R.id.action_splashScreenFragment_to_homeFragment)
            }
        }, Constants.LOADING_TIME)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

}
package com.ergea.githubuserapp.ui.setting

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatDelegate
import com.ergea.githubuserapp.R
import com.ergea.githubuserapp.databinding.FragmentSettingBinding
import com.ergea.githubuserapp.di.ServiceLocator
import com.ergea.githubuserapp.utils.viewModelFactory
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SettingFragment : BottomSheetDialogFragment() {
    private var _binding: FragmentSettingBinding? = null
    private val binding get() = _binding!!
    private val viewModel: SettingViewModel by viewModelFactory {
        SettingViewModel(ServiceLocator.provideLocalRepository(requireContext()))
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getThemeSettings().observe(viewLifecycleOwner) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                binding.switchTheme.isChecked = true
                binding.switchTheme.text = getString(R.string.white_mode)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                binding.switchTheme.isChecked = false
                binding.switchTheme.text = getString(R.string.dark_mode)
            }
        }

        binding.switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            viewModel.saveThemeSetting(isChecked)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val TAG = "SettingBottomSheet"
    }

}
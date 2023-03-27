package com.ergea.githubuserapp.ui.setting

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.ergea.githubuserapp.data.repository.LocalRepository
import kotlinx.coroutines.launch

class SettingViewModel(private val localRepository: LocalRepository) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> = localRepository.getTheme().asLiveData()

    fun saveThemeSetting(condition: Boolean) = viewModelScope.launch {
        localRepository.setTheme(condition)
    }
}
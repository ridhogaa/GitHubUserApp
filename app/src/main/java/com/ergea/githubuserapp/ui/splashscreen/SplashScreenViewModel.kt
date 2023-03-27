package com.ergea.githubuserapp.ui.splashscreen

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.ergea.githubuserapp.data.repository.LocalRepository

/**
 * @Author: ridhogymnastiar
 * Github: https://github.com/ridhogaa
 */

class SplashScreenViewModel(private val localRepository: LocalRepository) : ViewModel() {
    fun getThemeSettings(): LiveData<Boolean> = localRepository.getTheme().asLiveData()
}
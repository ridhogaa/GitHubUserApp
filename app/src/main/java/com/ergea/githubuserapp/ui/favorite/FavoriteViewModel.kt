package com.ergea.githubuserapp.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.data.repository.LocalRepository
import com.ergea.githubuserapp.wrapper.Event
import com.ergea.githubuserapp.wrapper.Resource
import kotlinx.coroutines.launch

class FavoriteViewModel(private val localRepository: LocalRepository) : ViewModel() {

    private val _favorite = MutableLiveData<Resource<List<FavoriteEntity>>>()
    val favorite: LiveData<Resource<List<FavoriteEntity>>> = _favorite

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun getAllFavorites() = viewModelScope.launch {
        _favorite.postValue(Resource.Loading())
        if (localRepository.getAllFavorites().payload?.size!! > 0) {
            _favorite.postValue(localRepository.getAllFavorites())
        } else {
            _favorite.postValue(Resource.Empty())
        }
    }

    fun removeFavorite(favoriteEntity: FavoriteEntity) = viewModelScope.launch {
        localRepository.removeFavorite(favoriteEntity)
        _snackBar.postValue(Event("Success delete from favorite"))
    }
}
package com.ergea.githubuserapp.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.data.remote.model.*
import com.ergea.githubuserapp.data.repository.LocalRepository
import com.ergea.githubuserapp.data.repository.RemoteRepository
import com.ergea.githubuserapp.wrapper.Event
import com.ergea.githubuserapp.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DetailViewModel(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _detail = MutableLiveData<Resource<DetailResponse>>()
    val detail: LiveData<Resource<DetailResponse>> = _detail

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> = _isFavorite

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun getDetail(username: String) = viewModelScope.launch(Dispatchers.IO) {
        _detail.postValue(Resource.Loading())
        try {
            val response = remoteRepository.getDetailUser(username)
            viewModelScope.launch(Dispatchers.Main) {
                if (response.payload != null) {
                    _detail.postValue(Resource.Success(response.payload))
                } else {
                    _detail.postValue(Resource.Error(response.exception, null))
                    _snackBar.postValue(Event(response.message.toString()))
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                _detail.postValue(Resource.Error(e, null))
                _snackBar.postValue(Event(e.message.toString()))
            }
        }
    }

    fun addFavorite(favoriteEntity: FavoriteEntity) = viewModelScope.launch {
        localRepository.addFavorite(favoriteEntity)
        _snackBar.postValue(Event("Success add to favorite"))
    }

    fun removeFavorite(favoriteEntity: FavoriteEntity) = viewModelScope.launch {
        localRepository.removeFavorite(favoriteEntity)
        _snackBar.postValue(Event("Success delete from favorite"))
    }

    fun isFavorite(id: Int) = viewModelScope.launch(Dispatchers.Main) {
        _isFavorite.postValue(localRepository.isFavorite(id))
    }
}
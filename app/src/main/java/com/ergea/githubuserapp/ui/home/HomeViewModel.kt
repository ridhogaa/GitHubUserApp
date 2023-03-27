package com.ergea.githubuserapp.ui.home

import androidx.lifecycle.*
import com.ergea.githubuserapp.data.local.database.entity.FavoriteEntity
import com.ergea.githubuserapp.data.remote.model.SearchResponse
import com.ergea.githubuserapp.data.repository.LocalRepository
import com.ergea.githubuserapp.data.repository.RemoteRepository
import com.ergea.githubuserapp.wrapper.Event
import com.ergea.githubuserapp.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel(
    private val remoteRepository: RemoteRepository,
    private val localRepository: LocalRepository
) : ViewModel() {

    private val _searchResult = MutableLiveData<Resource<SearchResponse>>()
    val searchResult: LiveData<Resource<SearchResponse>> = _searchResult

    private val _favorite = MutableLiveData<Resource<List<FavoriteEntity>>>()
    val favorite: LiveData<Resource<List<FavoriteEntity>>> = _favorite

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    init {
        searchUsers("q", per_page = 10)
    }

    fun searchUsers(username: String, sort: String = "", per_page: Int = 0) =
        viewModelScope.launch(Dispatchers.IO) {
            _searchResult.postValue(Resource.Loading())
            try {
                val response = remoteRepository.searchUsers(username, sort, per_page)
                viewModelScope.launch(Dispatchers.Main) {
                    if (response.payload != null) {
                        if (response.payload.items?.size!! > 0) {
                            _searchResult.postValue(Resource.Success(response.payload))
                        } else {
                            _snackBar.postValue(Event("No results were found!"))
                        }
                    } else {
                        _searchResult.postValue(Resource.Error(response.exception, null))
                    }
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    _searchResult.postValue(Resource.Error(e, null))
                    _snackBar.postValue(Event(e.message.toString()))
                }
            }
        }

    fun getSomeFavoritesUsers() = viewModelScope.launch {
        _favorite.postValue(Resource.Loading())
        if (localRepository.getSomeFavorites().payload?.size!! > 0) {
            _favorite.postValue(localRepository.getSomeFavorites())
        } else {
            _favorite.postValue(Resource.Empty())
        }
    }

    fun removeFavorite(favoriteEntity: FavoriteEntity) = viewModelScope.launch {
        localRepository.removeFavorite(favoriteEntity)
        _snackBar.postValue(Event("Success delete from favorite"))
    }

}
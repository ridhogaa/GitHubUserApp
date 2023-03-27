package com.ergea.githubuserapp.ui.follows.followers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ergea.githubuserapp.data.remote.model.ListResponseUserItem
import com.ergea.githubuserapp.data.repository.RemoteRepository
import com.ergea.githubuserapp.wrapper.Event
import com.ergea.githubuserapp.wrapper.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FollowersViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    private val _followers = MutableLiveData<Resource<List<ListResponseUserItem>>>()
    val followers: LiveData<Resource<List<ListResponseUserItem>>> = _followers

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun getFollowers(username: String) = viewModelScope.launch(Dispatchers.IO) {
        _followers.postValue(Resource.Loading())
        try {
            val response = remoteRepository.getFollowers(username)
            viewModelScope.launch(Dispatchers.Main) {
                if (response.payload != null) {
                    if (response.payload.isNotEmpty()) {
                        _followers.postValue(Resource.Success(response.payload))
                    } else {
                        _followers.postValue(Resource.Empty())
                        _snackBar.postValue(Event("No results followers were found!"))
                    }
                } else {
                    _followers.postValue(Resource.Error(response.exception, null))
                    _snackBar.postValue(Event(response.message.toString()))
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                _followers.postValue(Resource.Error(e, null))
                _snackBar.postValue(Event(e.message.toString()))
            }
        }
    }
}
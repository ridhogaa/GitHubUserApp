package com.ergea.githubuserapp.ui.follows.following

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

class FollowingViewModel(private val remoteRepository: RemoteRepository) : ViewModel() {

    private val _following = MutableLiveData<Resource<List<ListResponseUserItem>>>()
    val following: LiveData<Resource<List<ListResponseUserItem>>> = _following

    private val _snackBar = MutableLiveData<Event<String>>()
    val snackBar: LiveData<Event<String>> = _snackBar

    fun getFollowing(username: String) = viewModelScope.launch(Dispatchers.IO) {
        _following.postValue(Resource.Loading())
        try {
            val response = remoteRepository.getFollowing(username)
            viewModelScope.launch(Dispatchers.Main) {
                if (response.payload != null) {
                    if (response.payload.isNotEmpty()) {
                        _following.postValue(Resource.Success(response.payload))
                    } else {
                        _following.postValue(Resource.Empty())
                        _snackBar.postValue(Event("No results following were found!"))
                    }
                } else {
                    _following.postValue(Resource.Error(response.exception, null))
                    _snackBar.postValue(Event(response.message.toString()))
                }
            }
        } catch (e: Exception) {
            viewModelScope.launch(Dispatchers.Main) {
                _following.postValue(Resource.Error(e, null))
                _snackBar.postValue(Event(e.message.toString()))
            }
        }
    }
}
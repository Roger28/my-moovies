package com.android.mymoovies.home

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.android.mymoovies.database.getDatabase
import com.android.mymoovies.repository.MoviesRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)
    private val database = getDatabase(application)
    private val videosRepository = MoviesRepository(database)

    init {
        viewModelScope.launch {
            videosRepository.refreshVideos()
        }
    }

    val playlist = videosRepository.movies

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}
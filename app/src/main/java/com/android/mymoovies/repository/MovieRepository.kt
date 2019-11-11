package com.android.mymoovies.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.android.mymoovies.database.MoviesDatabase
import com.android.mymoovies.database.asDomainModel
import com.android.mymoovies.domain.Movie
import com.android.mymoovies.network.Network
import com.android.mymoovies.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MoviesRepository(private val database: MoviesDatabase) {

    val movies: LiveData<List<Movie>> = Transformations.map(database.movieDao.getVideos()) {
        it.asDomainModel()
    }

    suspend fun refreshVideos() {
        withContext(Dispatchers.IO) {
            val playlist = Network.movieService.getPlaylistAsync("61e66f4478be035d176b1382fe38d6ce", "pt-BR").await()
            database.movieDao.insertAll(*playlist.asDatabaseModel())
        }
    }


}
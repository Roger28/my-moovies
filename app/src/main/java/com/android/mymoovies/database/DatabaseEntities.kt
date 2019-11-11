package com.android.mymoovies.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.mymoovies.domain.Movie

@Entity
data class DatabaseMovie constructor(
    @PrimaryKey
    val title: String,
    val release_date: String,
    val vote_average: Double,
    val poster_path: String)

fun List<DatabaseMovie>.asDomainModel(): List<Movie> {
    return map {
        Movie(
            title = it.title,
            release_date = it.release_date,
            vote_average = it.vote_average,
            poster_path = it.poster_path)
    }
}
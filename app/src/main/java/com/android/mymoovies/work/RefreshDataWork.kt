package com.android.mymoovies.work

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.android.mymoovies.database.getDatabase
import com.android.mymoovies.repository.MoviesRepository
import retrofit2.HttpException

class RefreshDataWork(appContext: Context, params: WorkerParameters) :
    CoroutineWorker(appContext, params) {

    override suspend fun doWork(): Result {
        val database = getDatabase(applicationContext)
        val repository = MoviesRepository(database)
        return try {
            repository.refreshMovies()
            Result.success()
        } catch (e: HttpException) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

}
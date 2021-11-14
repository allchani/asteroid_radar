package com.udacity.asteroidradar.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.api.NasaApi
import com.udacity.asteroidradar.api.getTodayDate
import com.udacity.asteroidradar.api.parseAsteroidsJsonResult
import com.udacity.asteroidradar.asDatabaseModel
import com.udacity.asteroidradar.database.AsteroidsDatabase
import com.udacity.asteroidradar.database.asDomainModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class AsteroidsRepository(private val database: AsteroidsDatabase) {

    private val today: String = getTodayDate()

    fun getAsteroidsWithFilter(filterValue: String): LiveData<List<Asteroid>> {
        return when (filterValue) {
            "week" -> Transformations.map(database.asteroidDao.getWeekAsteroids(today)) {
                it.asDomainModel()
            }
            "today" -> Transformations.map(database.asteroidDao.getTodayAsteroids(today)) {
                it.asDomainModel()
            }
            else -> Transformations.map(database.asteroidDao.getAsteroids()) {
                it.asDomainModel()
            }
        }

    }

    //No filter version
//    val asteroids: LiveData<List<Asteroid>> =
//            Transformations.map(database.asteroidDao.getAsteroids()) {
//             it.asDomainModel()
//            }

    suspend fun refreshAsteroids() {
        withContext(Dispatchers.IO) {
            try {
                val asteroidsJSONObject = NasaApi.retrofitService.getAsteroidsProperties()
                val networkAsteroids = parseAsteroidsJsonResult(JSONObject(asteroidsJSONObject))
                database.asteroidDao.insertAll(networkAsteroids.asDatabaseModel())

            } catch (e: Exception) {
                Log.w("ERROR", e.message.toString())
            }
        }
    }

    suspend fun refreshPicture() {
        withContext(Dispatchers.IO) {
            try {
                val networkPicture = NasaApi.retrofitService.getPictureOfDay()
                database.asteroidDao.insertPicture(networkPicture.asDatabaseModel())
            } catch (e: Exception) {
                Log.w("ERROR", e.message.toString())
            }

        }
    }

}
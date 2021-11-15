package com.udacity.asteroidradar.database

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface AsteroidDao {
    @Query("select * from databaseasteroid ORDER BY closeApproachDate ASC")
    fun getAsteroids(): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid WHERE closeApproachDate >= (:today) ORDER BY closeApproachDate ASC")
    fun getWeekAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Query("select * from databaseasteroid WHERE closeApproachDate == (:today)")
    fun getTodayAsteroids(today: String): LiveData<List<DatabaseAsteroid>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(asteroids: List<DatabaseAsteroid>)

    @Query("select * from databasepicture")
    fun getPicture(): LiveData<DatabasePicture>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPicture(picture: DatabasePicture)
}

@Database(entities = [DatabaseAsteroid::class, DatabasePicture::class], version = 3, exportSchema = false)
abstract class AsteroidsDatabase : RoomDatabase() {
    abstract val asteroidDao: AsteroidDao
}

private lateinit var INSTANCE: AsteroidsDatabase

fun getDataBase(context: Context): AsteroidsDatabase {
    synchronized(AsteroidsDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(context.applicationContext,
                    AsteroidsDatabase::class.java, "asteroids")
                .fallbackToDestructiveMigration()
                .build()
        }
    }
    return INSTANCE
}
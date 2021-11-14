package com.udacity.asteroidradar.main

import android.app.Application
import androidx.lifecycle.*
import com.udacity.asteroidradar.Asteroid
import com.udacity.asteroidradar.PictureOfDay
import com.udacity.asteroidradar.database.asDomainModel
import com.udacity.asteroidradar.database.getDataBase
import com.udacity.asteroidradar.repository.AsteroidsRepository
import kotlinx.android.synthetic.main.fragment_detail.*
import kotlinx.coroutines.launch

enum class Filters(val value: String) {
    SHOW_WEEK("week"), SHOW_TODAY("today"), SHOW_SAVED("saved")
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    //Data
    private val database = getDataBase(application)
    private val asteroidsRepository = AsteroidsRepository(database)

    //Navigation
    private val _navigateToSelectedProperty = MutableLiveData<Asteroid>()
    val navigateToSelectedProperty: LiveData<Asteroid>
        get() = _navigateToSelectedProperty

    fun displayPropertyDetails(asteroid: Asteroid) {
        _navigateToSelectedProperty.value = asteroid
    }

    fun displayPropertyDetailsComplete() {
        _navigateToSelectedProperty.value = null
    }

    //Filter
    private val filterType = MutableLiveData(Filters.SHOW_WEEK)

    fun updateFilter(filter: Filters) {
        setAsteroidsListFilter(filter)
    }

    private fun setAsteroidsListFilter(filter: Filters){
        filterType.value = filter
    }

    //Select the filter and get list with this part to show in RecyclerView.
    val asteroidsList: LiveData<List<Asteroid>> = Transformations.switchMap(filterType){
        asteroidsRepository.getAsteroidsWithFilter(it.value)
    }

    // For Picture of the day
    val pictureOfTheDay: LiveData<PictureOfDay> = Transformations.map(database.asteroidDao.getPicture()){
        it.asDomainModel()
    }




    init {
        viewModelScope.launch {
            asteroidsRepository.refreshAsteroids()
            asteroidsRepository.refreshPicture()
        }
    }






//    class Factory(val app: Application) : ViewModelProvider.Factory {
//        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
//            if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
//                @Suppress("UNCHECKED_CAST")
//                return MainViewModel(app) as T
//            }
//            throw IllegalArgumentException("Unable to construct viewmodel")
//        }
//    }

}
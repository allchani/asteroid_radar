package com.udacity.asteroidradar.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.udacity.asteroidradar.Asteroid

class DetailViewModel(asteroid: Asteroid) : ViewModel() {
    private val _selectedProperty = MutableLiveData<Asteroid>()

    // The external LiveData for the SelectedProperty
    val selectedProperty: LiveData<Asteroid>
        get() = _selectedProperty

    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProperty.value = asteroid
    }
}
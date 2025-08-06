package com.myapp.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myapp.model.DataStore
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch


class SensorViewModel(application: Application) : AndroidViewModel(application) {
    val altitudeLiveData = MutableLiveData<Float>()
    val accelLiveData = MutableLiveData<Triple<Float, Float, Float>>()

    fun startAltitudeMonitoring() { /* AltitudeSensor starten */ }
}
package com.myapp.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myapp.model.sensors.MotionSensor
import com.myapp.model.sensors.AltitudeSensor
import com.myapp.model.sensors.MicrophoneRecorder
import com.myapp.model.sensors.CameraHandler

class SensorViewModel(application: Application) : AndroidViewModel(application) {

    // --- LiveData für Accelerometer ---
    private val _motionLiveData = MutableLiveData<Triple<Float, Float, Float>>()
    val motionLiveData: LiveData<Triple<Float, Float, Float>> = _motionLiveData

    // --- LiveData für Höhe (Barometer) ---
    private val _altitudeLiveData = MutableLiveData<Float>()
    val altitudeLiveData: LiveData<Float> = _altitudeLiveData

    // --- LiveData für Aufnahme-Status ---
    private val _isRecording = MutableLiveData<Boolean>(false)
    val isRecording: LiveData<Boolean> = _isRecording

    // --- Sensor-Helper-Instanzen ---
    private var motionSensor: MotionSensor? = null
    private var altitudeSensor: AltitudeSensor? = null
    private var microphoneRecorder: MicrophoneRecorder? = null
    private var cameraHandler: CameraHandler? = null

    // --- Sensors starten ---
    fun startSensors(context: Context, audioPath: String) {
        motionSensor = MotionSensor(context) { x, y, z ->
            _motionLiveData.postValue(Triple(x, y, z))
        }.also { it.register() }

        altitudeSensor = AltitudeSensor(context) { altitude ->
            _altitudeLiveData.postValue(altitude)
        }.also { it.register() }

        microphoneRecorder = MicrophoneRecorder(audioPath)
    }

    // --- Sensors stoppen ---
    fun stopSensors() {
        motionSensor?.unregister()
        altitudeSensor?.unregister()
    }

    // --- Aufnahme-Methoden ---
    fun startRecording() {
        microphoneRecorder?.startRecording()
        _isRecording.postValue(true)
    }

    fun stopRecording() {
        microphoneRecorder?.stopRecording()
        _isRecording.postValue(false)
    }

    // --- CameraHandler für CameraX ---
    fun getCameraHandler(context: Context): CameraHandler {
        if (cameraHandler == null) {
            cameraHandler = CameraHandler(context)
        }
        return cameraHandler!!
    }
}
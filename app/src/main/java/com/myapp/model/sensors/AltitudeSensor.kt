package com.myapp.model.sensors


import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log

class AltitudeSensor(
    context: Context,
    private val onNewAltitude: (altitude: Float) -> Unit = {}
) {

    companion object {
        private const val TAG = "PressureSensor"
    }

    private val sensorManager by lazy {
        context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    }

    private val pressureSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

    private val sensorEventListener = object : SensorEventListener {
        override fun onSensorChanged(event: SensorEvent?) {
            val pressure = event?.values?.getOrNull(0) ?: return
            // Berechne Höhe (Altitüde) in Meter
            val altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
            Log.d(TAG, "Höhe: $altitude m")
            onNewAltitude(altitude)
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }

    fun register() {
        if (!hasSensor()) return
        sensorManager.registerListener(
            sensorEventListener,
            pressureSensor,
            SensorManager.SENSOR_DELAY_NORMAL
        )
    }

    fun unregister() {
        sensorManager.unregisterListener(sensorEventListener)
    }

    fun hasSensor(): Boolean = pressureSensor != null
}

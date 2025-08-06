val sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
val pressureSensor = sensorManager.getDefaultSensor(Sensor.TYPE_PRESSURE)

val sensorEventListener = object : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        val pressure = event?.values?.get(0) ?: return
        // Umrechnung in Höhe (optional)
        val altitude = SensorManager.getAltitude(SensorManager.PRESSURE_STANDARD_ATMOSPHERE, pressure)
        Log.d("Altitude", "Höhe: $altitude m")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

sensorManager.registerListener(sensorEventListener, pressureSensor, SensorManager.SENSOR_DELAY_NORMAL)
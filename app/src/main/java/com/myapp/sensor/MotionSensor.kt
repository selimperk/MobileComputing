val accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)

val accelListener = object : SensorEventListener {
    override fun onSensorChanged(event: SensorEvent?) {
        val x = event?.values?.get(0)
        val y = event?.values?.get(1)
        val z = event?.values?.get(2)
        Log.d("Accelerometer", "x=$x y=$y z=$z")
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
}

sensorManager.registerListener(accelListener, accelerometer, SensorManager.SENSOR_DELAY_NORMAL)
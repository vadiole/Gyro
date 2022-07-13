package vadiole.gyro.data

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import vadiole.gyro.util.toDegrees

class RotationManager @Inject constructor(@ApplicationContext context: Context) {
    private val sensorManager: SensorManager = context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
    private var rotationVectorSensor: Sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR)

    fun getOrientation(): Flow<Triple<Float, Float, Float>> = callbackFlow {
        val listener = object : SensorEventListener {
            override fun onSensorChanged(event: SensorEvent) {
                val values = event.values
                val rotationMatrix = FloatArray(16)
                SensorManager.getRotationMatrixFromVector(rotationMatrix, values)
                val orientation = SensorManager.getOrientation(rotationMatrix, FloatArray(3))
                val triple = Triple(
                    orientation[0].toDegrees(), // Azimuth, angle of rotation about the -z axis
                    orientation[1].toDegrees(), // Pitch, angle of rotation about the x axis
                    orientation[2].toDegrees(), // Roll, angle of rotation about the y axis
                )
                trySend(triple)
            }

            override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) = Unit
        }
        sensorManager.registerListener(listener, rotationVectorSensor, SensorManager.SENSOR_DELAY_GAME)
        awaitClose {
            sensorManager.unregisterListener(listener)
        }
    }
}
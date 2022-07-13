package vadiole.gyro.data

import javax.inject.Inject

class TimestampProvider @Inject constructor() {
    fun getCurrentTimestamp(): Long {
        return System.currentTimeMillis()
    }
}
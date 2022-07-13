package vadiole.gyro.util

fun Float.toDegrees(): Float {
    return this * RADIANS_TO_DEGREES
}

/**
 * Constant by which to multiply an angular value in radians to obtain an
 * angular value in degrees.
 */
private const val RADIANS_TO_DEGREES = 57.29577951308232f
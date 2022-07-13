package vadiole.gyro.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import vadiole.gyro.data.RotationManager

class GetTextSizeUseCase @Inject constructor(
    private val rotationManager: RotationManager,
) {
    fun invoke(): Flow<Float> {
        return rotationManager
            .getOrientation()
            .map { (_, _, roll) ->
                when {
                    roll > ROTATION_THRESHOLD -> {
                        MAX_TEXT_SIZE
                    }
                    roll < -ROTATION_THRESHOLD -> {
                        MIN_TEXT_SIZE
                    }
                    else -> {
                        DEFAULT_TEXT_SIZE
                    }
                }
            }
    }

    companion object {
        private const val MIN_TEXT_SIZE = 12f
        private const val DEFAULT_TEXT_SIZE = 16f
        private const val MAX_TEXT_SIZE = 20f
        private const val ROTATION_THRESHOLD = 30f
    }
}
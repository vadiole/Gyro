package vadiole.gyro.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import vadiole.gyro.domain.GetSessionUseCase
import vadiole.gyro.domain.GetTextSizeUseCase

@HiltViewModel
class GyroViewModel @Inject constructor(
    private val getSessionUseCase: GetSessionUseCase,
    private val getTextSizeUseCase: GetTextSizeUseCase,
) : ViewModel() {
    val sessionCounter = liveData {
        emitSource(
            source = getSessionUseCase
                .invoke()
                .asLiveData(
                    timeoutInMs = 0L
                )
        )
    }

    val textSize = liveData {
        emitSource(
            source = getTextSizeUseCase
                .invoke()
                .asLiveData(
                    timeoutInMs = 0L
                )
        )
    }
}
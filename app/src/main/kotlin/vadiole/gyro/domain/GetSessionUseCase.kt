package vadiole.gyro.domain

import javax.inject.Inject
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.launch
import vadiole.gyro.data.ConfigManager
import vadiole.gyro.data.TimestampProvider

class GetSessionUseCase @Inject constructor(
    private val configManager: ConfigManager,
    private val timestampProvider: TimestampProvider,
) {

    fun invoke(): Flow<Int> = callbackFlow {
        val now = timestampProvider.getCurrentTimestamp()
        val lastSessionTimestamp = configManager.lastSessionTimestamp
        val isSessionExpired = now - lastSessionTimestamp > SESSION_MAX_LENGTH
        val session = if (isSessionExpired) {
            val newSession = configManager.sessionCount + 1
            configManager.sessionCount = newSession
            newSession
        } else {
            configManager.sessionCount
        }
        trySend(session)

        val activeSession = launch {
            // we need to save the last time of an active session while the application is in foreground,
            // so that in case of death we know when the last session was active
            while (true) {
                delay(timeMillis = SESSION_JOB_DELAY)
                configManager.lastSessionTimestamp = timestampProvider.getCurrentTimestamp()
            }
        }

        awaitClose {
            activeSession.cancel()
        }
    }

    companion object {
        private const val SESSION_MAX_LENGTH: Long = 10 * 60 * 1000 // 10 min
        private const val SESSION_JOB_DELAY: Long = 1 * 1000 // 1 sec
    }
}
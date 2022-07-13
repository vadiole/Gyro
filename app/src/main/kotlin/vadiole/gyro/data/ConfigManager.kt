package vadiole.gyro.data

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class ConfigManager @Inject constructor(@ApplicationContext context: Context) {
    private val prefs = context.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    private val editor = prefs.edit()

    var lastSessionTimestamp: Long
        get() = prefs.getLong(KEY_LAST_SESSION_TIMESTAMP, Long.MAX_VALUE)
        set(value) {
            edit {
                putLong(KEY_LAST_SESSION_TIMESTAMP, value)
            }
        }

    var sessionCount: Int
        get() = prefs.getInt(KEY_SESSION_COUNT, 0)
        set(value) {
            edit {
                putInt(KEY_SESSION_COUNT, value)
            }
        }

    private fun edit(commit: Boolean = false, block: SharedPreferences.Editor.() -> Unit) {
        block.invoke(editor)
        if (commit) {
            editor.commit()
        } else {
            editor.apply()
        }
    }

    companion object {
        private const val NAME = "gyro_local_config"
        private const val KEY_LAST_SESSION_TIMESTAMP = "lastSessionTimestamp"
        private const val KEY_SESSION_COUNT = "sessionCount"
    }
}
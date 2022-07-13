package vadiole.gyro.presentation

import android.content.res.Configuration.UI_MODE_NIGHT_MASK
import android.content.res.Configuration.UI_MODE_NIGHT_YES
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.widget.FrameLayout.LayoutParams
import android.widget.FrameLayout.LayoutParams.MATCH_PARENT
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat.setOnApplyWindowInsetsListener
import androidx.core.view.WindowCompat
import androidx.core.view.WindowCompat.setDecorFitsSystemWindows
import androidx.core.view.WindowInsetsCompat.Type.navigationBars
import androidx.core.view.WindowInsetsCompat.Type.statusBars
import dagger.hilt.android.AndroidEntryPoint
import vadiole.gyro.R

@AndroidEntryPoint
class GyroActivity : AppCompatActivity() {

    private val gyroViewModel: GyroViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setDecorFitsSystemWindows(window, false)
        setOnApplyWindowInsetsListener(window.decorView) { v, insets ->
            v.setPadding(
                0, insets.getInsets(statusBars()).top,
                0, insets.getInsets(navigationBars()).bottom
            )
            insets
        }

        val sessionCounterTextView = TextView(this).apply {
            layoutParams = LayoutParams(MATCH_PARENT, MATCH_PARENT)
            setTextSize(TypedValue.COMPLEX_UNIT_SP, 16f)
            setTextColor(getColor(R.color.text_primary))
            gravity = Gravity.CENTER
            isAllCaps = true
        }
        setContentView(sessionCounterTextView)

        subscribe(sessionCounterTextView)
    }

    private fun subscribe(sessionCounterTextView: TextView) {
        gyroViewModel.sessionCounter.observe(this) { sessionCounter ->
            sessionCounterTextView.text = getString(R.string.session_count, sessionCounter)
        }
        gyroViewModel.textSize.observe(this) { textSize ->
            sessionCounterTextView.textSize = textSize
        }
    }

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        if (hasFocus) {
            val isNightMode = resources.configuration.uiMode and UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES
            val insetsController = WindowCompat.getInsetsController(window, window.decorView)
            insetsController.isAppearanceLightStatusBars = !isNightMode
            insetsController.isAppearanceLightNavigationBars = !isNightMode
        }
    }
}
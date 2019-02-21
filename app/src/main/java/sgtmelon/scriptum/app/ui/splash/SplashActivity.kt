package sgtmelon.scriptum.app.ui.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.office.utils.AppUtils.beforeFinish

/**
 * Стартовый экран приложения
 */
class SplashActivity : AppCompatActivity(), SplashCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java)
    }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.context = this
        viewModel.callback = this

        beforeFinish { viewModel.onStartApplication(intent.extras) }
    }

    override fun startFromNotification(arrayIntent: Array<Intent>) = startActivities(arrayIntent)

    override fun startNormal(intent: Intent) = startActivity(intent)

    companion object {
        const val STATUS_OPEN = "INTENT_SPLASH_STATUS_OPEN"
        const val NOTE_ID = "INTENT_SPLASH_NOTE_ID"

        fun getIntent(context: Context, noteId: Long): Intent {
            return Intent(context, SplashActivity::class.java)
                    .putExtra(STATUS_OPEN, true)
                    .putExtra(NOTE_ID, noteId)
        }
    }

}
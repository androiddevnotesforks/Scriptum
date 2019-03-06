package sgtmelon.scriptum.app.screen.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.office.annot.key.NoteType
import sgtmelon.scriptum.office.data.NoteData
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

        fun getIntent(context: Context, noteType: NoteType, id: Long): Intent {
            return Intent(context, SplashActivity::class.java)
                    .putExtra(STATUS_OPEN, true)
                    .putExtra(NoteData.Intent.TYPE, noteType.ordinal)
                    .putExtra(NoteData.Intent.ID, id)
        }
    }

}
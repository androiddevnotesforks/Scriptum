package sgtmelon.scriptum.screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.office.utils.beforeFinish
import sgtmelon.scriptum.screen.callback.SplashCallback
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Стартовый экран приложения
 *
 * @author SerjantArbuz
 */
class SplashActivity : AppCompatActivity(), SplashCallback {

    private val viewModel by lazy {
        ViewModelProviders.of(this).get(SplashViewModel::class.java).apply {
            callback = this@SplashActivity
        }
    }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        beforeFinish { viewModel.onStartApplication(intent.extras) }
    }

    companion object {
        const val STATUS_OPEN = "INTENT_SPLASH_STATUS_OPEN"

        fun Context.getSplashIntent(noteItem: NoteItem): Intent =
                Intent(this, SplashActivity::class.java)
                        .putExtra(STATUS_OPEN, true)
                        .putExtra(NoteData.Intent.TYPE, noteItem.type.ordinal)
                        .putExtra(NoteData.Intent.ID, noteItem.id)
    }

}
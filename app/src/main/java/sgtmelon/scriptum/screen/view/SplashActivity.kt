package sgtmelon.scriptum.screen.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProviders
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.room.entity.NoteEntity
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

        beforeFinish { viewModel.onSetup(intent.extras) }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    companion object {
        const val OPEN_SCREEN = "INTENT_SPLASH_OPEN_SCREEN"

        @StringDef(OpenFrom.BIND, OpenFrom.ALARM)
        annotation class OpenFrom {
            companion object {
                private const val PREFIX = "OPEN_FROM"

                const val BIND = "${PREFIX}_BIND"
                const val ALARM = "${PREFIX}_ALARM"
            }
        }

        fun Context.getSplashAlarmIntent(noteEntity: NoteEntity): Intent =
                Intent(this, SplashActivity::class.java)
                        .putExtra(OPEN_SCREEN, OpenFrom.ALARM)
                        .putExtra(NoteData.Intent.ID, noteEntity.id)
                        .putExtra(NoteData.Intent.COLOR, noteEntity.color)

        fun Context.getSplashBindIntent(noteEntity: NoteEntity): Intent =
                Intent(this, SplashActivity::class.java)
                        .putExtra(OPEN_SCREEN, OpenFrom.BIND)
                        .putExtra(NoteData.Intent.ID, noteEntity.id)
                        .putExtra(NoteData.Intent.TYPE, noteEntity.type.ordinal)
    }

}
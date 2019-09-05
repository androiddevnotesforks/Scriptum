package sgtmelon.scriptum.screen.ui

import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.annotation.StringDef
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.R
import sgtmelon.scriptum.control.alarm.AlarmControl
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.factory.VmFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.room.entity.NoteEntity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity

/**
 * Start screen of application
 *
 * @author SerjantArbuz
 */
class SplashActivity : AppCompatActivity(), ISplashActivity {

    private val iViewModel by lazy { VmFactory.getSplashViewModel(activity = this) }

    private val iAlarmControl by lazy { AlarmControl.getInstance(context = this) }

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        beforeFinish { iViewModel.onSetup(intent.extras) }
    }

    override fun onDestroy() {
        super.onDestroy()
        iViewModel.onDestroy()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    override fun cancelAlarm(intent: PendingIntent) = iAlarmControl.cancel(intent)

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

        fun getAlarmInstance(context: Context, noteEntity: NoteEntity) =
                getAlarmInstance(context, noteEntity.id, noteEntity.color)

        fun getAlarmInstance(context: Context, id: Long, @Color color: Int): Intent =
                Intent(context, SplashActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(OPEN_SCREEN, OpenFrom.ALARM)
                        .putExtra(NoteData.Intent.ID, id)
                        .putExtra(NoteData.Intent.COLOR, color)

        fun getBindInstance(context: Context, noteEntity: NoteEntity): Intent =
                Intent(context, SplashActivity::class.java)
                        .putExtra(OPEN_SCREEN, OpenFrom.BIND)
                        .putExtra(NoteData.Intent.ID, noteEntity.id)
                        .putExtra(NoteData.Intent.TYPE, noteEntity.type.ordinal)

    }

}
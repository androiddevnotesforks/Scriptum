package sgtmelon.scriptum.screen.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.factory.ViewModelFactory
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.OpenFrom
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.ui.notification.NotificationActivity

/**
 * Start screen of application
 */
class SplashActivity : AppCompatActivity(), ISplashActivity {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    private val iViewModel by lazy { ViewModelFactory.getSplashViewModel(activity = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /**
         * If keyboard was open in another app.
         */
        hideKeyboard()

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

    override fun startIntroActivity() = startActivity(IntroActivity[this])

    override fun startMainActivity() = startActivity(MainActivity[this])

    override fun startAlarmActivity(id: Long) {
        startActivities(arrayOf(MainActivity[this], AlarmActivity[this, id]))
    }

    override fun startNoteActivity(id: Long, @Color color: Int, type: NoteType) {
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type, id, color]))
    }

    override fun startNotificationActivity() {
        startActivities(arrayOf(MainActivity[this], NotificationActivity[this]))
    }


    companion object {
        fun getAlarmInstance(context: Context, id: Long): Intent =
                Intent(context, SplashActivity::class.java)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                        .putExtra(NoteData.Intent.ID, id)

        fun getBindInstance(context: Context, item: NoteItem): Intent =
                Intent(context, SplashActivity::class.java)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                        .putExtra(NoteData.Intent.ID, item.id)
                        .putExtra(NoteData.Intent.COLOR, item.color)
                        .putExtra(NoteData.Intent.TYPE, item.type.ordinal)

        fun getNotificationInstance(context: Context): Intent =
                Intent(context, SplashActivity::class.java)
                        .putExtra(OpenFrom.INTENT_KEY, OpenFrom.INFO)
    }

}
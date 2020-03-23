package sgtmelon.scriptum.presentation.screen.ui.impl

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import sgtmelon.scriptum.R
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.OpenFrom
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel
import javax.inject.Inject

/**
 * Start screen of application
 */
class SplashActivity : AppCompatActivity(), ISplashActivity {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    @Inject internal lateinit var viewModel: ISplashViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getSplashBuilder().set(activity = this).build()
                .inject(activity = this)

        super.onCreate(savedInstanceState)

        /**
         * If keyboard was open in another app.
         */
        hideKeyboard()

        beforeFinish { viewModel.onSetup(intent.extras) }
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
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

    override fun startNoteActivity(id: Long, @Color color: Int, type: Int) {
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type, id, color]))
    }

    override fun startNotificationActivity() {
        startActivities(arrayOf(MainActivity[this], NotificationActivity[this]))
    }


    companion object {
        fun getAlarmInstance(context: Context, id: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            return Intent(context, SplashActivity::class.java)
                    .addFlags(flags)
                    .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                    .putExtra(NoteData.Intent.ID, id)
        }

        fun getBindInstance(context: Context, item: NoteItem): Intent {
            return Intent(context, SplashActivity::class.java)
                    .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                    .putExtra(NoteData.Intent.ID, item.id)
                    .putExtra(NoteData.Intent.COLOR, item.color)
                    .putExtra(NoteData.Intent.TYPE, item.type.ordinal)
        }

        fun getNotificationInstance(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
                    .putExtra(OpenFrom.INTENT_KEY, OpenFrom.INFO)
        }
    }

}
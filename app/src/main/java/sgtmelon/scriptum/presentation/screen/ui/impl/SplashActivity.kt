package sgtmelon.scriptum.presentation.screen.ui.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.domain.model.annotation.firebase.FireKey
import sgtmelon.common.test.annotation.RunPrivate
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.domain.model.key.firebase.RunType
import sgtmelon.scriptum.extension.beforeFinish
import sgtmelon.scriptum.extension.hideKeyboard
import sgtmelon.common.test.idling.impl.WaitIdlingResource
import sgtmelon.scriptum.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel
import javax.inject.Inject
import android.graphics.Color as AndroidColor

/**
 * Start screen of application.
 */
class SplashActivity : ParentActivity(),
    ISplashActivity {

    init {
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
    }

    @Inject internal lateinit var viewModel: ISplashViewModel

    private val broadcastControl by lazy { BroadcastControl[this] }

    override fun onCreate(savedInstanceState: Bundle?) {
        ScriptumApplication.component.getSplashBuilder().set(activity = this).build()
            .inject(activity = this)

        super.onCreate(savedInstanceState)

        setCrashlyticsKeys()

        /**
         * If keyboard was open in another app.
         */
        hideKeyboard()

        viewModel.onSetup(intent.extras)
    }

    private fun setCrashlyticsKeys() {
        val instance = FirebaseCrashlytics.getInstance()

        instance.setCustomKey(FireKey.RUN_TYPE, when {
            BuildConfig.DEBUG && isTesting -> RunType.TEST.toString()
            BuildConfig.DEBUG && !isTesting -> RunType.DEBUG.toString()
            else -> RunType.RELEASE.toString()
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }

    override fun setStatusBarColor() {
        window.statusBarColor = AndroidColor.TRANSPARENT
    }

    override fun setNavigationColor(@Theme theme: Int) {
        window.navigationBarColor = AndroidColor.TRANSPARENT
    }

    override fun setNavigationDividerColor(@Theme theme: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            window.navigationBarDividerColor = AndroidColor.TRANSPARENT
        }
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    /**
     * [beforeFinish] not needed because inside [IntroActivity.get] happen clear start.
     */
    override fun openIntroScreen() = startActivity(IntroActivity[this])

    override fun openMainScreen() = beforeFinish { startActivity(MainActivity[this]) }

    override fun openAlarmScreen(id: Long) = beforeFinish {
        WaitIdlingResource.getInstance().startWork(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], AlarmActivity[this, id]))
    }

    override fun openNoteScreen(id: Long, @Color color: Int, type: Int) = beforeFinish {
        WaitIdlingResource.getInstance().startWork(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type, id, color]))
    }

    override fun openNotificationScreen() = beforeFinish {
        WaitIdlingResource.getInstance().startWork(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NotificationActivity[this]))
    }

    override fun openHelpDisappearScreen() = beforeFinish {
        WaitIdlingResource.getInstance().startWork(waitMillis = 3000)
        startActivities(arrayOf(
            MainActivity[this],
            PreferenceActivity[this, PreferenceScreen.PREFERENCE],
            PreferenceActivity[this, PreferenceScreen.HELP],
            HelpDisappearActivity[this]
        ))
    }

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = broadcastControl.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = broadcastControl.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcastControl.sendNotifyInfoBind(count)

    //endregion

    companion object {
        /**
         * Variable for detect test running.
         */
        @RunPrivate var isTesting = false

        fun getAlarmInstance(context: Context, id: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            return Intent(context, SplashActivity::class.java)
                .addFlags(flags)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.ALARM)
                .putExtra(Note.Intent.ID, id)
        }

        fun getBindInstance(context: Context, item: NoteItem): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.BIND)
                .putExtra(Note.Intent.ID, item.id)
                .putExtra(Note.Intent.COLOR, item.color)
                .putExtra(Note.Intent.TYPE, item.type.ordinal)
        }

        fun getNotificationInstance(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.NOTIFICATIONS)
        }

        fun getHelpDisappearInstance(context: Context): Intent {
            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, OpenFrom.HELP_DISAPPEAR)
        }
    }

}
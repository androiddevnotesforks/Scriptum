package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.cleanup.domain.model.data.IntentData.Note
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.key.firebase.RunType
import sgtmelon.scriptum.cleanup.extension.beforeFinish
import sgtmelon.scriptum.cleanup.extension.hideKeyboard
import sgtmelon.scriptum.cleanup.presentation.control.broadcast.BroadcastControl
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ParentActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.infrastructure.model.firebase.FireKey
import sgtmelon.scriptum.infrastructure.model.key.ThemeDisplayed
import sgtmelon.test.idling.getWaitIdling
import sgtmelon.test.prod.RunPrivate
import android.graphics.Color as AndroidColor

/**
 * Start screen of application.
 */
// TODO lint
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

    override fun setNavigationColor(theme: ThemeDisplayed) {
        window.navigationBarColor = AndroidColor.TRANSPARENT
    }

    override fun setNavigationDividerColor(theme: ThemeDisplayed) {
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
        getWaitIdling().start(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], AlarmActivity[this, id]))
    }

    override fun openNoteScreen(id: Long, color: Int, type: Int) = beforeFinish {
        getWaitIdling().start(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type, id, color]))
    }

    override fun openNotificationScreen() = beforeFinish {
        getWaitIdling().start(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NotificationActivity[this]))
    }

    override fun openHelpDisappearScreen() = beforeFinish {
        getWaitIdling().start(waitMillis = 3000)
        startActivities(
            arrayOf(
                MainActivity[this],
                PreferenceActivity[this, PreferenceScreen.PREFERENCE],
                PreferenceActivity[this, PreferenceScreen.HELP],
                HelpDisappearActivity[this]
            )
        )
    }

    override fun openCreateNoteScreen(type: NoteType) = beforeFinish {
        getWaitIdling().start(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type.ordinal]))
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

        fun getCreateNoteInstance(context: Context, type: NoteType): Intent {
            val key = when (type) {
                NoteType.TEXT -> OpenFrom.CREATE_TEXT
                NoteType.ROLL -> OpenFrom.CREATE_ROLL
            }

            return Intent(context, SplashActivity::class.java)
                .putExtra(OpenFrom.INTENT_KEY, key)
        }
    }

}
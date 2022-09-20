package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import com.google.firebase.crashlytics.FirebaseCrashlytics
import javax.inject.Inject
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.cleanup.domain.model.key.PreferenceScreen
import sgtmelon.scriptum.cleanup.domain.model.key.firebase.RunType
import sgtmelon.scriptum.cleanup.extension.NO_ID_LAYOUT
import sgtmelon.scriptum.cleanup.extension.beforeFinish
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.intro.IntroActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.note.NoteActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.notification.NotificationActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.PreferenceActivity
import sgtmelon.scriptum.cleanup.presentation.screen.ui.impl.preference.help.HelpDisappearActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.BroadcastDelegator
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.test.idling.getWaitIdling
import sgtmelon.test.prod.RunPrivate

/**
 * Start screen of application.
 */
// TODO lint
class SplashActivity : ThemeActivity<ViewDataBinding>(),
    ISplashActivity {

    override val layoutId: Int = NO_ID_LAYOUT

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    @Inject lateinit var viewModel: ISplashViewModel

    private val broadcast by lazy { BroadcastDelegator(context = this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCrashlyticsKeys()

        viewModel.onSetup(intent.extras)
    }

    override fun inject(component: ScriptumComponent) {
        component.getSplashBuilder()
            .set(activity = this)
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    private fun setCrashlyticsKeys() {
        val instance = FirebaseCrashlytics.getInstance()

        instance.setCustomKey(
            FireData.RUN_TYPE, when {
                BuildConfig.DEBUG && isTesting -> RunType.TEST.toString()
                BuildConfig.DEBUG && !isTesting -> RunType.DEBUG.toString()
                else -> RunType.RELEASE.toString()
            }
        )
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
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

    override fun openNewNoteScreen(type: NoteType) = beforeFinish {
        getWaitIdling().start(waitMillis = 3000)
        startActivities(arrayOf(MainActivity[this], NoteActivity[this, type.ordinal]))
    }

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = broadcast.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) = broadcast.sendNotifyInfoBind(count)

    //endregion

    companion object {
        /** Variable for detect test running. */
        @RunPrivate var isTesting = false
    }
}
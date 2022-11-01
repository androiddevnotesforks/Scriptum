package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import javax.inject.Inject
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.firebase.RunType
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.NO_ID_LAYOUT
import sgtmelon.scriptum.infrastructure.utils.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.getCrashlytics
import sgtmelon.test.prod.RunPrivate

/**
 * Start screen of application.
 */
// TODO lint
class SplashActivity : ThemeActivity<ViewDataBinding>() {

    override val layoutId: Int = NO_ID_LAYOUT

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    // TODO changes in di
    @Inject lateinit var viewModel: SplashViewModelImpl

    private val bundleProvider = SplashBundleProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setCrashlyticsKeys()
        chooseOpenScreen()
    }

    override fun inject(component: ScriptumComponent) {
        component.getSplashBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    private fun setCrashlyticsKeys() {
        val instance = getCrashlytics()

        val runType = if (BuildConfig.DEBUG) {
            if (isTesting) RunType.TEST else RunType.DEBUG
        } else {
            RunType.RELEASE
        }
        instance.setCustomKey(FireData.RUN_TYPE, runType.name)
    }

    private fun chooseOpenScreen() {
        delegators?.broadcast?.run {
            sendTidyUpAlarm()
            sendNotifyNotesBind()
            sendNotifyInfoBind(count = null)
        }

        when (val it = bundleProvider.getData(intent.extras, viewModel.isFirstStart)) {
            is SplashOpen.Intro -> openIntroScreen()
            is SplashOpen.Simple -> openMainScreen()
            is SplashOpen.Alarm -> openAlarmScreen(it.id)
            is SplashOpen.Note -> openNoteScreen(it.id, it.color, it.type)
            is SplashOpen.Notifications -> openNotificationsScreen()
            is SplashOpen.HelpDisappear -> openHelpDisappearScreen()
            is SplashOpen.CreateNote -> openNewNoteScreen(it.type)
        }
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
    }

    /** [beforeFinish] not needed because [InstanceFactory.Intro] launch clear start. */
    private fun openIntroScreen() = startActivity(InstanceFactory.Intro[this])

    private fun openMainScreen() = beforeFinish { startActivity(InstanceFactory.Main[this]) }

    private fun openAlarmScreen(noteId: Long) = beforeFinish {
        startActivities(InstanceFactory.Chains.toAlarm(context = this, noteId))
    }

    private fun openNoteScreen(noteId: Long, color: Int, type: Int) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNote(context = this, noteId, color, type))
    }

    private fun openNotificationsScreen() = beforeFinish {
        startActivities(InstanceFactory.Chains.toNotifications(context = this))
    }

    private fun openHelpDisappearScreen() = beforeFinish {
        startActivities(InstanceFactory.Chains.toHelpDisappear(context = this))
    }

    private fun openNewNoteScreen(type: NoteType) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNewNote(context = this, type))
    }

    companion object {
        /** Variable for detect test running. */
        // TODO move to application
        @RunPrivate var isTesting = false
    }
}
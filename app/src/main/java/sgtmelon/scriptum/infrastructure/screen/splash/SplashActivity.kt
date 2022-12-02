package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.firebase.RunType
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.NO_LAYOUT
import sgtmelon.scriptum.infrastructure.utils.extensions.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.extensions.getCrashlytics

/**
 * Start screen of application.
 */
// TODO lint
class SplashActivity : ThemeActivity<ViewDataBinding>() {

    override val layoutId: Int = NO_LAYOUT

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    private val bundleProvider = SplashBundleProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        bundleProvider.getData(bundle = savedInstanceState ?: intent.extras)

        setCrashlyticsKeys()
        chooseOpenScreen()
    }

    override fun inject(component: ScriptumComponent) {
        component.getSplashBuilder()
            .set(owner = this)
            .build()
            .inject(activity = this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        bundleProvider.saveData(outState)
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
    }

    private fun setCrashlyticsKeys() {
        val instance = getCrashlytics()

        val runType = if (BuildConfig.DEBUG) {
            if (ScriptumApplication.isTesting) RunType.TEST else RunType.DEBUG
        } else {
            RunType.RELEASE
        }
        instance.setCustomKey(FireData.RUN_TYPE, runType.name)
    }

    private fun chooseOpenScreen() {
        delegators?.broadcast?.run {
            sendTidyUpAlarm()
            sendNotifyNotesBind()
            sendNotifyInfoBind()
        }

        when (val it = bundleProvider.open) {
            is SplashOpen.Main -> openMainScreen()
            is SplashOpen.Alarm -> openAlarmScreen(it.id)
            is SplashOpen.BindNote -> openNoteScreen(it.id, it.color, it.type)
            is SplashOpen.Notifications -> openNotificationsScreen()
            is SplashOpen.HelpDisappear -> openHelpDisappearScreen()
            is SplashOpen.CreateNote -> openNoteScreen(it.type)
        }
    }

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

    private fun openNoteScreen(type: NoteType) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNote(context = this, type))
    }
}
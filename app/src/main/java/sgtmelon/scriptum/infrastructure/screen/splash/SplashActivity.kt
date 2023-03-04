package sgtmelon.scriptum.infrastructure.screen.splash

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.BundleValueImpl
import sgtmelon.scriptum.infrastructure.bundle.decode
import sgtmelon.scriptum.infrastructure.bundle.encode
import sgtmelon.scriptum.infrastructure.bundle.intent
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Splash.Key
import sgtmelon.scriptum.infrastructure.model.key.SplashOpen
import sgtmelon.scriptum.infrastructure.model.key.firebase.RunType
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.NO_LAYOUT
import sgtmelon.scriptum.infrastructure.utils.extensions.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.extensions.getCrashlytics
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Start screen of application.
 */
// TODO lint
class SplashActivity : ThemeActivity<ViewDataBinding>() {

    override val layoutId: Int = NO_LAYOUT

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    private val openFrom = BundleValueImpl<String>(Key.OPEN)
    override val bundleValues: List<BundleValue> = listOf(openFrom)

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
        system?.broadcast?.run {
            sendTidyUpAlarm()
            sendNotifyNotesBind()
            sendNotifyInfoBind()
        }

        when (val it = openFrom.value?.decode<SplashOpen>()) {
            is SplashOpen.Main -> openMainScreen()
            is SplashOpen.Alarm -> openAlarmScreen(it.noteId)
            is SplashOpen.BindNote -> openNoteScreen(it)
            is SplashOpen.Notifications -> openNotificationsScreen()
            is SplashOpen.HelpDisappear -> openHelpDisappearScreen()
            is SplashOpen.CreateNote -> openNoteScreen(it.type)
            else -> openMainScreen()
        }
    }

    private fun openMainScreen() = beforeFinish { startActivity(InstanceFactory.Main[this]) }

    private fun openAlarmScreen(noteId: Long) = beforeFinish {
        startActivities(InstanceFactory.Chains.toAlarm(context = this, noteId))
    }

    private fun openNoteScreen(data: SplashOpen.BindNote) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNote(context = this, data))
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

    companion object {

        operator fun get(context: Context): Intent = context.intent<SplashActivity>()

        operator fun get(context: Context, open: SplashOpen): Intent =
            context.intent<SplashActivity>(Key.OPEN to open.encode())

        fun getAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return get(context, SplashOpen.Alarm(noteId)).addFlags(flags)
        }

        fun getBind(context: Context, item: NoteItem): Intent {
            return get(context, with(item) { SplashOpen.BindNote(id, color, type) })
        }
    }
}
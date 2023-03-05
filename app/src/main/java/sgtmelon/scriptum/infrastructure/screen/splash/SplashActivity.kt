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
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Splash.Key
import sgtmelon.scriptum.infrastructure.model.key.firebase.RunType
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.NO_LAYOUT
import sgtmelon.scriptum.infrastructure.utils.extensions.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.extensions.getCrashlytics
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type
import sgtmelon.test.idling.getWaitIdling

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

        system?.broadcast?.run {
            sendTidyUpAlarm()
            sendNotifyNotesBind()
            sendNotifyInfoBind()
        }

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

    private fun chooseOpenScreen() = beforeFinish {
        val open = openFrom.value?.decode<SplashOpen>() ?: SplashOpen.Main

        /** Needed for Android (UI) tests, when we open chain of screens. */
        if (open !is SplashOpen.Main) {
            getWaitIdling().start(waitMillis = 2000)
        }

        startActivities(open.getIntents(context = this))
    }

    companion object {

        operator fun get(context: Context): Intent = context.intent<SplashActivity>()

        operator fun get(context: Context, open: SplashOpen): Intent {
            return context.intent<SplashActivity>(Key.OPEN to open.encode())
        }

        fun getAlarm(context: Context, noteId: Long): Intent {
            val flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            return get(context, SplashOpen.Alarm(noteId)).addFlags(flags)
        }

        fun getBind(context: Context, item: NoteItem): Intent {
            return get(context, with(item) { SplashOpen.BindNote(id, type, color, name) })
        }

        fun getNotification(context: Context): Intent = get(context, SplashOpen.Notifications)

        @Deprecated("Remove after help disappear refactor")
        fun getHelpDisappear(context: Context): Intent = get(context, SplashOpen.HelpDisappear)

        /** This instance also used inside xml/shortcuts.xml. */
        fun getNewNote(context: Context, type: NoteType): Intent {
            return get(context, SplashOpen.NewNote(type))
        }

    }
}
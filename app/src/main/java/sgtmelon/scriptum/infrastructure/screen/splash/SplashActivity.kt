package sgtmelon.scriptum.infrastructure.screen.splash

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.infrastructure.bundle.BundleValue
import sgtmelon.scriptum.infrastructure.bundle.json.BundleSplashValue
import sgtmelon.scriptum.infrastructure.model.data.FireData
import sgtmelon.scriptum.infrastructure.model.key.firebase.RunType
import sgtmelon.scriptum.infrastructure.screen.ScriptumApplication
import sgtmelon.scriptum.infrastructure.screen.theme.ThemeActivity
import sgtmelon.scriptum.infrastructure.system.delegators.window.WindowUiKeys
import sgtmelon.scriptum.infrastructure.utils.extensions.NO_LAYOUT
import sgtmelon.scriptum.infrastructure.utils.extensions.beforeFinish
import sgtmelon.scriptum.infrastructure.utils.extensions.getCrashlytics
import sgtmelon.test.idling.getWaitIdling
import javax.inject.Inject
import sgtmelon.scriptum.infrastructure.screen.splash.SplashOpen as Open

/**
 * Start screen of application.
 */
// TODO lint
class SplashActivity : ThemeActivity<ViewDataBinding>() {

    override val layoutId: Int = NO_LAYOUT

    @Inject lateinit var viewModel: SplashViewModel

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    private val openFrom = BundleSplashValue()
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
        val open = openFrom.value

        /** Needed for Android (UI) tests, when we open chain of screens. */
        if (open !is Open.Main) {
            getWaitIdling().start(waitMillis = 2000)
        }

        val context = this
        when(open) {
            is Open.Main -> startActivity(open.getIntent(context))
            is Open.NotificationsHelp -> {
                viewModel.resetNotificationsHelp()
                startActivity(open.getIntent(context))
            }
            is Open.Notifications -> startActivities(open.getIntents(context))
            is Open.Alarm -> startActivities(open.getIntents(context))
            is Open.BindNote -> startActivities(open.getIntents(context))
            is Open.NewNote -> {
                val item = viewModel.getNewNote(open.type)
                startActivities(open.getIntents(context, item))
            }
        }
    }
}
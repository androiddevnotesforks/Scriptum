package sgtmelon.scriptum.cleanup.presentation.screen.ui.impl

import android.os.Bundle
import androidx.databinding.ViewDataBinding
import javax.inject.Inject
import sgtmelon.scriptum.BuildConfig
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.dagger.component.ScriptumComponent
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.infrastructure.factory.InstanceFactory
import sgtmelon.scriptum.infrastructure.model.data.FireData
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
class SplashActivity : ThemeActivity<ViewDataBinding>(),
    ISplashActivity {

    override val layoutId: Int = NO_ID_LAYOUT

    override val statusBar = WindowUiKeys.StatusBar.Transparent
    override val navigation = WindowUiKeys.Navigation.Transparent
    override val navDivider = WindowUiKeys.NavDivider.Transparent

    // TODO remove callback and move parsing inside activity or delegator
    @Inject lateinit var viewModel: ISplashViewModel

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
        val instance = getCrashlytics()

        val runType = if (BuildConfig.DEBUG) {
            if (isTesting) RunType.TEST else RunType.DEBUG
        } else {
            RunType.RELEASE
        }
        instance.setCustomKey(FireData.RUN_TYPE, runType.name)
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.onDestroy()
    }


    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fragment_fade_in, R.anim.fragment_fade_out)
    }

    /** [beforeFinish] not needed because [InstanceFactory.Intro] launch clear start. */
    override fun openIntroScreen() = startActivity(InstanceFactory.Intro[this])

    override fun openMainScreen() = beforeFinish { startActivity(InstanceFactory.Main[this]) }

    override fun openAlarmScreen(noteId: Long) = beforeFinish {
        startActivities(InstanceFactory.Chains.toAlarm(context = this, noteId))
    }

    override fun openNoteScreen(noteId: Long, color: Int, type: Int) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNote(context = this, noteId, color, type))
    }

    override fun openNotificationScreen() = beforeFinish {
        startActivities(InstanceFactory.Chains.toNotifications(context = this))
    }

    override fun openHelpDisappearScreen() = beforeFinish {
        startActivities(InstanceFactory.Chains.toHelpDisappear(context = this))
    }

    override fun openNewNoteScreen(type: NoteType) = beforeFinish {
        startActivities(InstanceFactory.Chains.toNewNote(context = this, type))
    }

    //region Broadcast functions

    override fun sendTidyUpAlarmBroadcast() = delegators.broadcast.sendTidyUpAlarm()

    override fun sendNotifyNotesBroadcast() = delegators.broadcast.sendNotifyNotesBind()

    /**
     * Not used here.
     */
    override fun sendCancelNoteBroadcast(id: Long) = Unit

    override fun sendNotifyInfoBroadcast(count: Int?) {
        delegators.broadcast.sendNotifyInfoBind(count)
    }

    //endregion

    companion object {
        /** Variable for detect test running. */
        @RunPrivate var isTesting = false
    }
}
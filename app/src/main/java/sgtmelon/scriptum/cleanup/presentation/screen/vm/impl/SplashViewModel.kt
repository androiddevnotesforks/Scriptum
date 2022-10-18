package sgtmelon.scriptum.cleanup.presentation.screen.vm.impl

import android.os.Bundle
import sgtmelon.scriptum.cleanup.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.cleanup.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.ISplashViewModel
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.model.data.IntentData.Note
import sgtmelon.scriptum.infrastructure.model.key.NoteType

/**
 * ViewModel for [ISplashActivity].
 */
class SplashViewModel(
    callback: ISplashActivity?,
    private val preferencesRepo: PreferencesRepo
) : ParentViewModel<ISplashActivity>(callback),
    ISplashViewModel {

    /**
     * Don't use coroutines here. Activity will be quickly destroyed.
     */
    override fun onSetup(bundle: Bundle?) {
        callback?.sendTidyUpAlarmBroadcast()
        callback?.sendNotifyNotesBroadcast()
        callback?.sendNotifyInfoBroadcast()

        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(OpenFrom.INTENT_KEY)) {
                OpenFrom.ALARM -> onAlarmStart(bundle)
                OpenFrom.BIND -> onBindStart(bundle)
                OpenFrom.NOTIFICATIONS -> callback?.openNotificationScreen()
                OpenFrom.HELP_DISAPPEAR -> callback?.openHelpDisappearScreen()
                OpenFrom.CREATE_TEXT -> callback?.openNewNoteScreen(NoteType.TEXT)
                OpenFrom.CREATE_ROLL -> callback?.openNewNoteScreen(NoteType.ROLL)
                else -> onSimpleStart()
            }
        }
    }

    private fun onSimpleStart() {
        if (preferencesRepo.isFirstStart) {
            callback?.openIntroScreen()
        } else {
            callback?.openMainScreen()
        }
    }

    private fun onAlarmStart(bundle: Bundle) {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)

        callback?.openAlarmScreen(id)
    }

    private fun onBindStart(bundle: Bundle) {
        val id = bundle.getLong(Note.Intent.ID, Note.Default.ID)
        val color = bundle.getInt(Note.Intent.COLOR, Note.Default.COLOR)
        val type = bundle.getInt(Note.Intent.TYPE, Note.Default.TYPE)

        callback?.openNoteScreen(id, color, type)
    }
}
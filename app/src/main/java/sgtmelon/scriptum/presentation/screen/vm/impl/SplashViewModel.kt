package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.domain.model.data.IntentData.Note
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [ISplashActivity].
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    private lateinit var interactor: ISplashInteractor

    fun setInteractor(interactor: ISplashInteractor) {
        this.interactor = interactor
    }


    /**
     * Don't use coroutines here. Activity will be quickly destroyed.
     */
    override fun onSetup(bundle: Bundle?) {
        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(OpenFrom.INTENT_KEY)) {
                OpenFrom.ALARM -> onAlarmStart(bundle)
                OpenFrom.BIND -> onBindStart(bundle)
                OpenFrom.NOTIFICATIONS -> callback?.openNotificationScreen()
                OpenFrom.HELP_DISAPPEAR -> callback?.openHelpDisappearScreen()
                else -> onSimpleStart()
            }
        }
    }

    private fun onSimpleStart() {
        if (interactor.firstStart) {
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
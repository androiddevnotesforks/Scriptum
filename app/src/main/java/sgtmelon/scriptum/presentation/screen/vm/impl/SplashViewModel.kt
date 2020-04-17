package sgtmelon.scriptum.presentation.screen.vm.impl

import android.app.Application
import android.os.Bundle
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.domain.model.annotation.OpenFrom
import sgtmelon.scriptum.domain.model.data.NoteData
import sgtmelon.scriptum.presentation.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.SplashActivity
import sgtmelon.scriptum.presentation.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
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
                OpenFrom.INFO -> callback?.startNotificationActivity()
                else -> onSimpleStart()
            }
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }


    private fun onSimpleStart() = if (interactor.firstStart) {
        callback?.startIntroActivity()
    } else {
        callback?.startMainActivity()
    }

    private fun onAlarmStart(bundle: Bundle) {
        val id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)

        callback?.startAlarmActivity(id)
    }

    private fun onBindStart(bundle: Bundle) {
        val id = bundle.getLong(NoteData.Intent.ID, NoteData.Default.ID)
        val color = bundle.getInt(NoteData.Intent.COLOR, NoteData.Default.COLOR)
        val type = bundle.getInt(NoteData.Intent.TYPE, NoteData.Default.TYPE)

        callback?.startNoteActivity(id, color, type)
    }

}
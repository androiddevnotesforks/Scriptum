package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.SplashInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.model.annotation.OpenFrom
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    private val iInteractor: ISplashInteractor by lazy { SplashInteractor(context, callback) }
    private val iBindInteractor: IBindInteractor by lazy { BindInteractor(context) }

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch {
            iInteractor.tidyUpAlarm()
            iBindInteractor.notifyNoteBind(callback)
            iBindInteractor.notifyInfoBind(callback)
        }

        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(OpenFrom.INTENT_KEY) ?: "") {
                OpenFrom.ALARM -> onAlarmStart(bundle)
                OpenFrom.BIND -> onBindStart(bundle)
                OpenFrom.INFO -> callback?.startNotificationActivity()
                else -> onSimpleStart()
            }
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { iInteractor.onDestroy() }


    private fun onSimpleStart() = if (iInteractor.firstStart) {
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
        val type = NoteType.values()[bundle.getInt(NoteData.Intent.TYPE, NoteData.Default.TYPE)]

        callback?.startNoteActivity(id, type)
    }

}
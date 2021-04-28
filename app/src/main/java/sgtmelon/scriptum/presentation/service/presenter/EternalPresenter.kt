package sgtmelon.scriptum.presentation.service.presenter

import sgtmelon.scriptum.domain.interactor.callback.eternal.IEternalInteractor
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.presentation.service.IEternalService
import java.util.*

/**
 * Presenter for [IEternalService].
 */
class EternalPresenter(
    private val interactor: IEternalInteractor
) : ParentPresenter<IEternalService>(),
    IEternalPresenter {

    override fun onSetup() {
        mainScope.launchBack {
            interactor.tidyUpAlarm()
            interactor.notifyNotesBind()
            interactor.notifyInfoBind()
        }
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }

    //region Receiver callback

    override fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        callback?.setAlarm(id, calendar, showToast)
    }

    override fun cancelAlarm(id: Long) {
        callback?.cancelAlarm(id)
    }

    override fun notifyAllNotes() {
        mainScope.launchBack { interactor.notifyNotesBind() }
    }

    override fun cancelNote(id: Long) {
        mainScope.launchBack { interactor.unbindNote(id) }
        callback?.cancelNoteBind(id)
    }

    override fun notifyInfo(count: Int?) {
        mainScope.launchBack { interactor.notifyInfoBind() }
    }

    //endregion

}
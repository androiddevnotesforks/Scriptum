package sgtmelon.scriptum.presentation.screen.presenter.system

import sgtmelon.scriptum.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.extension.launchBack
import sgtmelon.scriptum.presentation.screen.presenter.ParentPresenter
import sgtmelon.scriptum.presentation.screen.system.ISystemLogic
import java.util.*

/**
 * Presenter for [ISystemLogic].
 */
class SystemPresenter(
    private val interactor: ISystemInteractor
) : ParentPresenter<ISystemLogic>(),
    ISystemPresenter {

    override fun onSetup() {
        mainScope.launchBack {
            interactor.tidyUpAlarm()
            interactor.notifyNotesBind()
            interactor.notifyCountBind()
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

    override fun notifyCount(count: Int?) {
        mainScope.launchBack { interactor.notifyCountBind() }
    }

    //endregion

}
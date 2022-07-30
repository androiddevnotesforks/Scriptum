package sgtmelon.scriptum.cleanup.presentation.screen.presenter.system

import java.util.Calendar
import sgtmelon.common.utils.launchBack
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.ParentPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemLogic

/**
 * Presenter for [ISystemLogic].
 */
class SystemPresenter(
    private val interactor: ISystemInteractor
) : ParentPresenter<ISystemLogic>(),
    ISystemPresenter {

    override fun onSetup() {
        tidyUpAlarm()
        notifyAllNotes()
        notifyCount(count = null)
    }

    override fun onDestroy(func: () -> Unit) = super.onDestroy { interactor.onDestroy() }

    //region Receiver callback

    override fun tidyUpAlarm() {
        mainScope.launchBack { interactor.tidyUpAlarm() }
    }

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
        if (count != null) {
            callback?.notifyCountBind(count)
        } else {
            mainScope.launchBack { interactor.notifyCountBind() }
        }
    }

    override fun clearBind() {
        callback?.clearBind()
    }

    override fun clearAlarm() {
        callback?.clearAlarm()
    }

    //endregion

}
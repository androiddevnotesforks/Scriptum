package sgtmelon.scriptum.cleanup.presentation.screen.system

import android.content.Context
import android.content.IntentFilter
import java.util.Calendar
import javax.inject.Inject
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import sgtmelon.scriptum.infrastructure.factory.DelegatorFactory
import sgtmelon.scriptum.infrastructure.model.data.ReceiverData

/**
 * Logic class for working with alarm's and notifications.
 */
class SystemLogic(private val context: Context) : ISystemLogic {

    private var _delegators: DelegatorFactory? = null
    private val delegators get() = _delegators

    //region cleanup

    @Inject lateinit var presenter: ISystemPresenter

    private val receiver by lazy { SystemReceiver[presenter] }

    override fun setup() {
        _delegators = DelegatorFactory(context, lifecycle = null)

        ScriptumApplication.component.getSystemBuilder().set(logic = this).build()
            .inject(logic = this)

        context.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.SYSTEM))

        presenter.onSetup()
    }

    override fun release() {
        presenter.onDestroy()
        context.unregisterReceiver(receiver)
        delegators?.toast?.cancel()
    }

    //region Bridge functions

    override fun notifyNotesBind(itemList: List<NoteItem>) {
        delegators?.bind?.notifyNotes(itemList)
    }

    override fun cancelNoteBind(id: Long) {
        delegators?.bind?.cancelNote(id)
    }

    override fun notifyCountBind(count: Int) {
        delegators?.bind?.notifyCount(count)
    }

    override fun clearBind() {
        delegators?.bind?.clearRecent()
    }

    override fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        delegators?.alarm?.set(calendar, id, showToast)
    }

    override fun cancelAlarm(id: Long) {
        delegators?.alarm?.cancel(id)
    }

    override fun clearAlarm() {
        delegators?.alarm?.clear()
    }

    //endregion

    //endregion

}
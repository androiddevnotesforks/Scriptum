package sgtmelon.scriptum.cleanup.presentation.screen.system

import android.content.Context
import android.content.IntentFilter
import sgtmelon.scriptum.cleanup.domain.model.data.ReceiverData
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.presentation.control.system.AlarmControl
import sgtmelon.scriptum.cleanup.presentation.control.system.BindControl
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IAlarmControl
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IBindControl
import sgtmelon.scriptum.cleanup.presentation.control.toast.ToastControl
import sgtmelon.scriptum.cleanup.presentation.receiver.SystemReceiver
import sgtmelon.scriptum.cleanup.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.cleanup.presentation.screen.ui.ScriptumApplication
import java.util.*
import javax.inject.Inject

/**
 * Class with logic for alarm/notification setup/cancel.
 */
class SystemLogic : ISystemLogic {

    @Inject internal lateinit var presenter: ISystemPresenter

    private lateinit var toastControl: ToastControl
    private lateinit var bindControl: IBindControl
    private lateinit var alarmControl: IAlarmControl

    private val receiver by lazy { SystemReceiver[presenter] }

    override fun onCreate(context: Context) {
        ScriptumApplication.component.getSystemBuilder().set(logic = this).build()
            .inject(logic = this)

        context.registerReceiver(receiver, IntentFilter(ReceiverData.Filter.SYSTEM))

        toastControl = ToastControl(context)
        bindControl = BindControl[context]
        alarmControl = AlarmControl[context, toastControl]

        presenter.onSetup()
    }

    override fun onDestroy(context: Context) {
        presenter.onDestroy()
        context.unregisterReceiver(receiver)

        toastControl.onDestroy()
    }

    //region Bridge functions


    override fun notifyNotesBind(itemList: List<NoteItem>) = bindControl.notifyNotes(itemList)

    override fun cancelNoteBind(id: Long) = bindControl.cancelNote(id)

    override fun notifyCountBind(count: Int) = bindControl.notifyCount(count)

    override fun clearBind() = bindControl.clearRecent()

    override fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        alarmControl.set(calendar, id, showToast)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun clearAlarm() = alarmControl.clear()

    //endregion

}
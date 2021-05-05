package sgtmelon.scriptum.presentation.screen.system

import android.content.Context
import android.content.IntentFilter
import sgtmelon.scriptum.domain.model.data.ReceiverData
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.AlarmControl
import sgtmelon.scriptum.presentation.control.system.BindControl
import sgtmelon.scriptum.presentation.control.system.callback.IAlarmControl
import sgtmelon.scriptum.presentation.control.system.callback.IBindControl
import sgtmelon.scriptum.presentation.receiver.EternalReceiver
import sgtmelon.scriptum.presentation.screen.presenter.system.ISystemPresenter
import sgtmelon.scriptum.presentation.screen.ui.ScriptumApplication
import java.util.*
import javax.inject.Inject

/**
 * Class with logic for alarm/notification setup/cancel.
 */
class SystemLogic : ISystemLogic {

    @Inject internal lateinit var presenter: ISystemPresenter

    private lateinit var alarmControl: IAlarmControl
    private lateinit var bindControl: IBindControl

    private val eternalReceiver by lazy { EternalReceiver[presenter] }

    override fun onCreate(context: Context) {
        ScriptumApplication.component.getSystemBuilder().set(logic = this).build()
            .inject(logic = this)

        context.registerReceiver(eternalReceiver, IntentFilter(ReceiverData.Filter.ETERNAL))

        alarmControl = AlarmControl[context]
        bindControl = BindControl[context]

        presenter.onSetup()
    }

    override fun onDestroy(context: Context) {
        presenter.onDestroy()
        context.unregisterReceiver(eternalReceiver)
    }

    //region Bridge functions

    override fun setAlarm(id: Long, calendar: Calendar, showToast: Boolean) {
        alarmControl.set(calendar, id, showToast)
    }

    override fun cancelAlarm(id: Long) = alarmControl.cancel(id)

    override fun notifyNotesBind(itemList: List<NoteItem>) = bindControl.notifyNotes(itemList)

    override fun cancelNoteBind(id: Long) = bindControl.cancelNote(id)

    override fun notifyCountBind(count: Int) = bindControl.notifyCount(count)

    //endregion

}
package sgtmelon.scriptum.interactor.main

import android.content.Context
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.ui.callback.main.IMainBridge

class MainInteractor(context: Context, private var callback: IMainBridge?) :
        ParentInteractor(context),
        IMainInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override suspend fun tidyUpAlarm() = iAlarmRepo.getList().forEach {
        val calendar = it.alarm.date.getCalendar()
        val id = it.note.id

        if (calendar.beforeNow()) {
            callback?.cancelAlarm(id)
            iAlarmRepo.delete(id)
        } else {
            callback?.setAlarm(calendar, id)
        }
    }

}
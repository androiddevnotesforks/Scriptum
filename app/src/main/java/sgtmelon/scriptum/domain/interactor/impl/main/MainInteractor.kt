package sgtmelon.scriptum.domain.interactor.impl.main

import androidx.annotation.VisibleForTesting
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.domain.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.domain.interactor.impl.ParentInteractor
import sgtmelon.scriptum.presentation.screen.ui.callback.main.IMainBridge
import sgtmelon.scriptum.presentation.screen.vm.impl.main.MainViewModel

/**
 * Interactor for [MainViewModel].
 */
class MainInteractor(
        private val alarmRepo: IAlarmRepo,
        @VisibleForTesting var callback: IMainBridge?
) : ParentInteractor(),
        IMainInteractor {

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override suspend fun tidyUpAlarm() {
        alarmRepo.getList()?.forEach {
            val calendar = it.alarm.date.getCalendar()
            val id = it.note.id

            if (calendar.beforeNow()) {
                callback?.cancelAlarm(id)
                alarmRepo.delete(id)
            } else {
                callback?.setAlarm(calendar, id)
            }
        }
    }

}
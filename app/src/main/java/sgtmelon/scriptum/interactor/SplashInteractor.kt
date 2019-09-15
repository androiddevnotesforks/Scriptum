package sgtmelon.scriptum.interactor

import android.content.Context
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.ui.callback.ISplashBridge
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel]
 */
class SplashInteractor(context: Context, private var callback: ISplashBridge?) :
        ParentInteractor(context),
        ISplashInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override fun onDestroy(func: () -> Unit) = super.onDestroy { callback = null }


    override val firstStart: Boolean
        get() = iPreferenceRepo.firstStart.apply {
            if (this) iPreferenceRepo.firstStart = false
        }

    override suspend fun clearPastAlarm() = iAlarmRepo.getList().forEach {
        if (it.alarm.date.getCalendar().beforeNow()) {
            callback?.cancelAlarm(AlarmReceiver[it])
            iAlarmRepo.delete(it.note.id)
        }
    }

}
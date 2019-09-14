package sgtmelon.scriptum.interactor.splash

import android.content.Context
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.interactor.ParentInteractor
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.alarm.IAlarmRepo
import sgtmelon.scriptum.screen.vm.SplashViewModel

/**
 * Interactor for [SplashViewModel]
 */
class SplashInteractor(context: Context) : ParentInteractor(context), ISplashInteractor {

    private val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    override val firstStart: Boolean
        get() = iPreferenceRepo.firstStart.apply {
            if (this) iPreferenceRepo.firstStart = false
        }

    override suspend fun clearPastAlarm(callback: AlarmCallback.Cancel?) =
            iAlarmRepo.getList().forEach {
                if (it.alarm.date.getCalendar().beforeNow()) {
                    callback?.cancelAlarm(AlarmReceiver.getInstance(context, it))
                    iAlarmRepo.delete(it.note.id)
                }
            }

}
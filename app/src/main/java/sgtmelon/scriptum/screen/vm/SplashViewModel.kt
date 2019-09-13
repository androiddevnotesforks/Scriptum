package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.extension.beforeNow
import sgtmelon.extension.getCalendar
import sgtmelon.scriptum.interactor.splash.ISplashInteractor
import sgtmelon.scriptum.interactor.splash.SplashInteractor
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.SplashActivity.Companion.OpenFrom
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    private val iSplashInteractor: ISplashInteractor = SplashInteractor(context)

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch { clearPastAlarm() }

        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(SplashActivity.OPEN_SCREEN) ?: "") {
                OpenFrom.BIND -> {
                    val intent = NoteActivity.getInstance(context,
                            NoteType.values()[bundle.getInt(NoteData.Intent.TYPE)],
                            bundle.getLong(NoteData.Intent.ID)
                    )

                    callback?.startActivities(arrayOf(MainActivity.getInstance(context), intent))
                }
                OpenFrom.ALARM -> {
                    callback?.startActivity(AlarmActivity.getInstance(context,
                            bundle.getLong(NoteData.Intent.ID),
                            bundle.getInt(NoteData.Intent.COLOR)
                    ))
                }
                else -> onSimpleStart()
            }
        }
    }

    // TODO #RELEASE3
    @Suppress("RedundantSuspendModifier")
    private suspend fun clearPastAlarm() = iRoomAlarmRepo.getList().forEach {
        if (it.alarm.date.getCalendar().beforeNow()) {
            callback?.cancelAlarm(AlarmReceiver.getInstance(context, it))
            iRoomAlarmRepo.delete(it.note.id)
        }
    }

    private fun onSimpleStart(firstStart: Boolean = iSplashInteractor.firstStart) =
            callback?.startActivity(if (firstStart) {
                IntroActivity.getInstance(context)
            } else {
                MainActivity.getInstance(context)
            })

}
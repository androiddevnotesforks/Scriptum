package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.os.Bundle
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sgtmelon.scriptum.extension.beforeNow
import sgtmelon.scriptum.extension.getCalendar
import sgtmelon.scriptum.model.data.NoteData
import sgtmelon.scriptum.model.key.NoteType
import sgtmelon.scriptum.receiver.AlarmReceiver
import sgtmelon.scriptum.screen.ui.SplashActivity
import sgtmelon.scriptum.screen.ui.SplashActivity.Companion.OpenFrom
import sgtmelon.scriptum.screen.ui.callback.ISplashActivity
import sgtmelon.scriptum.screen.ui.intro.IntroActivity
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.ui.note.NoteActivity.Companion.getNoteIntent
import sgtmelon.scriptum.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.callback.ISplashViewModel

/**
 * ViewModel for [SplashActivity]
 *
 * @author SerjantArbuz
 */
class SplashViewModel(application: Application) : ParentViewModel<ISplashActivity>(application),
        ISplashViewModel {

    override fun onSetup(bundle: Bundle?) {
        viewModelScope.launch { clearPastAlarm() }

        if (bundle == null) {
            onSimpleStart()
        } else {
            when (bundle.getString(SplashActivity.OPEN_SCREEN) ?: "") {
                OpenFrom.BIND -> {
                    val intent = context.getNoteIntent(
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
    private suspend fun clearPastAlarm() = iAlarmRepo.getList().forEach {
        if (it.alarm.date.getCalendar(context).beforeNow()) {
            callback?.cancelAlarm(AlarmReceiver.getInstance(context, it.note.id, it.note.color))
            iAlarmRepo.delete(it.note.id)
        }
    }

    private fun onSimpleStart(firstStart: Boolean = iPreferenceRepo.firstStart) =
            callback?.startActivity(if (firstStart) {
                IntroActivity.getInstance(context)
            } else {
                MainActivity.getInstance(context)
            })

}
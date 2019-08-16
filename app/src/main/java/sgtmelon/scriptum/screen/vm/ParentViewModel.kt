package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.repository.alarm.AlarmRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.RoomRepo

/**
 * Родительский ViewModel
 *
 * @author SerjantArbuz
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    protected val iPreferenceRepo = PreferenceRepo(context)
    protected val iRoomRepo = RoomRepo.getInstance(context)
    protected val iAlarmRepo = AlarmRepo.getInstance(context)

    var callback: T? = null

    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
        callback = null
    }

}
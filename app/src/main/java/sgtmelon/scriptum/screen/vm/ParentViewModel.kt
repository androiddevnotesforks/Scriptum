package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo
import sgtmelon.scriptum.repository.room.alarm.AlarmRepo
import sgtmelon.scriptum.repository.room.alarm.IAlarmRepo

/**
 * Родительский ViewModel
 *
 * @author SerjantArbuz
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    protected val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)
    protected val iRoomRepo: IRoomRepo = RoomRepo(context)
    protected val iAlarmRepo: IAlarmRepo = AlarmRepo(context)

    var callback: T? = null

    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
        callback = null
    }

}
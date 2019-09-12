package sgtmelon.scriptum.screen.vm

import android.app.Application
import android.content.Context
import androidx.annotation.CallSuper
import androidx.lifecycle.AndroidViewModel
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo
import sgtmelon.scriptum.repository.room.alarm.IRoomAlarmRepo
import sgtmelon.scriptum.repository.room.alarm.RoomAlarmRepo

/**
 * Родительский ViewModel
 *
 * @author SerjantArbuz
 */
abstract class ParentViewModel<T>(application: Application) : AndroidViewModel(application) {

    protected val context: Context = application.applicationContext

    protected val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)
    protected val iRoomRepo: IRoomRepo = RoomRepo(context)
    protected val iRoomAlarmRepo: IRoomAlarmRepo = RoomAlarmRepo(context)

    var callback: T? = null

    @CallSuper open fun onDestroy(func: () -> Unit = {}) {
        func()
        callback = null
    }

}
package sgtmelon.scriptum.interactor

import android.content.Context
import androidx.annotation.CallSuper
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.repository.room.IRoomRepo
import sgtmelon.scriptum.repository.room.RoomRepo

/**
 * Parent class for interactor's
 */
abstract class ParentInteractor(context: Context) {

    protected val iPreferenceRepo: IPreferenceRepo = PreferenceRepo(context)
    protected val iRoomRepo: IRoomRepo = RoomRepo(context)

    @CallSuper open fun onDestroy(func: () -> Unit = {}) {}

}
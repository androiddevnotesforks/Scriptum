package sgtmelon.scriptum.interactor

import android.content.Context
import androidx.annotation.CallSuper
import sgtmelon.scriptum.interactor.callback.IParentInteractor
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

    /**
     * Same func like in [IParentInteractor] use for clear callback when cause [onDestroy]
     */
    @CallSuper open fun onDestroy(func: () -> Unit = {}) {}

}
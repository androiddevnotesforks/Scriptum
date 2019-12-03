package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.model.item.NoteItem
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Interface for communication [AlarmViewModel] with [AlarmInteractor]
 */
interface IAlarmInteractor : IParentInteractor {

    @Theme val theme: Int

    @Repeat val repeat: Int

    val volume: Int

    val volumeIncrease: Boolean

    suspend fun getModel(id: Long): NoteItem?

    fun setupRepeat(noteItem: NoteItem, valueArray: IntArray)

}
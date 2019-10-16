package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Interface for communication [AlarmViewModel] with [AlarmInteractor]
 */
interface IAlarmInteractor : IParentInteractor {

    @Theme val theme: Int

    @Repeat val repeat: Int

    val volume: Int

    val volumeIncrease: Boolean

    fun getModel(id: Long): NoteModel?

    /**
     * Return postpone time
     */
    fun setupRepeat(noteModel: NoteModel, valueArray: IntArray)

}
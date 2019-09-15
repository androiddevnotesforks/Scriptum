package sgtmelon.scriptum.interactor.callback.notification

import sgtmelon.scriptum.interactor.callback.IParentInteractor
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [AlarmInteractor]
 */
interface IAlarmInteractor : IParentInteractor {

    @Theme val theme: Int

    @Repeat val repeat: Int

    val volume: Int

    val volumeIncrease: Boolean

    fun getModel(id: Long): NoteModel?

    fun setupRepeat(noteModel: NoteModel, valueArray: IntArray)

}
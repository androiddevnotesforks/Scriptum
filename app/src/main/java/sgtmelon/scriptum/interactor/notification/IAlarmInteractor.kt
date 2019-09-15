package sgtmelon.scriptum.interactor.notification

import sgtmelon.scriptum.control.alarm.callback.IAlarmBridge
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Theme

/**
 * Interface for communicate with [AlarmInteractor]
 */
interface IAlarmInteractor {

    @Theme val theme: Int

    @Repeat val repeat: Int

    val volume: Int

    val volumeIncrease: Boolean

    fun getModel(id: Long): NoteModel?

    fun setupRepeat(noteModel: NoteModel, callback: IAlarmBridge.Set?, valueArray: IntArray)

}
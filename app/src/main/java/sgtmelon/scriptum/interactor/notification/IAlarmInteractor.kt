package sgtmelon.scriptum.interactor.notification

import sgtmelon.scriptum.control.alarm.callback.AlarmCallback
import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.annotation.Color
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

    @Color val defaultColor: Int

    fun getModel(id: Long): NoteModel?

    fun setupRepeat(noteModel: NoteModel, callback: AlarmCallback.Set?, valueArray: IntArray)

}
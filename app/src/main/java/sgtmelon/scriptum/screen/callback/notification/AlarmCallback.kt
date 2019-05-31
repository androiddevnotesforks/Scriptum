package sgtmelon.scriptum.screen.callback.notification

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.screen.view.notification.AlarmActivity
import sgtmelon.scriptum.screen.vm.notification.AlarmViewModel

/**
 * Интерфейс для общения [AlarmViewModel] с [AlarmActivity]
 *
 * @author SerjantArbuz
 */
interface AlarmCallback {

    fun finishOnLong(millis: Long)

    fun setupNote(noteModel: NoteModel)

    fun showControl()

    fun finishAlarm()

}
package sgtmelon.scriptum.repository.alarm

import sgtmelon.scriptum.model.NoteModel
import sgtmelon.scriptum.model.item.AlarmItem

/**
 * Интерфейс для общения с [AlarmRepo]
 *
 * @author SerjantArbuz
 */
interface IAlarmRepo {

    fun getList(): MutableList<NoteModel>

    fun delete(item: AlarmItem)

}
package sgtmelon.scriptum.cleanup.data.room.converter.model

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.domain.model.item.NoteItem
import sgtmelon.scriptum.cleanup.domain.model.item.RollItem
import sgtmelon.scriptum.infrastructure.database.DbData.RollVisible
import sgtmelon.scriptum.infrastructure.model.key.preference.NoteType
import sgtmelon.scriptum.infrastructure.utils.extensions.note.type

/**
 * Converter for [NoteEntity]/[RollEntity]/[AlarmEntity] and [NoteItem]
 */
class NoteConverter(
    private val alarmConverter: AlarmConverter,
    private val rankConverter: RankConverter
) {

    // TODO check null, arraylist null usage
    fun toItem(
        noteEntity: NoteEntity,
        isVisible: Boolean? = null,
        rollList: MutableList<RollItem> = ArrayList(),
        alarmEntity: AlarmEntity? = null
    ): NoteItem = with(noteEntity) {
        val isRollVisible = isVisible ?: RollVisible.Default.VALUE
        val rank = rankConverter.toNoteRank(noteEntity)
        val alarm = alarmConverter.toNoteAlarm(alarmEntity)

        return@with when (noteEntity.type) {
            NoteType.TEXT -> NoteItem.Text(
                id, create, change, name, text, color, rank, isBin, isStatus, alarm
            )
            NoteType.ROLL -> NoteItem.Roll(
                id, create, change, name, text, color, rank, isBin, isStatus,
                alarm, isRollVisible, rollList
            )
        }
    }

    fun toEntity(item: NoteItem) = with(item) {
        NoteEntity(
            id, create, change, name, text, color, type, rank.id, rank.position, isBin, isStatus
        )
    }

    fun toEntity(list: List<NoteItem>): MutableList<NoteEntity> {
        return list.map { toEntity(it) }.toMutableList()
    }

}
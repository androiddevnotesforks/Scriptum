package sgtmelon.scriptum.domain.model.result

import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity

/**
 * Class with result of backup processing (export/import).
 */
sealed class ParserResult(
    open val noteList: List<NoteEntity>,
    open val rollList: List<RollEntity>,
    open val rollVisibleList: List<RollVisibleEntity>,
    open val rankList: List<RankEntity>,
    open val alarmList: List<AlarmEntity>
) {

    class Export(
        noteList: List<NoteEntity>,
        rollList: List<RollEntity>,
        rollVisibleList: List<RollVisibleEntity>,
        rankList: List<RankEntity>,
        alarmList: List<AlarmEntity>
    ) : ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)

    class Import(
        override val noteList: MutableList<NoteEntity>,
        override val rollList: MutableList<RollEntity>,
        override val rollVisibleList: MutableList<RollVisibleEntity>,
        override val rankList: MutableList<RankEntity>,
        override val alarmList: MutableList<AlarmEntity>
    ) : ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)
}
package sgtmelon.scriptum.parent

import sgtmelon.scriptum.data.room.backup.BackupParserTest
import sgtmelon.scriptum.data.room.backup.BackupSelectorTest
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.key.NoteType

/**
 * Parent test for [BackupParserTest] and [BackupSelectorTest].
 */
abstract class ParentBackupTest : ParentTest() {

    protected val noteList = listOf(
            NoteEntity(
                    id = 11, create = "0", change = "3", name = "6", text = "9", color = 1,
                    type = NoteType.TEXT, rankId = 10, rankPs = 0, isBin = false, isStatus = true
            ),
            NoteEntity(
                    id = 42, create = "1", change = "4", name = "7", text = "0", color = 2,
                    type = NoteType.ROLL, rankId = 23, rankPs = 10, isBin = true, isStatus = false
            ),
            NoteEntity(
                    id = 98, create = "2", change = "5", name = "8", text = "1", color = 32,
                    type = NoteType.TEXT, rankId = 0, rankPs = 2, isBin = false, isStatus = true
            )
    )

    protected val noteListJson = """[
        {"NT_CREATE":"0","NT_RANK_PS":0,"NT_COLOR":1,"NT_ID":11,"NT_STATUS":true,"NT_NAME":"6","NT_RANK_ID":10,"NT_TEXT":"9","NT_BIN":false,"NT_TYPE":0,"NT_CHANGE":"3"},
        {"NT_CREATE":"1","NT_RANK_PS":10,"NT_COLOR":2,"NT_ID":42,"NT_STATUS":false,"NT_NAME":"7","NT_RANK_ID":23,"NT_TEXT":"0","NT_BIN":true,"NT_TYPE":1,"NT_CHANGE":"4"},
        {"NT_CREATE":"2","NT_RANK_PS":2,"NT_COLOR":32,"NT_ID":98,"NT_STATUS":true,"NT_NAME":"8","NT_RANK_ID":0,"NT_TEXT":"1","NT_BIN":false,"NT_TYPE":0,"NT_CHANGE":"5"}
    ]""".clearAllSpace()

    protected val rollList = listOf(
            RollEntity(id = 2, noteId = 4212, position = 0, isCheck = true, text = "first"),
            RollEntity(id = 202, noteId = 80, position = 1, isCheck = false, text = "second"),
            RollEntity(id = 75, noteId = 345, position = 2, isCheck = true, text = "third")
    )

    protected val rollListJson = """[
        {"RL_POSITION":0,"RL_TEXT":"first","RL_ID":2,"RL_CHECK":true,"RL_NOTE_ID":4212},
        {"RL_POSITION":1,"RL_TEXT":"second","RL_ID":202,"RL_CHECK":false,"RL_NOTE_ID":80},
        {"RL_POSITION":2,"RL_TEXT":"third","RL_ID":75,"RL_CHECK":true,"RL_NOTE_ID":345}
    ]""".clearAllSpace()

    protected val rollVisibleList = listOf(
            RollVisibleEntity(id = 1012, noteId = 452, value = true),
            RollVisibleEntity(id = 214, noteId = 168, value = true),
            RollVisibleEntity(id = 975, noteId = 324, value = false)
    )

    protected val rollVisibleListJson = """[
        {"RL_VS_ID":1012,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":452},
        {"RL_VS_ID":214,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":168},
        {"RL_VS_ID":975,"RL_VS_VALUE":false,"RL_VS_NOTE_ID":324}
    ]""".clearAllSpace()

    protected val rankList = listOf(
            RankEntity(id = 12, noteId = mutableListOf(102, 145, 32), position = 0, name = "first", isVisible = false),
            RankEntity(id = 24, noteId = mutableListOf(107), position = 1, name = "second", isVisible = true),
            RankEntity(id = 65, noteId = mutableListOf(198, 123, 282), position = 2, name = "third", isVisible = true)
    )

    protected val rankListJson = """[
        {"RK_VISIBLE":false,"RK_NOTE_ID":"102, 145, 32","RK_ID":12,"RK_NAME":"first","RK_POSITION":0},
        {"RK_VISIBLE":true,"RK_NOTE_ID":"107","RK_ID":24,"RK_NAME":"second","RK_POSITION":1},
        {"RK_VISIBLE":true,"RK_NOTE_ID":"198, 123, 282","RK_ID":65,"RK_NAME":"third","RK_POSITION":2}
    ]""".clearAllSpace()

    protected val alarmList = listOf(
            AlarmEntity(id = 12, noteId = 102, date = "first"),
            AlarmEntity(id = 24, noteId = 107, date = "second"),
            AlarmEntity(id = 65, noteId = 198, date = "third")
    )

    protected val alarmListJson = """[
        {"AL_ID":12,"AL_DATE":"first","AL_NOTE_ID":102},
        {"AL_ID":24,"AL_DATE":"second","AL_NOTE_ID":107},
        {"AL_ID":65,"AL_DATE":"third","AL_NOTE_ID":198}
    ]""".clearAllSpace()


    private fun String.clearAllSpace(): String {
        return trim()
                .replace("\n".toRegex(), replacement = "")
                .replace("\\[\\s+\\{".toRegex(), replacement = "[{")
                .replace("},\\s+\\{".toRegex(), replacement = "},{")
                .replace("}\\s+]".toRegex(), replacement = "}]")
    }

}
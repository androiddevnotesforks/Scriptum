package sgtmelon.scriptum.cleanup.parent

import org.json.JSONObject
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.key.NoteType
import sgtmelon.scriptum.data.backup.BackupParserImpl
import sgtmelon.scriptum.infrastructure.model.key.Color
import sgtmelon.test.common.nextString

/**
 * Parent test for backup staff.
 */
abstract class ParentBackupTest : ParentTest() {

    //region Json help

    protected val versionKey = nextString()
    protected val hashKey = nextString()
    protected val databaseKey = nextString()

    /**
     * Imitate the backup file content.
     */
    protected fun getBackupJson(
        hash: String,
        data: String,
        version: Any = BackupParserImpl.VERSION
    ) = JSONObject().apply {
        put(versionKey, version)
        put(hashKey, hash)
        put(databaseKey, data)
    }.toString()

    //endregion

    //region Note data

    protected val noteEntityList = listOf(
        NoteEntity(
            id = 11, create = "0", change = "3", name = "6", text = "9", color = Color.RED,
            type = NoteType.TEXT, rankId = 10, rankPs = 0, isBin = false, isStatus = true
        ),
        NoteEntity(
            id = 42, create = "1", change = "4", name = "7", text = "0", color = Color.GREEN,
            type = NoteType.ROLL, rankId = 23, rankPs = 10, isBin = true, isStatus = false
        ),
        NoteEntity(
            id = 98, create = "2", change = "5", name = "8", text = "1", color = Color.YELLOW,
            type = NoteType.TEXT, rankId = 0, rankPs = 2, isBin = false, isStatus = true
        )
    )

    protected val noteJsonList = listOf(
        """{"NT_CREATE":"0","NT_RANK_PS":0,"NT_COLOR":0,"NT_ID":11,"NT_STATUS":true,"NT_NAME":"6","NT_RANK_ID":10,"NT_TEXT":"9","NT_BIN":false,"NT_TYPE":0,"NT_CHANGE":"3"}""",
        """{"NT_CREATE":"1","NT_RANK_PS":10,"NT_COLOR":5,"NT_ID":42,"NT_STATUS":false,"NT_NAME":"7","NT_RANK_ID":23,"NT_TEXT":"0","NT_BIN":true,"NT_TYPE":1,"NT_CHANGE":"4"}""",
        """{"NT_CREATE":"2","NT_RANK_PS":2,"NT_COLOR":6,"NT_ID":98,"NT_STATUS":true,"NT_NAME":"8","NT_RANK_ID":0,"NT_TEXT":"1","NT_BIN":false,"NT_TYPE":0,"NT_CHANGE":"5"}"""
    )

    protected val noteJsonArray = "[${noteJsonList.joinToString()}]".clearAllSpace()

    protected val noteBadColorJson = """{
        "NT_CREATE":"BAD_COLOR","NT_RANK_PS":0,"NT_COLOR":-1,"NT_ID":11,"NT_STATUS":true,
        "NT_NAME":"6","NT_RANK_ID":10,"NT_TEXT":"9","NT_BIN":false,"NT_TYPE":0,"NT_CHANGE":"3"
    }""".clearAllSpace()

    protected val noteBadTypeJson = """{
        "NT_CREATE":"BAD_TYPE","NT_RANK_PS":0,"NT_COLOR":0,"NT_ID":11,"NT_STATUS":true,
        "NT_NAME":"6","NT_RANK_ID":10,"NT_TEXT":"9","NT_BIN":false,"NT_TYPE":-1,"NT_CHANGE":"3"
    }""".clearAllSpace()

    //endregion

    //region Roll data

    protected val rollEntityList = listOf(
        RollEntity(id = 2, noteId = 4212, position = 0, isCheck = true, text = "first"),
        RollEntity(id = 202, noteId = 80, position = 1, isCheck = false, text = "second"),
        RollEntity(id = 75, noteId = 345, position = 2, isCheck = true, text = "third")
    )

    protected val rollJsonList = listOf(
        """{"RL_POSITION":0,"RL_TEXT":"first","RL_ID":2,"RL_CHECK":true,"RL_NOTE_ID":4212}""",
        """{"RL_POSITION":1,"RL_TEXT":"second","RL_ID":202,"RL_CHECK":false,"RL_NOTE_ID":80}""",
        """{"RL_POSITION":2,"RL_TEXT":"third","RL_ID":75,"RL_CHECK":true,"RL_NOTE_ID":345}"""
    )

    protected val rollJsonArray = "[${rollJsonList.joinToString()}]".clearAllSpace()

    //endregion

    //region RollVisible data

    protected val rollVisibleEntityList = listOf(
        RollVisibleEntity(id = 1012, noteId = 452, value = true),
        RollVisibleEntity(id = 214, noteId = 168, value = true),
        RollVisibleEntity(id = 975, noteId = 324, value = false)
    )

    protected val rollVisibleJsonList = listOf(
        """{"RL_VS_ID":1012,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":452}""",
        """{"RL_VS_ID":214,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":168}""",
        """{"RL_VS_ID":975,"RL_VS_VALUE":false,"RL_VS_NOTE_ID":324}"""
    )

    protected val rollVisibleJsonArray = "[${rollVisibleJsonList.joinToString()}]".clearAllSpace()

    //endregion

    //region Rank data

    protected val rankEntityList = listOf(
        RankEntity(
            id = 12, noteId = mutableListOf(102, 145, 32), position = 0, name = "first",
            isVisible = false
        ),
        RankEntity(
            id = 24, noteId = mutableListOf(107), position = 1, name = "second",
            isVisible = true
        ),
        RankEntity(
            id = 65, noteId = mutableListOf(198, 123, 282), position = 2, name = "third",
            isVisible = true
        )
    )

    protected val rankJsonList = listOf(
        """{"RK_VISIBLE":false,"RK_NOTE_ID":"102, 145, 32","RK_ID":12,"RK_NAME":"first","RK_POSITION":0}""",
        """{"RK_VISIBLE":true,"RK_NOTE_ID":"107","RK_ID":24,"RK_NAME":"second","RK_POSITION":1}""",
        """{"RK_VISIBLE":true,"RK_NOTE_ID":"198, 123, 282","RK_ID":65,"RK_NAME":"third","RK_POSITION":2}"""
    )

    protected val rankJsonArray = "[${rankJsonList.joinToString()}]".clearAllSpace()

    //endregion

    //region Alarm data

    protected val alarmEntityList = listOf(
        AlarmEntity(id = 12, noteId = 102, date = "first"),
        AlarmEntity(id = 24, noteId = 107, date = "second"),
        AlarmEntity(id = 65, noteId = 198, date = "third")
    )

    protected val alarmJsonList = listOf(
        """{"AL_ID":12,"AL_DATE":"first","AL_NOTE_ID":102}""",
        """{"AL_ID":24,"AL_DATE":"second","AL_NOTE_ID":107}""",
        """{"AL_ID":65,"AL_DATE":"third","AL_NOTE_ID":198}"""
    )

    protected val alarmJsonArray = "[${alarmJsonList.joinToString()}]".clearAllSpace()

    //endregion

    private fun String.clearAllSpace(): String {
        return trim()
            .replace("\n".toRegex(), replacement = "")
            .replace("\\[\\s+\\{".toRegex(), replacement = "[{")
            .replace("},\\s+\\{".toRegex(), replacement = "},{")
            .replace("}\\s+]".toRegex(), replacement = "}]")
    }
}
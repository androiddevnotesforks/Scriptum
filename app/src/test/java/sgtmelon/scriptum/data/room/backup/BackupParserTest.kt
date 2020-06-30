package sgtmelon.scriptum.data.room.backup

import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.room.entity.*
import sgtmelon.scriptum.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.domain.model.data.DbData.Note
import sgtmelon.scriptum.domain.model.data.DbData.Rank
import sgtmelon.scriptum.domain.model.data.DbData.Roll
import sgtmelon.scriptum.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.domain.model.key.NoteType
import kotlin.random.Random

/**
 * Test for [BackupParser].
 */
class BackupParserTest : ParentTest() {

    @MockK lateinit var context: Context
    @MockK lateinit var selector: BackupSelector

    private val backupParser by lazy { BackupParser(context, selector) }
    private val spyBackupParser by lazy { spyk(backupParser) }

    private val tagVersion = Random.nextString()
    private val tagHash = Random.nextString()
    private val tagRoom = Random.nextString()


    @Test fun collect() {
        val model = mockk<BackupParser.Model>()
        val roomData = getRoomData()
        val hash = Random.nextString()

        mockTag()
        every { spyBackupParser.collectRoom(model) } returns roomData
        every { spyBackupParser.getHash(roomData) } returns hash

        assertEquals(getBackupData(hash, roomData), spyBackupParser.collect(model))

        verifySequence {
            spyBackupParser.collect(model)

            spyBackupParser.collectRoom(model)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            spyBackupParser.getHash(roomData)
            context.getString(R.string.backup_room)
        }
    }

    @Test fun collectRoom() {
        val model = mockk<BackupParser.Model>()

        val noteList = mockk<List<NoteEntity>>()
        val rollList = mockk<List<RollEntity>>()
        val rollVisibleList = mockk<List<RollVisibleEntity>>()
        val rankList = mockk<List<RankEntity>>()
        val alarmList = mockk<List<AlarmEntity>>()

        val noteTableData = Random.nextString()
        val rollTableData = Random.nextString()
        val rollVisibleTableData = Random.nextString()
        val rankTableData = Random.nextString()
        val alarmTableData = Random.nextString()

        val roomData = JSONObject().apply {
            put(Note.TABLE, noteTableData)
            put(Roll.TABLE, rollTableData)
            put(RollVisible.TABLE, rollVisibleTableData)
            put(Rank.TABLE, rankTableData)
            put(Alarm.TABLE, alarmTableData)
        }.toString()

        every { model.noteList } returns noteList
        every { model.rollList } returns rollList
        every { model.rollVisibleList } returns rollVisibleList
        every { model.rankList } returns rankList
        every { model.alarmList } returns alarmList

        every { spyBackupParser.collectNoteTable(noteList) } returns noteTableData
        every { spyBackupParser.collectRollTable(rollList) } returns rollTableData
        every { spyBackupParser.collectRollVisibleTable(rollVisibleList) } returns rollVisibleTableData
        every { spyBackupParser.collectRankTable(rankList) } returns rankTableData
        every { spyBackupParser.collectAlarmTable(alarmList) } returns alarmTableData

        assertEquals(roomData, spyBackupParser.collectRoom(model))

        verifySequence {
            spyBackupParser.collectRoom(model)

            model.noteList
            spyBackupParser.collectNoteTable(noteList)
            model.rollList
            spyBackupParser.collectRollTable(rollList)
            model.rollVisibleList
            spyBackupParser.collectRollVisibleTable(rollVisibleList)
            model.rankList
            spyBackupParser.collectRankTable(rankList)
            model.alarmList
            spyBackupParser.collectAlarmTable(alarmList)
        }
    }

    @Test fun collectNoteTable() {
        val list = listOf(
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

        val result = """[
            {"NT_CREATE":"0","NT_RANK_PS":0,"NT_COLOR":1,"NT_ID":11,"NT_STATUS":true,"NT_NAME":"6","NT_RANK_ID":10,"NT_TEXT":"9","NT_BIN":false,"NT_TYPE":"TEXT","NT_CHANGE":"3"},
            {"NT_CREATE":"1","NT_RANK_PS":10,"NT_COLOR":2,"NT_ID":42,"NT_STATUS":false,"NT_NAME":"7","NT_RANK_ID":23,"NT_TEXT":"0","NT_BIN":true,"NT_TYPE":"ROLL","NT_CHANGE":"4"},
            {"NT_CREATE":"2","NT_RANK_PS":2,"NT_COLOR":32,"NT_ID":98,"NT_STATUS":true,"NT_NAME":"8","NT_RANK_ID":0,"NT_TEXT":"1","NT_BIN":false,"NT_TYPE":"TEXT","NT_CHANGE":"5"}
        ]""".clearAllSpace()

        assertEquals(result, backupParser.collectNoteTable(list))
    }

    @Test fun collectRollTable() {
        val list = listOf(
                RollEntity(id = 2, noteId = 4212, position = 0, isCheck = true, text = "first"),
                RollEntity(id = 202, noteId = 80, position = 1, isCheck = false, text = "second"),
                RollEntity(id = 75, noteId = 345, position = 2, isCheck = true, text = "third")
        )

        val result = """[
            {"RL_POSITION":0,"RL_TEXT":"first","RL_ID":2,"RL_CHECK":true,"RL_NOTE_ID":4212},
            {"RL_POSITION":1,"RL_TEXT":"second","RL_ID":202,"RL_CHECK":false,"RL_NOTE_ID":80},
            {"RL_POSITION":2,"RL_TEXT":"third","RL_ID":75,"RL_CHECK":true,"RL_NOTE_ID":345}
        ]""".clearAllSpace()

        assertEquals(result, backupParser.collectRollTable(list))
    }

    @Test fun collectRollVisibleTable() {
        val list = listOf(
                RollVisibleEntity(id = 1012, noteId = 452, value = true),
                RollVisibleEntity(id = 214, noteId = 168, value = true),
                RollVisibleEntity(id = 975, noteId = 324, value = false)
        )

        val result = """[
            {"RL_VS_ID":1012,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":452},
            {"RL_VS_ID":214,"RL_VS_VALUE":true,"RL_VS_NOTE_ID":168},
            {"RL_VS_ID":975,"RL_VS_VALUE":false,"RL_VS_NOTE_ID":324}
        ]""".clearAllSpace()

        assertEquals(result, backupParser.collectRollVisibleTable(list))
    }

    @Test fun collectRankTable() {
        val list = listOf(
                RankEntity(id = 12, noteId = mutableListOf(102, 145, 32), position = 0, name = "first", isVisible = false),
                RankEntity(id = 24, noteId = mutableListOf(107), position = 1, name = "second", isVisible = true),
                RankEntity(id = 65, noteId = mutableListOf(198, 123, 282), position = 2, name = "third", isVisible = true)
        )

        val result = """[
            {"RK_VISIBLE":false,"RK_NOTE_ID":[102,145,32],"RK_ID":12,"RK_NAME":"first","RK_POSITION":0},
            {"RK_VISIBLE":true,"RK_NOTE_ID":[107],"RK_ID":24,"RK_NAME":"second","RK_POSITION":1},
            {"RK_VISIBLE":true,"RK_NOTE_ID":[198,123,282],"RK_ID":65,"RK_NAME":"third","RK_POSITION":2}
        ]""".clearAllSpace()

        assertEquals(result, backupParser.collectRankTable(list))
    }

    @Test fun collectAlarmTable() {
        val list = listOf(
                AlarmEntity(id = 12, noteId = 102, date = "first"),
                AlarmEntity(id = 24, noteId = 107, date = "second"),
                AlarmEntity(id = 65, noteId = 198, date = "third")
        )

        val result = """[
            {"AL_ID":12,"AL_DATE":"first","AL_NOTE_ID":102},
            {"AL_ID":24,"AL_DATE":"second","AL_NOTE_ID":107},
            {"AL_ID":65,"AL_DATE":"third","AL_NOTE_ID":198}
        ]""".clearAllSpace()

        assertEquals(result, backupParser.collectAlarmTable(list))
    }


    @Test fun parse_badData() {
        val dataError = Random.nextString()
        val versionError = JSONObject().apply { put(tagVersion, Random.nextString()) }.toString()
        val hashError = JSONObject().apply { put(tagVersion, Random.nextInt()) }.toString()
        val roomError = JSONObject().apply {
            put(tagVersion, Random.nextInt())
            put(tagHash, Random.nextString())
        }.toString()

        mockTag()

        assertNull(spyBackupParser.parse(dataError))
        assertNull(spyBackupParser.parse(versionError))
        assertNull(spyBackupParser.parse(hashError))
        assertNull(spyBackupParser.parse(roomError))

        verifySequence {
            spyBackupParser.parse(dataError)

            spyBackupParser.parse(versionError)
            context.getString(R.string.backup_version)

            spyBackupParser.parse(hashError)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)

            spyBackupParser.parse(roomError)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_room)
        }
    }

    @Test fun parse_badHash() {
        val roomData = getRoomData()
        val data = getBackupData(Random.nextString(), roomData, Random.nextInt())

        mockTag()
        every { spyBackupParser.getHash(roomData) } returns Random.nextString()

        assertNull(spyBackupParser.parse(data))

        verifySequence {
            spyBackupParser.parse(data)

            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_room)
            spyBackupParser.getHash(roomData)
        }
    }

    @Test fun parse_goodData() {
        val model = mockk<BackupParser.Model>()
        val roomData = getRoomData()
        val hash = Random.nextString()
        val version = Random.nextInt()

        val data = getBackupData(hash, roomData, version)

        mockTag()
        every { spyBackupParser.getHash(roomData) } returns hash
        every { selector.parseByVersion(roomData, version) } returns model

        assertEquals(model, spyBackupParser.parse(data))

        verifySequence {
            spyBackupParser.parse(data)

            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_room)
            spyBackupParser.getHash(roomData)
            selector.parseByVersion(roomData, version)
        }
    }



    /**
     * Get values from website: https://emn178.github.io/online-tools/sha256.html
     */
    @Test fun getHash() {
        val dataResultMap = mapOf(
                "" to "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                "Тest of SHA256." to "86cdf15b207ef2bca14242cb700f6a67038431ea3af004b2741527ee75135d4c",
                "My name is Alexey" to "262d410b26232a1e948b317aa013b3210a324db7c442460410ec0e8ede35a990",
                "Подушечка из манго самая мягкая" to "62b8d1d61be6acb1a76f55a4d2fd866ef8dd68cd3a6fb23b9f032b7f376f09c7"
        )

        dataResultMap.forEach { assertEquals(it.value, backupParser.getHash(it.key)) }
    }


    /**
     * Imitate the result of collect room.
     */
    private fun getRoomData() = StringBuilder().apply {
        repeat(times = 5) { append(Random.nextString()) }
    }.toString()

    /**
     * Imitate the backup file content.
     */
    private fun getBackupData(hash: String, roomData: String,
                              version: Any = BackupParser.VERSION) = JSONObject().apply {
        put(tagVersion, version)
        put(tagHash, hash)
        put(tagRoom, roomData)
    }.toString()

    private fun mockTag() {
        every { context.getString(R.string.backup_version) } returns tagVersion
        every { context.getString(R.string.backup_hash) } returns tagHash
        every { context.getString(R.string.backup_room) } returns tagRoom
    }

    private fun String.clearAllSpace(): String {
        return trim()
                .replace("\\s+".toRegex(), replacement = "")
                .replace("\n".toRegex(), replacement = "")
    }

}
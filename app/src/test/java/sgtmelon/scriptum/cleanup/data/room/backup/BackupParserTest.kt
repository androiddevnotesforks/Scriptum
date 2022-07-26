package sgtmelon.scriptum.cleanup.data.room.backup

import android.content.Context
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.parent.ParentBackupTest

/**
 * Test for [BackupParser].
 */
class BackupParserTest : ParentBackupTest() {

    @MockK lateinit var context: Context
    @MockK lateinit var selector: BackupSelector

    private val colorConverter = ColorConverter()
    private val typeConverter = NoteTypeConverter()
    private val stringConverter = StringConverter()

    private val backupParser by lazy {
        BackupParser(context, selector, colorConverter, typeConverter, stringConverter)
    }

    private val spyBackupParser by lazy { spyk(backupParser) }

    private val tagVersion = nextString()
    private val tagHash = nextString()
    private val tagRoom = nextString()

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(context, selector)
    }

    @Test fun collect() {
        val model = mockk<ParserResult>()
        val roomData = getRoomData()
        val hash = nextString()

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
        val model = mockk<ParserResult>()

        val noteList = mockk<List<NoteEntity>>()
        val rollList = mockk<List<RollEntity>>()
        val rollVisibleList = mockk<List<RollVisibleEntity>>()
        val rankList = mockk<List<RankEntity>>()
        val alarmList = mockk<List<AlarmEntity>>()

        val noteTableData = nextString()
        val rollTableData = nextString()
        val rollVisibleTableData = nextString()
        val rankTableData = nextString()
        val alarmTableData = nextString()

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
        assertEquals(noteListJson, backupParser.collectNoteTable(noteList))
    }

    @Test fun collectRollTable() {
        assertEquals(rollListJson, backupParser.collectRollTable(rollList))
    }

    @Test fun collectRollVisibleTable() {
        assertEquals(rollVisibleListJson, backupParser.collectRollVisibleTable(rollVisibleList))
    }

    @Test fun collectRankTable() {
        assertEquals(rankListJson, backupParser.collectRankTable(rankList))
    }

    @Test fun collectAlarmTable() {
        assertEquals(alarmListJson, backupParser.collectAlarmTable(alarmList))
    }


    @Test fun parse_badData() {
        val dataError = nextString()
        val versionError = JSONObject().apply { put(tagVersion, nextString()) }.toString()
        val hashError = JSONObject().apply { put(tagVersion, Random.nextInt()) }.toString()
        val roomError = JSONObject().apply {
            put(tagVersion, Random.nextInt())
            put(tagHash, nextString())
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
        val data = getBackupData(nextString(), roomData, Random.nextInt())

        mockTag()
        every { spyBackupParser.getHash(roomData) } returns nextString()

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
        val model = mockk<ParserResult>()
        val roomData = getRoomData()
        val hash = nextString()
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

        for (it in dataResultMap) {
            assertEquals(it.value, backupParser.getHash(it.key))
        }
    }

    //region Private help functions

    /**
     * Imitate the result of collect room.
     */
    private fun getRoomData() = StringBuilder().apply {
        repeat(times = 5) { append(nextString()) }
    }.toString()

    /**
     * Imitate the backup file content.
     */
    private fun getBackupData(
        hash: String, roomData: String,
        version: Any = BackupParser.VERSION
    ) = JSONObject().apply {
        put(tagVersion, version)
        put(tagHash, hash)
        put(tagRoom, roomData)
    }.toString()

    private fun mockTag() {
        every { context.getString(R.string.backup_version) } returns tagVersion
        every { context.getString(R.string.backup_hash) } returns tagHash
        every { context.getString(R.string.backup_room) } returns tagRoom
    }

    //endregion

}
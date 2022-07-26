package sgtmelon.scriptum.cleanup.data.room.backup

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Alarm
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Note
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Rank
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.Roll
import sgtmelon.scriptum.cleanup.domain.model.data.DbData.RollVisible
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.parent.ParentBackupTest

/**
 * Test for [BackupSelector].
 */
class BackupSelectorTest : ParentBackupTest() {

    private val colorConverter = ColorConverter()
    private val typeConverter = NoteTypeConverter()
    private val stringConverter = StringConverter()

    private val backupSelector by lazy {
        BackupSelector(colorConverter, typeConverter, stringConverter)
    }
    private val spyBackupSelector by lazy { spyk(backupSelector) }

    @Test fun parseByVersion() {
        val model = mockk<ParserResult>()
        val roomData = nextString()

        every { spyBackupSelector.getModelV1(roomData) } returns model

        assertNull(spyBackupSelector.parseByVersion(nextString(), version = -1))
        assertEquals(model, spyBackupSelector.parseByVersion(roomData, version = 1))

        verifySequence {
            spyBackupSelector.parseByVersion(any(), version = -1)

            spyBackupSelector.parseByVersion(roomData, version = 1)
            spyBackupSelector.getModelV1(roomData)
        }
    }

    //region Version 1

    @Test fun getModelV1() {
        val roomData = JSONObject().apply {
            put(Note.TABLE, noteListJson)
            put(Roll.TABLE, rollListJson)
            put(RollVisible.TABLE, rollVisibleListJson)
            put(Rank.TABLE, rankListJson)
            put(Alarm.TABLE, alarmListJson)
        }.toString()

        val model = ParserResult(noteList, rollList, rollVisibleList, rankList, alarmList)

        every { spyBackupSelector.getNoteTableV1(any()) } returns null

        assertNull(spyBackupSelector.getModelV1(roomData))

        every { spyBackupSelector.getNoteTableV1(any()) } returns noteList
        every { spyBackupSelector.getRollTableV1(any()) } returns null

        assertNull(spyBackupSelector.getModelV1(roomData))

        every { spyBackupSelector.getRollTableV1(any()) } returns rollList
        every { spyBackupSelector.getRollVisibleTableV1(any()) } returns null

        assertNull(spyBackupSelector.getModelV1(roomData))

        every { spyBackupSelector.getRollVisibleTableV1(any()) } returns rollVisibleList
        every { spyBackupSelector.getRankTableV1(any()) } returns null

        assertNull(spyBackupSelector.getModelV1(roomData))

        every { spyBackupSelector.getRankTableV1(any()) } returns rankList
        every { spyBackupSelector.getAlarmTableV1(any()) } returns null

        assertNull(spyBackupSelector.getModelV1(roomData))

        every { spyBackupSelector.getAlarmTableV1(any()) } returns alarmList

        assertEquals(model, spyBackupSelector.getModelV1(roomData))

        verifySequence {
            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())

            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())
            spyBackupSelector.getRollTableV1(any())

            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())
            spyBackupSelector.getRollTableV1(any())
            spyBackupSelector.getRollVisibleTableV1(any())

            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())
            spyBackupSelector.getRollTableV1(any())
            spyBackupSelector.getRollVisibleTableV1(any())
            spyBackupSelector.getRankTableV1(any())

            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())
            spyBackupSelector.getRollTableV1(any())
            spyBackupSelector.getRollVisibleTableV1(any())
            spyBackupSelector.getRankTableV1(any())
            spyBackupSelector.getAlarmTableV1(any())

            spyBackupSelector.getModelV1(roomData)
            spyBackupSelector.getNoteTableV1(any())
            spyBackupSelector.getRollTableV1(any())
            spyBackupSelector.getRollVisibleTableV1(any())
            spyBackupSelector.getRankTableV1(any())
            spyBackupSelector.getAlarmTableV1(any())
        }
    }

    // TODO add skip test (if converter return null)
    @Test fun getNoteTableV1() {
        val jsonArray = JSONArray(noteListJson)
        assertEquals(noteList, backupSelector.getNoteTableV1(jsonArray))
    }

    @Test fun getRollTableV1() {
        val jsonArray = JSONArray(rollListJson)
        assertEquals(rollList, backupSelector.getRollTableV1(jsonArray))
    }

    @Test fun getRollVisibleTableV1() {
        val jsonArray = JSONArray(rollVisibleListJson)
        assertEquals(rollVisibleList, backupSelector.getRollVisibleTableV1(jsonArray))
    }

    @Test fun getRankTableV1() {
        val jsonArray = JSONArray(rankListJson)
        assertEquals(rankList, backupSelector.getRankTableV1(jsonArray))
    }

    @Test fun getAlarmTableV1() {
        val jsonArray = JSONArray(alarmListJson)
        assertEquals(alarmList, backupSelector.getAlarmTableV1(jsonArray))
    }

    //endregion

}
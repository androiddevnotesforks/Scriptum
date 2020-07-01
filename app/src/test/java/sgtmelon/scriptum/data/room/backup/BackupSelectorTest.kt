package sgtmelon.scriptum.data.room.backup

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.json.JSONArray
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentBackupTest
import sgtmelon.scriptum.data.room.converter.model.StringConverter
import sgtmelon.scriptum.data.room.converter.type.NoteTypeConverter
import kotlin.random.Random

/**
 * Test for [BackupSelector].
 */
class BackupSelectorTest : ParentBackupTest() {

    private val typeConverter = NoteTypeConverter()
    private val stringConverter = StringConverter()

    private val backupSelector by lazy { BackupSelector(typeConverter, stringConverter) }
    private val spyBackupSelector by lazy { spyk(backupSelector) }

    @Test fun parseByVersion() {
        val model = mockk<BackupParser.Model>()
        val roomData = Random.nextString()

        every { spyBackupSelector.getModelV1(roomData) } returns model

        assertNull(spyBackupSelector.parseByVersion(Random.nextString(), version = -1))
        assertEquals(model, spyBackupSelector.parseByVersion(roomData, version = 1))

        verifySequence {
            spyBackupSelector.parseByVersion(Random.nextString(), version = -1)

            spyBackupSelector.parseByVersion(roomData, version = 1)
            spyBackupSelector.getModelV1(roomData)
        }
    }

    //region Version 1

    @Test fun getModelV1() {
//        val roomData = Random.nextString()
//
//        val noteList = mockk<List<NoteEntity>>()
//        val rollList = mockk<List<RollEntity>>()
//        val rollVisibleList = mockk<List<RollVisibleEntity>>()
//        val rankList = mockk<List<RankEntity>>()
//        val alarmList = mockk<List<AlarmEntity>>()
//
//        val model = BackupParser.Model(noteList, rollList, rollVisibleList, rankList, alarmList)
//
//        every { spyBackupSelector.getNoteTableV1(roomData) } returns noteList
//        every { spyBackupSelector.getRollTableV1(roomData) } returns rollList
//        every { spyBackupSelector.getRollVisibleTableV1(roomData) } returns rollVisibleList
//        every { spyBackupSelector.getRankTableV1(roomData) } returns rankList
//        every { spyBackupSelector.getAlarmTableV1(roomData) } returns alarmList
//
//        assertEquals(model, spyBackupSelector.getModelV1(roomData))
//
//        verifySequence {
//            spyBackupSelector.getModelV1(roomData)
//
//            spyBackupSelector.getNoteTableV1(roomData)
//            spyBackupSelector.getRollTableV1(roomData)
//            spyBackupSelector.getRollVisibleTableV1(roomData)
//            spyBackupSelector.getRankTableV1(roomData)
//            spyBackupSelector.getAlarmTableV1(roomData)
//        }
    }

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
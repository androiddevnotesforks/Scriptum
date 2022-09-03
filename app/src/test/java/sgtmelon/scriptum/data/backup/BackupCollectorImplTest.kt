package sgtmelon.scriptum.data.backup

import io.mockk.impl.annotations.MockK
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentBackupTest
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource

/**
 * Test for [BackupCollectorImpl].
 */
class BackupCollectorImplTest : ParentBackupTest() {

    @MockK lateinit var dataSource: BackupDataSource
    @MockK lateinit var hashMaker: BackupHashMaker
    @MockK lateinit var jsonConverter: BackupJsonConverter

    private val collector by lazy { BackupCollectorImpl(dataSource, hashMaker, jsonConverter) }

    @Test fun collect() {
        TODO()
        //        val model = mockk<ParserResult>()
        //        val data = getData()
        //        val hash = nextString()
        //
        //        mockTag()
        //        every { spyBackupParser.collectDatabase(model) } returns data
        //        every { spyBackupParser.getHash(data) } returns hash
        //
        //        assertEquals(getBackupJson(hash, data), spyBackupParser.collect(model))
        //
        //        verifySequence {
        //            spyBackupParser.collect(model)
        //
        //            spyBackupParser.collectDatabase(model)
        //            context.getString(R.string.backup_version)
        //            context.getString(R.string.backup_hash)
        //            spyBackupParser.getHash(data)
        //            context.getString(R.string.backup_database)
        //        }
    }

    //    @Test fun collectDatabase() {
    //        val model = mockk<ParserResult>()
    //
    //        val noteList = mockk<List<NoteEntity>>()
    //        val rollList = mockk<List<RollEntity>>()
    //        val rollVisibleList = mockk<List<RollVisibleEntity>>()
    //        val rankList = mockk<List<RankEntity>>()
    //        val alarmList = mockk<List<AlarmEntity>>()
    //
    //        val noteTableData = nextString()
    //        val rollTableData = nextString()
    //        val rollVisibleTableData = nextString()
    //        val rankTableData = nextString()
    //        val alarmTableData = nextString()
    //
    //        val data = JSONObject().apply {
    //            put(DbData.Note.TABLE, noteTableData)
    //            put(DbData.Roll.TABLE, rollTableData)
    //            put(DbData.RollVisible.TABLE, rollVisibleTableData)
    //            put(DbData.Rank.TABLE, rankTableData)
    //            put(DbData.Alarm.TABLE, alarmTableData)
    //        }.toString()
    //
    //        every { model.noteList } returns noteList
    //        every { model.rollList } returns rollList
    //        every { model.rollVisibleList } returns rollVisibleList
    //        every { model.rankList } returns rankList
    //        every { model.alarmList } returns alarmList
    //
    //        every { spyBackupParser.collectNoteTable(noteList) } returns noteTableData
    //        every { spyBackupParser.collectRollTable(rollList) } returns rollTableData
    //        every { spyBackupParser.collectRollVisibleTable(rollVisibleList) } returns rollVisibleTableData
    //        every { spyBackupParser.collectRankTable(rankList) } returns rankTableData
    //        every { spyBackupParser.collectAlarmTable(alarmList) } returns alarmTableData
    //
    //        assertEquals(data, spyBackupParser.collectDatabase(model))
    //
    //        verifySequence {
    //            spyBackupParser.collectDatabase(model)
    //
    //            model.noteList
    //            spyBackupParser.collectNoteTable(noteList)
    //            model.rollList
    //            spyBackupParser.collectRollTable(rollList)
    //            model.rollVisibleList
    //            spyBackupParser.collectRollVisibleTable(rollVisibleList)
    //            model.rankList
    //            spyBackupParser.collectRankTable(rankList)
    //            model.alarmList
    //            spyBackupParser.collectAlarmTable(alarmList)
    //        }
    //    }
    //
    //    @Test fun collectNoteTable() {
    //        assertEquals(noteListJson, backupParser.collectNoteTable(noteList))
    //    }
    //
    //    @Test fun collectRollTable() {
    //        assertEquals(rollListJson, backupParser.collectRollTable(rollList))
    //    }
    //
    //    @Test fun collectRollVisibleTable() {
    //        assertEquals(rollVisibleListJson, backupParser.collectRollVisibleTable(rollVisibleList))
    //    }
    //
    //    @Test fun collectRankTable() {
    //        assertEquals(rankListJson, backupParser.collectRankTable(rankList))
    //    }
    //
    //    @Test fun collectAlarmTable() {
    //        assertEquals(alarmListJson, backupParser.collectAlarmTable(alarmList))
    //    }
}
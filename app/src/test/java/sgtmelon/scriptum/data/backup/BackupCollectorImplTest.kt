package sgtmelon.scriptum.data.backup

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.parent.ParentBackupTest
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.common.nextString

/**
 * Test for [BackupCollectorImpl].
 */
class BackupCollectorImplTest : ParentBackupTest() {

    @MockK lateinit var dataSource: BackupDataSource
    @MockK lateinit var hashMaker: BackupHashMaker
    @MockK lateinit var jsonConverter: BackupJsonConverter

    private val collector by lazy { BackupCollectorImpl(dataSource, hashMaker, jsonConverter) }
    private val spyCollector by lazy { spyk(collector) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource, hashMaker, jsonConverter)
    }

    @Test fun convert() {
        val result = mockk<ParserResult.Export>()
        val database = nextString()
        val hash = nextString()

        every { spyCollector.convertDatabase(result) } returns null

        assertNull(spyCollector.convert(result))

        every { spyCollector.convertDatabase(result) } returns database
        every { dataSource.versionKey } returns versionKey
        every { dataSource.hashKey } returns hashKey
        every { hashMaker.get(database) } returns hash
        every { dataSource.databaseKey } returns databaseKey

        assertEquals(spyCollector.convert(result), getBackupJson(hash, database))

        verifySequence {
            spyCollector.convert(result)
            spyCollector.convertDatabase(result)

            spyCollector.convert(result)
            spyCollector.convertDatabase(result)
            dataSource.versionKey
            dataSource.hashKey
            hashMaker.get(database)
            dataSource.databaseKey
        }
    }

    @Test fun convertDatabase() = startConvertTest()

    @Test fun `convertDatabase + noteTable error`() = startConvertTest(noteList = null)

    @Test fun `convertDatabase + rollTable error`() = startConvertTest(rollList = null)

    @Test fun `convertDatabase + rollVisibleTable error`() = startConvertTest(visibleList = null)

    @Test fun `convertDatabase + rankTable error`() = startConvertTest(rankList = null)

    @Test fun `convertDatabase + alarmTable error`() = startConvertTest(alarmList = null)

    private fun startConvertTest(
        noteList: List<NoteEntity>? = noteEntityList,
        rollList: List<RollEntity>? = rollEntityList,
        visibleList: List<RollVisibleEntity>? = rollVisibleEntityList,
        rankList: List<RankEntity>? = rankEntityList,
        alarmList: List<AlarmEntity>? = alarmEntityList
    ) {
        val result = mockk<ParserResult.Export>()

        val mockkNote = mockk<NoteEntity>()
        val noteJsonList = noteJsonList.map { JSONObject(it) }
        val mockkRoll = mockk<RollEntity>()
        val rollJsonList = rollJsonList.map { JSONObject(it) }
        val mockkVisible = mockk<RollVisibleEntity>()
        val visibleJsonList = rollVisibleJsonList.map { JSONObject(it) }
        val mockkRank = mockk<RankEntity>()
        val rankJsonList = rankJsonList.map { JSONObject(it) }
        val mockkAlarm = mockk<AlarmEntity>()
        val alarmJsonList = alarmJsonList.map { JSONObject(it) }

        mockkToJson(noteList, noteJsonList, mockkNote) { jsonConverter.toJson(it) }
        mockkToJson(rollList, rollJsonList, mockkRoll) { jsonConverter.toJson(it) }
        mockkToJson(visibleList, visibleJsonList, mockkVisible) { jsonConverter.toJson(it) }
        mockkToJson(rankList, rankJsonList, mockkRank) { jsonConverter.toJson(it) }
        mockkToJson(alarmList, alarmJsonList, mockkAlarm) { jsonConverter.toJson(it) }

        val data = JSONObject()
            .put(DbData.Note.TABLE, noteJsonArray)
            .put(DbData.Roll.TABLE, rollJsonArray)
            .put(DbData.RollVisible.TABLE, rollVisibleJsonArray)
            .put(DbData.Rank.TABLE, rankJsonArray)
            .put(DbData.Alarm.TABLE, alarmJsonArray)
            .toString()

        every { result.noteList } returns (noteList ?: listOf(mockkNote))
        every { result.rollList } returns (rollList ?: listOf(mockkRoll))
        every { result.rollVisibleList } returns (visibleList ?: listOf(mockkVisible))
        every { result.rankList } returns (rankList ?: listOf(mockkRank))
        every { result.alarmList } returns (alarmList ?: listOf(mockkAlarm))

        val isError = noteList == null || rollList == null || visibleList == null
                || rankList == null || alarmList == null

        if (isError) {
            FastMock.fireExtensions()
            every { any<BackupParserException>().record() } returns Unit
        }

        if (isError) {
            assertNull(collector.convertDatabase(result))
        } else {
            assertEquals(collector.convertDatabase(result), data)
        }

        verifySequence {
            result.noteList
            verifyToJson(noteList, mockkNote) { jsonConverter.toJson(it) }
            if (noteList == null) return@verifySequence

            result.rollList
            verifyToJson(rollList, mockkRoll) { jsonConverter.toJson(it) }
            if (rollList == null) return@verifySequence

            result.rollVisibleList
            verifyToJson(visibleList, mockkVisible) { jsonConverter.toJson(it) }
            if (visibleList == null) return@verifySequence

            result.rankList
            verifyToJson(rankList, mockkRank) { jsonConverter.toJson(it) }
            if (rankList == null) return@verifySequence

            result.alarmList
            verifyToJson(alarmList, mockkAlarm) { jsonConverter.toJson(it) }
            if (alarmList == null) return@verifySequence
        }
    }

    private inline fun <T> mockkToJson(
        entityList: List<T>?,
        jsonList: List<JSONObject>,
        mockkEntity: T,
        crossinline toJson: (T) -> JSONObject
    ) {
        if (entityList != null) {
            for ((i, entity) in entityList.withIndex()) {
                every { toJson(entity) } returns jsonList[i]
            }
        } else {
            every { toJson(mockkEntity) } throws Throwable()
        }
    }

    private inline fun <T> verifyToJson(
        entityList: List<T>?,
        mockkEntity: T,
        crossinline toJson: (T) -> JSONObject
    ) {
        if (entityList != null) {
            for (entity in entityList) {
                toJson(entity)
            }
        } else {
            toJson(mockkEntity)
        }
    }
}
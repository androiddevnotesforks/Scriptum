package sgtmelon.scriptum.data.backup

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.math.abs
import kotlin.random.Random
import org.json.JSONObject
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.FastMock
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.domain.model.data.DbData
import sgtmelon.scriptum.cleanup.parent.ParentBackupTest
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.domain.model.result.ParserResult
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.model.exception.BackupParserException
import sgtmelon.scriptum.infrastructure.utils.record
import sgtmelon.test.common.nextString

/**
 * Test for [BackupParserImpl].
 */
class BackupParserImplTest : ParentBackupTest() {

    @MockK lateinit var dataSource: BackupDataSource
    @MockK lateinit var hashMaker: BackupHashMaker
    @MockK lateinit var jsonConverter: BackupJsonConverter

    private val parser by lazy { BackupParserImpl(dataSource, hashMaker, jsonConverter) }
    private val jsonParser by lazy {
        BackupParserImpl(
            dataSource, hashMaker,
            BackupJsonConverter(ColorConverter(), NoteTypeConverter(), StringConverter())
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource, hashMaker, jsonConverter)
    }

    @Test fun `convert with throw on getting keys`() {
        every { dataSource.versionKey } returns versionKey
        every { dataSource.hashKey } returns hashKey
        every { dataSource.databaseKey } returns databaseKey

        FastMock.fireExtensions()
        every { any<BackupParserException>().record() } returns mockk()

        val jsonObject = JSONObject()

        assertNull(parser.convert(nextString()))

        jsonObject.put(versionKey, Random.nextInt())
        assertNull(parser.convert(jsonObject.toString()))

        jsonObject.put(hashKey, nextString())
        assertNull(parser.convert(jsonObject.toString()))

        verifySequence {
            dataSource.versionKey
            dataSource.hashKey

            dataSource.versionKey
            dataSource.hashKey
            dataSource.databaseKey
        }
    }

    @Test fun `convert with bad hash`() {
        val hash = nextString()
        val falseHash = nextString()

        assertNotEquals(hash, falseHash)

        wrongConvertData(BackupParserImpl.VERSION, nextString(), nextString(), falseHash)
    }

    @Test fun `convert with wrong version`() {
        wrongConvertData(version = -abs(Random.nextInt()), nextString(), nextString())
    }

    @Test fun `convert with wrong database data`() {
        FastMock.fireExtensions()
        every { any<BackupParserException>().record() } returns mockk()

        wrongConvertData(version = 1, nextString(), nextString())
    }

    private fun wrongConvertData(
        version: Int,
        database: String,
        hash: String,
        returnHash: String = hash
    ) {
        val data = getBackupJson(hash, database, version)

        every { dataSource.versionKey } returns versionKey
        every { dataSource.hashKey } returns hashKey
        every { dataSource.databaseKey } returns databaseKey
        every { hashMaker.get(database) } returns returnHash

        assertNull(parser.convert(data))

        verifySequence {
            dataSource.versionKey
            dataSource.hashKey
            dataSource.databaseKey
            hashMaker.get(database)
        }
    }

    @Test fun `convert version 1`() {
        val expectedResult = ParserResult.Import(
            noteEntityList.toMutableList(),
            rollEntityList.toMutableList(),
            rollVisibleEntityList.toMutableList(),
            rankEntityList.toMutableList(),
            alarmEntityList.toMutableList()
        )

        val database = JSONObject().apply {
            put(DbData.Note.TABLE, noteJsonArray)
            put(DbData.Roll.TABLE, rollJsonArray)
            put(DbData.RollVisible.TABLE, rollVisibleJsonArray)
            put(DbData.Rank.TABLE, rankJsonArray)
            put(DbData.Alarm.TABLE, alarmJsonArray)
        }.toString()
        val hash = nextString()
        val data = getBackupJson(hash, database, version = 1)

        every { dataSource.versionKey } returns versionKey
        every { dataSource.hashKey } returns hashKey
        every { dataSource.databaseKey } returns databaseKey
        every { hashMaker.get(database) } returns hash

        val actualResult = jsonParser.convert(data) ?: throw NullPointerException()

        assertEquals(expectedResult.noteList, actualResult.noteList)
        assertEquals(expectedResult.rollList, actualResult.rollList)
        assertEquals(expectedResult.rollVisibleList, actualResult.rollVisibleList)
        assertEquals(expectedResult.rankList, actualResult.rankList)
        assertEquals(expectedResult.alarmList, actualResult.alarmList)

        coVerifySequence {
            dataSource.versionKey
            dataSource.hashKey
            dataSource.databaseKey
            hashMaker.get(database)
        }
    }
}
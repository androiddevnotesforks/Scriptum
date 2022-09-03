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
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.domain.model.result.ParserResult
import sgtmelon.scriptum.cleanup.parent.ParentBackupTest
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.test.common.nextString

/**
 * Test for [BackupParserImpl].
 */
class BackupParserImplTest : ParentBackupTest() {

    @MockK lateinit var context: Context
    @MockK lateinit var selector: BackupParserSelectorImpl

    private val colorConverter = ColorConverter()
    private val typeConverter = NoteTypeConverter()
    private val stringConverter = StringConverter()

    private val backupParser by lazy {
        BackupParserImpl(context, selector, colorConverter, typeConverter, stringConverter)
    }

    private val spyBackupParser by lazy { spyk(backupParser) }

    private val tagVersion = nextString()
    private val tagHash = nextString()
    private val tagDatabase = nextString()

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(context, selector)
    }

    @Test fun parse_badData() {
        val dataError = nextString()
        val versionError = JSONObject().apply { put(tagVersion, nextString()) }.toString()
        val hashError = JSONObject().apply { put(tagVersion, Random.nextInt()) }.toString()
        val databaseError = JSONObject().apply {
            put(tagVersion, Random.nextInt())
            put(tagHash, nextString())
        }.toString()

        mockTag()

        assertNull(spyBackupParser.parse(dataError))
        assertNull(spyBackupParser.parse(versionError))
        assertNull(spyBackupParser.parse(hashError))
        assertNull(spyBackupParser.parse(databaseError))

        verifySequence {
            spyBackupParser.parse(dataError)

            spyBackupParser.parse(versionError)
            context.getString(R.string.backup_version)

            spyBackupParser.parse(hashError)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)

            spyBackupParser.parse(databaseError)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_database)
        }
    }

    @Test fun parse_badHash() {
        val data = getData()
        val backupJson = getBackupJson(nextString(), data, Random.nextInt())

        mockTag()
        every { spyBackupParser.getHash(data) } returns nextString()

        assertNull(spyBackupParser.parse(backupJson))

        verifySequence {
            spyBackupParser.parse(backupJson)

            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_database)
            spyBackupParser.getHash(data)
        }
    }

    @Test fun parse_goodData() {
        val model = mockk<ParserResult>()
        val data = getData()
        val hash = nextString()
        val version = Random.nextInt()

        val backupJson = getBackupJson(hash, data, version)

        mockTag()
        every { spyBackupParser.getHash(data) } returns hash
        every { selector.parse(data, version) } returns model

        assertEquals(model, spyBackupParser.parse(backupJson))

        verifySequence {
            spyBackupParser.parse(backupJson)

            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            context.getString(R.string.backup_database)
            spyBackupParser.getHash(data)
            selector.parse(data, version)
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
     * Imitate the result of collected database.
     */
    private fun getData() = StringBuilder().apply {
        repeat(times = 5) { append(nextString()) }
    }.toString()

    /**
     * Imitate the backup file content.
     */
    private fun getBackupJson(
        hash: String,
        data: String,
        version: Any = BackupParserImpl.VERSION
    ) = JSONObject().apply {
        put(tagVersion, version)
        put(tagHash, hash)
        put(tagDatabase, data)
    }.toString()

    private fun mockTag() {
        every { context.getString(R.string.backup_version) } returns tagVersion
        every { context.getString(R.string.backup_hash) } returns tagHash
        every { context.getString(R.string.backup_database) } returns tagDatabase
    }

    //endregion

}
package sgtmelon.scriptum.data.room.backup

import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.R
import kotlin.random.Random

/**
 * Test for [BackupParser].
 */
class BackupParserTest : ParentTest() {

    @MockK lateinit var context: Context

    private val backupParser by lazy { BackupParser(context) }
    private val spyBackupParser by lazy { spyk(backupParser) }

    private val versionStart = "<version>"
    private val versionEnd = "</version>"
    private val hashStart = "<hash>"
    private val hashEnd = "</hash>"
    private val roomStart = "<room>"
    private val roomEnd = "</room>"

    private val tagList = listOf(versionStart, versionEnd, hashStart, hashEnd, roomStart, roomEnd)

    @Test fun getVersionTag() {
        every { context.getString(R.string.tag_version_start) } returns versionStart
        every { context.getString(R.string.tag_version_end) } returns versionEnd

        assertEquals(Pair(versionStart, versionEnd), backupParser.getVersionTag())

        verifySequence {
            context.getString(R.string.tag_version_start)
            context.getString(R.string.tag_version_end)
        }
    }

    @Test fun getHashTag() {
        every { context.getString(R.string.tag_hash_start) } returns hashStart
        every { context.getString(R.string.tag_hash_end) } returns hashEnd

        assertEquals(Pair(hashStart, hashEnd), backupParser.getHashTag())

        verifySequence {
            context.getString(R.string.tag_hash_start)
            context.getString(R.string.tag_hash_end)
        }
    }

    @Test fun getRoomTag() {
        every { context.getString(R.string.tag_room_start) } returns roomStart
        every { context.getString(R.string.tag_room_end) } returns roomEnd

        assertEquals(Pair(roomStart, roomEnd), backupParser.getRoomTag())

        verifySequence {
            context.getString(R.string.tag_room_start)
            context.getString(R.string.tag_room_end)
        }
    }


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

            verifyTag()

            spyBackupParser.collectRoom(model)
            spyBackupParser.getHash(roomData)
        }
    }

    @Test fun collectRoom() {
        TODO()
    }


    @Test fun parse_badHash() {
        val roomData = getRoomData()
        val data = getBackupData(Random.nextString(), roomData, Random.nextInt())

        mockTag()
        every { spyBackupParser.getHash(roomData) } returns Random.nextString()

        assertNull(spyBackupParser.parse(data))

        verifySequence {
            spyBackupParser.parse(data)

            verifyTag()
            spyBackupParser.getHash(roomData)
        }
    }

    @Test fun parse_badData_version() {
        val substringBeforeError = Random.nextString()
        val substringBetweenError = "$versionEnd $versionStart"
        val toIntError = "$versionStart${Random.nextString()}$versionStart"

        mockTag()

        assertNull(spyBackupParser.parse(substringBeforeError))
        assertNull(spyBackupParser.parse(substringBetweenError))
        assertNull(spyBackupParser.parse(toIntError))

        verifySequence {
            spyBackupParser.parse(substringBeforeError)
            verifyTag()

            spyBackupParser.parse(substringBetweenError)
            verifyTag()

            spyBackupParser.parse(toIntError)
            verifyTag()
        }
    }

    @Test fun parse_badData_hash() {
        val correctVersion = "$versionStart${Random.nextInt()}$versionEnd\n"

        val substringBeforeError = "$correctVersion$hashStart${Random.nextString()}$hashEnd"
        val substringBetweenError = "$correctVersion$hashStart${Random.nextString()}$hashEnd$roomStart"

        mockTag()

        assertNull(spyBackupParser.parse(substringBeforeError))
        assertNull(spyBackupParser.parse(substringBetweenError))

        verifySequence {
            spyBackupParser.parse(substringBeforeError)
            verifyTag()

            spyBackupParser.parse(substringBetweenError)
            verifyTag()
        }
    }

    @Test fun parse_badData_room() {
        val correctHash = "$versionStart${Random.nextInt()}$versionEnd\n" +
                "$hashStart${Random.nextString()}$hashEnd\n"

        val substringBetweenError = "$correctHash$roomEnd${Random.nextString()}$roomStart"

        mockTag()

        assertNull(spyBackupParser.parse(substringBetweenError))

        verifySequence {
            spyBackupParser.parse(substringBetweenError)
            verifyTag()
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
        every { spyBackupParser.parseByVersion(roomData, version) } returns model

        assertEquals(model, spyBackupParser.parse(data))

        verifySequence {
            spyBackupParser.parse(data)

            verifyTag()
            spyBackupParser.getHash(roomData)
            spyBackupParser.parseByVersion(roomData, version)
        }
    }

    @Test fun parseByVersion() {
        TODO()
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
        repeat(times = tagList.size) { append(Random.nextString()).append(tagList.random()) }
    }.toString()

    /**
     * Imitate the backup file content.
     */
    private fun getBackupData(hash: String, roomResult: String,
                              version: Any = BackupParser.VERSION) = StringBuilder().apply {
        append(versionStart).append(version).append(versionEnd).append("\n")
        append(hashStart).append(hash).append(hashEnd).append("\n")
        append(roomStart).append(roomResult).append(roomEnd)
    }.toString()

    private fun mockTag() {
        every { spyBackupParser.getVersionTag() } returns Pair(versionStart, versionEnd)
        every { spyBackupParser.getHashTag() } returns Pair(hashStart, hashEnd)
        every { spyBackupParser.getRoomTag() } returns Pair(roomStart, roomEnd)
    }

    private fun verifyTag() {
        spyBackupParser.getVersionTag()
        spyBackupParser.getHashTag()
        spyBackupParser.getRoomTag()
    }

}
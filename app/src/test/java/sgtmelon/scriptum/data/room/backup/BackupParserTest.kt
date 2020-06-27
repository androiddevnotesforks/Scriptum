package sgtmelon.scriptum.data.room.backup

import android.content.Context
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
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

    @Test fun collect() {
        val model = mockk<BackupParser.Model>()
        val roomResult = StringBuilder().apply {
            repeat(times = 5) { append(Random.nextString()) }
        }.toString()
        val hash = Random.nextString()

        val titleString = Random.nextString()
        val versionString = Random.nextString()
        val hashString = Random.nextString()

        every { context.getString(R.string.backup_title) } returns titleString
        every { context.getString(R.string.backup_version) } returns versionString
        every { context.getString(R.string.backup_hash) } returns hashString

        every { spyBackupParser.collectRoom(model) } returns roomResult
        every { spyBackupParser.getHash(roomResult) } returns hash

        val result = StringBuilder().apply {
            append(titleString).append("\n")
            append(versionString).append(BackupParser.VERSION).append("\n")
            append(hashString).append(hash).append("\n\n")
            append(roomResult)
        }.toString()

        assertEquals(result, spyBackupParser.collect(model))

        verifySequence {
            spyBackupParser.collect(model)

            spyBackupParser.collectRoom(model)
            context.getString(R.string.backup_title)
            context.getString(R.string.backup_version)
            context.getString(R.string.backup_hash)
            spyBackupParser.getHash(roomResult)
        }
    }

    @Test fun collectRoom() {
        TODO()
    }

    @Test fun parse() {
        TODO()
    }

    /**
     * Get values from website: https://emn178.github.io/online-tools/sha256.html
     */
    @Test fun getHash() {
        val inputResultMap = mapOf(
                "" to "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855",
                "Тest of SHA256." to "86cdf15b207ef2bca14242cb700f6a67038431ea3af004b2741527ee75135d4c",
                "My name is Alexey" to "262d410b26232a1e948b317aa013b3210a324db7c442460410ec0e8ede35a990",
                "Подушечка из манго самая мягкая" to "62b8d1d61be6acb1a76f55a4d2fd866ef8dd68cd3a6fb23b9f032b7f376f09c7"
        )

        inputResultMap.forEach { assertEquals(it.value, backupParser.getHash(it.key)) }
    }

}
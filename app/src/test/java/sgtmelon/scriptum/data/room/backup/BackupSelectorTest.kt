package sgtmelon.scriptum.data.room.backup

import io.mockk.every
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import kotlin.random.Random

/**
 * Test for [BackupSelector].
 */
class BackupSelectorTest : ParentTest() {

    private val backupSelector by lazy { BackupSelector() }
    private val spyBackupSelector by lazy { spyk(backupSelector) }

    @Test fun parseByVersion() {
        val model = mockk<BackupParser.Model>()
        val roomData = Random.nextString()

        every { spyBackupSelector.getVersion1(roomData) } returns model

        assertNull(spyBackupSelector.parseByVersion(Random.nextString(), version = -1))
        assertEquals(model, spyBackupSelector.parseByVersion(roomData, version = 1))

        verifySequence {
            spyBackupSelector.parseByVersion(Random.nextString(), version = -1)

            spyBackupSelector.parseByVersion(roomData, version = 1)
            spyBackupSelector.getVersion1(roomData)
        }
    }

    @Test fun getVersion1() {
        TODO()
    }

}
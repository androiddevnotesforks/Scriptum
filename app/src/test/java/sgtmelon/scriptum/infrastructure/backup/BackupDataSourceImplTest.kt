package sgtmelon.scriptum.infrastructure.backup

import android.content.Context
import androidx.annotation.StringRes
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [BackupDataSourceImpl].
 */
class BackupDataSourceImplTest : ParentTest() {

    @MockK lateinit var context: Context

    private val dataSource by lazy { BackupDataSourceImpl(context) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(context)
    }

    @Test fun getVersionKey() = runTest(R.string.backup_version) { dataSource.versionKey }

    @Test fun getHashKey() = runTest(R.string.backup_hash) { dataSource.hashKey }

    @Test fun getDatabaseKey() = runTest(R.string.backup_database) { dataSource.databaseKey }

    private inline fun runTest(@StringRes stringId: Int, getResult: () -> String) {
        val value = nextString()

        every { context.getString(stringId) } returns value

        assertEquals(getResult(), value)

        verifySequence {
            context.getString(stringId)
        }
    }
}
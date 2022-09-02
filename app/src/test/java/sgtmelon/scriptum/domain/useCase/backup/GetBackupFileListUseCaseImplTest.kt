package sgtmelon.scriptum.domain.useCase.backup

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.item.FileItem

/**
 * Test for [GetBackupFileListUseCaseImpl].
 */
class GetBackupFileListUseCaseImplTest : ParentTest() {

    @MockK lateinit var dataSource: FileDataSource

    private val getBackupFileList by lazy { GetBackupFileListUseCaseImpl(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }


    @Test fun `get list`() {
        val list = mockk<List<FileItem>>()

        coEvery { dataSource.getBackupFileList() } returns list
        runBlocking { assertEquals(getBackupFileList(), list) }

        coEvery { dataSource.getBackupFileList() } returns mockk()
        runBlocking { assertEquals(getBackupFileList(), list) }

        coVerifySequence {
            dataSource.getBackupFileList()
        }
    }

    @Test fun reset() {
        val firstList = mockk<List<FileItem>>()
        val secondList = mockk<List<FileItem>>()

        coEvery { dataSource.getBackupFileList() } returns firstList
        runBlocking { assertEquals(getBackupFileList(), firstList) }

        coEvery { dataSource.getBackupFileList() } returns secondList
        runBlocking { assertEquals(getBackupFileList(), firstList) }

        getBackupFileList.reset()
        runBlocking { assertEquals(getBackupFileList(), secondList) }

        coVerifySequence {
            dataSource.getBackupFileList()
            dataSource.getBackupFileList()
        }
    }
}
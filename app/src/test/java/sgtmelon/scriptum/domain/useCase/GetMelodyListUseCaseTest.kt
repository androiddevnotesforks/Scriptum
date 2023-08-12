package sgtmelon.scriptum.domain.useCase

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.tests.uniter.ParentTest

/**
 * Test for [GetMelodyListUseCase].
 */
class GetMelodyListUseCaseTest : ParentTest() {

    @MockK lateinit var dataSource: RingtoneDataSource

    private val getMelodyList by lazy { GetMelodyListUseCase(dataSource) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(dataSource)
    }


    @Test fun `get list`() {
        val list = mockk<List<MelodyItem>>()

        coEvery { dataSource.getAlarmList() } returns list
        runBlocking { assertEquals(getMelodyList(), list) }

        coEvery { dataSource.getAlarmList() } returns mockk()
        runBlocking { assertEquals(getMelodyList(), list) }

        coVerifySequence {
            dataSource.getAlarmList()
        }
    }

    @Test fun reset() {
        val firstList = mockk<List<MelodyItem>>()
        val secondList = mockk<List<MelodyItem>>()

        coEvery { dataSource.getAlarmList() } returns firstList
        runBlocking { assertEquals(getMelodyList(), firstList) }

        coEvery { dataSource.getAlarmList() } returns secondList
        runBlocking { assertEquals(getMelodyList(), firstList) }

        getMelodyList.reset()
        runBlocking { assertEquals(getMelodyList(), secondList) }

        coVerifySequence {
            dataSource.getAlarmList()
            dataSource.getAlarmList()
        }
    }
}
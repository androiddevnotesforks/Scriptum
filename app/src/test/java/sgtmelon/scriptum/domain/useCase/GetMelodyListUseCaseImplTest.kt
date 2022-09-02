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
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCaseImpl
import sgtmelon.scriptum.infrastructure.model.MelodyItem
import sgtmelon.scriptum.infrastructure.provider.RingtoneProvider

/**
 * Test for [GetMelodyListUseCaseImpl].
 */
class GetMelodyListUseCaseImplTest : ParentTest() {

    @MockK lateinit var ringtoneProvider: RingtoneProvider

    private val getMelodyList by lazy { GetMelodyListUseCaseImpl(ringtoneProvider) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(ringtoneProvider)
    }


    @Test fun `get list`() {
        val list = mockk<List<MelodyItem>>()

        coEvery { ringtoneProvider.getAlarmList() } returns list
        runBlocking { assertEquals(getMelodyList(), list) }

        coEvery { ringtoneProvider.getAlarmList() } returns mockk()
        runBlocking { assertEquals(getMelodyList(), list) }

        coVerifySequence {
            ringtoneProvider.getAlarmList()
        }
    }

    @Test fun reset() {
        val firstList = mockk<List<MelodyItem>>()
        val secondList = mockk<List<MelodyItem>>()

        coEvery { ringtoneProvider.getAlarmList() } returns firstList
        runBlocking { assertEquals(getMelodyList(), firstList) }

        coEvery { ringtoneProvider.getAlarmList() } returns secondList
        runBlocking { assertEquals(getMelodyList(), firstList) }

        getMelodyList.reset()
        runBlocking { assertEquals(getMelodyList(), secondList) }

        coVerifySequence {
            ringtoneProvider.getAlarmList()
            ringtoneProvider.getAlarmList()
        }
    }
}
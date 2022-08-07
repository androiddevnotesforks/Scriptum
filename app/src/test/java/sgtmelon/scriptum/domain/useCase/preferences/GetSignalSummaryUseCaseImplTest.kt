package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCaseImpl
import sgtmelon.scriptum.infrastructure.provider.SummaryProvider
import sgtmelon.test.common.nextString

/**
 * Test for [GetSignalSummaryUseCaseImpl].
 */
class GetSignalSummaryUseCaseImplTest : ParentTest(),
    GetSummaryUseCaseTest {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val getSummary by lazy {
        GetSignalSummaryUseCaseImpl(summaryProvider, preferencesRepo)
    }
    private val spyGetSummary by lazy { spyk(getSummary) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferencesRepo)
    }

    @Test override fun `simple summary get`() {
        val typeCheck = BooleanArray(getRandomSize()) { Random.nextBoolean() }
        val summary = nextString()

        every { preferencesRepo.signalTypeCheck } returns typeCheck
        every { summaryProvider.getSignal(typeCheck) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.signalTypeCheck
            summaryProvider.getSignal(typeCheck)
        }
    }

    @Test override fun `get summary with data set`() {
        val valueArray = BooleanArray(getRandomSize()) { Random.nextBoolean() }
        val summary = nextString()

        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(valueArray), summary)

        verifySequence {
            spyGetSummary(valueArray)

            preferencesRepo.signalTypeCheck = valueArray
            spyGetSummary()
        }
    }
}
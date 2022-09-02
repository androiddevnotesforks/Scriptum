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
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSignalSummaryUseCaseImpl
import sgtmelon.test.common.nextString

/**
 * Test for [GetSignalSummaryUseCaseImpl].
 */
class GetSignalSummaryUseCaseImplTest : ParentTest(),
    GetSummaryUseCaseTest {

    @MockK lateinit var summaryDataSource: SummaryDataSource
    @MockK lateinit var preferencesRepo: PreferencesRepo

    private val getSummary by lazy {
        GetSignalSummaryUseCaseImpl(summaryDataSource, preferencesRepo)
    }
    private val spyGetSummary by lazy { spyk(getSummary) }

    override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryDataSource, preferencesRepo)
    }

    @Test override fun `simple summary get`() {
        val typeCheck = BooleanArray(getRandomSize()) { Random.nextBoolean() }
        val summary = nextString()

        every { preferencesRepo.signalTypeCheck } returns typeCheck
        every { summaryDataSource.getSignal(typeCheck) } returns summary

        assertEquals(getSummary(), summary)

        verifySequence {
            preferencesRepo.signalTypeCheck
            summaryDataSource.getSignal(typeCheck)
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
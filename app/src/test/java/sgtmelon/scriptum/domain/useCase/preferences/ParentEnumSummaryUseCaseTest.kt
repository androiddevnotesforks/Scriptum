package sgtmelon.scriptum.domain.useCase.preferences

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.data.dataSource.system.SummaryDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.converter.key.ParentEnumConverter
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
import kotlin.random.Random

/**
 * Test for all child of [GetSummaryUseCase].
 */
abstract class ParentEnumSummaryUseCaseTest<T: ParentEnumConverter<*>> : ParentTest(),
    GetSummaryUseCaseTest {

    @MockK lateinit var summaryDataSource: SummaryDataSource
    @MockK lateinit var preferencesRepo: PreferencesRepo
    abstract var converter: T

    abstract val getSummary: GetSummaryUseCase

    protected val spyGetSummary by lazy { spyk(getSummary) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryDataSource, preferencesRepo, converter)
    }

    @Test fun `get summary with bad converting`() {
        val value = Random.nextInt()
        val summary = nextString()

        every { converter.toEnum(value) } returns null
        every { spyGetSummary() } returns summary

        assertEquals(spyGetSummary(value), summary)

        verifySequence {
            spyGetSummary(value)
            converter.toEnum(value)
            spyGetSummary()
        }
    }
}
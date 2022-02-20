package sgtmelon.scriptum.domain.interactor.impl.preference

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.spyk
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentInteractorTest
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import kotlin.random.Random

/**
 * Test for [PreferenceInteractor]
 */
@ExperimentalCoroutinesApi
class PreferenceInteractorTest : ParentInteractorTest() {

    //region Setup

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { PreferenceInteractor(summaryProvider, preferenceRepo) }
    private val spyInteractor by lazy { spyk(interactor) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(summaryProvider, preferenceRepo)
    }

    //endregion

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getThemeSummary() {
        val size = getRandomSize()
        val valueArray = Array(size) { nextString() }
        val index = valueArray.indices.random()
        val value = valueArray[index]

        every { summaryProvider.theme } returns valueArray
        every { spyInteractor.theme } returns -1
        assertNull(spyInteractor.getThemeSummary())

        every { spyInteractor.theme } returns index
        assertEquals(value, spyInteractor.getThemeSummary())

        verifySequence {
            repeat(times = 2) {
                spyInteractor.getThemeSummary()
                summaryProvider.theme
                spyInteractor.theme
            }
        }
    }

    @Test fun updateTheme() {
        val value = Random.nextInt()
        val summary = nextString()

        every { preferenceRepo.theme = value } returns Unit
        every { spyInteractor.getThemeSummary() } returns null
        assertNull(spyInteractor.updateTheme(value))

        every { spyInteractor.getThemeSummary() } returns summary
        assertEquals(summary, spyInteractor.updateTheme(value))

        verifySequence {
            repeat(times = 2) {
                spyInteractor.updateTheme(value)
                preferenceRepo.theme = value
                spyInteractor.getThemeSummary()
            }
        }
    }


    @Test fun getIsDeveloper() {
        val value = Random.nextBoolean()

        every { preferenceRepo.isDeveloper } returns value

        assertEquals(value, interactor.isDeveloper)

        verifySequence {
            preferenceRepo.isDeveloper
        }
    }

    @Test fun setIsDeveloper() {
        val value = Random.nextBoolean()

        every { preferenceRepo.isDeveloper = value } returns Unit

        interactor.isDeveloper = value

        verifySequence {
            preferenceRepo.isDeveloper = value
        }
    }
}
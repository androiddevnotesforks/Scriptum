package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import kotlin.random.Random

/**
 * Test for [PreferenceInteractor]
 */
@ExperimentalCoroutinesApi
class PreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { PreferenceInteractor(summaryProvider, preferenceRepo) }

    @Test fun getTheme() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(interactor.theme, value)
        }

        val valueList = listOf(Theme.LIGHT, Theme.DARK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.theme }
        }
    }

    @Test fun getThemeSummary() {
                TODO()

        every { summaryProvider.theme } returns summaryList

        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(interactor.getThemeSummary(), summaryList.getOrNull(value))
        }

        val valueList = listOf(Theme.LIGHT, Theme.DARK, Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.theme
                preferenceRepo.theme
            }
        }
    }

    @Test fun updateTheme() {
                TODO()

        every { summaryProvider.theme } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(interactor.updateTheme(value), summaryList.getOrNull(value))
        }

        val valueList = listOf(Theme.LIGHT, Theme.DARK, Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.theme = it

                summaryProvider.theme
                preferenceRepo.theme
            }
        }
    }


    @Test fun getSort() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(interactor.sort, value)
        }

        val valueList = listOf(Sort.CHANGE, Sort.RANK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.sort }
        }
    }

    @Test fun getSortSummary() {
                TODO()

        every { summaryProvider.sort } returns summaryList

        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(interactor.getSortSummary(), summaryList.getOrNull(value))
        }

        val valueList = listOf(Sort.CHANGE, Sort.RANK, Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.sort
                preferenceRepo.sort
            }
        }
    }

    @Test fun updateSort() {
                TODO()

        every { summaryProvider.sort } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(interactor.updateSort(value), summaryList.getOrNull(value))
        }

        val valueList = listOf(Sort.CHANGE, Sort.RANK, Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.sort = it

                summaryProvider.sort
                preferenceRepo.sort
            }
        }
    }


    @Test fun getDefaultColor() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(interactor.defaultColor, value)
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Color.INDIGO, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.defaultColor }
        }
    }

    @Test fun getDefaultColorSummary() {
                TODO()

        every { summaryProvider.color } returns summaryList

        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(interactor.getDefaultColorSummary(), summaryList.getOrNull(value))
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Color.INDIGO, Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.color
                preferenceRepo.defaultColor
            }
        }
    }

    @Test fun updateDefaultColor() {
                TODO()

        every { summaryProvider.color } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(interactor.updateDefaultColor(value), summaryList.getOrNull(value))
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Color.INDIGO, Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.defaultColor = it

                summaryProvider.color
                preferenceRepo.defaultColor
            }
        }
    }


    @Test fun getSavePeriod() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(interactor.savePeriod, value)
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.savePeriod }
        }
    }

    @Test fun getSavePeriodSummary() {
                TODO()

        every { summaryProvider.savePeriod } returns summaryList

        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(interactor.getSavePeriodSummary(), summaryList.getOrNull(value))
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.savePeriod
                preferenceRepo.savePeriod
            }
        }
    }

    @Test fun updateSavePeriod() {
                TODO()

        every { summaryProvider.savePeriod } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(interactor.updateSavePeriod(value), summaryList.getOrNull(value))
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.savePeriod = it

                summaryProvider.savePeriod
                preferenceRepo.savePeriod
            }
        }
    }


    @Test fun getRepeat() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(interactor.repeat, value)
        }

        val valueList = listOf(Repeat.MIN_10, Repeat.MIN_180, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.repeat }
        }
    }

    @Test fun getRepeatSummary() {
                TODO()

        every { summaryProvider.repeat } returns summaryList

        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(interactor.getRepeatSummary(), summaryList.getOrNull(value))
        }

        val valueList = listOf(Repeat.MIN_10, Repeat.MIN_180, Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.repeat
                preferenceRepo.repeat
            }
        }
    }

    @Test fun updateRepeat() {
                TODO()

        every { summaryProvider.repeat } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(interactor.updateRepeat(value), summaryList.getOrNull(value))
        }

        val valueList = listOf(Repeat.MIN_10, Repeat.MIN_180, Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.repeat = it

                summaryProvider.repeat
                preferenceRepo.repeat
            }
        }
    }


    @Test fun getSignalSummaryArray() {
                TODO()

        TODO()
    }

    @Test fun updateSignal() {
                TODO()

        TODO()
    }


    @Test fun getVolume() {
                TODO()

        fun checkRequestGet(value: Int) {
            every { preferenceRepo.volume } returns value
            assertEquals(interactor.volume, value)
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volume }
        }
    }

    @Test fun getVolumeSummary() {
                TODO()

        fun getSummary(value: Int) = if (value % 2 == 0) summaryVolume.plus(value) else null

        fun checkRequestSummary(value: Int) {
            every { summaryProvider.getVolume(value) } returns getSummary(value)
            every { preferenceRepo.volume } returns value
            assertEquals(interactor.getVolumeSummary(), getSummary(value))
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.volume
                summaryProvider.getVolume(it)
            }
        }
    }

    @Test fun updateVolume() {
                TODO()

        fun getSummary(value: Int) = if (value % 2 == 0) summaryVolume.plus(value) else null

        fun checkRequestUpdate(value: Int) {
            every { summaryProvider.getVolume(value) } returns getSummary(value)
            every { preferenceRepo.volume } returns value
            assertEquals(interactor.updateVolume(value), getSummary(value))
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestUpdate(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.volume = it

                preferenceRepo.volume
                summaryProvider.getVolume(it)
            }
        }
    }


    companion object {
        private val summaryList = arrayOf("summary 1", "summary 2", "summary 3")
        private const val summaryVolume = "Volume: "
    }

}
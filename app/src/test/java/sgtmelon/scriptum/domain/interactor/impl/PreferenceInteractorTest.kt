package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.coEvery
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.FastTest
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import java.util.*
import kotlin.random.Random

/**
 * Test for [PreferenceInteractor]
 */
@ExperimentalCoroutinesApi
class PreferenceInteractorTest : ParentInteractorTest() {

    @MockK lateinit var summaryProvider: SummaryProvider
    @MockK lateinit var preferenceRepo: IPreferenceRepo

    private val interactor by lazy { PreferenceInteractor(summaryProvider, preferenceRepo) }

    @Test fun getTheme() = FastTest.getTheme(preferenceRepo) { interactor.theme }

    @Test fun getThemeSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(summaryList.getOrNull(value), interactor.getThemeSummary())
        }

        val valueList = listOf(Theme.LIGHT, Random.nextInt())

        every { summaryProvider.theme } returns summaryList
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.theme
                preferenceRepo.theme
            }
        }
    }

    @Test fun updateTheme() {
        every { summaryProvider.theme } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(summaryList.getOrNull(value), interactor.updateTheme(value))
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


    @Test fun getSort() = FastTest.getSort(preferenceRepo) { interactor.sort }

    @Test fun getSortSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(summaryList.getOrNull(value), interactor.getSortSummary())
        }

        val valueList = listOf(Sort.CHANGE, Random.nextInt())

        every { summaryProvider.sort } returns summaryList
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.sort
                preferenceRepo.sort
            }
        }
    }

    @Test fun updateSort() {
        every { summaryProvider.sort } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(summaryList.getOrNull(value), interactor.updateSort(value))
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


    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }

    @Test fun getDefaultColorSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(summaryList.getOrNull(value), interactor.getDefaultColorSummary())
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Random.nextInt())

        every { summaryProvider.color } returns summaryList
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.color
                preferenceRepo.defaultColor
            }
        }
    }

    @Test fun updateDefaultColor() {
        every { summaryProvider.color } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(summaryList.getOrNull(value), interactor.updateDefaultColor(value))
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
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(value, interactor.savePeriod)
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.savePeriod }
        }
    }

    @Test fun getSavePeriodSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(summaryList.getOrNull(value), interactor.getSavePeriodSummary())
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt())

        every { summaryProvider.savePeriod } returns summaryList
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.savePeriod
                preferenceRepo.savePeriod
            }
        }
    }

    @Test fun updateSavePeriod() {
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


    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getRepeatSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(summaryList.getOrNull(value), interactor.getRepeatSummary())
        }

        val valueList = listOf(Repeat.MIN_10, Random.nextInt())

        every { summaryProvider.repeat } returns summaryList
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            repeat(valueList.size) {
                summaryProvider.repeat
                preferenceRepo.repeat
            }
        }
    }

    @Test fun updateRepeat() {
        every { summaryProvider.repeat } returns summaryList

        fun checkRequestUpdate(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(summaryList.getOrNull(value), interactor.updateRepeat(value))
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
        val summaryArray = Array(size = 3) { Random.nextString() }
        val checkArray = booleanArrayOf(true, false, true)

        fun String.getLow() = toLowerCase(Locale.getDefault())
        val resultString = "${summaryArray.first().getLow()}, ${summaryArray.last().getLow()}"

        coEvery { summaryProvider.signal } returns arrayOf(Random.nextString())
        assertNull(interactor.getSignalSummaryArray(checkArray))

        coEvery { summaryProvider.signal } returns summaryArray
        assertEquals(resultString, interactor.getSignalSummaryArray(checkArray))
    }

    @Test fun updateSignal() {
        val summaryArray = Array(size = 3) { Random.nextString() }
        val checkArray = booleanArrayOf(true, false, true)

        fun String.getLow() = toLowerCase(Locale.getDefault())
        val resultString = "${summaryArray.first().getLow()}, ${summaryArray.last().getLow()}"

        coEvery { summaryProvider.signal } returns summaryArray
        assertEquals(resultString, interactor.updateSignal(checkArray))

        verifySequence {
            preferenceRepo.signal = IntConverter().toInt(checkArray)
        }
    }


    @Test fun getVolume() = FastTest.getVolume(preferenceRepo) { interactor.volume }

    @Test fun getVolumeSummary() {
        fun checkRequestSummary(value: Int) {
            every { preferenceRepo.volume } returns value
            every { summaryProvider.getVolume(value) } returns summaryVolume.plus(value)

            assertEquals(summaryVolume.plus(value), interactor.getVolumeSummary())
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.volume
                summaryProvider.getVolume(it)
            }
        }
    }

    @Test fun updateVolume() {
        fun checkRequestUpdate(value: Int) {
            every { summaryProvider.getVolume(value) } returns summaryVolume.plus(value)
            every { preferenceRepo.volume } returns value
            assertEquals(summaryVolume.plus(value), interactor.updateVolume(value))
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


    private val summaryList = arrayOf("summary 1", "summary 2", "summary 3")

    companion object {
        private const val summaryVolume = "Volume: "
    }

}
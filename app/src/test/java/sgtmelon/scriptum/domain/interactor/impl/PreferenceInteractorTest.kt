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
        fun checkRequestSummary(value: Int?, summaryList: Array<String>?) {
            every { preferenceRepo.theme } returns value
            assertEquals(value?.let { summaryList?.getOrNull(it) }, interactor.getThemeSummary())
        }

        val valueList = listOf(null, Theme.LIGHT, null, Random.nextInt())

        every { summaryProvider.theme } returns null
        valueList.forEach { checkRequestSummary(it, summaryList = null) }

        every { summaryProvider.theme } returns summaryList
        valueList.forEach { checkRequestSummary(it, summaryList) }

        verifySequence {
            repeat(times = 2) {
                valueList.forEach {
                    preferenceRepo.theme
                    it?.let { summaryProvider.theme }
                }
            }
        }
    }

    @Test fun updateTheme() {
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

                preferenceRepo.theme
                summaryProvider.theme
            }
        }
    }


    @Test fun getSort() = FastTest.getSort(preferenceRepo) { interactor.sort }

    @Test fun getSortSummary() {
        fun checkRequestSummary(value: Int?, summaryList: Array<String>?) {
            every { preferenceRepo.sort } returns value
            assertEquals(value?.let { summaryList?.getOrNull(it) }, interactor.getSortSummary())
        }

        val valueList = listOf(null, Sort.CHANGE, null, Random.nextInt())

        every { summaryProvider.sort } returns null
        valueList.forEach { checkRequestSummary(it, summaryList = null) }

        every { summaryProvider.sort } returns summaryList
        valueList.forEach { checkRequestSummary(it, summaryList) }

        verifySequence {
            repeat(times = 2) {
                valueList.forEach {
                    preferenceRepo.sort
                    it?.let { summaryProvider.sort }
                }
            }
        }
    }

    @Test fun updateSort() {
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

                preferenceRepo.sort
                summaryProvider.sort
            }
        }
    }


    @Test fun getDefaultColor() = FastTest.getDefaultColor(preferenceRepo) {
        interactor.defaultColor
    }

    @Test fun getDefaultColorSummary() {
        fun checkRequestSummary(value: Int?, summaryList: Array<String>?) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(value?.let { summaryList?.getOrNull(it) }, interactor.getDefaultColorSummary())
        }

        val valueList = listOf(null, Color.RED, Color.PURPLE, null, Random.nextInt())

        every { summaryProvider.color } returns null
        valueList.forEach { checkRequestSummary(it, summaryList = null) }

        every { summaryProvider.color } returns summaryList
        valueList.forEach { checkRequestSummary(it, summaryList) }

        verifySequence {
            repeat(times = 2) {
                valueList.forEach {
                    preferenceRepo.defaultColor
                    it?.let { summaryProvider.color }
                }
            }
        }
    }

    @Test fun updateDefaultColor() {
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

                preferenceRepo.defaultColor
                summaryProvider.color
            }
        }
    }


    @Test fun getSavePeriod() {
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
        fun checkRequestSummary(value: Int?, summaryList: Array<String>?) {
            every { preferenceRepo.savePeriod } returns value
            assertEquals(value?.let { summaryList?.getOrNull(it) }, interactor.getSavePeriodSummary())
        }

        val valueList = listOf(null, Random.nextInt(), Random.nextInt(), null)

        every { summaryProvider.savePeriod } returns null
        valueList.forEach { checkRequestSummary(it, summaryList = null) }

        every { summaryProvider.savePeriod } returns summaryList
        valueList.forEach { checkRequestSummary(it, summaryList) }

        verifySequence {
            repeat(times = 2) {
                valueList.forEach {
                    preferenceRepo.savePeriod
                    it?.let { summaryProvider.savePeriod }
                }
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

                preferenceRepo.savePeriod
                summaryProvider.savePeriod
            }
        }
    }


    @Test fun getRepeat() = FastTest.getRepeat(preferenceRepo) { interactor.repeat }

    @Test fun getRepeatSummary() {
        fun checkRequestSummary(value: Int?, summaryList: Array<String>?) {
            every { preferenceRepo.repeat } returns value
            assertEquals(value?.let { summaryList?.getOrNull(it) }, interactor.getRepeatSummary())
        }

        val valueList = listOf(null, Repeat.MIN_10, null, Random.nextInt())

        every { summaryProvider.repeat } returns null
        valueList.forEach { checkRequestSummary(it, summaryList = null) }

        every { summaryProvider.repeat } returns summaryList
        valueList.forEach { checkRequestSummary(it, summaryList) }

        verifySequence {
            repeat(times = 2) {
                valueList.forEach {
                    preferenceRepo.repeat
                    it?.let { summaryProvider.repeat }
                }
            }
        }
    }

    @Test fun updateRepeat() {
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

                preferenceRepo.repeat
                summaryProvider.repeat
            }
        }
    }


    @Test fun getSignalSummaryArray() {
        val summaryArray = Array(size = 3) { Random.nextString() }
        val checkArray = booleanArrayOf(true, false, true)

        fun String.getLow() = toLowerCase(Locale.getDefault())
        val resultString = "${summaryArray.first().getLow()}, ${summaryArray.last().getLow()}"

        coEvery { summaryProvider.signal } returns null
        assertNull(interactor.getSignalSummaryArray(checkArray))

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
        fun getSummary(value: Int) = if (value % 2 == 0) summaryVolume.plus(value) else null

        fun checkRequestSummary(value: Int?) {
            every { preferenceRepo.volume } returns value

            if (value != null) {
                every { summaryProvider.getVolume(value) } returns getSummary(value)
            }

            assertEquals( value?.let { getSummary(it) }, interactor.getVolumeSummary())
        }

        val valueList = listOf(null, Random.nextInt(), Random.nextInt(), null)
        valueList.forEach { checkRequestSummary(it) }

        verifySequence {
            valueList.forEach {
                preferenceRepo.volume

                if (it != null) {
                    summaryProvider.getVolume(it)
                }
            }
        }
    }

    @Test fun updateVolume() {
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


    private val summaryList = arrayOf("summary 1", "summary 2", "summary 3")

    companion object {
        private const val summaryVolume = "Volume: "
    }

}
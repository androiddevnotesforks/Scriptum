package sgtmelon.scriptum

import io.mockk.every
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.presentation.control.note.save.SaveControl
import kotlin.random.Random

/**
 * Object for describe common and fast tests.
 */
object FastTest {

    object Note {

        fun getSaveModel(preferenceRepo: IPreferenceRepo, callFun: () -> SaveControl.Model) {
            val model = with(Random) { SaveControl.Model(nextBoolean(), nextBoolean(), nextInt()) }

            every { preferenceRepo.pauseSaveOn } returns model.pauseSaveOn
            every { preferenceRepo.autoSaveOn } returns model.autoSaveOn
            every { preferenceRepo.savePeriod } returns model.savePeriod
            assertEquals(model, callFun())

            verifySequence {
                preferenceRepo.pauseSaveOn
                preferenceRepo.autoSaveOn
                preferenceRepo.savePeriod
            }
        }

    }

    fun getFirstStart(preferenceRepo: IPreferenceRepo, callFun: () -> Boolean) {
        fun checkRequestGet(value: Boolean) {
            every { preferenceRepo.firstStart } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Random.nextBoolean(), Random.nextBoolean())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.firstStart }
        }
    }

    fun getTheme(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.theme } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Theme.LIGHT, Theme.DARK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.theme }
        }
    }

    fun getSort(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.sort } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Sort.CHANGE, Sort.RANK, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.sort }
        }
    }

    fun getDefaultColor(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.defaultColor } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Color.RED, Color.PURPLE, Color.INDIGO, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.defaultColor }
        }
    }

    fun getRepeat(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.repeat } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Repeat.MIN_10, Repeat.MIN_180, Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.repeat }
        }
    }

    fun getVolume(preferenceRepo: IPreferenceRepo, callFun: () -> Int) {
        fun checkRequestGet(value: Int) {
            every { preferenceRepo.volume } returns value
            assertEquals(callFun(), value)
        }

        val valueList = listOf(Random.nextInt(), Random.nextInt(), Random.nextInt())
        valueList.forEach { checkRequestGet(it) }

        verifySequence {
            repeat(valueList.size) { preferenceRepo.volume }
        }
    }

}
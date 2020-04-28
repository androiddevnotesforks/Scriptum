package sgtmelon.scriptum

import io.mockk.every
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Theme
import kotlin.random.Random

/**
 * Object for describe common and fast tests.
 */
object FastTest {

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
package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.repository.preference.PreferenceRepo
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.model.annotation.Color
import sgtmelon.scriptum.domain.model.annotation.Repeat
import sgtmelon.scriptum.domain.model.annotation.Sort
import sgtmelon.scriptum.domain.model.annotation.Theme
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [PreferenceRepo]
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRepoTest : ParentIntegrationTest()  {

    // TODO null values

    private val def = PreferenceProvider.Def(context.resources)

    override fun setUp() {
        super.setUp()
        preferenceRepo.clear()
    }

    @Test fun defaultValues() {
        TODO("nullable")

        assertEquals(def.FIRST_START, preferenceRepo.firstStart)
        assertEquals(def.THEME, preferenceRepo.theme)

        assertEquals(def.REPEAT, preferenceRepo.repeat)
        assertEquals(def.SIGNAL, preferenceRepo.signal)
        assertEquals(def.MELODY_URI, preferenceRepo.melodyUri)
        assertEquals(def.VOLUME, preferenceRepo.volume)
        assertEquals(def.VOLUME_INCREASE, preferenceRepo.volumeIncrease)

        assertEquals(def.SORT, preferenceRepo.sort)
        assertEquals(def.DEFAULT_COLOR, preferenceRepo.defaultColor)
        assertEquals(def.PAUSE_SAVE_ON, preferenceRepo.pauseSaveOn)
        assertEquals(def.AUTO_SAVE_ON, preferenceRepo.autoSaveOn)
        assertEquals(def.SAVE_PERIOD, preferenceRepo.savePeriod)
    }


    @Test fun firstStart() = false.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { firstStart = it }.firstStart)
    }

    @Test fun theme() = Theme.DARK.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { theme = it }.theme)
    }


    @Test fun repeat() = Repeat.MIN_60.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { repeat = it }.repeat)
    }

    @Test fun signal() = IntConverter().toInt(booleanArrayOf(true, true)).let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { signal = it }.signal)
    }

    @Test fun melodyUrl() = data.uniqueString.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { melodyUri = it }.melodyUri)
    }

    @Test fun volume() = Random.nextInt().let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { volume = it }.volume)
    }

    @Test fun volumeIncrease() = true.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { volumeIncrease = it }.volumeIncrease)
    }


    @Test fun sort() = Sort.COLOR.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { sort = it }.sort)
    }

    @Test fun defaultColor() = Color.RED.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { defaultColor = it }.defaultColor)
    }

    @Test fun pauseSaveOn() = true.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { pauseSaveOn = it }.pauseSaveOn)
    }

    @Test fun autoSaveOn() = true.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { autoSaveOn = it }.autoSaveOn)
    }

    @Test fun savePeriod() = 1.let {
        TODO("nullable")

        assertEquals(it, preferenceRepo.apply { savePeriod = it }.savePeriod)
    }

}
package sgtmelon.scriptum.test.integration.repo

import androidx.test.ext.junit.runners.AndroidJUnit4
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import sgtmelon.scriptum.model.annotation.Color
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.model.annotation.Sort
import sgtmelon.scriptum.model.annotation.Theme
import sgtmelon.scriptum.provider.PreferenceProvider
import sgtmelon.scriptum.repository.preference.PreferenceRepo
import sgtmelon.scriptum.room.converter.type.IntConverter
import sgtmelon.scriptum.test.ParentIntegrationTest
import kotlin.random.Random

/**
 * Integration test for [PreferenceRepo]
 */
@RunWith(AndroidJUnit4::class)
class PreferenceRepoTest : ParentIntegrationTest()  {

    private val def = PreferenceProvider.Def(context)

    override fun setUp() {
        super.setUp()
        iPreferenceRepo.clear()
    }

    @Test fun defaultValues() {
        assertEquals(def.FIRST_START, iPreferenceRepo.firstStart)
        assertEquals(def.THEME, iPreferenceRepo.theme)

        assertEquals(def.REPEAT, iPreferenceRepo.repeat)
        assertEquals(def.SIGNAL, iPreferenceRepo.signal)
        assertEquals(def.MELODY_URI, iPreferenceRepo.melodyUri)
        assertEquals(def.VOLUME, iPreferenceRepo.volume)
        assertEquals(def.VOLUME_INCREASE, iPreferenceRepo.volumeIncrease)

        assertEquals(def.SORT, iPreferenceRepo.sort)
        assertEquals(def.DEFAULT_COLOR, iPreferenceRepo.defaultColor)
        assertEquals(def.PAUSE_SAVE_ON, iPreferenceRepo.pauseSaveOn)
        assertEquals(def.AUTO_SAVE_ON, iPreferenceRepo.autoSaveOn)
        assertEquals(def.SAVE_PERIOD, iPreferenceRepo.savePeriod)
    }


    @Test fun firstStart() = false.let {
        assertEquals(it, iPreferenceRepo.apply { firstStart = it }.firstStart)
    }

    @Test fun theme() = Theme.DARK.let {
        assertEquals(it, iPreferenceRepo.apply { theme = it }.theme)
    }


    @Test fun repeat() = Repeat.MIN_60.let {
        assertEquals(it, iPreferenceRepo.apply { repeat = it }.repeat)
    }

    @Test fun signal() = IntConverter().toInt(booleanArrayOf(true, true)).let {
        assertEquals(it, iPreferenceRepo.apply { signal = it }.signal)
    }

    @Test fun melodyUrl() = data.uniqueString.let {
        assertEquals(it, iPreferenceRepo.apply { melodyUri = it }.melodyUri)
    }

    @Test fun volume() = Random.nextInt().let {
        assertEquals(it, iPreferenceRepo.apply { volume = it }.volume)
    }

    @Test fun volumeIncrease() = true.let {
        assertEquals(it, iPreferenceRepo.apply { volumeIncrease = it }.volumeIncrease)
    }


    @Test fun sort() = Sort.COLOR.let {
        assertEquals(it, iPreferenceRepo.apply { sort = it }.sort)
    }

    @Test fun defaultColor() = Color.RED.let {
        assertEquals(it, iPreferenceRepo.apply { defaultColor = it }.defaultColor)
    }

    @Test fun pauseSaveOn() = true.let {
        assertEquals(it, iPreferenceRepo.apply { pauseSaveOn = it }.pauseSaveOn)
    }

    @Test fun autoSaveOn() = true.let {
        assertEquals(it, iPreferenceRepo.apply { autoSaveOn = it }.autoSaveOn)
    }

    @Test fun savePeriod() = 1.let {
        assertEquals(it, iPreferenceRepo.apply { savePeriod = it }.savePeriod)
    }

}
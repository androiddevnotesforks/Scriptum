package sgtmelon.scriptum.data.provider

import android.content.res.Resources
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.R
import kotlin.random.Random

/**
 * Test of [PreferenceProvider].
 */
class PreferenceProviderTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val providerKey by lazy { PreferenceProvider.Key(resources) }
    private val providerDef by lazy { PreferenceProvider.Def(resources) }

    @Test fun valueKey() {
        val firstStart = nextString()
        val theme = nextString()
        val importSkip = nextString()
        val sort = nextString()
        val defaultColor = nextString()
        val pauseSaveOn = nextString()
        val autoSaveOn = nextString()
        val savePeriod = nextString()
        val repeat = nextString()
        val signal = nextString()
        val melodyUri = nextString()
        val volume = nextString()
        val volumeIncrease = nextString()

        every { resources.getString(R.string.pref_key_first_start) } returns firstStart
        every { resources.getString(R.string.pref_key_app_theme) } returns theme
        every { resources.getString(R.string.pref_key_backup_import_skip) } returns importSkip
        every { resources.getString(R.string.pref_key_note_sort) } returns sort
        every { resources.getString(R.string.pref_key_note_color) } returns defaultColor
        every { resources.getString(R.string.pref_key_note_pause) } returns pauseSaveOn
        every { resources.getString(R.string.pref_key_note_auto) } returns autoSaveOn
        every { resources.getString(R.string.pref_key_note_time) } returns savePeriod
        every { resources.getString(R.string.pref_key_alarm_repeat) } returns repeat
        every { resources.getString(R.string.pref_key_alarm_signal) } returns signal
        every { resources.getString(R.string.pref_key_alarm_melody) } returns melodyUri
        every { resources.getString(R.string.pref_key_alarm_volume) } returns volume
        every { resources.getString(R.string.pref_key_alarm_increase) } returns volumeIncrease

        assertEquals(firstStart, providerKey.firstStart)
        assertEquals(theme, providerKey.theme)
        assertEquals(importSkip, providerKey.importSkip)
        assertEquals(sort, providerKey.sort)
        assertEquals(defaultColor, providerKey.defaultColor)
        assertEquals(pauseSaveOn, providerKey.pauseSaveOn)
        assertEquals(autoSaveOn, providerKey.autoSaveOn)
        assertEquals(savePeriod, providerKey.savePeriod)
        assertEquals(repeat, providerKey.repeat)
        assertEquals(signal, providerKey.signal)
        assertEquals(melodyUri, providerKey.melodyUri)
        assertEquals(volume, providerKey.volume)
        assertEquals(volumeIncrease, providerKey.volumeIncrease)

        verifySequence {
            resources.getString(R.string.pref_key_first_start)
            resources.getString(R.string.pref_key_app_theme)
            resources.getString(R.string.pref_key_backup_import_skip)
            resources.getString(R.string.pref_key_note_sort)
            resources.getString(R.string.pref_key_note_color)
            resources.getString(R.string.pref_key_note_pause)
            resources.getString(R.string.pref_key_note_auto)
            resources.getString(R.string.pref_key_note_time)
            resources.getString(R.string.pref_key_alarm_repeat)
            resources.getString(R.string.pref_key_alarm_signal)
            resources.getString(R.string.pref_key_alarm_melody)
            resources.getString(R.string.pref_key_alarm_volume)
            resources.getString(R.string.pref_key_alarm_increase)
        }
    }

    @Test fun valueDef() {
        val firstStart = Random.nextBoolean()
        val theme = Random.nextInt()
        val importSkip = Random.nextBoolean()
        val sort = Random.nextInt()
        val defaultColor = Random.nextInt()
        val pauseSaveOn = Random.nextBoolean()
        val autoSaveOn = Random.nextBoolean()
        val savePeriod = Random.nextInt()
        val repeat = Random.nextInt()
        val signal = Random.nextInt()
        val melodyUri = nextString()
        val volume = Random.nextInt()
        val volumeIncrease = Random.nextBoolean()

        every { resources.getBoolean(R.bool.pref_first_start) } returns firstStart
        every { resources.getInteger(R.integer.pref_app_theme) } returns theme
        every { resources.getBoolean(R.bool.pref_backup_import_skip) } returns importSkip
        every { resources.getInteger(R.integer.pref_note_sort) } returns sort
        every { resources.getInteger(R.integer.pref_note_color) } returns defaultColor
        every { resources.getBoolean(R.bool.pref_note_save_pause) } returns pauseSaveOn
        every { resources.getBoolean(R.bool.pref_note_save_auto) } returns autoSaveOn
        every { resources.getInteger(R.integer.pref_note_save_time) } returns savePeriod
        every { resources.getInteger(R.integer.pref_alarm_repeat) } returns repeat
        every { resources.getInteger(R.integer.pref_alarm_signal) } returns signal
        every { resources.getString(R.string.pref_alarm_melody) } returns melodyUri
        every { resources.getInteger(R.integer.pref_alarm_volume) } returns volume
        every { resources.getBoolean(R.bool.pref_alarm_increase) } returns volumeIncrease

        assertEquals(firstStart, providerDef.firstStart)
        assertEquals(theme, providerDef.theme)
        assertEquals(importSkip, providerDef.importSkip)
        assertEquals(sort, providerDef.sort)
        assertEquals(defaultColor, providerDef.defaultColor)
        assertEquals(pauseSaveOn, providerDef.pauseSaveOn)
        assertEquals(autoSaveOn, providerDef.autoSaveOn)
        assertEquals(savePeriod, providerDef.savePeriod)
        assertEquals(repeat, providerDef.repeat)
        assertEquals(signal, providerDef.signal)
        assertEquals(melodyUri, providerDef.melodyUri)
        assertEquals(volume, providerDef.volume)
        assertEquals(volumeIncrease, providerDef.volumeIncrease)

        verifySequence {
            resources.getBoolean(R.bool.pref_first_start)
            resources.getInteger(R.integer.pref_app_theme)
            resources.getBoolean(R.bool.pref_backup_import_skip)
            resources.getInteger(R.integer.pref_note_sort)
            resources.getInteger(R.integer.pref_note_color)
            resources.getBoolean(R.bool.pref_note_save_pause)
            resources.getBoolean(R.bool.pref_note_save_auto)
            resources.getInteger(R.integer.pref_note_save_time)
            resources.getInteger(R.integer.pref_alarm_repeat)
            resources.getInteger(R.integer.pref_alarm_signal)
            resources.getString(R.string.pref_alarm_melody)
            resources.getInteger(R.integer.pref_alarm_volume)
            resources.getBoolean(R.bool.pref_alarm_increase)
        }
    }

}
package sgtmelon.scriptum.data.provider

import android.content.res.Resources
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.TestData
import kotlin.random.Random

/**
 * Test of [PreferenceProvider].
 */
class PreferenceProviderTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val badProviderKey by lazy { PreferenceProvider.Key(resources = null) }
    private val badProviderDef by lazy { PreferenceProvider.Def(resources = null) }

    private val goodProviderKey by lazy { PreferenceProvider.Key(resources) }
    private val goodProviderDef by lazy { PreferenceProvider.Def(resources) }

    @Test fun valueKey_bad() {
        assertNull(badProviderKey.FIRST_START)
        assertNull(badProviderKey.THEME)
        assertNull(badProviderKey.SORT)
        assertNull(badProviderKey.DEFAULT_COLOR)
        assertNull(badProviderKey.PAUSE_SAVE_ON)
        assertNull(badProviderKey.AUTO_SAVE_ON)
        assertNull(badProviderKey.SAVE_PERIOD)
        assertNull(badProviderKey.REPEAT)
        assertNull(badProviderKey.SIGNAL)
        assertNull(badProviderKey.MELODY_URI)
        assertNull(badProviderKey.VOLUME)
        assertNull(badProviderKey.VOLUME_INCREASE)
    }

    @Test fun valueKey_good() {
        val firstStart = TestData.uniqueString
        val theme = TestData.uniqueString
        val sort = TestData.uniqueString
        val defaultColor = TestData.uniqueString
        val pauseSaveOn = TestData.uniqueString
        val autoSaveOn = TestData.uniqueString
        val savePeriod = TestData.uniqueString
        val repeat = TestData.uniqueString
        val signal = TestData.uniqueString
        val melodyUri = TestData.uniqueString
        val volume = TestData.uniqueString
        val volumeIncrease = TestData.uniqueString

        every { resources.getString(R.string.pref_key_first_start) } returns firstStart
        every { resources.getString(R.string.pref_key_app_theme) } returns theme
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

        assertEquals(firstStart, goodProviderKey.FIRST_START)
        assertEquals(theme, goodProviderKey.THEME)
        assertEquals(sort, goodProviderKey.SORT)
        assertEquals(defaultColor, goodProviderKey.DEFAULT_COLOR)
        assertEquals(pauseSaveOn, goodProviderKey.PAUSE_SAVE_ON)
        assertEquals(autoSaveOn, goodProviderKey.AUTO_SAVE_ON)
        assertEquals(savePeriod, goodProviderKey.SAVE_PERIOD)
        assertEquals(repeat, goodProviderKey.REPEAT)
        assertEquals(signal, goodProviderKey.SIGNAL)
        assertEquals(melodyUri, goodProviderKey.MELODY_URI)
        assertEquals(volume, goodProviderKey.VOLUME)
        assertEquals(volumeIncrease, goodProviderKey.VOLUME_INCREASE)

        verifySequence {
            resources.getString(R.string.pref_key_first_start)
            resources.getString(R.string.pref_key_app_theme)
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

    @Test fun valueDef_bad() {
        assertNull(badProviderDef.FIRST_START)
        assertNull(badProviderDef.THEME)
        assertNull(badProviderDef.SORT)
        assertNull(badProviderDef.DEFAULT_COLOR)
        assertNull(badProviderDef.PAUSE_SAVE_ON)
        assertNull(badProviderDef.AUTO_SAVE_ON)
        assertNull(badProviderDef.SAVE_PERIOD)
        assertNull(badProviderDef.REPEAT)
        assertNull(badProviderDef.SIGNAL)
        assertNull(badProviderDef.MELODY_URI)
        assertNull(badProviderDef.VOLUME)
        assertNull(badProviderDef.VOLUME_INCREASE)
    }

    @Test fun valueDef_good() {
        val firstStart = Random.nextBoolean()
        val theme = Random.nextInt()
        val sort = Random.nextInt()
        val defaultColor = Random.nextInt()
        val pauseSaveOn = Random.nextBoolean()
        val autoSaveOn = Random.nextBoolean()
        val savePeriod = Random.nextInt()
        val repeat = Random.nextInt()
        val signal = Random.nextInt()
        val melodyUri = TestData.uniqueString
        val volume = Random.nextInt()
        val volumeIncrease = Random.nextBoolean()

        every { resources.getBoolean(R.bool.pref_first_start) } returns firstStart
        every { resources.getInteger(R.integer.pref_app_theme) } returns theme
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

        assertEquals(firstStart, goodProviderDef.FIRST_START)
        assertEquals(theme, goodProviderDef.THEME)
        assertEquals(sort, goodProviderDef.SORT)
        assertEquals(defaultColor, goodProviderDef.DEFAULT_COLOR)
        assertEquals(pauseSaveOn, goodProviderDef.PAUSE_SAVE_ON)
        assertEquals(autoSaveOn, goodProviderDef.AUTO_SAVE_ON)
        assertEquals(savePeriod, goodProviderDef.SAVE_PERIOD)
        assertEquals(repeat, goodProviderDef.REPEAT)
        assertEquals(signal, goodProviderDef.SIGNAL)
        assertEquals(melodyUri, goodProviderDef.MELODY_URI)
        assertEquals(volume, goodProviderDef.VOLUME)
        assertEquals(volumeIncrease, goodProviderDef.VOLUME_INCREASE)

        verifySequence {
            resources.getBoolean(R.bool.pref_first_start)
            resources.getInteger(R.integer.pref_app_theme)
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
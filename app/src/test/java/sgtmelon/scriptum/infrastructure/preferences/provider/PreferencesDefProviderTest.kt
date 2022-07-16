package sgtmelon.scriptum.infrastructure.preferences.provider

import android.content.res.Resources
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.parent.ParentTest
import kotlin.random.Random

/**
 * Test of [PreferencesDefProvider].
 */
class PreferencesDefProviderTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val providerDef by lazy { PreferencesDefProvider(resources) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(resources)
    }

    @Test fun `correct links`() {
        val isFirstStart = Random.nextBoolean()
        val theme = Random.nextInt()
        val isBackupSkipImports = Random.nextBoolean()
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
        val isDeveloper = Random.nextBoolean()

        every { resources.getBoolean(R.bool.pref_first_start) } returns isFirstStart
        every { resources.getInteger(R.integer.pref_app_theme) } returns theme
        every { resources.getBoolean(R.bool.pref_backup_import_skip) } returns isBackupSkipImports
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
        every { resources.getBoolean(R.bool.pref_other_develop) } returns isDeveloper

        assertEquals(isFirstStart, providerDef.isFirstStart)
        assertEquals(theme, providerDef.theme)
        assertEquals(isBackupSkipImports, providerDef.isBackupSkipImports)
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
        assertEquals(isDeveloper, providerDef.isDeveloper)

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
            resources.getBoolean(R.bool.pref_other_develop)
        }
    }
}
package sgtmelon.scriptum.infrastructure.preferences.provider

import android.content.res.Resources
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.parent.ParentTest

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
        val isPauseSaveOn = Random.nextBoolean()
        val isAutoSaveOn = Random.nextBoolean()
        val savePeriod = Random.nextInt()
        val repeat = Random.nextInt()
        val signal = nextString()
        val melodyUri = nextString()
        val volume = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()
        val isDeveloper = Random.nextBoolean()

        every { resources.getBoolean(R.bool.pref_first_start) } returns isFirstStart
        every { resources.getInteger(R.integer.pref_app_theme) } returns theme
        every { resources.getBoolean(R.bool.pref_backup_import_skip) } returns isBackupSkipImports
        every { resources.getInteger(R.integer.pref_note_sort) } returns sort
        every { resources.getInteger(R.integer.pref_note_color) } returns defaultColor
        every { resources.getBoolean(R.bool.pref_note_save_pause) } returns isPauseSaveOn
        every { resources.getBoolean(R.bool.pref_note_save_auto) } returns isAutoSaveOn
        every { resources.getInteger(R.integer.pref_note_save_time) } returns savePeriod
        every { resources.getInteger(R.integer.pref_alarm_repeat) } returns repeat
        every { resources.getString(R.string.pref_alarm_signal) } returns signal
        every { resources.getString(R.string.pref_alarm_melody) } returns melodyUri
        every { resources.getInteger(R.integer.pref_alarm_volume) } returns volume
        every { resources.getBoolean(R.bool.pref_alarm_increase) } returns isVolumeIncrease
        every { resources.getBoolean(R.bool.pref_other_develop) } returns isDeveloper

        assertEquals(isFirstStart, providerDef.isFirstStart)
        assertEquals(theme, providerDef.theme)
        assertEquals(isBackupSkipImports, providerDef.isBackupSkipImports)
        assertEquals(sort, providerDef.sort)
        assertEquals(defaultColor, providerDef.defaultColor)
        assertEquals(isPauseSaveOn, providerDef.isPauseSaveOn)
        assertEquals(isAutoSaveOn, providerDef.isAutoSaveOn)
        assertEquals(savePeriod, providerDef.savePeriod)
        assertEquals(repeat, providerDef.repeat)
        assertEquals(signal, providerDef.signal)
        assertEquals(melodyUri, providerDef.melodyUri)
        assertEquals(volume, providerDef.volume)
        assertEquals(isVolumeIncrease, providerDef.isVolumeIncrease)
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
            resources.getString(R.string.pref_alarm_signal)
            resources.getString(R.string.pref_alarm_melody)
            resources.getInteger(R.integer.pref_alarm_volume)
            resources.getBoolean(R.bool.pref_alarm_increase)
            resources.getBoolean(R.bool.pref_other_develop)
        }
    }
}
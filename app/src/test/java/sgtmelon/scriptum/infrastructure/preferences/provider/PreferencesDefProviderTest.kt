package sgtmelon.scriptum.infrastructure.preferences.provider

import android.content.res.Resources
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.test.common.nextString
import sgtmelon.tests.uniter.ParentTest
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
        val showNotificationsHelp = Random.nextBoolean()
        val theme = Random.nextInt()
        val isBackupSkip = Random.nextBoolean()
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

        with(resources) {
            every { getBoolean(R.bool.pref_first_start) } returns isFirstStart
            every { getBoolean(R.bool.pref_notifications_help) } returns showNotificationsHelp
            every { getInteger(R.integer.pref_app_theme) } returns theme
            every { getBoolean(R.bool.pref_backup_skip) } returns isBackupSkip
            every { getInteger(R.integer.pref_note_sort) } returns sort
            every { getInteger(R.integer.pref_note_color) } returns defaultColor
            every { getBoolean(R.bool.pref_note_pause_save) } returns isPauseSaveOn
            every { getBoolean(R.bool.pref_note_auto_save) } returns isAutoSaveOn
            every { getInteger(R.integer.pref_note_save_period) } returns savePeriod
            every { getInteger(R.integer.pref_alarm_repeat) } returns repeat
            every { getString(R.string.pref_alarm_signal) } returns signal
            every { getString(R.string.pref_alarm_melody) } returns melodyUri
            every { getInteger(R.integer.pref_alarm_volume) } returns volume
            every { getBoolean(R.bool.pref_alarm_increase) } returns isVolumeIncrease
            every { getBoolean(R.bool.pref_developer) } returns isDeveloper
        }

        assertEquals(isFirstStart, providerDef.isFirstStart)
        assertEquals(showNotificationsHelp, providerDef.showNotificationsHelp)
        assertEquals(theme, providerDef.theme)
        assertEquals(isBackupSkip, providerDef.isBackupSkip)
        assertEquals(sort, providerDef.sort)
        assertEquals(defaultColor, providerDef.defaultColor)
        assertEquals(isPauseSaveOn, providerDef.isPauseSaveOn)
        assertEquals(isAutoSaveOn, providerDef.isAutoSaveOn)
        assertEquals(savePeriod, providerDef.savePeriod)
        assertEquals(repeat, providerDef.repeat)
        assertEquals(signal, providerDef.signal)
        assertEquals(melodyUri, providerDef.melodyUri)
        assertEquals(volume, providerDef.volumePercent)
        assertEquals(isVolumeIncrease, providerDef.isVolumeIncrease)
        assertEquals(isDeveloper, providerDef.isDeveloper)

        verifySequence {
            resources.getBoolean(R.bool.pref_first_start)
            resources.getBoolean(R.bool.pref_notifications_help)
            resources.getInteger(R.integer.pref_app_theme)
            resources.getBoolean(R.bool.pref_backup_skip)
            resources.getInteger(R.integer.pref_note_sort)
            resources.getInteger(R.integer.pref_note_color)
            resources.getBoolean(R.bool.pref_note_pause_save)
            resources.getBoolean(R.bool.pref_note_auto_save)
            resources.getInteger(R.integer.pref_note_save_period)
            resources.getInteger(R.integer.pref_alarm_repeat)
            resources.getString(R.string.pref_alarm_signal)
            resources.getString(R.string.pref_alarm_melody)
            resources.getInteger(R.integer.pref_alarm_volume)
            resources.getBoolean(R.bool.pref_alarm_increase)
            resources.getBoolean(R.bool.pref_developer)
        }
    }
}
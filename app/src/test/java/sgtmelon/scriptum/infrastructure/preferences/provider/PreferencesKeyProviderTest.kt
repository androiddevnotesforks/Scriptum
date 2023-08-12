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

/**
 * Test of [PreferencesKeyProvider].
 */
class PreferencesKeyProviderTest : ParentTest() {

    @MockK lateinit var resources: Resources

    private val providerKey by lazy { PreferencesKeyProvider(resources) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(resources)
    }

    @Test fun `correct links`() {
        val isFirstStart = nextString()
        val showNotificationsHelp = nextString()
        val permissionHistory = nextString()
        val theme = nextString()
        val isBackupSkip = nextString()
        val sort = nextString()
        val defaultColor = nextString()
        val isPauseSaveOn = nextString()
        val isAutoSaveOn = nextString()
        val savePeriod = nextString()
        val repeat = nextString()
        val signal = nextString()
        val melodyUri = nextString()
        val volume = nextString()
        val isVolumeIncrease = nextString()
        val isDeveloper = nextString()

        with(resources) {
            every { getString(R.string.pref_key_first_start) } returns isFirstStart
            every { getString(R.string.pref_key_notifications_help) } returns showNotificationsHelp
            every { getString(R.string.pref_key_permission_history) } returns permissionHistory
            every { getString(R.string.pref_key_theme) } returns theme
            every { getString(R.string.pref_key_backup_skip) } returns isBackupSkip
            every { getString(R.string.pref_key_note_sort) } returns sort
            every { getString(R.string.pref_key_note_color) } returns defaultColor
            every { getString(R.string.pref_key_note_pause_save) } returns isPauseSaveOn
            every { getString(R.string.pref_key_note_auto_save) } returns isAutoSaveOn
            every { getString(R.string.pref_key_note_save_period) } returns savePeriod
            every { getString(R.string.pref_key_alarm_repeat) } returns repeat
            every { getString(R.string.pref_key_alarm_signal) } returns signal
            every { getString(R.string.pref_key_alarm_melody) } returns melodyUri
            every { getString(R.string.pref_key_alarm_volume) } returns volume
            every { getString(R.string.pref_key_alarm_increase) } returns isVolumeIncrease
            every { getString(R.string.pref_key_developer) } returns isDeveloper
        }

        assertEquals(isFirstStart, providerKey.isFirstStart)
        assertEquals(showNotificationsHelp, providerKey.showNotificationsHelp)
        assertEquals(permissionHistory, providerKey.permissionHistory)
        assertEquals(theme, providerKey.theme)
        assertEquals(isBackupSkip, providerKey.isBackupSkip)
        assertEquals(sort, providerKey.sort)
        assertEquals(defaultColor, providerKey.defaultColor)
        assertEquals(isPauseSaveOn, providerKey.isPauseSaveOn)
        assertEquals(isAutoSaveOn, providerKey.isAutoSaveOn)
        assertEquals(savePeriod, providerKey.savePeriod)
        assertEquals(repeat, providerKey.repeat)
        assertEquals(signal, providerKey.signal)
        assertEquals(melodyUri, providerKey.melodyUri)
        assertEquals(volume, providerKey.volumePercent)
        assertEquals(isVolumeIncrease, providerKey.isVolumeIncrease)
        assertEquals(isDeveloper, providerKey.isDeveloper)

        verifySequence {
            resources.getString(R.string.pref_key_first_start)
            resources.getString(R.string.pref_key_notifications_help)
            resources.getString(R.string.pref_key_permission_history)
            resources.getString(R.string.pref_key_theme)
            resources.getString(R.string.pref_key_backup_skip)
            resources.getString(R.string.pref_key_note_sort)
            resources.getString(R.string.pref_key_note_color)
            resources.getString(R.string.pref_key_note_pause_save)
            resources.getString(R.string.pref_key_note_auto_save)
            resources.getString(R.string.pref_key_note_save_period)
            resources.getString(R.string.pref_key_alarm_repeat)
            resources.getString(R.string.pref_key_alarm_signal)
            resources.getString(R.string.pref_key_alarm_melody)
            resources.getString(R.string.pref_key_alarm_volume)
            resources.getString(R.string.pref_key_alarm_increase)
            resources.getString(R.string.pref_key_developer)
        }
    }
}
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
import sgtmelon.scriptum.cleanup.parent.ParentTest

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
        val theme = nextString()
        val isBackupSkipImports = nextString()
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

        every { resources.getString(R.string.pref_key_first_start) } returns isFirstStart
        every { resources.getString(R.string.pref_key_app_theme) } returns theme
        every { resources.getString(R.string.pref_key_backup_skip) } returns isBackupSkipImports
        every { resources.getString(R.string.pref_key_note_sort) } returns sort
        every { resources.getString(R.string.pref_key_note_color) } returns defaultColor
        every { resources.getString(R.string.pref_key_note_pause) } returns isPauseSaveOn
        every { resources.getString(R.string.pref_key_note_auto) } returns isAutoSaveOn
        every { resources.getString(R.string.pref_key_note_time) } returns savePeriod
        every { resources.getString(R.string.pref_key_alarm_repeat) } returns repeat
        every { resources.getString(R.string.pref_key_alarm_signal) } returns signal
        every { resources.getString(R.string.pref_key_alarm_melody) } returns melodyUri
        every { resources.getString(R.string.pref_key_alarm_volume) } returns volume
        every { resources.getString(R.string.pref_key_alarm_increase) } returns isVolumeIncrease
        every { resources.getString(R.string.pref_key_other_develop) } returns isDeveloper

        assertEquals(isFirstStart, providerKey.isFirstStart)
        assertEquals(theme, providerKey.theme)
        assertEquals(isBackupSkipImports, providerKey.isBackupSkipImports)
        assertEquals(sort, providerKey.sort)
        assertEquals(defaultColor, providerKey.defaultColor)
        assertEquals(isPauseSaveOn, providerKey.isPauseSaveOn)
        assertEquals(isAutoSaveOn, providerKey.isAutoSaveOn)
        assertEquals(savePeriod, providerKey.savePeriod)
        assertEquals(repeat, providerKey.repeat)
        assertEquals(signal, providerKey.signal)
        assertEquals(melodyUri, providerKey.melodyUri)
        assertEquals(volume, providerKey.volume)
        assertEquals(isVolumeIncrease, providerKey.isVolumeIncrease)
        assertEquals(isDeveloper, providerKey.isDeveloper)

        verifySequence {
            resources.getString(R.string.pref_key_first_start)
            resources.getString(R.string.pref_key_app_theme)
            resources.getString(R.string.pref_key_backup_skip)
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
            resources.getString(R.string.pref_key_other_develop)
        }
    }
}
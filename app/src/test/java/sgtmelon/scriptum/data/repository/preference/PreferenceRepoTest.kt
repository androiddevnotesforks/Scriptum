package sgtmelon.scriptum.data.repository.preference

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.data.provider.PreferenceProvider
import kotlin.random.Random

/**
 * Test for [PreferenceRepo].
 */
class PreferenceRepoTest : ParentTest() {

    @MockK lateinit var keyProvider: PreferenceProvider.Key
    @MockK lateinit var defProvider: PreferenceProvider.Def

    @MockK lateinit var preferences: SharedPreferences
    @MockK lateinit var preferencesEditor: SharedPreferences.Editor

    private val preferenceRepo by lazy { PreferenceRepo(keyProvider, defProvider, preferences) }

    override fun setUp() {
        super.setUp()

        every { preferences.edit() } returns preferencesEditor

        every { preferencesEditor.putBoolean(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putInt(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putString(any(), any()) } returns preferencesEditor
        every { preferencesEditor.clear() } returns preferencesEditor
    }

    @Test fun getFirstStart() {
        val keyValue = Random.nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.FIRST_START } returns keyValue
        every { defProvider.FIRST_START } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.firstStart)

        verifySequence {
            keyProvider.FIRST_START
            defProvider.FIRST_START

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setFirstStart() {
        val keyValue = Random.nextString()
        val value = Random.nextBoolean()

        every { keyProvider.FIRST_START } returns keyValue
        preferenceRepo.firstStart = value

        verifySequence {
            preferences.edit()
            keyProvider.FIRST_START
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getTheme() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.THEME } returns keyValue
        every { defProvider.THEME } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.theme)

        verifySequence {
            keyProvider.THEME
            defProvider.THEME

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setTheme() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.THEME } returns keyValue
        preferenceRepo.theme = value

        verifySequence {
            preferences.edit()
            keyProvider.THEME
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getSort() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SORT } returns keyValue
        every { defProvider.SORT } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.sort)

        verifySequence {
            keyProvider.SORT
            defProvider.SORT

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSort() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.SORT } returns keyValue
        preferenceRepo.sort = value

        verifySequence {
            preferences.edit()
            keyProvider.SORT
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getDefaultColor() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        every { defProvider.DEFAULT_COLOR } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.defaultColor)

        verifySequence {
            keyProvider.DEFAULT_COLOR
            defProvider.DEFAULT_COLOR

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setDefaultColor() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        preferenceRepo.defaultColor = value

        verifySequence {
            preferences.edit()
            keyProvider.DEFAULT_COLOR
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getPauseSaveOn() {
        val keyValue = Random.nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        every { defProvider.PAUSE_SAVE_ON } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.pauseSaveOn)

        verifySequence {
            keyProvider.PAUSE_SAVE_ON
            defProvider.PAUSE_SAVE_ON

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setPauseSaveOn() {
        val keyValue = Random.nextString()
        val value = Random.nextBoolean()

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        preferenceRepo.pauseSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.PAUSE_SAVE_ON
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getAutoSaveOn() {
        val keyValue = Random.nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        every { defProvider.AUTO_SAVE_ON } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.autoSaveOn)

        verifySequence {
            keyProvider.AUTO_SAVE_ON
            defProvider.AUTO_SAVE_ON

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setAutoSaveOn() {
        val keyValue = Random.nextString()
        val value = Random.nextBoolean()

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        preferenceRepo.autoSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.AUTO_SAVE_ON
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSavePeriod() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SAVE_PERIOD } returns keyValue
        every { defProvider.SAVE_PERIOD } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.savePeriod)

        verifySequence {
            keyProvider.SAVE_PERIOD
            defProvider.SAVE_PERIOD

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSavePeriod() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.SAVE_PERIOD } returns keyValue
        preferenceRepo.savePeriod = value

        verifySequence {
            preferences.edit()
            keyProvider.SAVE_PERIOD
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getRepeat() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.REPEAT } returns keyValue
        every { defProvider.REPEAT } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.repeat)

        verifySequence {
            keyProvider.REPEAT
            defProvider.REPEAT

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setRepeat() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.REPEAT } returns keyValue
        preferenceRepo.repeat = value

        verifySequence {
            preferences.edit()
            keyProvider.REPEAT
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSignal() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SIGNAL } returns keyValue
        every { defProvider.SIGNAL } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.signal)

        verifySequence {
            keyProvider.SIGNAL
            defProvider.SIGNAL

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSignal() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.SIGNAL } returns keyValue
        preferenceRepo.signal = value

        verifySequence {
            preferences.edit()
            keyProvider.SIGNAL
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getMelodyUri() {
        val keyValue = Random.nextString()
        val defValue = Random.nextString()
        val value = Random.nextString()

        every { keyProvider.MELODY_URI } returns keyValue
        every { defProvider.MELODY_URI } returns defValue
        every { preferences.getString(keyValue, defValue) } returns null
        assertEquals(defValue, preferenceRepo.melodyUri)

        every { preferences.getString(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.melodyUri)

        verifySequence {
            keyProvider.MELODY_URI
            defProvider.MELODY_URI
            preferences.getString(keyValue, defValue)
            defProvider.MELODY_URI

            keyProvider.MELODY_URI
            defProvider.MELODY_URI
            preferences.getString(keyValue, defValue)
        }
    }

    @Test fun setMelodyUri() {
        val keyValue = Random.nextString()
        val value = Random.nextString()

        every { keyProvider.MELODY_URI } returns keyValue
        preferenceRepo.melodyUri = value

        verifySequence {
            preferences.edit()
            keyProvider.MELODY_URI
            preferencesEditor.putString(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolume() {
        val keyValue = Random.nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.VOLUME } returns keyValue
        every { defProvider.VOLUME } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.volume)

        verifySequence {
            keyProvider.VOLUME
            defProvider.VOLUME

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setVolume() {
        val keyValue = Random.nextString()
        val value = Random.nextInt()

        every { keyProvider.VOLUME } returns keyValue
        preferenceRepo.volume = value

        verifySequence {
            preferences.edit()
            keyProvider.VOLUME
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolumeIncrease() {
        val keyValue = Random.nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        every { defProvider.VOLUME_INCREASE } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.volumeIncrease)

        verifySequence {
            keyProvider.VOLUME_INCREASE
            defProvider.VOLUME_INCREASE

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setVolumeIncrease() {
        val keyValue = Random.nextString()
        val value = Random.nextBoolean()

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        preferenceRepo.volumeIncrease = value

        verifySequence {
            preferences.edit()
            keyProvider.VOLUME_INCREASE
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun clear() {
        preferenceRepo.clear()

        verifySequence {
            preferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
        }
    }

}
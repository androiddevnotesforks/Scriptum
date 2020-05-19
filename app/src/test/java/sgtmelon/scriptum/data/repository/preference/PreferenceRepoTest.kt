package sgtmelon.scriptum.data.repository.preference

import android.content.SharedPreferences
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.ParentTest
import sgtmelon.scriptum.TestData
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

    private val badPreferenceRepo by lazy { PreferenceRepo(keyProvider, defProvider, preferences = null) }
    private val goodPreferenceRepo by lazy { PreferenceRepo(keyProvider, defProvider, preferences) }

    override fun setUp() {
        super.setUp()

        every { preferences.edit() } returns preferencesEditor

        every { preferencesEditor.putBoolean(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putInt(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putString(any(), any()) } returns preferencesEditor
        every { preferencesEditor.clear() } returns preferencesEditor
    }

    @Test fun getFirstStart() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.FIRST_START } returns keyValue
        every { defProvider.FIRST_START } returns defValue
        assertNull(badPreferenceRepo.firstStart)

        every { keyProvider.FIRST_START } returns null
        every { defProvider.FIRST_START } returns null
        assertNull(goodPreferenceRepo.firstStart)

        every { keyProvider.FIRST_START } returns keyValue
        every { defProvider.FIRST_START } returns null
        assertNull(goodPreferenceRepo.firstStart)

        every { keyProvider.FIRST_START } returns null
        every { defProvider.FIRST_START } returns defValue
        assertNull(goodPreferenceRepo.firstStart)

        every { keyProvider.FIRST_START } returns keyValue
        every { defProvider.FIRST_START } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.firstStart)

        verifySequence {
            repeat(times = 5) {
                keyProvider.FIRST_START
                defProvider.FIRST_START
            }

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setFirstStart() {
        val keyValue = TestData.uniqueString
        val value = Random.nextBoolean()

        every { keyProvider.FIRST_START } returns keyValue
        badPreferenceRepo.firstStart = value

        every { keyProvider.FIRST_START } returns null
        goodPreferenceRepo.firstStart = null

        every { keyProvider.FIRST_START } returns keyValue
        goodPreferenceRepo.firstStart = null

        every { keyProvider.FIRST_START } returns null
        goodPreferenceRepo.firstStart = value

        every { keyProvider.FIRST_START } returns keyValue
        goodPreferenceRepo.firstStart = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.FIRST_START
            }

            preferences.edit()
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getTheme() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.THEME } returns keyValue
        every { defProvider.THEME } returns defValue
        assertNull(badPreferenceRepo.theme)

        every { keyProvider.THEME } returns null
        every { defProvider.THEME } returns null
        assertNull(goodPreferenceRepo.theme)

        every { keyProvider.THEME } returns keyValue
        every { defProvider.THEME } returns null
        assertNull(goodPreferenceRepo.theme)

        every { keyProvider.THEME } returns null
        every { defProvider.THEME } returns defValue
        assertNull(goodPreferenceRepo.theme)

        every { keyProvider.THEME } returns keyValue
        every { defProvider.THEME } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.theme)

        verifySequence {
            repeat(times = 5) {
                keyProvider.THEME
                defProvider.THEME
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setTheme() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.THEME } returns keyValue
        badPreferenceRepo.theme = value

        every { keyProvider.THEME } returns null
        goodPreferenceRepo.theme = null

        every { keyProvider.THEME } returns keyValue
        goodPreferenceRepo.theme = null

        every { keyProvider.THEME } returns null
        goodPreferenceRepo.theme = value

        every { keyProvider.THEME } returns keyValue
        goodPreferenceRepo.theme = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.THEME
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getSort() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SORT } returns keyValue
        every { defProvider.SORT } returns defValue
        assertNull(badPreferenceRepo.sort)

        every { keyProvider.SORT } returns null
        every { defProvider.SORT } returns null
        assertNull(goodPreferenceRepo.sort)

        every { keyProvider.SORT } returns keyValue
        every { defProvider.SORT } returns null
        assertNull(goodPreferenceRepo.sort)

        every { keyProvider.SORT } returns null
        every { defProvider.SORT } returns defValue
        assertNull(goodPreferenceRepo.sort)

        every { keyProvider.SORT } returns keyValue
        every { defProvider.SORT } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.sort)

        verifySequence {
            repeat(times = 5) {
                keyProvider.SORT
                defProvider.SORT
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSort() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.SORT } returns keyValue
        badPreferenceRepo.sort = value

        every { keyProvider.SORT } returns null
        goodPreferenceRepo.sort = null

        every { keyProvider.SORT } returns keyValue
        goodPreferenceRepo.sort = null

        every { keyProvider.SORT } returns null
        goodPreferenceRepo.sort = value

        every { keyProvider.SORT } returns keyValue
        goodPreferenceRepo.sort = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.SORT
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getDefaultColor() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        every { defProvider.DEFAULT_COLOR } returns defValue
        assertNull(badPreferenceRepo.defaultColor)

        every { keyProvider.DEFAULT_COLOR } returns null
        every { defProvider.DEFAULT_COLOR } returns null
        assertNull(goodPreferenceRepo.defaultColor)

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        every { defProvider.DEFAULT_COLOR } returns null
        assertNull(goodPreferenceRepo.defaultColor)

        every { keyProvider.DEFAULT_COLOR } returns null
        every { defProvider.DEFAULT_COLOR } returns defValue
        assertNull(goodPreferenceRepo.defaultColor)

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        every { defProvider.DEFAULT_COLOR } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.defaultColor)

        verifySequence {
            repeat(times = 5) {
                keyProvider.DEFAULT_COLOR
                defProvider.DEFAULT_COLOR
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setDefaultColor() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        badPreferenceRepo.defaultColor = value

        every { keyProvider.DEFAULT_COLOR } returns null
        goodPreferenceRepo.defaultColor = null

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        goodPreferenceRepo.defaultColor = null

        every { keyProvider.DEFAULT_COLOR } returns null
        goodPreferenceRepo.defaultColor = value

        every { keyProvider.DEFAULT_COLOR } returns keyValue
        goodPreferenceRepo.defaultColor = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.DEFAULT_COLOR
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getPauseSaveOn() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        every { defProvider.PAUSE_SAVE_ON } returns defValue
        assertNull(badPreferenceRepo.pauseSaveOn)

        every { keyProvider.PAUSE_SAVE_ON } returns null
        every { defProvider.PAUSE_SAVE_ON } returns null
        assertNull(goodPreferenceRepo.pauseSaveOn)

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        every { defProvider.PAUSE_SAVE_ON } returns null
        assertNull(goodPreferenceRepo.pauseSaveOn)

        every { keyProvider.PAUSE_SAVE_ON } returns null
        every { defProvider.PAUSE_SAVE_ON } returns defValue
        assertNull(goodPreferenceRepo.pauseSaveOn)

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        every { defProvider.PAUSE_SAVE_ON } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.pauseSaveOn)

        verifySequence {
            repeat(times = 5) {
                keyProvider.PAUSE_SAVE_ON
                defProvider.PAUSE_SAVE_ON
            }

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setPauseSaveOn() {
        val keyValue = TestData.uniqueString
        val value = Random.nextBoolean()

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        badPreferenceRepo.pauseSaveOn = value

        every { keyProvider.PAUSE_SAVE_ON } returns null
        goodPreferenceRepo.pauseSaveOn = null

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        goodPreferenceRepo.pauseSaveOn = null

        every { keyProvider.PAUSE_SAVE_ON } returns null
        goodPreferenceRepo.pauseSaveOn = value

        every { keyProvider.PAUSE_SAVE_ON } returns keyValue
        goodPreferenceRepo.pauseSaveOn = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.PAUSE_SAVE_ON
            }

            preferences.edit()
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getAutoSaveOn() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        every { defProvider.AUTO_SAVE_ON } returns defValue
        assertNull(badPreferenceRepo.autoSaveOn)

        every { keyProvider.AUTO_SAVE_ON } returns null
        every { defProvider.AUTO_SAVE_ON } returns null
        assertNull(goodPreferenceRepo.autoSaveOn)

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        every { defProvider.AUTO_SAVE_ON } returns null
        assertNull(goodPreferenceRepo.autoSaveOn)

        every { keyProvider.AUTO_SAVE_ON } returns null
        every { defProvider.AUTO_SAVE_ON } returns defValue
        assertNull(goodPreferenceRepo.autoSaveOn)

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        every { defProvider.AUTO_SAVE_ON } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.autoSaveOn)

        verifySequence {
            repeat(times = 5) {
                keyProvider.AUTO_SAVE_ON
                defProvider.AUTO_SAVE_ON
            }

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setAutoSaveOn() {
        val keyValue = TestData.uniqueString
        val value = Random.nextBoolean()

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        badPreferenceRepo.autoSaveOn = value

        every { keyProvider.AUTO_SAVE_ON } returns null
        goodPreferenceRepo.autoSaveOn = null

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        goodPreferenceRepo.autoSaveOn = null

        every { keyProvider.AUTO_SAVE_ON } returns null
        goodPreferenceRepo.autoSaveOn = value

        every { keyProvider.AUTO_SAVE_ON } returns keyValue
        goodPreferenceRepo.autoSaveOn = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.AUTO_SAVE_ON
            }

            preferences.edit()
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSavePeriod() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SAVE_PERIOD } returns keyValue
        every { defProvider.SAVE_PERIOD } returns defValue
        assertNull(badPreferenceRepo.savePeriod)

        every { keyProvider.SAVE_PERIOD } returns null
        every { defProvider.SAVE_PERIOD } returns null
        assertNull(goodPreferenceRepo.savePeriod)

        every { keyProvider.SAVE_PERIOD } returns keyValue
        every { defProvider.SAVE_PERIOD } returns null
        assertNull(goodPreferenceRepo.savePeriod)

        every { keyProvider.SAVE_PERIOD } returns null
        every { defProvider.SAVE_PERIOD } returns defValue
        assertNull(goodPreferenceRepo.savePeriod)

        every { keyProvider.SAVE_PERIOD } returns keyValue
        every { defProvider.SAVE_PERIOD } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.savePeriod)

        verifySequence {
            repeat(times = 5) {
                keyProvider.SAVE_PERIOD
                defProvider.SAVE_PERIOD
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSavePeriod() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.SAVE_PERIOD } returns keyValue
        badPreferenceRepo.savePeriod = value

        every { keyProvider.SAVE_PERIOD } returns null
        goodPreferenceRepo.savePeriod = null

        every { keyProvider.SAVE_PERIOD } returns keyValue
        goodPreferenceRepo.savePeriod = null

        every { keyProvider.SAVE_PERIOD } returns null
        goodPreferenceRepo.savePeriod = value

        every { keyProvider.SAVE_PERIOD } returns keyValue
        goodPreferenceRepo.savePeriod = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.SAVE_PERIOD
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getRepeat() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.REPEAT } returns keyValue
        every { defProvider.REPEAT } returns defValue
        assertNull(badPreferenceRepo.repeat)

        every { keyProvider.REPEAT } returns null
        every { defProvider.REPEAT } returns null
        assertNull(goodPreferenceRepo.repeat)

        every { keyProvider.REPEAT } returns keyValue
        every { defProvider.REPEAT } returns null
        assertNull(goodPreferenceRepo.repeat)

        every { keyProvider.REPEAT } returns null
        every { defProvider.REPEAT } returns defValue
        assertNull(goodPreferenceRepo.repeat)

        every { keyProvider.REPEAT } returns keyValue
        every { defProvider.REPEAT } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.repeat)

        verifySequence {
            repeat(times = 5) {
                keyProvider.REPEAT
                defProvider.REPEAT
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setRepeat() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.REPEAT } returns keyValue
        badPreferenceRepo.repeat = value

        every { keyProvider.REPEAT } returns null
        goodPreferenceRepo.repeat = null

        every { keyProvider.REPEAT } returns keyValue
        goodPreferenceRepo.repeat = null

        every { keyProvider.REPEAT } returns null
        goodPreferenceRepo.repeat = value

        every { keyProvider.REPEAT } returns keyValue
        goodPreferenceRepo.repeat = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.REPEAT
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSignal() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.SIGNAL } returns keyValue
        every { defProvider.SIGNAL } returns defValue
        assertNull(badPreferenceRepo.signal)

        every { keyProvider.SIGNAL } returns null
        every { defProvider.SIGNAL } returns null
        assertNull(goodPreferenceRepo.signal)

        every { keyProvider.SIGNAL } returns keyValue
        every { defProvider.SIGNAL } returns null
        assertNull(goodPreferenceRepo.signal)

        every { keyProvider.SIGNAL } returns null
        every { defProvider.SIGNAL } returns defValue
        assertNull(goodPreferenceRepo.signal)

        every { keyProvider.SIGNAL } returns keyValue
        every { defProvider.SIGNAL } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.signal)

        verifySequence {
            repeat(times = 5) {
                keyProvider.SIGNAL
                defProvider.SIGNAL
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSignal() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.SIGNAL } returns keyValue
        badPreferenceRepo.signal = value

        every { keyProvider.SIGNAL } returns null
        goodPreferenceRepo.signal = null

        every { keyProvider.SIGNAL } returns keyValue
        goodPreferenceRepo.signal = null

        every { keyProvider.SIGNAL } returns null
        goodPreferenceRepo.signal = value

        every { keyProvider.SIGNAL } returns keyValue
        goodPreferenceRepo.signal = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.SIGNAL
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getMelodyUri() {
        val keyValue = TestData.uniqueString
        val defValue = TestData.uniqueString
        val value = TestData.uniqueString

        every { keyProvider.MELODY_URI } returns keyValue
        every { defProvider.MELODY_URI } returns defValue
        assertNull(badPreferenceRepo.melodyUri)

        every { keyProvider.MELODY_URI } returns null
        every { defProvider.MELODY_URI } returns null
        assertNull(goodPreferenceRepo.melodyUri)

        every { keyProvider.MELODY_URI } returns keyValue
        every { defProvider.MELODY_URI } returns null
        assertNull(goodPreferenceRepo.melodyUri)

        every { keyProvider.MELODY_URI } returns null
        every { defProvider.MELODY_URI } returns defValue
        assertNull(goodPreferenceRepo.melodyUri)

        every { keyProvider.MELODY_URI } returns keyValue
        every { defProvider.MELODY_URI } returns defValue
        every { preferences.getString(keyValue, defValue) } returns null
        assertEquals(defValue, goodPreferenceRepo.melodyUri)

        every { preferences.getString(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.melodyUri)

        verifySequence {
            repeat(times = 5) {
                keyProvider.MELODY_URI
                defProvider.MELODY_URI
            }

            preferences.getString(keyValue, defValue)

            keyProvider.MELODY_URI
            defProvider.MELODY_URI
            preferences.getString(keyValue, defValue)
        }
    }

    @Test fun setMelodyUri() {
        val keyValue = TestData.uniqueString
        val value = TestData.uniqueString

        every { keyProvider.MELODY_URI } returns keyValue
        badPreferenceRepo.melodyUri = value

        every { keyProvider.MELODY_URI } returns null
        goodPreferenceRepo.melodyUri = null

        every { keyProvider.MELODY_URI } returns keyValue
        goodPreferenceRepo.melodyUri = null

        every { keyProvider.MELODY_URI } returns null
        goodPreferenceRepo.melodyUri = value

        every { keyProvider.MELODY_URI } returns keyValue
        goodPreferenceRepo.melodyUri = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.MELODY_URI
            }

            preferences.edit()
            preferencesEditor.putString(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolume() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.VOLUME } returns keyValue
        every { defProvider.VOLUME } returns defValue
        assertNull(badPreferenceRepo.volume)

        every { keyProvider.VOLUME } returns null
        every { defProvider.VOLUME } returns null
        assertNull(goodPreferenceRepo.volume)

        every { keyProvider.VOLUME } returns keyValue
        every { defProvider.VOLUME } returns null
        assertNull(goodPreferenceRepo.volume)

        every { keyProvider.VOLUME } returns null
        every { defProvider.VOLUME } returns defValue
        assertNull(goodPreferenceRepo.volume)

        every { keyProvider.VOLUME } returns keyValue
        every { defProvider.VOLUME } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.volume)

        verifySequence {
            repeat(times = 5) {
                keyProvider.VOLUME
                defProvider.VOLUME
            }

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setVolume() {
        val keyValue = TestData.uniqueString
        val value = Random.nextInt()

        every { keyProvider.VOLUME } returns keyValue
        badPreferenceRepo.volume = value

        every { keyProvider.VOLUME } returns null
        goodPreferenceRepo.volume = null

        every { keyProvider.VOLUME } returns keyValue
        goodPreferenceRepo.volume = null

        every { keyProvider.VOLUME } returns null
        goodPreferenceRepo.volume = value

        every { keyProvider.VOLUME } returns keyValue
        goodPreferenceRepo.volume = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.VOLUME
            }

            preferences.edit()
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolumeIncrease() {
        val keyValue = TestData.uniqueString
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        every { defProvider.VOLUME_INCREASE } returns defValue
        assertNull(badPreferenceRepo.volumeIncrease)

        every { keyProvider.VOLUME_INCREASE } returns null
        every { defProvider.VOLUME_INCREASE } returns null
        assertNull(goodPreferenceRepo.volumeIncrease)

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        every { defProvider.VOLUME_INCREASE } returns null
        assertNull(goodPreferenceRepo.volumeIncrease)

        every { keyProvider.VOLUME_INCREASE } returns null
        every { defProvider.VOLUME_INCREASE } returns defValue
        assertNull(goodPreferenceRepo.volumeIncrease)

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        every { defProvider.VOLUME_INCREASE } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, goodPreferenceRepo.volumeIncrease)

        verifySequence {
            repeat(times = 5) {
                keyProvider.VOLUME_INCREASE
                defProvider.VOLUME_INCREASE
            }

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setVolumeIncrease() {
        val keyValue = TestData.uniqueString
        val value = Random.nextBoolean()

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        badPreferenceRepo.volumeIncrease = value

        every { keyProvider.VOLUME_INCREASE } returns null
        goodPreferenceRepo.volumeIncrease = null

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        goodPreferenceRepo.volumeIncrease = null

        every { keyProvider.VOLUME_INCREASE } returns null
        goodPreferenceRepo.volumeIncrease = value

        every { keyProvider.VOLUME_INCREASE } returns keyValue
        goodPreferenceRepo.volumeIncrease = value

        verifySequence {
            repeat(times = 5) {
                keyProvider.VOLUME_INCREASE
            }

            preferences.edit()
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun clear() {
        badPreferenceRepo.clear()
        goodPreferenceRepo.clear()

        verifySequence {
            preferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
        }
    }

}
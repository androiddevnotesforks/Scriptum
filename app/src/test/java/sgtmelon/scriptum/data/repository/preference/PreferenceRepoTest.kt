package sgtmelon.scriptum.data.repository.preference

import android.content.SharedPreferences
import io.mockk.confirmVerified
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

    override fun tearDown() {
        super.tearDown()
        confirmVerified(keyProvider, defProvider, preferences, preferencesEditor)
    }

    @Test fun getFirstStart() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.firstStart } returns keyValue
        every { defProvider.firstStart } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.firstStart)

        verifySequence {
            keyProvider.firstStart
            defProvider.firstStart

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setFirstStart() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.firstStart } returns keyValue
        preferenceRepo.firstStart = value

        verifySequence {
            preferences.edit()
            keyProvider.firstStart
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getTheme() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.theme } returns keyValue
        every { defProvider.theme } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.theme)

        verifySequence {
            keyProvider.theme
            defProvider.theme

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setTheme() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.theme } returns keyValue
        preferenceRepo.theme = value

        verifySequence {
            preferences.edit()
            keyProvider.theme
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getImportSkip() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.importSkip } returns keyValue
        every { defProvider.importSkip } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.importSkip)

        verifySequence {
            keyProvider.importSkip
            defProvider.importSkip

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setImportSkip() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.importSkip } returns keyValue
        preferenceRepo.importSkip = value

        verifySequence {
            preferences.edit()
            keyProvider.importSkip
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getSort() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.sort } returns keyValue
        every { defProvider.sort } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.sort)

        verifySequence {
            keyProvider.sort
            defProvider.sort

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSort() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.sort } returns keyValue
        preferenceRepo.sort = value

        verifySequence {
            preferences.edit()
            keyProvider.sort
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getDefaultColor() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.defaultColor } returns keyValue
        every { defProvider.defaultColor } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.defaultColor)

        verifySequence {
            keyProvider.defaultColor
            defProvider.defaultColor

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setDefaultColor() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.defaultColor } returns keyValue
        preferenceRepo.defaultColor = value

        verifySequence {
            preferences.edit()
            keyProvider.defaultColor
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getPauseSaveOn() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.pauseSaveOn } returns keyValue
        every { defProvider.pauseSaveOn } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.pauseSaveOn)

        verifySequence {
            keyProvider.pauseSaveOn
            defProvider.pauseSaveOn

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setPauseSaveOn() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.pauseSaveOn } returns keyValue
        preferenceRepo.pauseSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.pauseSaveOn
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getAutoSaveOn() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.autoSaveOn } returns keyValue
        every { defProvider.autoSaveOn } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.autoSaveOn)

        verifySequence {
            keyProvider.autoSaveOn
            defProvider.autoSaveOn

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setAutoSaveOn() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.autoSaveOn } returns keyValue
        preferenceRepo.autoSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.autoSaveOn
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSavePeriod() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.savePeriod } returns keyValue
        every { defProvider.savePeriod } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.savePeriod)

        verifySequence {
            keyProvider.savePeriod
            defProvider.savePeriod

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSavePeriod() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.savePeriod } returns keyValue
        preferenceRepo.savePeriod = value

        verifySequence {
            preferences.edit()
            keyProvider.savePeriod
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getRepeat() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.repeat } returns keyValue
        every { defProvider.repeat } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.repeat)

        verifySequence {
            keyProvider.repeat
            defProvider.repeat

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setRepeat() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.repeat } returns keyValue
        preferenceRepo.repeat = value

        verifySequence {
            preferences.edit()
            keyProvider.repeat
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getSignal() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.signal } returns keyValue
        every { defProvider.signal } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.signal)

        verifySequence {
            keyProvider.signal
            defProvider.signal

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setSignal() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.signal } returns keyValue
        preferenceRepo.signal = value

        verifySequence {
            preferences.edit()
            keyProvider.signal
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getMelodyUri() {
        val keyValue = nextString()
        val defValue = nextString()
        val value = nextString()

        every { keyProvider.melodyUri } returns keyValue
        every { defProvider.melodyUri } returns defValue
        every { preferences.getString(keyValue, defValue) } returns null
        assertEquals(defValue, preferenceRepo.melodyUri)

        every { preferences.getString(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.melodyUri)

        verifySequence {
            keyProvider.melodyUri
            defProvider.melodyUri
            preferences.getString(keyValue, defValue)
            defProvider.melodyUri

            keyProvider.melodyUri
            defProvider.melodyUri
            preferences.getString(keyValue, defValue)
        }
    }

    @Test fun setMelodyUri() {
        val keyValue = nextString()
        val value = nextString()

        every { keyProvider.melodyUri } returns keyValue
        preferenceRepo.melodyUri = value

        verifySequence {
            preferences.edit()
            keyProvider.melodyUri
            preferencesEditor.putString(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolume() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.volume } returns keyValue
        every { defProvider.volume } returns defValue
        every { preferences.getInt(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.volume)

        verifySequence {
            keyProvider.volume
            defProvider.volume

            preferences.getInt(keyValue, defValue)
        }
    }

    @Test fun setVolume() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.volume } returns keyValue
        preferenceRepo.volume = value

        verifySequence {
            preferences.edit()
            keyProvider.volume
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getVolumeIncrease() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.volumeIncrease } returns keyValue
        every { defProvider.volumeIncrease } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, preferenceRepo.volumeIncrease)

        verifySequence {
            keyProvider.volumeIncrease
            defProvider.volumeIncrease

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setVolumeIncrease() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.volumeIncrease } returns keyValue
        preferenceRepo.volumeIncrease = value

        verifySequence {
            preferences.edit()
            keyProvider.volumeIncrease
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
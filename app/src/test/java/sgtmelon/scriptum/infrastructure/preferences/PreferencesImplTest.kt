package sgtmelon.scriptum.infrastructure.preferences

import android.content.SharedPreferences
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.parent.ParentTest
import kotlin.random.Random
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider

/**
 * Test for [PreferencesImpl].
 */
class PreferencesImplTest : ParentTest() {

    @MockK lateinit var keyProvider: PreferencesKeyProvider
    @MockK lateinit var defProvider: PreferencesDefProvider

    @MockK lateinit var preferences: SharedPreferences
    @MockK lateinit var preferencesEditor: SharedPreferences.Editor

    private val appPreferences by lazy { PreferencesImpl(keyProvider, defProvider, preferences) }

    @Before override fun setup() {
        super.setup()

        every { preferences.edit() } returns preferencesEditor

        every { preferencesEditor.putBoolean(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putInt(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putString(any(), any()) } returns preferencesEditor
        every { preferencesEditor.clear() } returns preferencesEditor
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(keyProvider, defProvider, preferences, preferencesEditor)
    }

    @Test fun getFirstStart() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isFirstStart } returns keyValue
        every { defProvider.isFirstStart } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isFirstStart)

        verifySequence {
            keyProvider.isFirstStart
            defProvider.isFirstStart

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setFirstStart() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isFirstStart } returns keyValue
        appPreferences.isFirstStart = value

        verifySequence {
            preferences.edit()
            keyProvider.isFirstStart
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
        assertEquals(value, appPreferences.theme)

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
        appPreferences.theme = value

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

        every { keyProvider.isBackupSkipImports } returns keyValue
        every { defProvider.isBackupSkipImports } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isBackupSkipImports)

        verifySequence {
            keyProvider.isBackupSkipImports
            defProvider.isBackupSkipImports

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setImportSkip() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isBackupSkipImports } returns keyValue
        appPreferences.isBackupSkipImports = value

        verifySequence {
            preferences.edit()
            keyProvider.isBackupSkipImports
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
        assertEquals(value, appPreferences.sort)

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
        appPreferences.sort = value

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
        assertEquals(value, appPreferences.defaultColor)

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
        appPreferences.defaultColor = value

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

        every { keyProvider.isPauseSaveOn } returns keyValue
        every { defProvider.isPauseSaveOn } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isPauseSaveOn)

        verifySequence {
            keyProvider.isPauseSaveOn
            defProvider.isPauseSaveOn

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setPauseSaveOn() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isPauseSaveOn } returns keyValue
        appPreferences.isPauseSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.isPauseSaveOn
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun getAutoSaveOn() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isAutoSaveOn } returns keyValue
        every { defProvider.isAutoSaveOn } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isAutoSaveOn)

        verifySequence {
            keyProvider.isAutoSaveOn
            defProvider.isAutoSaveOn

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setAutoSaveOn() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isAutoSaveOn } returns keyValue
        appPreferences.isAutoSaveOn = value

        verifySequence {
            preferences.edit()
            keyProvider.isAutoSaveOn
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
        assertEquals(value, appPreferences.savePeriod)

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
        appPreferences.savePeriod = value

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
        assertEquals(value, appPreferences.repeat)

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
        appPreferences.repeat = value

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
        assertEquals(value, appPreferences.signal)

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
        appPreferences.signal = value

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
        assertEquals(defValue, appPreferences.melodyUri)

        every { preferences.getString(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.melodyUri)

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
        appPreferences.melodyUri = value

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
        assertEquals(value, appPreferences.volume)

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
        appPreferences.volume = value

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

        every { keyProvider.isVolumeIncrease } returns keyValue
        every { defProvider.isVolumeIncrease } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isVolumeIncrease)

        verifySequence {
            keyProvider.isVolumeIncrease
            defProvider.isVolumeIncrease

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setVolumeIncrease() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isVolumeIncrease } returns keyValue
        appPreferences.isVolumeIncrease = value

        verifySequence {
            preferences.edit()
            keyProvider.isVolumeIncrease
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun getIsDeveloper() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isDeveloper } returns keyValue
        every { defProvider.isDeveloper } returns defValue
        every { preferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(value, appPreferences.isDeveloper)

        verifySequence {
            keyProvider.isDeveloper
            defProvider.isDeveloper

            preferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun setIsDeveloper() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isDeveloper } returns keyValue
        appPreferences.isDeveloper = value

        verifySequence {
            preferences.edit()
            keyProvider.isDeveloper
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun clear() {
        appPreferences.clear()

        verifySequence {
            preferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
        }
    }
}
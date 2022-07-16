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

    @MockK lateinit var sharedPreferences: SharedPreferences
    @MockK lateinit var preferencesEditor: SharedPreferences.Editor

    private val preferences by lazy { PreferencesImpl(keyProvider, defProvider, sharedPreferences) }

    @Before override fun setup() {
        super.setup()

        every { sharedPreferences.edit() } returns preferencesEditor

        every { preferencesEditor.putBoolean(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putInt(any(), any()) } returns preferencesEditor
        every { preferencesEditor.putString(any(), any()) } returns preferencesEditor
        every { preferencesEditor.clear() } returns preferencesEditor
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(keyProvider, defProvider, sharedPreferences, preferencesEditor)
    }

    @Test fun `get isFirstStart`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isFirstStart } returns keyValue
        every { defProvider.isFirstStart } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isFirstStart, value)

        verifySequence {
            keyProvider.isFirstStart
            defProvider.isFirstStart

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isFirstStart`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isFirstStart } returns keyValue
        preferences.isFirstStart = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isFirstStart
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get theme`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.theme } returns keyValue
        every { defProvider.theme } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.theme, value)

        verifySequence {
            keyProvider.theme
            defProvider.theme

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set theme`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.theme } returns keyValue
        preferences.theme = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.theme
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun `get isBackupSkipImports`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isBackupSkipImports } returns keyValue
        every { defProvider.isBackupSkipImports } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isBackupSkipImports, value)

        verifySequence {
            keyProvider.isBackupSkipImports
            defProvider.isBackupSkipImports

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isBackupSkipImports`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isBackupSkipImports } returns keyValue
        preferences.isBackupSkipImports = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isBackupSkipImports
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun `get sort`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.sort } returns keyValue
        every { defProvider.sort } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.sort, value)

        verifySequence {
            keyProvider.sort
            defProvider.sort

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set sort`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.sort } returns keyValue
        preferences.sort = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.sort
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get defaultColor`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.defaultColor } returns keyValue
        every { defProvider.defaultColor } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.defaultColor, value)

        verifySequence {
            keyProvider.defaultColor
            defProvider.defaultColor

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set defaultColor`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.defaultColor } returns keyValue
        preferences.defaultColor = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.defaultColor
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get isPauseSaveOn`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isPauseSaveOn } returns keyValue
        every { defProvider.isPauseSaveOn } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isPauseSaveOn, value)

        verifySequence {
            keyProvider.isPauseSaveOn
            defProvider.isPauseSaveOn

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isPauseSaveOn`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isPauseSaveOn } returns keyValue
        preferences.isPauseSaveOn = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isPauseSaveOn
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get isAutoSaveOn`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isAutoSaveOn } returns keyValue
        every { defProvider.isAutoSaveOn } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isAutoSaveOn, value)

        verifySequence {
            keyProvider.isAutoSaveOn
            defProvider.isAutoSaveOn

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isAutoSaveOn`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isAutoSaveOn } returns keyValue
        preferences.isAutoSaveOn = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isAutoSaveOn
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get savePeriod`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.savePeriod } returns keyValue
        every { defProvider.savePeriod } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.savePeriod, value)

        verifySequence {
            keyProvider.savePeriod
            defProvider.savePeriod

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set savePeriod`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.savePeriod } returns keyValue
        preferences.savePeriod = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.savePeriod
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun `get repeat`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.repeat } returns keyValue
        every { defProvider.repeat } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.repeat, value)

        verifySequence {
            keyProvider.repeat
            defProvider.repeat

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set repeat`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.repeat } returns keyValue
        preferences.repeat = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.repeat
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get signal`() {
        val keyValue = nextString()
        val defValue = nextString()
        val value = nextString()

        every { keyProvider.signal } returns keyValue
        every { defProvider.signal } returns defValue
        every { sharedPreferences.getString(keyValue, defValue) } returns value
        assertEquals(preferences.signal, value)

        verifySequence {
            keyProvider.signal
            defProvider.signal

            sharedPreferences.getString(keyValue, defValue)
        }
    }

    @Test fun `set signal`() {
        val keyValue = nextString()
        val value = nextString()

        every { keyProvider.signal } returns keyValue
        preferences.signal = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.signal
            preferencesEditor.putString(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get melodyUri`() {
        val keyValue = nextString()
        val defValue = nextString()
        val value = nextString()

        every { keyProvider.melodyUri } returns keyValue
        every { defProvider.melodyUri } returns defValue
        every { sharedPreferences.getString(keyValue, defValue) } returns null
        assertEquals(defValue, preferences.melodyUri)

        every { sharedPreferences.getString(keyValue, defValue) } returns value
        assertEquals(preferences.melodyUri, value)

        verifySequence {
            keyProvider.melodyUri
            defProvider.melodyUri
            sharedPreferences.getString(keyValue, defValue)
            defProvider.melodyUri

            keyProvider.melodyUri
            defProvider.melodyUri
            sharedPreferences.getString(keyValue, defValue)
        }
    }

    @Test fun `set melodyUri`() {
        val keyValue = nextString()
        val value = nextString()

        every { keyProvider.melodyUri } returns keyValue
        preferences.melodyUri = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.melodyUri
            preferencesEditor.putString(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get volume`() {
        val keyValue = nextString()
        val defValue = Random.nextInt()
        val value = Random.nextInt()

        every { keyProvider.volume } returns keyValue
        every { defProvider.volume } returns defValue
        every { sharedPreferences.getInt(keyValue, defValue) } returns value
        assertEquals(preferences.volume, value)

        verifySequence {
            keyProvider.volume
            defProvider.volume

            sharedPreferences.getInt(keyValue, defValue)
        }
    }

    @Test fun `set volume`() {
        val keyValue = nextString()
        val value = Random.nextInt()

        every { keyProvider.volume } returns keyValue
        preferences.volume = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.volume
            preferencesEditor.putInt(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun `get isVolumeIncrease`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isVolumeIncrease } returns keyValue
        every { defProvider.isVolumeIncrease } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isVolumeIncrease, value)

        verifySequence {
            keyProvider.isVolumeIncrease
            defProvider.isVolumeIncrease

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isVolumeIncrease`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isVolumeIncrease } returns keyValue
        preferences.isVolumeIncrease = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isVolumeIncrease
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }


    @Test fun `get isDeveloper`() {
        val keyValue = nextString()
        val defValue = Random.nextBoolean()
        val value = Random.nextBoolean()

        every { keyProvider.isDeveloper } returns keyValue
        every { defProvider.isDeveloper } returns defValue
        every { sharedPreferences.getBoolean(keyValue, defValue) } returns value
        assertEquals(preferences.isDeveloper, value)

        verifySequence {
            keyProvider.isDeveloper
            defProvider.isDeveloper

            sharedPreferences.getBoolean(keyValue, defValue)
        }
    }

    @Test fun `set isDeveloper`() {
        val keyValue = nextString()
        val value = Random.nextBoolean()

        every { keyProvider.isDeveloper } returns keyValue
        preferences.isDeveloper = value

        verifySequence {
            sharedPreferences.edit()
            keyProvider.isDeveloper
            preferencesEditor.putBoolean(keyValue, value)
            preferencesEditor.apply()
        }
    }

    @Test fun clear() {
        preferences.clear()

        verifySequence {
            sharedPreferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
        }
    }
}
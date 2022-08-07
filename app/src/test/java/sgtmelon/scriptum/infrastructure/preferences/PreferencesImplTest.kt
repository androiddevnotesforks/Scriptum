package sgtmelon.scriptum.infrastructure.preferences

import android.content.SharedPreferences
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentTest
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.test.common.nextString

/**
 * Test for [PreferencesImpl].
 */
class PreferencesImplTest : ParentTest() {

    //region Setup

    @MockK lateinit var keyProvider: PreferencesKeyProvider
    @MockK lateinit var defProvider: PreferencesDefProvider

    @MockK lateinit var sharedPreferences: SharedPreferences
    @MockK lateinit var preferencesEditor: SharedPreferences.Editor

    private val preferences by lazy { PreferencesImpl(keyProvider, defProvider, sharedPreferences) }

    @Before override fun setUp() {
        super.setUp()

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

    //endregion

    //region Help functions

    private fun getIllegalException(): Nothing = throw IllegalStateException("Not supported type")

    /**
     * For [T] is String, please use [getStringTest]. Because it's make test of null returning
     * state.
     */
    private fun <T> getTest(
        pair: Pair<T, T>,
        keyFunc: () -> String,
        defFunc: () -> T,
        runFunc: () -> T
    ) {
        val key = nextString()
        val (def, value) = pair

        every { keyFunc() } returns key
        every { defFunc() } returns def
        when (value) {
            is Int -> every { sharedPreferences.getInt(key, def as Int) } returns value
            is Boolean -> every { sharedPreferences.getBoolean(key, def as Boolean) } returns value
            else -> getIllegalException()
        }

        assertEquals(runFunc(), value)

        verifySequence {
            keyFunc()
            defFunc()
            when (value) {
                is Int -> sharedPreferences.getInt(key, def as Int)
                is Boolean -> sharedPreferences.getBoolean(key, def as Boolean)
                else -> getIllegalException()
            }
        }
    }

    private fun getStringTest(
        keyFunc: () -> String,
        defFunc: () -> String,
        runFunc: () -> String
    ) {
        val key = nextString()
        val def = nextString()
        val value = nextString()

        every { keyFunc() } returns key
        every { defFunc() } returns def

        every { sharedPreferences.getString(key, def) } returns null
        assertEquals(runFunc(), def)

        every { sharedPreferences.getString(key, def) } returns value
        assertEquals(runFunc(), value)

        verifySequence {
            keyFunc()
            defFunc()
            sharedPreferences.getString(key, def)
            defFunc()

            keyFunc()
            defFunc()
            sharedPreferences.getString(key, def)
        }
    }

    private fun <T> setTest(value: T, keyFunc: () -> String, runFunc: (value: T) -> Unit) {
        val key = nextString()

        every { keyFunc() } returns key
        runFunc(value)

        verifySequence {
            sharedPreferences.edit()
            keyFunc()
            when (value) {
                is Int -> preferencesEditor.putInt(key, value)
                is Boolean -> preferencesEditor.putBoolean(key, value)
                is String -> preferencesEditor.putString(key, value)
                else -> getIllegalException()
            }
            preferencesEditor.apply()
        }
    }

    //endregion

    @Test fun isFirstStart() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isFirstStart },
        { defProvider.isFirstStart },
        { preferences.isFirstStart }
    )

    @Test fun setFirstStart() = setTest(
        Random.nextBoolean(),
        { keyProvider.isFirstStart },
        { preferences.isFirstStart = it }
    )

    // App settings

    @Test fun getTheme() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.theme },
        { defProvider.theme },
        { preferences.theme }
    )

    @Test fun setTheme() = setTest(
        Random.nextInt(),
        { keyProvider.theme },
        { preferences.theme = it }
    )

    // Backup settings

    @Test fun isBackupSkipImports() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isBackupSkipImports },
        { defProvider.isBackupSkipImports },
        { preferences.isBackupSkipImports }
    )

    @Test fun setBackupSkipImports() = setTest(
        Random.nextBoolean(),
        { keyProvider.isBackupSkipImports },
        { preferences.isBackupSkipImports = it }
    )

    // Note settings

    @Test fun getSort() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.sort },
        { defProvider.sort },
        { preferences.sort }
    )

    @Test fun setSort() = setTest(
        Random.nextInt(),
        { keyProvider.sort },
        { preferences.sort = it }
    )

    @Test fun getDefaultColor() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.defaultColor },
        { defProvider.defaultColor },
        { preferences.defaultColor }
    )

    @Test fun setDefaultColor() = setTest(
        Random.nextInt(),
        { keyProvider.defaultColor },
        { preferences.defaultColor = it }
    )

    @Test fun isPauseSaveOn() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isPauseSaveOn },
        { defProvider.isPauseSaveOn },
        { preferences.isPauseSaveOn }
    )

    @Test fun setPauseSaveOn() = setTest(
        Random.nextBoolean(),
        { keyProvider.isPauseSaveOn },
        { preferences.isPauseSaveOn = it }
    )

    @Test fun isAutoSaveOn() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isAutoSaveOn },
        { defProvider.isAutoSaveOn },
        { preferences.isAutoSaveOn }
    )

    @Test fun setAutoSaveOn() = setTest(
        Random.nextBoolean(),
        { keyProvider.isAutoSaveOn },
        { preferences.isAutoSaveOn = it }
    )

    @Test fun getSavePeriod() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.savePeriod },
        { defProvider.savePeriod },
        { preferences.savePeriod }
    )

    @Test fun setSavePeriod() = setTest(
        Random.nextInt(),
        { keyProvider.savePeriod },
        { preferences.savePeriod = it }
    )

    // Alarm settings

    @Test fun getRepeat() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.repeat },
        { defProvider.repeat },
        { preferences.repeat }
    )

    @Test fun setRepeat() = setTest(
        Random.nextInt(),
        { keyProvider.repeat },
        { preferences.repeat = it }
    )

    @Test fun getSignal() = getStringTest(
        { keyProvider.signal },
        { defProvider.signal },
        { preferences.signal }
    )

    @Test fun setSignal() = setTest(
        nextString(),
        { keyProvider.signal },
        { preferences.signal = it }
    )

    @Test fun getMelodyUri() = getStringTest(
        { keyProvider.melodyUri },
        { defProvider.melodyUri },
        { preferences.melodyUri }
    )

    @Test fun setMelodyUri() = setTest(
        nextString(),
        { keyProvider.melodyUri },
        { preferences.melodyUri = it }
    )

    @Test fun getVolume() = getTest(
        Pair(Random.nextInt(), Random.nextInt()),
        { keyProvider.volume },
        { defProvider.volume },
        { preferences.volume }
    )

    @Test fun setVolume() = setTest(
        Random.nextInt(),
        { keyProvider.volume },
        { preferences.volume = it }
    )

    @Test fun isVolumeIncrease() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isVolumeIncrease },
        { defProvider.isVolumeIncrease },
        { preferences.isVolumeIncrease }
    )

    @Test fun setVolumeIncrease() = setTest(
        Random.nextBoolean(),
        { keyProvider.isVolumeIncrease },
        { preferences.isVolumeIncrease = it }
    )

    // Developer settings

    @Test fun isDeveloper() = getTest(
        Pair(Random.nextBoolean(), Random.nextBoolean()),
        { keyProvider.isDeveloper },
        { defProvider.isDeveloper },
        { preferences.isDeveloper }
    )

    @Test fun setDeveloper() = setTest(
        Random.nextBoolean(),
        { keyProvider.isDeveloper },
        { preferences.isDeveloper = it }
    )

    @Test fun clear() {
        preferences.clear()

        verifySequence {
            sharedPreferences.edit()
            preferencesEditor.clear()
            preferencesEditor.apply()
        }
    }
}
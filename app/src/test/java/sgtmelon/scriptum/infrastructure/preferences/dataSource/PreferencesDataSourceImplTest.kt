package sgtmelon.scriptum.infrastructure.preferences.dataSource

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.test.common.nextString

/**
 * Test for [PreferencesDataSourceImpl].
 */
class PreferencesDataSourceImplTest : ParentTest() {

    @MockK lateinit var preferences: Preferences

    private val dataSource by lazy { PreferencesDataSourceImpl(preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences)
    }

    //region Help functions

    private fun <T> getTest(value: T, getFunc: () -> T, runFunc: () -> T) {
        every { getFunc() } returns value

        assertEquals(runFunc(), value)

        verifySequence {
            getFunc()
        }
    }

    private fun <T> setTest(value: T, setFunc: (value: T) -> Unit, runFunc: (value: T) -> Unit) {
        runFunc(value)

        verifySequence {
            setFunc(value)
        }
    }

    //endregion

    @Test fun isFirstStart() = getTest(
        Random.nextBoolean(),
        { preferences.isFirstStart },
        { dataSource.isFirstStart }
    )

    @Test fun setFirstStart() = setTest(
        Random.nextBoolean(),
        { preferences.isFirstStart = it },
        { dataSource.isFirstStart = it }
    )

    // App settings

    @Test fun getTheme() = getTest(Random.nextInt(), { preferences.theme }, { dataSource.theme })

    @Test fun setTheme() = setTest(
        Random.nextInt(),
        { preferences.theme = it },
        { dataSource.theme = it }
    )

    // Backup settings

    @Test fun isBackupSkipImports() = getTest(
        Random.nextBoolean(),
        { preferences.isBackupSkipImports },
        { dataSource.isBackupSkipImports }
    )

    // Note settings

    @Test fun getSort() = getTest(
        Random.nextInt(),
        { preferences.sort },
        { dataSource.sort }
    )

    @Test fun setSort() = setTest(
        Random.nextInt(),
        { preferences.sort = it },
        { dataSource.sort = it }
    )

    @Test fun getDefaultColor() = getTest(
        Random.nextInt(),
        { preferences.defaultColor },
        { dataSource.defaultColor }
    )

    @Test fun setDefaultColor() = setTest(
        Random.nextInt(),
        { preferences.defaultColor = it },
        { dataSource.defaultColor = it }
    )

    @Test fun isPauseSaveOn() = getTest(
        Random.nextBoolean(),
        { preferences.isPauseSaveOn },
        { dataSource.isPauseSaveOn }
    )

    @Test fun isAutoSaveOn() = getTest(
        Random.nextBoolean(),
        { preferences.isAutoSaveOn },
        { dataSource.isAutoSaveOn }
    )

    @Test fun getSavePeriod() = getTest(
        Random.nextInt(),
        { preferences.savePeriod },
        { dataSource.savePeriod }
    )

    @Test fun setSavePeriod() = setTest(
        Random.nextInt(),
        { preferences.savePeriod = it },
        { dataSource.savePeriod = it }
    )

    // Alarm settings

    @Test fun getRepeat() = getTest(
        Random.nextInt(),
        { preferences.repeat },
        { dataSource.repeat }
    )

    @Test fun setRepeat() = setTest(
        Random.nextInt(),
        { preferences.repeat = it },
        { dataSource.repeat = it }
    )

    @Test fun getSignal() = getTest(
        nextString(),
        { preferences.signal },
        { dataSource.signal }
    )

    @Test fun setSignal() = setTest(
        nextString(),
        { preferences.signal = it },
        { dataSource.signal = it }
    )

    @Test fun getMelodyUri() = getTest(
        nextString(),
        { preferences.melodyUri },
        { dataSource.melodyUri }
    )

    @Test fun setMelodyUri() = setTest(
        nextString(),
        { preferences.melodyUri = it },
        { dataSource.melodyUri = it }
    )

    @Test fun getVolume() = getTest(
        Random.nextInt(),
        { preferences.volumePercent },
        { dataSource.volumePercent }
    )

    @Test fun setVolume() = setTest(
        Random.nextInt(),
        { preferences.volumePercent = it },
        { dataSource.volumePercent = it }
    )

    @Test fun isVolumeIncrease() = getTest(
        Random.nextBoolean(),
        { preferences.isVolumeIncrease },
        { dataSource.isVolumeIncrease }
    )

    // Developer settings

    @Test fun isDeveloper() = getTest(
        Random.nextBoolean(),
        { preferences.isDeveloper },
        { dataSource.isDeveloper }
    )

    @Test fun setDeveloper() = setTest(
        Random.nextBoolean(),
        { preferences.isDeveloper = it },
        { dataSource.isDeveloper = it }
    )

    @Test fun clear() {
        dataSource.clear()

        verifySequence {
            preferences.clear()
        }
    }

}
package sgtmelon.scriptum.infrastructure.preferences.dataSource

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import kotlin.random.Random
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.common.utils.nextString
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.parent.ParentTest

/**
 * Test for [PreferencesDataSourceImpl]
 */
class PreferencesDataSourceImplTest : ParentTest() {

    @MockK lateinit var preferences: Preferences

    private val dataSource by lazy { PreferencesDataSourceImpl(preferences) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferences)
    }

    // region Help functions

    private fun <T> setTest(value: T, setFunc: (value: T) -> Unit, runFunc: (value: T) -> Unit) {
        every { setFunc(value) } returns Unit

        runFunc(value)

        verifySequence {
            setFunc(value)
        }
    }

    private fun <T> getTest(value: T, getFunc: () -> T, runFunc: () -> T) {
        every { getFunc() } returns value

        assertEquals(runFunc(), value)

        verifySequence {
            getFunc()
        }
    }

    //endregion

    @Test fun `set isFirstStart`() = setTest(
        Random.nextBoolean(),
        { preferences.isFirstStart = it },
        { dataSource.isFirstStart = it }
    )

    @Test fun `get isFirstStart`() = getTest(
        Random.nextBoolean(),
        { preferences.isFirstStart },
        { dataSource.isFirstStart }
    )

    @Test fun `set theme`() = setTest(
        Random.nextInt(),
        { preferences.theme = it },
        { dataSource.theme = it }
    )

    @Test fun `get theme`() = getTest(Random.nextInt(), { preferences.theme }, { dataSource.theme })

    @Test fun `set isBackupSkipImports`() = setTest(
        Random.nextBoolean(),
        { preferences.isBackupSkipImports = it },
        { dataSource.isBackupSkipImports = it }
    )

    @Test fun `get isBackupSkipImports`() = getTest(
        Random.nextBoolean(),
        { preferences.isBackupSkipImports },
        { dataSource.isBackupSkipImports }
    )

    @Test fun `set sort`() = setTest(
        Random.nextInt(),
        { preferences.sort = it },
        { dataSource.sort = it }
    )

    @Test fun `get sort`() = getTest(
        Random.nextInt(),
        { preferences.sort },
        { dataSource.sort }
    )

    @Test fun `set defaultColor`() = setTest(
        Random.nextInt(),
        { preferences.defaultColor = it },
        { dataSource.defaultColor = it }
    )

    @Test fun `get defaultColor`() = getTest(
        Random.nextInt(),
        { preferences.defaultColor },
        { dataSource.defaultColor }
    )

    @Test fun `set isPauseSaveOn`() = setTest(
        Random.nextBoolean(),
        { preferences.isPauseSaveOn = it },
        { dataSource.isPauseSaveOn = it }
    )

    @Test fun `get isPauseSaveOn`() = getTest(
        Random.nextBoolean(),
        { preferences.isPauseSaveOn },
        { dataSource.isPauseSaveOn }
    )

    @Test fun `set isAutoSaveOn`() = setTest(
        Random.nextBoolean(),
        { preferences.isAutoSaveOn = it },
        { dataSource.isAutoSaveOn = it }
    )

    @Test fun `get isAutoSaveOn`() = getTest(
        Random.nextBoolean(),
        { preferences.isAutoSaveOn },
        { dataSource.isAutoSaveOn }
    )

    @Test fun `set savePeriod`() = setTest(
        Random.nextInt(),
        { preferences.savePeriod = it },
        { dataSource.savePeriod = it }
    )

    @Test fun `get savePeriod`() = getTest(
        Random.nextInt(),
        { preferences.savePeriod },
        { dataSource.savePeriod }
    )

    @Test fun `set repeat`() = setTest(
        Random.nextInt(),
        { preferences.repeat = it },
        { dataSource.repeat = it }
    )

    @Test fun `get repeat`() = getTest(
        Random.nextInt(),
        { preferences.repeat },
        { dataSource.repeat }
    )

    @Test fun `set signal`() = setTest(
        nextString(),
        { preferences.signal = it },
        { dataSource.signal = it }
    )

    @Test fun `get signal`() = getTest(
        nextString(),
        { preferences.signal },
        { dataSource.signal }
    )

    @Test fun `set melodyUri`() = setTest(
        nextString(),
        { preferences.melodyUri = it },
        { dataSource.melodyUri = it }
    )

    @Test fun `get melodyUri`() = getTest(
        nextString(),
        { preferences.melodyUri },
        { dataSource.melodyUri }
    )

    @Test fun `set volume`() = setTest(
        Random.nextInt(),
        { preferences.volume = it },
        { dataSource.volume = it }
    )

    @Test fun `get volume`() = getTest(
        Random.nextInt(),
        { preferences.volume },
        { dataSource.volume }
    )

    @Test fun `set isVolumeIncrease`() = setTest(
        Random.nextBoolean(),
        { preferences.isVolumeIncrease = it },
        { dataSource.isVolumeIncrease = it }
    )

    @Test fun `get isVolumeIncrease`() = getTest(
        Random.nextBoolean(),
        { preferences.isVolumeIncrease },
        { dataSource.isVolumeIncrease }
    )

    @Test fun `set isDeveloper`() = setTest(
        Random.nextBoolean(),
        { preferences.isDeveloper = it },
        { dataSource.isDeveloper = it }
    )

    @Test fun `get isDeveloper`() = getTest(
        Random.nextBoolean(),
        { preferences.isDeveloper },
        { dataSource.isDeveloper }
    )

    @Test fun clear() {
        every { preferences.clear() } returns Unit

        dataSource.clear()

        verifySequence {
            preferences.clear()
        }
    }

}
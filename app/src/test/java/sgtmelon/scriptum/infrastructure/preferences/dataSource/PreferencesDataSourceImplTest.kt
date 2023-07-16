package sgtmelon.scriptum.infrastructure.preferences.dataSource

import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.verifySequence
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import sgtmelon.scriptum.data.model.PermissionKey
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextShortString
import sgtmelon.test.common.nextString
import kotlin.random.Random

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

    @Test fun isShowNotificationsHelp() = getTest(
        Random.nextBoolean(),
        { preferences.showNotificationsHelp },
        { dataSource.showNotificationsHelp }
    )

    @Test fun setShowNotificationsHelp() = setTest(
        Random.nextBoolean(),
        { preferences.showNotificationsHelp = it },
        { dataSource.showNotificationsHelp = it }
    )

    @Test fun isPermissionCalled() {
        val set = List(getRandomSize()) { nextString() }.toSet()

        every { preferences.permissionHistory } returns set

        assertFalse(dataSource.isPermissionCalled(PermissionKey(nextShortString())))
        assertTrue(dataSource.isPermissionCalled(PermissionKey(set.random())))

        verifySequence {
            preferences.permissionHistory
            preferences.permissionHistory
        }
    }

    @Test fun setPermissionCalled() {
        val set = List(getRandomSize()) { nextString() }.toSet()
        val key = PermissionKey(nextShortString())
        val newSet = set.toMutableSet().apply { add(key.value) }

        every { preferences.permissionHistory } returns set
        every { preferences.permissionHistory = newSet } returns Unit

        dataSource.setPermissionCalled(key)

        verifySequence {
            preferences.permissionHistory
            preferences.permissionHistory = newSet
        }
    }

    // App settings

    @Test fun getTheme() = getTest(Random.nextInt(), { preferences.theme }, { dataSource.theme })

    @Test fun setTheme() = setTest(
        Random.nextInt(),
        { preferences.theme = it },
        { dataSource.theme = it }
    )

    // Backup settings

    @Test fun isBackupSkip() = getTest(
        Random.nextBoolean(),
        { preferences.isBackupSkip },
        { dataSource.isBackupSkip }
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
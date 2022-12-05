package sgtmelon.scriptum.data.repository.preferences

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertArrayEquals
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.TestData
import sgtmelon.scriptum.data.dataSource.PreferencesDataSource
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.ParentEnumConverter
import sgtmelon.scriptum.infrastructure.converter.key.RepeatConverter
import sgtmelon.scriptum.infrastructure.converter.key.SavePeriodConverter
import sgtmelon.scriptum.infrastructure.converter.key.SortConverter
import sgtmelon.scriptum.infrastructure.converter.key.ThemeConverter
import sgtmelon.scriptum.infrastructure.model.item.MelodyItem
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.Repeat
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.infrastructure.model.key.preference.Theme
import sgtmelon.scriptum.infrastructure.model.state.AlarmState
import sgtmelon.scriptum.infrastructure.model.state.NoteSaveState
import sgtmelon.scriptum.infrastructure.model.state.SignalState
import sgtmelon.scriptum.testing.getRandomSize
import sgtmelon.scriptum.testing.parent.ParentTest
import sgtmelon.test.common.nextString

/**
 * Test for [PreferencesRepoImpl].
 */
class PreferencesRepoImplTest : ParentTest() {

    //region Setup

    @MockK lateinit var dataSource: PreferencesDataSource
    @MockK lateinit var themeConverter: ThemeConverter
    @MockK lateinit var sortConverter: SortConverter
    @MockK lateinit var colorConverter: ColorConverter
    @MockK lateinit var savePeriodConverter: SavePeriodConverter
    @MockK lateinit var repeatConverter: RepeatConverter
    @MockK lateinit var signalConverter: SignalConverter

    private val repo by lazy {
        PreferencesRepoImpl(
            dataSource,
            themeConverter, sortConverter, colorConverter,
            savePeriodConverter, repeatConverter, signalConverter
        )
    }
    private val spyRepo by lazy { spyk(repo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            dataSource,
            themeConverter, sortConverter, colorConverter,
            savePeriodConverter, repeatConverter, signalConverter
        )
    }

    //endregion

    //region Help functions

    private inline fun <T> getTest(value: T, crossinline getFunc: () -> T, runFunc: () -> T) {
        every { getFunc() } returns value

        assertEquals(runFunc(), value)

        verifySequence {
            getFunc()
        }
    }

    private inline fun <T> setTest(
        value: T,
        crossinline setFunc: (value: T) -> Unit,
        runFunc: (value: T) -> Unit
    ) {
        runFunc(value)

        verifySequence {
            setFunc(value)
        }
    }

    private fun getMelodyList(): List<MelodyItem> = TestData.Melody.melodyList.shuffled()

    private inline fun <E : Enum<E>> getEnumTest(
        enumValue: E,
        defEnumValue: E,
        converter: ParentEnumConverter<E>,
        crossinline getFunc: () -> Int,
        crossinline setFunc: (value: Int) -> Unit,
        runFunc: () -> E
    ) {
        val ordinal = Random.nextInt()
        val defOrdinal = Random.nextInt()

        every { getFunc() } returns ordinal
        every { converter.toEnum(ordinal) } returns null
        every { converter.toInt(defEnumValue) } returns defOrdinal
        assertEquals(runFunc(), defEnumValue)

        every { converter.toEnum(ordinal) } returns enumValue
        assertEquals(runFunc(), enumValue)

        verifySequence {
            getFunc()
            converter.toEnum(ordinal)
            converter.toInt(defEnumValue)
            setFunc(defOrdinal)

            getFunc()
            converter.toEnum(ordinal)
        }
    }

    private inline fun <E : Enum<E>> setEnumTest(
        enumValue: E,
        converter: ParentEnumConverter<E>,
        crossinline setFunc: (value: Int) -> Unit,
        runFunc: (value: E) -> Unit
    ) {
        val ordinal = Random.nextInt()

        every { converter.toInt(enumValue) } returns ordinal

        runFunc(enumValue)

        verifySequence {
            converter.toInt(enumValue)
            setFunc(ordinal)
        }
    }

    //endregion

    @Test fun isFirstStart() = getTest(
        Random.nextBoolean(),
        { dataSource.isFirstStart },
        { repo.isFirstStart }
    )

    @Test fun setFirstStart() = setTest(
        Random.nextBoolean(),
        { dataSource.isFirstStart = it },
        { repo.isFirstStart = it }
    )

    // App settings

    @Test fun getTheme() = getEnumTest(
        mockk(), Theme.SYSTEM, themeConverter,
        { dataSource.theme },
        { dataSource.theme = it },
        { repo.theme }
    )

    @Test fun setTheme() = setEnumTest(
        mockk(), themeConverter,
        { dataSource.theme = it },
        { repo.theme = it }
    )

    // Backup settings

    @Test fun isBackupSkipImports() = getTest(
        Random.nextBoolean(),
        { dataSource.isBackupSkipImports },
        { repo.isBackupSkipImports }
    )

    // Note settings

    @Test fun getSort() = getEnumTest(
        mockk(), Sort.CHANGE, sortConverter,
        { dataSource.sort },
        { dataSource.sort = it },
        { repo.sort }
    )

    @Test fun setSort() = setEnumTest(
        mockk(), sortConverter,
        { dataSource.sort = it },
        { repo.sort = it }
    )

    @Test fun getDefaultColor() = getEnumTest(
        mockk(), Color.WHITE, colorConverter,
        { dataSource.defaultColor },
        { dataSource.defaultColor = it },
        { repo.defaultColor }
    )

    @Test fun setDefaultColor() = setEnumTest(
        mockk(), colorConverter,
        { dataSource.defaultColor = it },
        { repo.defaultColor = it }
    )

    @Test fun getSaveState() {
        val isPauseSaveOn = Random.nextBoolean()
        val isAutoSaveOn = Random.nextBoolean()
        val savePeriod = mockk<SavePeriod>()

        val saveState = NoteSaveState(isPauseSaveOn, isAutoSaveOn, savePeriod)

        every { dataSource.isPauseSaveOn } returns isPauseSaveOn
        every { dataSource.isAutoSaveOn } returns isAutoSaveOn
        every { spyRepo.savePeriod } returns savePeriod

        assertEquals(spyRepo.saveState, saveState)

        verifySequence {
            spyRepo.saveState
            dataSource.isPauseSaveOn
            dataSource.isAutoSaveOn
            spyRepo.savePeriod
        }
    }

    @Test fun getSavePeriod() = getEnumTest(
        mockk(), SavePeriod.MIN_1, savePeriodConverter,
        { dataSource.savePeriod },
        { dataSource.savePeriod = it },
        { repo.savePeriod }
    )

    @Test fun setSavePeriod() = setEnumTest(
        mockk(), savePeriodConverter,
        { dataSource.savePeriod = it },
        { repo.savePeriod = it }
    )

    // Alarm settings

    @Test fun getRepeat() = getEnumTest(
        mockk(), Repeat.MIN_10, repeatConverter,
        { dataSource.repeat },
        { dataSource.repeat = it },
        { repo.repeat }
    )

    @Test fun setRepeat() = setEnumTest(
        mockk(), repeatConverter,
        { dataSource.repeat = it },
        { repo.repeat = it }
    )

    @Test fun getSignalTypeCheck() {
        val value = nextString()
        val size = getRandomSize()
        val typeCheck = BooleanArray(size) { Random.nextBoolean() }

        every { dataSource.signal } returns value
        every { signalConverter.toArray(value) } returns typeCheck

        assertArrayEquals(repo.signalTypeCheck, typeCheck)

        verifySequence {
            dataSource.signal
            signalConverter.toArray(value)
        }
    }

    @Test fun setSignalTypeCheck() {
        val value = nextString()
        val size = getRandomSize()
        val typeCheck = BooleanArray(size) { Random.nextBoolean() }

        every { signalConverter.toString(typeCheck) } returns value

        repo.signalTypeCheck = typeCheck

        verifySequence {
            signalConverter.toString(typeCheck)
            dataSource.signal = value
        }
    }

    @Test fun getSignalState() {
        val value = nextString()
        val state = mockk<SignalState>()

        every { dataSource.signal } returns value
        every { signalConverter.toState(value) } returns null
        assertEquals(repo.signalState, SignalState(isMelody = true, isVibration = true))

        every { signalConverter.toState(value) } returns state
        assertEquals(repo.signalState, state)

        verifySequence {
            dataSource.signal
            signalConverter.toState(value)
            dataSource.signal = "0, 1"

            dataSource.signal
            signalConverter.toState(value)
        }
    }

    @Test fun getMelodyUri() = runTest {
        val melodyList = getMelodyList()

        val wrongUri = nextString()
        val wrongReturnUri = melodyList.first().uri
        val goodUri = melodyList.random().uri

        every { dataSource.melodyUri } returns ""
        assertNull(repo.getMelodyUri(emptyList()))

        every { dataSource.melodyUri } returns ""
        assertEquals(repo.getMelodyUri(melodyList), wrongReturnUri)

        every { dataSource.melodyUri } returns wrongUri
        assertEquals(repo.getMelodyUri(melodyList), wrongReturnUri)

        every { dataSource.melodyUri } returns goodUri
        assertEquals(repo.getMelodyUri(melodyList), goodUri)

        coVerifySequence {
            dataSource.melodyUri

            repeat(times = 2) {
                dataSource.melodyUri
                dataSource.melodyUri = wrongReturnUri
            }

            dataSource.melodyUri
        }
    }

    @Test fun setMelodyUri() = runTest {
        val melodyList = getMelodyList()

        val wrongTitle = nextString()
        val wrongItem = melodyList.first()
        val melodyItem = melodyList.random()

        assertNull(repo.setMelodyUri(emptyList(), wrongTitle))
        assertEquals(repo.setMelodyUri(melodyList, wrongTitle), wrongItem.title)
        assertEquals(repo.setMelodyUri(melodyList, melodyItem.title), melodyItem.title)

        coVerifySequence {
            dataSource.melodyUri = wrongItem.uri
            dataSource.melodyUri = melodyItem.uri
        }
    }

    @Test fun getMelodyCheck() = runTest {
        val melodyList = getMelodyList()
        val index = melodyList.indices.random()

        coEvery { spyRepo.getMelodyUri(melodyList) } returns nextString()
        assertNull(spyRepo.getMelodyCheck(melodyList))

        coEvery { spyRepo.getMelodyUri(melodyList) } returns melodyList[index].uri
        assertEquals(spyRepo.getMelodyCheck(melodyList), index)

        coVerifySequence {
            repeat(times = 2) {
                spyRepo.getMelodyCheck(melodyList)
                spyRepo.getMelodyUri(melodyList)
            }
        }
    }

    @Test fun getVolume() = getTest(
        Random.nextInt(),
        { dataSource.volumePercent },
        { repo.volumePercent }
    )

    @Test fun setVolume() = setTest(
        (10..100).random(),
        { dataSource.volumePercent = it },
        { repo.volumePercent = it }
    )

    @Test fun `setVolume min value`() {
        val value = (-100..9).random()

        repo.volumePercent = value

        verifySequence {
            dataSource.volumePercent = 10
        }
    }

    @Test fun `setVolume max value`() {
        val value = (101..1000).random()

        repo.volumePercent = value

        verifySequence {
            dataSource.volumePercent = 100
        }
    }

    @Test fun isVolumeIncrease() = getTest(
        Random.nextBoolean(),
        { dataSource.isVolumeIncrease },
        { repo.isVolumeIncrease }
    )

    @Test fun alarmState() {
        val signalState = mockk<SignalState>()
        val volumePercent = Random.nextInt()
        val isVolumeIncrease = Random.nextBoolean()

        val alarmState = AlarmState(signalState, volumePercent, isVolumeIncrease)

        every { spyRepo.signalState } returns signalState
        every { spyRepo.volumePercent } returns volumePercent
        every { spyRepo.isVolumeIncrease } returns isVolumeIncrease

        assertEquals(spyRepo.alarmState, alarmState)

        verifySequence {
            spyRepo.alarmState
            spyRepo.signalState
            spyRepo.volumePercent
            spyRepo.isVolumeIncrease
        }
    }

    // Developer settings

    @Test fun isDeveloper() = getTest(
        Random.nextBoolean(),
        { dataSource.isDeveloper },
        { repo.isDeveloper }
    )

    @Test fun setDeveloper() = setTest(
        Random.nextBoolean(),
        { dataSource.isDeveloper = it },
        { repo.isDeveloper = it }
    )

    @Test fun clear() {
        repo.clear()

        verifySequence {
            dataSource.clear()
        }
    }
}
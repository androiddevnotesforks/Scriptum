package sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import java.io.File
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem.Preference
import sgtmelon.scriptum.cleanup.domain.model.key.PrintType
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.test.common.nextString

/**
 * Test for [PrintDevelopInteractor]
 */
@ExperimentalCoroutinesApi
class PrintDevelopInteractorTest : ParentInteractorTest() {

    @MockK lateinit var developRepo: DevelopRepo
    @MockK lateinit var key: PreferencesKeyProvider
    @MockK lateinit var def: PreferencesDefProvider
    @MockK lateinit var preferences: Preferences
    @MockK lateinit var fileDataSource: FileDataSource

    private val interactor by lazy {
        PrintDevelopInteractor(developRepo, key, def, preferences, fileDataSource)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Test fun `getList for note`() = startCoTest {
        val type = PrintType.NOTE
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = false) } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = false)
        }
    }

    @Test fun `getList for bin`() = startCoTest {
        val type = PrintType.BIN
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = true) } returns list
        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = true)
        }
    }

    @Test fun `getList for roll`() = startCoTest {
        val type = PrintType.ROLL
        val list = mockk<List<PrintItem.Roll>>()

        coEvery { developRepo.getPrintRollList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRollList()
        }
    }

    @Test fun `getList for visible`() = startCoTest {
        val type = PrintType.VISIBLE
        val list = mockk<List<PrintItem.Visible>>()

        coEvery { developRepo.getPrintVisibleList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintVisibleList()
        }
    }

    @Test fun `getList for rank`() = startCoTest {
        val type = PrintType.RANK
        val list = mockk<List<PrintItem.Rank>>()

        coEvery { developRepo.getPrintRankList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRankList()
        }
    }

    @Test fun `getList for alarm`() = startCoTest {
        val type = PrintType.ALARM
        val list = mockk<List<PrintItem.Alarm>>()

        coEvery { developRepo.getPrintAlarmList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintAlarmList()
        }
    }

    @Test fun `getList for key`() = startCoTest {
        val type = PrintType.KEY
        val list = mockk<List<Preference>>()

        coEvery { spyInteractor.getPreferenceKeyList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceKeyList()
        }
    }

    @Test fun `getList for file`() = startCoTest {
        val type = PrintType.FILE
        val list = mockk<List<Preference>>()

        coEvery { spyInteractor.getPreferenceFileList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceFileList()
        }
    }

    @Test fun getPreferenceKeyList() {
        val firstStart = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())
        val theme = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val importSkip = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())
        val sort = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val defaultColor = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val pauseSaveOn = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())
        val autoSaveOn = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())
        val savePeriod = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val repeat = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val signal = Triple(nextString(), nextString(), nextString())
        val melodyUri = Triple(nextString(), nextString(), nextString())
        val volume = Triple(nextString(), Random.nextInt(), Random.nextInt())
        val volumeIncrease = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())
        val isDeveloper = Triple(nextString(), Random.nextBoolean(), Random.nextBoolean())

        every { key.isFirstStart } returns firstStart.first
        every { def.isFirstStart } returns firstStart.second
        every { preferences.isFirstStart } returns firstStart.third

        every { key.theme } returns theme.first
        every { def.theme } returns theme.second
        every { preferences.theme } returns theme.third

        every { key.isBackupSkipImports } returns importSkip.first
        every { def.isBackupSkipImports } returns importSkip.second
        every { preferences.isBackupSkipImports } returns importSkip.third

        every { key.sort } returns sort.first
        every { def.sort } returns sort.second
        every { preferences.sort } returns sort.third

        every { key.defaultColor } returns defaultColor.first
        every { def.defaultColor } returns defaultColor.second
        every { preferences.defaultColor } returns defaultColor.third

        every { key.isPauseSaveOn } returns pauseSaveOn.first
        every { def.isPauseSaveOn } returns pauseSaveOn.second
        every { preferences.isPauseSaveOn } returns pauseSaveOn.third

        every { key.isAutoSaveOn } returns autoSaveOn.first
        every { def.isAutoSaveOn } returns autoSaveOn.second
        every { preferences.isAutoSaveOn } returns autoSaveOn.third

        every { key.savePeriod } returns savePeriod.first
        every { def.savePeriod } returns savePeriod.second
        every { preferences.savePeriod } returns savePeriod.third

        every { key.repeat } returns repeat.first
        every { def.repeat } returns repeat.second
        every { preferences.repeat } returns repeat.third

        every { key.signal } returns signal.first
        every { def.signal } returns signal.second
        every { preferences.signal } returns signal.third

        every { key.melodyUri } returns melodyUri.first
        every { def.melodyUri } returns melodyUri.second
        every { preferences.melodyUri } returns melodyUri.third

        every { key.volume } returns volume.first
        every { def.volume } returns volume.second
        every { preferences.volume } returns volume.third

        every { key.isVolumeIncrease } returns volumeIncrease.first
        every { def.isVolumeIncrease } returns volumeIncrease.second
        every { preferences.isVolumeIncrease } returns volumeIncrease.third

        every { key.isDeveloper } returns isDeveloper.first
        every { def.isDeveloper } returns isDeveloper.second
        every { preferences.isDeveloper } returns isDeveloper.third

        val list = listOf(
            Preference.Title(R.string.pref_header_app),
            Preference.Key(firstStart.first, firstStart.second, firstStart.third),
            Preference.Key(theme.first, theme.second, theme.third),
            Preference.Title(R.string.pref_title_backup),
            Preference.Key(importSkip.first, importSkip.second, importSkip.third),
            Preference.Title(R.string.pref_title_note),
            Preference.Key(sort.first, sort.second, sort.third),
            Preference.Key(defaultColor.first, defaultColor.second, defaultColor.third),
            Preference.Key(pauseSaveOn.first, pauseSaveOn.second, pauseSaveOn.third),
            Preference.Key(autoSaveOn.first, autoSaveOn.second, autoSaveOn.third),
            Preference.Key(savePeriod.first, savePeriod.second, savePeriod.third),
            Preference.Title(R.string.pref_title_alarm),
            Preference.Key(repeat.first, repeat.second, repeat.third),
            Preference.Key(signal.first, signal.second, signal.third),
            Preference.Key(melodyUri.first, melodyUri.second, melodyUri.third),
            Preference.Key(volume.first, volume.second, volume.third),
            Preference.Key(volumeIncrease.first, volumeIncrease.second, volumeIncrease.third),
            Preference.Title(R.string.pref_header_other),
            Preference.Key(isDeveloper.first, isDeveloper.second, isDeveloper.third)
        )

        assertEquals(list, interactor.getPreferenceKeyList())

        verifySequence {
            key.isFirstStart
            def.isFirstStart
            preferences.isFirstStart
            key.theme
            def.theme
            preferences.theme
            key.isBackupSkipImports
            def.isBackupSkipImports
            preferences.isBackupSkipImports
            key.sort
            def.sort
            preferences.sort
            key.defaultColor
            def.defaultColor
            preferences.defaultColor
            key.isPauseSaveOn
            def.isPauseSaveOn
            preferences.isPauseSaveOn
            key.isAutoSaveOn
            def.isAutoSaveOn
            preferences.isAutoSaveOn
            key.savePeriod
            def.savePeriod
            preferences.savePeriod
            key.repeat
            def.repeat
            preferences.repeat
            key.signal
            def.signal
            preferences.signal
            key.melodyUri
            def.melodyUri
            preferences.melodyUri
            key.volume
            def.volume
            preferences.volume
            key.isVolumeIncrease
            def.isVolumeIncrease
            preferences.isVolumeIncrease
            key.isDeveloper
            def.isDeveloper
            preferences.isDeveloper
        }
    }

    @Test fun getPreferenceFileList() = startCoTest {
        val saveDirectory = mockk<File>()
        val externalFiles = List(getRandomSize()) { mockk<File>() }
        val externalCache = List(getRandomSize()) { mockk<File>() }
        val backupFiles = List(getRandomSize()) { mockk<FileItem>() }

        every { fileDataSource.saveDirectory } returns saveDirectory
        every { fileDataSource.getExternalFiles() } returns externalFiles
        every { fileDataSource.getExternalCache() } returns externalCache
        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns backupFiles

        val list = mutableListOf<Preference>()
        list.add(Preference.Title(R.string.pref_header_path_save))
        list.add(Preference.Path(saveDirectory))
        list.add(Preference.Title(R.string.pref_header_path_files))
        list.addAll(externalFiles.map { Preference.Path(it) })
        list.add(Preference.Title(R.string.pref_header_path_cache))
        list.addAll(externalCache.map { Preference.Path(it) })
        list.add(Preference.Title(R.string.pref_header_backup_files))
        list.addAll(backupFiles.map { Preference.File(it) })

        assertEquals(list, interactor.getPreferenceFileList())

        coVerifySequence {
            fileDataSource.saveDirectory
            fileDataSource.getExternalFiles()
            fileDataSource.getExternalCache()
            fileDataSource.getFileList(FileType.BACKUP)
        }
    }
}
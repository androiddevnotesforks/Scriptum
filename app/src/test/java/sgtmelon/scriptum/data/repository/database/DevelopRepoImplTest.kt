package sgtmelon.scriptum.data.repository.database

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import java.io.File
import kotlin.random.Random
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.R
import sgtmelon.scriptum.cleanup.data.room.entity.AlarmEntity
import sgtmelon.scriptum.cleanup.data.room.entity.NoteEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RankEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollEntity
import sgtmelon.scriptum.cleanup.data.room.entity.RollVisibleEntity
import sgtmelon.scriptum.cleanup.domain.model.annotation.FileType
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.item.PrintItem
import sgtmelon.scriptum.cleanup.getRandomSize
import sgtmelon.scriptum.cleanup.parent.ParentRepoTest
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider
import sgtmelon.test.common.nextString

/**
 * Test for [DevelopRepoImpl].
 */
class DevelopRepoImplTest : ParentRepoTest() {

    @MockK lateinit var key: PreferencesKeyProvider
    @MockK lateinit var def: PreferencesDefProvider
    @MockK lateinit var preferences: Preferences
    @MockK lateinit var fileDataSource: FileDataSource

    private val repo by lazy {
        DevelopRepoImpl(
            noteDataSource, rollDataSource, rollVisibleDataSource,
            rankDataSource, alarmDataSource,
            key, def, preferences, fileDataSource
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(key, def, preferences, fileDataSource)
    }

    @Test fun getPrintNoteList() {
        val isBin = Random.nextBoolean()
        val list = List(getRandomSize()) { mockk<NoteEntity>() }
        val resultList = list.map { PrintItem.Note(it) }

        coEvery { noteDataSource.getList(isBin) } returns list

        runBlocking {
            assertEquals(repo.getPrintNoteList(isBin), resultList)
        }

        coVerifySequence {
            noteDataSource.getList(isBin)
        }
    }

    @Test fun getPrintRollList() {
        val list = List(getRandomSize()) { mockk<RollEntity>() }
        val resultList = list.map { PrintItem.Roll(it) }

        coEvery { rollDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintRollList(), resultList)
        }

        coVerifySequence {
            rollDataSource.getList()
        }
    }

    @Test fun getPrintVisibleList() {
        val list = List(getRandomSize()) { mockk<RollVisibleEntity>() }
        val resultList = list.map { PrintItem.Visible(it) }

        coEvery { rollVisibleDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintVisibleList(), resultList)
        }

        coVerifySequence {
            rollVisibleDataSource.getList()
        }
    }

    @Test fun getPrintRankList() {
        val list = List(getRandomSize()) { mockk<RankEntity>() }
        val resultList = list.map { PrintItem.Rank(it) }

        coEvery { rankDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintRankList(), resultList)
        }

        coVerifySequence {
            rankDataSource.getList()
        }
    }

    @Test fun getPrintAlarmList() {
        val list = List(getRandomSize()) { mockk<AlarmEntity>() }
        val resultList = list.map { PrintItem.Alarm(it) }

        coEvery { alarmDataSource.getList() } returns list

        runBlocking {
            assertEquals(repo.getPrintAlarmList(), resultList)
        }

        coVerifySequence {
            alarmDataSource.getList()
        }
    }

    @Test fun getPrintPreferenceList() {
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
            PrintItem.Preference.Title(R.string.pref_header_app),
            PrintItem.Preference.Key(firstStart.first, firstStart.second, firstStart.third),
            PrintItem.Preference.Key(theme.first, theme.second, theme.third),
            PrintItem.Preference.Title(R.string.pref_title_backup),
            PrintItem.Preference.Key(importSkip.first, importSkip.second, importSkip.third),
            PrintItem.Preference.Title(R.string.pref_title_note),
            PrintItem.Preference.Key(sort.first, sort.second, sort.third),
            PrintItem.Preference.Key(defaultColor.first, defaultColor.second, defaultColor.third),
            PrintItem.Preference.Key(pauseSaveOn.first, pauseSaveOn.second, pauseSaveOn.third),
            PrintItem.Preference.Key(autoSaveOn.first, autoSaveOn.second, autoSaveOn.third),
            PrintItem.Preference.Key(savePeriod.first, savePeriod.second, savePeriod.third),
            PrintItem.Preference.Title(R.string.pref_title_alarm),
            PrintItem.Preference.Key(repeat.first, repeat.second, repeat.third),
            PrintItem.Preference.Key(signal.first, signal.second, signal.third),
            PrintItem.Preference.Key(melodyUri.first, melodyUri.second, melodyUri.third),
            PrintItem.Preference.Key(volume.first, volume.second, volume.third),
            PrintItem.Preference.Key(
                volumeIncrease.first,
                volumeIncrease.second,
                volumeIncrease.third
            ),
            PrintItem.Preference.Title(R.string.pref_header_other),
            PrintItem.Preference.Key(isDeveloper.first, isDeveloper.second, isDeveloper.third)
        )

        runBlocking {
            assertEquals(repo.getPrintPreferenceList(), list)
        }

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

    @Test fun getPrintFileList() {
        val saveDirectory = mockk<File>()
        val externalFiles = List(getRandomSize()) { mockk<File>() }
        val externalCache = List(getRandomSize()) { mockk<File>() }
        val backupFiles = List(getRandomSize()) { mockk<FileItem>() }

        every { fileDataSource.saveDirectory } returns saveDirectory
        every { fileDataSource.getExternalFiles() } returns externalFiles
        every { fileDataSource.getExternalCache() } returns externalCache
        coEvery { fileDataSource.getFileList(FileType.BACKUP) } returns backupFiles

        val list = mutableListOf<PrintItem.Preference>()
        list.add(PrintItem.Preference.Title(R.string.pref_header_path_save))
        list.add(PrintItem.Preference.Path(saveDirectory))
        list.add(PrintItem.Preference.Title(R.string.pref_header_path_files))
        list.addAll(externalFiles.map { PrintItem.Preference.Path(it) })
        list.add(PrintItem.Preference.Title(R.string.pref_header_path_cache))
        list.addAll(externalCache.map { PrintItem.Preference.Path(it) })
        list.add(PrintItem.Preference.Title(R.string.pref_header_backup_files))
        list.addAll(backupFiles.map { PrintItem.Preference.File(it) })

        runBlocking {
            assertEquals(repo.getPrintFileList(), list)
        }

        coVerifySequence {
            fileDataSource.saveDirectory
            fileDataSource.getExternalFiles()
            fileDataSource.getExternalCache()
            fileDataSource.getFileList(FileType.BACKUP)
        }
    }

    @Test fun getRandomNoteId() {
        val entityList = List(getRandomSize()) { mockk<NoteEntity>() }
        val id = Random.nextLong()

        for (entity in entityList) {
            every { entity.id } returns id
        }

        coEvery { noteDataSource.getList(isBin = false) } returns entityList

        runBlocking {
            assertEquals(repo.getRandomNoteId(), id)
        }

        coVerifySequence {
            noteDataSource.getList(isBin = false)
        }
    }

    @Test fun resetPreferences() {
        repo.resetPreferences()

        verifySequence {
            preferences.clear()
        }
    }
}
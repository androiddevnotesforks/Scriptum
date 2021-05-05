package sgtmelon.scriptum.domain.interactor.impl.preference.develop

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.extension.nextString
import sgtmelon.scriptum.R
import sgtmelon.scriptum.data.provider.PreferenceProvider
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.item.PrintItem.Preference
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.parent.ParentInteractorTest
import sgtmelon.scriptum.presentation.control.file.IFileControl
import java.io.File
import kotlin.random.Random

/**
 * Test for [PrintInteractor]
 */
@ExperimentalCoroutinesApi
class PrintInteractorTest : ParentInteractorTest() {

    @MockK lateinit var developRepo: IDevelopRepo
    @MockK lateinit var key: PreferenceProvider.Key
    @MockK lateinit var def: PreferenceProvider.Def
    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var fileControl: IFileControl

    private val interactor by lazy {
        PrintInteractor(developRepo, key, def, preferenceRepo, fileControl)
    }
    private val spyInteractor by lazy { spyk(interactor) }

    @Test fun getList_forNote() = startCoTest {
        val type = PrintType.NOTE
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = false) } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = false)
        }
    }

    @Test fun getList_forBin() = startCoTest {
        val type = PrintType.BIN
        val list = mockk<List<PrintItem.Note>>()

        coEvery { developRepo.getPrintNoteList(isBin = true) } returns list
        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintNoteList(isBin = true)
        }
    }

    @Test fun getList_forRoll() = startCoTest {
        val type = PrintType.ROLL
        val list = mockk<List<PrintItem.Roll>>()

        coEvery { developRepo.getPrintRollList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRollList()
        }
    }

    @Test fun getList_forVisible() = startCoTest {
        val type = PrintType.VISIBLE
        val list = mockk<List<PrintItem.Visible>>()

        coEvery { developRepo.getPrintVisibleList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintVisibleList()
        }
    }

    @Test fun getList_forRank() = startCoTest {
        val type = PrintType.RANK
        val list = mockk<List<PrintItem.Rank>>()

        coEvery { developRepo.getPrintRankList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintRankList()
        }
    }

    @Test fun getList_forAlarm() = startCoTest {
        val type = PrintType.ALARM
        val list = mockk<List<PrintItem.Alarm>>()

        coEvery { developRepo.getPrintAlarmList() } returns list

        assertEquals(list, interactor.getList(type))

        coVerifySequence {
            developRepo.getPrintAlarmList()
        }
    }

    @Test fun getList_forKey() = startCoTest {
        val type = PrintType.KEY
        val list = mockk<List<PrintItem.Preference>>()

        coEvery { spyInteractor.getPreferenceKeyList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceKeyList()
        }
    }

    @Test fun getList_forFile() = startCoTest {
        val type = PrintType.FILE
        val list = mockk<List<PrintItem.Preference>>()

        coEvery { spyInteractor.getPreferenceFileList() } returns list

        assertEquals(list, spyInteractor.getList(type))

        coVerifySequence {
            spyInteractor.getList(type)
            spyInteractor.getPreferenceFileList()
        }
    }

    @Test fun getPreferenceKeyList() {
        val pairFirstStart = Pair(nextString(), Random.nextBoolean())
        val valFirstStart = Random.nextBoolean()
        val pairTheme = Pair(nextString(), Random.nextInt())
        val valTheme = Random.nextInt()
        val pairImportSkip = Pair(nextString(), Random.nextBoolean())
        val valImportSkip = Random.nextBoolean()
        val pairSort = Pair(nextString(), Random.nextInt())
        val valSort = Random.nextInt()
        val pairDefaultColor = Pair(nextString(), Random.nextInt())
        val valDefaultColor = Random.nextInt()
        val pairPauseSaveOn = Pair(nextString(), Random.nextBoolean())
        val valPauseSaveOn = Random.nextBoolean()
        val pairAutoSaveOn = Pair(nextString(), Random.nextBoolean())
        val valAutoSaveOn = Random.nextBoolean()
        val pairSavePeriod = Pair(nextString(), Random.nextInt())
        val valSavePeriod = Random.nextInt()
        val pairRepeat = Pair(nextString(), Random.nextInt())
        val valRepeat = Random.nextInt()
        val pairSignal = Pair(nextString(), Random.nextInt())
        val valSignal = Random.nextInt()
        val pairMelodyUri = Pair(nextString(), nextString())
        val valMelodyUri = nextString()
        val pairVolume = Pair(nextString(), Random.nextInt())
        val valVolume = Random.nextInt()
        val pairVolumeIncrease = Pair(nextString(), Random.nextBoolean())
        val valVolumeIncrease = Random.nextBoolean()
        val pairIsDeveloper = Pair(nextString(), Random.nextBoolean())
        val valIsDeveloper = Random.nextBoolean()

        every { key.firstStart } returns pairFirstStart.first
        every { def.firstStart } returns pairFirstStart.second
        every { preferenceRepo.firstStart } returns valFirstStart

        every { key.theme } returns pairTheme.first
        every { def.theme } returns pairTheme.second
        every { preferenceRepo.theme } returns valTheme

        every { key.importSkip } returns pairImportSkip.first
        every { def.importSkip } returns pairImportSkip.second
        every { preferenceRepo.importSkip } returns valImportSkip

        every { key.sort } returns pairSort.first
        every { def.sort } returns pairSort.second
        every { preferenceRepo.sort } returns valSort

        every { key.defaultColor } returns pairDefaultColor.first
        every { def.defaultColor } returns pairDefaultColor.second
        every { preferenceRepo.defaultColor } returns valDefaultColor

        every { key.pauseSaveOn } returns pairPauseSaveOn.first
        every { def.pauseSaveOn } returns pairPauseSaveOn.second
        every { preferenceRepo.pauseSaveOn } returns valPauseSaveOn

        every { key.autoSaveOn } returns pairAutoSaveOn.first
        every { def.autoSaveOn } returns pairAutoSaveOn.second
        every { preferenceRepo.autoSaveOn } returns valAutoSaveOn

        every { key.savePeriod } returns pairSavePeriod.first
        every { def.savePeriod } returns pairSavePeriod.second
        every { preferenceRepo.savePeriod } returns valSavePeriod

        every { key.repeat } returns pairRepeat.first
        every { def.repeat } returns pairRepeat.second
        every { preferenceRepo.repeat } returns valRepeat

        every { key.signal } returns pairSignal.first
        every { def.signal } returns pairSignal.second
        every { preferenceRepo.signal } returns valSignal

        every { key.melodyUri } returns pairMelodyUri.first
        every { def.melodyUri } returns pairMelodyUri.second
        every { preferenceRepo.melodyUri } returns valMelodyUri

        every { key.volume } returns pairVolume.first
        every { def.volume } returns pairVolume.second
        every { preferenceRepo.volume } returns valVolume

        every { key.volumeIncrease } returns pairVolumeIncrease.first
        every { def.volumeIncrease } returns pairVolumeIncrease.second
        every { preferenceRepo.volumeIncrease } returns valVolumeIncrease

        every { key.isDeveloper } returns pairIsDeveloper.first
        every { def.isDeveloper } returns pairIsDeveloper.second
        every { preferenceRepo.isDeveloper } returns valIsDeveloper

        val list = listOf(
            Preference.Title(R.string.pref_header_app),
            Preference.Key(pairFirstStart.first, pairFirstStart.second, valFirstStart),
            Preference.Key(pairTheme.first, pairTheme.second, valTheme),
            Preference.Title(R.string.pref_header_backup),
            Preference.Key(pairImportSkip.first, pairImportSkip.second, valImportSkip),
            Preference.Title(R.string.pref_header_note),
            Preference.Key(pairSort.first, pairSort.second, valSort),
            Preference.Key(pairDefaultColor.first, pairDefaultColor.second, valDefaultColor),
            Preference.Key(pairPauseSaveOn.first, pairPauseSaveOn.second, valPauseSaveOn),
            Preference.Key(pairAutoSaveOn.first, pairAutoSaveOn.second, valAutoSaveOn),
            Preference.Key(pairSavePeriod.first, pairSavePeriod.second, valSavePeriod),
            Preference.Title(R.string.pref_header_alarm),
            Preference.Key(pairRepeat.first, pairRepeat.second, valRepeat),
            Preference.Key(pairSignal.first, pairSignal.second, valSignal),
            Preference.Key(pairMelodyUri.first, pairMelodyUri.second, valMelodyUri),
            Preference.Key(pairVolume.first, pairVolume.second, valVolume),
            Preference.Key(pairVolumeIncrease.first, pairVolumeIncrease.second, valVolumeIncrease),
            Preference.Title(R.string.pref_header_other),
            Preference.Key(pairIsDeveloper.first, pairIsDeveloper.second, valIsDeveloper)
        )

        assertEquals(list, interactor.getPreferenceKeyList())

        verifySequence {
            key.firstStart
            def.firstStart
            preferenceRepo.firstStart
            key.theme
            def.theme
            preferenceRepo.theme
            key.importSkip
            def.importSkip
            preferenceRepo.importSkip
            key.sort
            def.sort
            preferenceRepo.sort
            key.defaultColor
            def.defaultColor
            preferenceRepo.defaultColor
            key.pauseSaveOn
            def.pauseSaveOn
            preferenceRepo.pauseSaveOn
            key.autoSaveOn
            def.autoSaveOn
            preferenceRepo.autoSaveOn
            key.savePeriod
            def.savePeriod
            preferenceRepo.savePeriod
            key.repeat
            def.repeat
            preferenceRepo.repeat
            key.signal
            def.signal
            preferenceRepo.signal
            key.melodyUri
            def.melodyUri
            preferenceRepo.melodyUri
            key.volume
            def.volume
            preferenceRepo.volume
            key.volumeIncrease
            def.volumeIncrease
            preferenceRepo.volumeIncrease
            key.isDeveloper
            def.isDeveloper
            preferenceRepo.isDeveloper
        }
    }

    @Test fun getPreferenceFileList() = startCoTest {
        val saveDirectory = mockk<File>()
        val externalFiles = List(getRandomSize()) { mockk<File>() }
        val externalCache = List(getRandomSize()) { mockk<File>() }
        val backupFiles = List(getRandomSize()) { mockk<FileItem>() }

        every { fileControl.saveDirectory } returns saveDirectory
        every { fileControl.getExternalFiles() } returns externalFiles
        every { fileControl.getExternalCache() } returns externalCache
        coEvery { fileControl.getFileList(FileType.BACKUP) } returns backupFiles

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
            fileControl.saveDirectory
            fileControl.getExternalFiles()
            fileControl.getExternalCache()
            fileControl.getFileList(FileType.BACKUP)
        }
    }
}
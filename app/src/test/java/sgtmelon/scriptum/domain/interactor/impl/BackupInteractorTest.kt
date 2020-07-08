package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.data.room.backup.IBackupParser
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.IFileControl

/**
 * Test for [BackupInteractor].
 */
@ExperimentalCoroutinesApi
class BackupInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var alarmRepo: IAlarmRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo
    @MockK lateinit var backupParser: IBackupParser
    @MockK lateinit var fileControl: IFileControl
    @MockK lateinit var cipherControl: ICipherControl

    private val interactor by lazy {
        BackupInteractor(
                preferenceRepo, alarmRepo, rankRepo, noteRepo, backupParser, fileControl,
                cipherControl
        )
    }

    @Test fun getFileList() {
        TODO()
    }

    @Test fun resetFileList() {
        TODO()
    }

    @Test fun export() {
        TODO()
    }

    @Test fun import() {
        TODO()
    }

}
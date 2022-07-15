package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.BackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.item.FileItem
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult

/**
 * Interface for communicate with [BackupPreferenceInteractor].
 */
interface IBackupPreferenceInteractor {

    suspend fun getFileList(): List<FileItem>

    fun resetFileList()

    suspend fun export(): ExportResult

    suspend fun import(name: String): ImportResult

}
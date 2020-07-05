package sgtmelon.scriptum.domain.interactor.callback

import sgtmelon.scriptum.domain.interactor.impl.BackupInteractor
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult

/**
 * Interface for [BackupInteractor].
 */
interface IBackupInteractor {

    suspend fun getFileList(): List<FileItem>

    fun resetFileList()

    suspend fun export(): ExportResult

    suspend fun import(name: String): ImportResult

}
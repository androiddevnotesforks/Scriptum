package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.BackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.result.ExportResult
import sgtmelon.scriptum.cleanup.domain.model.result.ImportResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem

/**
 * Interface for communicate with [BackupPreferenceInteractor].
 */
interface IBackupPreferenceInteractor {

    suspend fun export(): ExportResult

    suspend fun import(name: String, backupFileList: List<FileItem>): ImportResult
}
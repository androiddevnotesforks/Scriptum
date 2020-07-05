package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.room.backup.IBackupParser
import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor
import sgtmelon.scriptum.domain.model.annotation.FileType
import sgtmelon.scriptum.domain.model.item.FileItem
import sgtmelon.scriptum.domain.model.result.ExportResult
import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.IFileControl

/**
 * Interactor for import/export backup files.
 */
class BackupInteractor(
        private val preferenceRepo: IPreferenceRepo,
        private val backupParser: IBackupParser,
        private val fileControl: IFileControl,
        private val cipherControl: ICipherControl
) : IBackupInteractor {

    private var fileList: List<FileItem>? = null

    override suspend fun getFileList(): List<FileItem> {
        return fileList ?: fileControl.getFileList(FileType.BACKUP).also { fileList = it }
    }

    override fun resetFileList() {
        fileList = null
    }


    override suspend fun export(): ExportResult {
        TODO("Not yet implemented")
    }

    override suspend fun import(name: String): ImportResult {
        TODO("Not yet implemented")
    }

}
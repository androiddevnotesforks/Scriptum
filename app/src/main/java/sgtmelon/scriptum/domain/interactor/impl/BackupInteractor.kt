package sgtmelon.scriptum.domain.interactor.impl

import sgtmelon.scriptum.domain.interactor.callback.IBackupInteractor
import sgtmelon.scriptum.domain.model.item.FileItem

/**
 * Interactor for import/export backup files.
 */
class BackupInteractor : IBackupInteractor {

    private var fileList: List<FileItem>? = null

    override suspend fun getFileList(): List<FileItem> {
        return fileList ?: listOf()
    }

    override fun resetFileList() {
        fileList = null
    }


    override suspend fun export(): String? {
        TODO("Not yet implemented")
    }

    override suspend fun import(fileItem: FileItem): Boolean {
        TODO("Not yet implemented")
    }

}
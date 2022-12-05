package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.item.FileItem

class GetBackupFileListUseCase(private val dataSource: FileDataSource) {

    private var list: List<FileItem>? = null

    suspend operator fun invoke(): List<FileItem> {
        return list ?: dataSource.getBackupFileList().also { list = it }
    }

    fun reset() {
        list = null
    }
}
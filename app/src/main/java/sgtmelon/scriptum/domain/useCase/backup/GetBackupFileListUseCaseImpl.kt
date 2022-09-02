package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.infrastructure.model.item.FileItem

class GetBackupFileListUseCaseImpl(
    private val dataSource: FileDataSource
) : GetBackupFileListUseCase {

    private var list: List<FileItem>? = null

    override suspend operator fun invoke(): List<FileItem> {
        return list ?: dataSource.getBackupFileList().also { list = it }
    }

    override fun reset() {
        list = null
    }
}
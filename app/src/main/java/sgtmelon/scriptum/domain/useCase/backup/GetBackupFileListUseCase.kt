package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.infrastructure.model.item.FileItem

interface GetBackupFileListUseCase {

    suspend operator fun invoke(): List<FileItem>

    fun reset()
}
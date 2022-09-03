package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.domain.model.result.ImportResult
import sgtmelon.scriptum.infrastructure.model.item.FileItem

interface StartBackupImportUseCase {

    suspend operator fun invoke(name: String, fileList: List<FileItem>): ImportResult
}
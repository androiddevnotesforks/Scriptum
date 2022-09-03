package sgtmelon.scriptum.domain.useCase.backup

import sgtmelon.scriptum.domain.model.result.ExportResult

interface StartBackupExportUseCase {

    suspend operator fun invoke(): ExportResult
}
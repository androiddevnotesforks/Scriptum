package sgtmelon.scriptum.domain.useCase.files

import sgtmelon.scriptum.data.dataSource.system.FileDataSource

/**
 * Provide path where will be stored created files.
 */
class GetSavePathUseCase(private val dataSource: FileDataSource) {
    operator fun invoke() = dataSource.savePath
}
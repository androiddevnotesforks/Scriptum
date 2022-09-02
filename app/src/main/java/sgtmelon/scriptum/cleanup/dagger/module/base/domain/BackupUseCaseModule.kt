package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCaseImpl

@Module
class BackupUseCaseModule {

    @Provides
    fun provideGetBackupFileListUseCase(dataSource: FileDataSource): GetBackupFileListUseCase {
        return GetBackupFileListUseCaseImpl(dataSource)
    }
}
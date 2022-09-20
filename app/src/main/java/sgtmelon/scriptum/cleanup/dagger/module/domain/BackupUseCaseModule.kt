package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.data.backup.BackupCollector
import sgtmelon.scriptum.data.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase

@Module
class BackupUseCaseModule {

    @Provides
    fun provideGetBackupFileListUseCase(dataSource: FileDataSource): GetBackupFileListUseCase {
        return GetBackupFileListUseCase(dataSource)
    }

    @Provides
    fun provideStartBackupExportUseCase(
        backupRepo: BackupRepo,
        backupCollector: BackupCollector,
        fileDataSource: FileDataSource,
        cipherDataSource: CipherDataSource
    ): StartBackupExportUseCase {
        return StartBackupExportUseCase(
            backupRepo, backupCollector, fileDataSource, cipherDataSource
        )
    }

    @Provides
    fun provideStartBackupImportUseCase(
        preferencesRepo: PreferencesRepo,
        backupRepo: BackupRepo,
        backupParser: BackupParser,
        fileDataSource: FileDataSource,
        cipherDataSource: CipherDataSource
    ): StartBackupImportUseCase {
        return StartBackupImportUseCase(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }
}
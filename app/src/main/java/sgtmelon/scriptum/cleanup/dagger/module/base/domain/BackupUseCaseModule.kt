package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.room.backup.BackupCollector
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.data.dataSource.system.CipherDataSource
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCase
import sgtmelon.scriptum.domain.useCase.backup.GetBackupFileListUseCaseImpl
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupExportUseCaseImpl
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCase
import sgtmelon.scriptum.domain.useCase.backup.StartBackupImportUseCaseImpl

@Module
class BackupUseCaseModule {

    @Provides
    fun provideGetBackupFileListUseCase(dataSource: FileDataSource): GetBackupFileListUseCase {
        return GetBackupFileListUseCaseImpl(dataSource)
    }

    @Provides
    fun provideStartBackupExportUseCase(
        backupRepo: BackupRepo,
        backupCollector: BackupCollector,
        fileDataSource: FileDataSource,
        cipherDataSource: CipherDataSource
    ): StartBackupExportUseCase {
        return StartBackupExportUseCaseImpl(
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
        return StartBackupImportUseCaseImpl(
            preferencesRepo, backupRepo, backupParser, fileDataSource, cipherDataSource
        )
    }
}
package sgtmelon.scriptum.cleanup.dagger.module.data

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.data.backup.BackupCollector
import sgtmelon.scriptum.data.backup.BackupCollectorImpl
import sgtmelon.scriptum.data.backup.BackupHashMaker
import sgtmelon.scriptum.data.backup.BackupJsonConverter
import sgtmelon.scriptum.data.backup.BackupParser
import sgtmelon.scriptum.data.backup.BackupParserImpl
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter
import sgtmelon.scriptum.infrastructure.converter.key.NoteTypeConverter
import sgtmelon.scriptum.infrastructure.converter.types.NumbersJoinConverter

@Module
class BackupModule {

    @Provides
    @Singleton
    fun provideBackupParser(
        dataSource: BackupDataSource,
        hashMaker: BackupHashMaker,
        jsonConverter: BackupJsonConverter
    ): BackupParser {
        return BackupParserImpl(dataSource, hashMaker, jsonConverter)
    }

    @Provides
    @Singleton
    fun provideBackupCollector(
        dataSource: BackupDataSource,
        hashMaker: BackupHashMaker,
        jsonConverter: BackupJsonConverter
    ): BackupCollector {
        return BackupCollectorImpl(dataSource, hashMaker, jsonConverter)
    }

    @Provides
    @Singleton
    fun provideBackupHashMaker(): BackupHashMaker {
        return BackupHashMaker()
    }

    @Provides
    @Singleton
    fun provideJsonConverter(
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        numbersJoinConverter: NumbersJoinConverter
    ): BackupJsonConverter {
        return BackupJsonConverter(colorConverter, typeConverter, numbersJoinConverter)
    }
}
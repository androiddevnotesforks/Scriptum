package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.backup.BackupCollector
import sgtmelon.scriptum.cleanup.data.room.backup.BackupCollectorImpl
import sgtmelon.scriptum.cleanup.data.room.backup.BackupHashMaker
import sgtmelon.scriptum.cleanup.data.room.backup.BackupJsonConverter
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserImpl
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.data.dataSource.backup.BackupDataSource
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

/**
 * Module for provide parser classes.
 */
// TODO refactor this classes and make them repos or something like this
@Module
class ParserModule {

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
    fun provideBackupHashMaker(): BackupHashMaker {
        return BackupHashMaker()
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
    fun provideJsonConverter(
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): BackupJsonConverter {
        return BackupJsonConverter(colorConverter, typeConverter, stringConverter)
    }
}
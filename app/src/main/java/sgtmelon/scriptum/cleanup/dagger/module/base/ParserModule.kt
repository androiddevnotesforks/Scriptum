package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.backup.BackupCollector
import sgtmelon.scriptum.cleanup.data.room.backup.BackupCollectorImpl
import sgtmelon.scriptum.cleanup.data.room.backup.BackupHashMaker
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserImpl
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserSelector
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserSelectorImpl
import sgtmelon.scriptum.cleanup.data.room.backup.EntityJsonConverter
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
    fun provideBackupSelector(
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): BackupParserSelector {
        return BackupParserSelectorImpl(colorConverter, typeConverter, stringConverter)
    }

    @Provides
    @Singleton
    fun provideBackupParser(
        dataSource: BackupDataSource,
        hashMaker: BackupHashMaker,
        selector: BackupParserSelector,
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): BackupParser {
        return BackupParserImpl(
            dataSource, hashMaker, selector, colorConverter, typeConverter, stringConverter
        )
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
        jsonConverter: EntityJsonConverter
    ): BackupCollector {
        return BackupCollectorImpl(dataSource, hashMaker, jsonConverter)
    }

    @Provides
    @Singleton
    fun provideJsonConverter(
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): EntityJsonConverter {
        return EntityJsonConverter(colorConverter, typeConverter, stringConverter)
    }
}
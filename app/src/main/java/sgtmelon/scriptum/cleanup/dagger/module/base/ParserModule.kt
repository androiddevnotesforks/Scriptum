package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserImpl
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserSelector
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParserSelectorImpl
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
        selector: BackupParserSelector,
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): BackupParser {
        return BackupParserImpl(
            dataSource, selector, colorConverter, typeConverter, stringConverter
        )
    }

}
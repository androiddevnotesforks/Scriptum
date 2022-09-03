package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import javax.inject.Singleton
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.BackupSelector
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupSelector
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.infrastructure.converter.key.ColorConverter

/**
 * Module for provide parser classes.
 */
@Module
class ParserModule {

    @Provides
    @Singleton
    fun provideBackupSelector(
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): IBackupSelector {
        return BackupSelector(colorConverter, typeConverter, stringConverter)
    }

    @Provides
    @Singleton
    fun provideBackupParser(
        context: Context,
        selector: IBackupSelector,
        colorConverter: ColorConverter,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): IBackupParser {
        return BackupParser(context, selector, colorConverter, typeConverter, stringConverter)
    }

}
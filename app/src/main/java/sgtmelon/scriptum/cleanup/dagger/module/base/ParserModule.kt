package sgtmelon.scriptum.cleanup.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.room.backup.BackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.BackupSelector
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupSelector
import sgtmelon.scriptum.cleanup.data.room.converter.type.StringConverter
import sgtmelon.scriptum.cleanup.data.room.converter.type.NoteTypeConverter

/**
 * Module for provide parser classes.
 */
@Module
class ParserModule {

    @Provides
    @ActivityScope
    fun provideBackupSelector(
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): IBackupSelector {
        return BackupSelector(typeConverter, stringConverter)
    }

    @Provides
    @ActivityScope
    fun provideBackupParser(
        context: Context,
        selector: IBackupSelector,
        typeConverter: NoteTypeConverter,
        stringConverter: StringConverter
    ): IBackupParser {
        return BackupParser(context, selector, typeConverter, stringConverter)
    }

}
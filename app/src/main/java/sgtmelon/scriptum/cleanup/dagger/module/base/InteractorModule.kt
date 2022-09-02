package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.BackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.PrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.system.SystemInteractor
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.data.dataSource.system.FileDataSource
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo

/**
 * Module for provide interactor's
 */
@Module
class InteractorModule {

    //region Main

    @Provides
    @ActivityScope
    fun provideRankInteractor(rankRepo: RankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo,
        alarmRepo: AlarmRepo,
    ): INotesInteractor {
        return NotesInteractor(preferencesRepo, alarmRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(
        preferencesRepo: PreferencesRepo,
        noteRepo: NoteRepo
    ): IBinInteractor {
        return BinInteractor(preferencesRepo, noteRepo)
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(
        alarmRepo: AlarmRepo,
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): ITextNoteInteractor {
        return TextNoteInteractor(alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(
        alarmRepo: AlarmRepo,
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): IRollNoteInteractor {
        return RollNoteInteractor(alarmRepo, rankRepo, noteRepo)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmInteractor(
        alarmRepo: AlarmRepo,
        noteRepo: NoteRepo
    ): IAlarmInteractor {
        return AlarmInteractor(alarmRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideNotificationInteractor(
        noteRepo: NoteRepo,
        alarmRepo: AlarmRepo,
        bindRepo: BindRepo
    ): INotificationInteractor {
        return NotificationInteractor(noteRepo, alarmRepo, bindRepo)
    }

    //region Preference

    @Provides
    @ActivityScope
    fun provideBackupPreferenceInteractor(
        preferencesRepo: PreferencesRepo,
        alarmRepo: AlarmRepo,
        rankRepo: RankRepo,
        noteRepo: NoteRepo,
        backupRepo: BackupRepo,
        backupParser: IBackupParser,
        fileDataSource: FileDataSource,
        cipherControl: ICipherControl
    ): IBackupPreferenceInteractor {
        return BackupPreferenceInteractor(
            preferencesRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileDataSource, cipherControl
        )
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopInteractor(repository: DevelopRepo): IPrintDevelopInteractor {
        return PrintDevelopInteractor(repository)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideSystemInteractor(
        logic: SystemLogic,
        preferencesRepo: PreferencesRepo,
        bindRepo: BindRepo,
        alarmRepo: AlarmRepo,
        rankRepo: RankRepo,
        noteRepo: NoteRepo
    ): ISystemInteractor {
        return SystemInteractor(preferencesRepo, bindRepo, alarmRepo, rankRepo, noteRepo, logic)
    }
}
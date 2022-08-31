package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.AlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.DevelopRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IDevelopInteractor
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
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.DevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.PrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.system.SystemInteractor
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

/**
 * Module for provide interactor's
 */
@Module
class InteractorModule {

    //region Main

    @Provides
    @ActivityScope
    fun provideRankInteractor(rankRepo: IRankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(
        preferencesRepo: PreferencesRepo,
        noteRepo: INoteRepo,
        alarmRepo: AlarmRepo,
    ): INotesInteractor {
        return NotesInteractor(preferencesRepo, alarmRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(
        preferencesRepo: PreferencesRepo,
        noteRepo: INoteRepo
    ): IBinInteractor {
        return BinInteractor(preferencesRepo, noteRepo)
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(
        alarmRepo: AlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ITextNoteInteractor {
        return TextNoteInteractor(alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(
        alarmRepo: AlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): IRollNoteInteractor {
        return RollNoteInteractor(alarmRepo, rankRepo, noteRepo)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmInteractor(
        alarmRepo: AlarmRepo,
        noteRepo: INoteRepo
    ): IAlarmInteractor {
        return AlarmInteractor(alarmRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideNotificationInteractor(
        noteRepo: INoteRepo,
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
        rankRepo: IRankRepo,
        noteRepo: INoteRepo,
        backupRepo: BackupRepo,
        backupParser: IBackupParser,
        fileControl: IFileControl,
        cipherControl: ICipherControl
    ): IBackupPreferenceInteractor {
        return BackupPreferenceInteractor(
            preferencesRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
        )
    }

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(
        developRepo: DevelopRepo,
        preferences: Preferences
    ): IDevelopInteractor {
        return DevelopInteractor(developRepo, preferences)
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopInteractor(
        developRepo: DevelopRepo,
        key: PreferencesKeyProvider,
        def: PreferencesDefProvider,
        preferences: Preferences,
        fileControl: IFileControl
    ): IPrintDevelopInteractor {
        return PrintDevelopInteractor(developRepo, key, def, preferences, fileControl)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideSystemInteractor(
        logic: SystemLogic,
        preferencesRepo: PreferencesRepo,
        bindRepo: BindRepo,
        alarmRepo: AlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ISystemInteractor {
        return SystemInteractor(preferencesRepo, bindRepo, alarmRepo, rankRepo, noteRepo, logic)
    }
}
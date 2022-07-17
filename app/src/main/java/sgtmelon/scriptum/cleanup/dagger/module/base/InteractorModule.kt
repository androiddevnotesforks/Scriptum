package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBackupRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IDevelopRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IAlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IBackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.INotePreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.IPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.preference.develop.IPrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.AppInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.IntroInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.SplashInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.NoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.AlarmPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.BackupPreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.NotePreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.PreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.DevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.develop.PrintDevelopInteractor
import sgtmelon.scriptum.cleanup.domain.interactor.impl.system.SystemInteractor
import sgtmelon.scriptum.cleanup.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.cleanup.presentation.control.file.IFileControl
import sgtmelon.scriptum.cleanup.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.cleanup.presentation.provider.SummaryProvider
import sgtmelon.scriptum.cleanup.presentation.screen.system.SystemLogic
import sgtmelon.scriptum.infrastructure.converter.SignalConverter
import sgtmelon.scriptum.infrastructure.preferences.Preferences
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesDefProvider
import sgtmelon.scriptum.infrastructure.preferences.provider.PreferencesKeyProvider

/**
 * Module for provide interactor's
 */
@Module
class InteractorModule {

    //region Common

    @Provides
    @ActivityScope
    fun provideSignalInteractor(
        ringtoneControl: IRingtoneControl,
        preferences: Preferences,
        signalConverter: SignalConverter
    ): ISignalInteractor {
        return SignalInteractor(ringtoneControl, preferences, signalConverter)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAppInteractor(preferences: Preferences): IAppInteractor {
        return AppInteractor(preferences)
    }

    @Provides
    @ActivityScope
    fun provideSplashInteractor(preferences: Preferences): ISplashInteractor {
        return SplashInteractor(preferences)
    }

    @Provides
    @ActivityScope
    fun provideIntroInteractor(preferences: Preferences): IIntroInteractor {
        return IntroInteractor(preferences)
    }

    //region Main

    @Provides
    @ActivityScope
    fun provideRankInteractor(rankRepo: IRankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(
        preferences: Preferences,
        noteRepo: INoteRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo
    ): INotesInteractor {
        return NotesInteractor(preferences, alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(
        preferences: Preferences,
        noteRepo: INoteRepo
    ): IBinInteractor {
        return BinInteractor(preferences, noteRepo)
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteInteractor(preferences: Preferences): INoteInteractor {
        return NoteInteractor(preferences)
    }

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(
        preferences: Preferences,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ITextNoteInteractor {
        return TextNoteInteractor(preferences, alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(
        preferences: Preferences,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): IRollNoteInteractor {
        return RollNoteInteractor(preferences, alarmRepo, rankRepo, noteRepo)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmInteractor(
        preferences: Preferences,
        alarmRepo: IAlarmRepo,
        noteRepo: INoteRepo
    ): IAlarmInteractor {
        return AlarmInteractor(preferences, alarmRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideNotificationInteractor(
        noteRepo: INoteRepo,
        alarmRepo: IAlarmRepo,
        bindRepo: IBindRepo
    ): INotificationInteractor {
        return NotificationInteractor(noteRepo, alarmRepo, bindRepo)
    }

    //region Preference

    @Provides
    @ActivityScope
    fun providePreferenceInteractor(
        summaryProvider: SummaryProvider,
        preferences: Preferences
    ): IPreferenceInteractor {
        return PreferenceInteractor(summaryProvider, preferences)
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceInteractor(
        preferences: Preferences,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo,
        backupRepo: IBackupRepo,
        backupParser: IBackupParser,
        fileControl: IFileControl,
        cipherControl: ICipherControl
    ): IBackupPreferenceInteractor {
        return BackupPreferenceInteractor(
            preferences, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
        )
    }

    @Provides
    @ActivityScope
    fun provideNotePreferenceInteractor(
        summaryProvider: SummaryProvider,
        preferences: Preferences
    ): INotePreferenceInteractor {
        return NotePreferenceInteractor(summaryProvider, preferences)
    }

    @Provides
    @ActivityScope
    fun provideAlarmPreferenceInteractor(
        summaryProvider: SummaryProvider,
        preferences: Preferences,
        signalConverter: SignalConverter
    ): IAlarmPreferenceInteractor {
        return AlarmPreferenceInteractor(summaryProvider, preferences, signalConverter)
    }

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(
        developRepo: IDevelopRepo,
        preferences: Preferences
    ): IDevelopInteractor {
        return DevelopInteractor(developRepo, preferences)
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopInteractor(
        developRepo: IDevelopRepo,
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
        preferences: Preferences,
        bindRepo: IBindRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ISystemInteractor {
        return SystemInteractor(preferences, bindRepo, alarmRepo, rankRepo, noteRepo, logic)
    }
}
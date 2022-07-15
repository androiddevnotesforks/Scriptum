package sgtmelon.scriptum.cleanup.dagger.module.base

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.cleanup.data.provider.PreferenceProvider
import sgtmelon.scriptum.cleanup.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.cleanup.data.repository.room.callback.*
import sgtmelon.scriptum.cleanup.data.room.backup.IBackupParser
import sgtmelon.scriptum.cleanup.data.room.converter.type.IntConverter
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
        preferenceRepo: IPreferenceRepo,
        intConverter: IntConverter
    ): ISignalInteractor {
        return SignalInteractor(ringtoneControl, preferenceRepo, intConverter)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAppInteractor(preferenceRepo: IPreferenceRepo): IAppInteractor {
        return AppInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideSplashInteractor(preferenceRepo: IPreferenceRepo): ISplashInteractor {
        return SplashInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideIntroInteractor(preferenceRepo: IPreferenceRepo): IIntroInteractor {
        return IntroInteractor(preferenceRepo)
    }

    //region Main

    @Provides
    @ActivityScope
    fun provideRankInteractor(rankRepo: IRankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(
        preferenceRepo: IPreferenceRepo,
        noteRepo: INoteRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo
    ): INotesInteractor {
        return NotesInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(
        preferenceRepo: IPreferenceRepo,
        noteRepo: INoteRepo
    ): IBinInteractor {
        return BinInteractor(preferenceRepo, noteRepo)
    }

    //endregion

    //region Note

    @Provides
    @ActivityScope
    fun provideNoteInteractor(preferenceRepo: IPreferenceRepo): INoteInteractor {
        return NoteInteractor(preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideTextNoteInteractor(
        preferenceRepo: IPreferenceRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ITextNoteInteractor {
        return TextNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(
        preferenceRepo: IPreferenceRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): IRollNoteInteractor {
        return RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmInteractor(
        preferenceRepo: IPreferenceRepo,
        alarmRepo: IAlarmRepo,
        noteRepo: INoteRepo
    ): IAlarmInteractor {
        return AlarmInteractor(preferenceRepo, alarmRepo, noteRepo)
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
        preferenceRepo: IPreferenceRepo
    ): IPreferenceInteractor {
        return PreferenceInteractor(summaryProvider, preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideBackupPreferenceInteractor(
        preferenceRepo: IPreferenceRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo,
        backupRepo: IBackupRepo,
        backupParser: IBackupParser,
        fileControl: IFileControl,
        cipherControl: ICipherControl
    ): IBackupPreferenceInteractor {
        return BackupPreferenceInteractor(
            preferenceRepo, alarmRepo, rankRepo, noteRepo, backupRepo,
            backupParser, fileControl, cipherControl
        )
    }

    @Provides
    @ActivityScope
    fun provideNotePreferenceInteractor(
        summaryProvider: SummaryProvider,
        preferenceRepo: IPreferenceRepo
    ): INotePreferenceInteractor {
        return NotePreferenceInteractor(summaryProvider, preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideAlarmPreferenceInteractor(
        summaryProvider: SummaryProvider,
        preferenceRepo: IPreferenceRepo,
        intConverter: IntConverter
    ): IAlarmPreferenceInteractor {
        return AlarmPreferenceInteractor(summaryProvider, preferenceRepo, intConverter)
    }

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(
        developRepo: IDevelopRepo,
        preferenceRepo: IPreferenceRepo
    ): IDevelopInteractor {
        return DevelopInteractor(developRepo, preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun providePrintDevelopInteractor(
        developRepo: IDevelopRepo,
        key: PreferenceProvider.Key,
        def: PreferenceProvider.Def,
        preferenceRepo: IPreferenceRepo,
        fileControl: IFileControl
    ): IPrintDevelopInteractor {
        return PrintDevelopInteractor(developRepo, key, def, preferenceRepo, fileControl)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideSystemInteractor(
        logic: SystemLogic,
        preferenceRepo: IPreferenceRepo,
        bindRepo: IBindRepo,
        alarmRepo: IAlarmRepo,
        rankRepo: IRankRepo,
        noteRepo: INoteRepo
    ): ISystemInteractor {
        return SystemInteractor(preferenceRepo, bindRepo, alarmRepo, rankRepo, noteRepo, logic)
    }
}
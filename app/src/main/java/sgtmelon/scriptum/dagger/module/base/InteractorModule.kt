package sgtmelon.scriptum.dagger.module.base

import android.content.res.Resources
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.*
import sgtmelon.scriptum.data.room.backup.IBackupParser
import sgtmelon.scriptum.data.room.converter.type.IntConverter
import sgtmelon.scriptum.domain.interactor.callback.*
import sgtmelon.scriptum.domain.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.domain.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.domain.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.domain.interactor.impl.*
import sgtmelon.scriptum.domain.interactor.impl.main.BinInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.MainInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.NotesInteractor
import sgtmelon.scriptum.domain.interactor.impl.main.RankInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.NoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.RollNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.note.TextNoteInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.AlarmInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.NotificationInteractor
import sgtmelon.scriptum.domain.interactor.impl.notification.SignalInteractor
import sgtmelon.scriptum.presentation.control.cipher.ICipherControl
import sgtmelon.scriptum.presentation.control.file.IFileControl
import sgtmelon.scriptum.presentation.control.system.callback.IRingtoneControl
import sgtmelon.scriptum.presentation.provider.SummaryProvider
import sgtmelon.scriptum.presentation.screen.ui.impl.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.impl.notification.NotificationActivity

/**
 * Module for provide interactor's
 */
@Module
class InteractorModule {

    //region Common

    @Provides
    @ActivityScope
    fun provideBindInteractor(preferenceRepo: IPreferenceRepo, bindRepo: IBindRepo,
                              rankRepo: IRankRepo, noteRepo: INoteRepo): IBindInteractor {
        return BindInteractor(preferenceRepo, bindRepo, rankRepo, noteRepo)
    }

    @Provides
    @ActivityScope
    fun provideSignalInteractor(ringtoneControl: IRingtoneControl,
                                preferenceRepo: IPreferenceRepo,
                                intConverter: IntConverter): ISignalInteractor {
        return SignalInteractor(ringtoneControl, preferenceRepo, intConverter)
    }

    @Provides
    @ActivityScope
    fun provideBackupInteractor(preferenceRepo: IPreferenceRepo, alarmRepo: IAlarmRepo,
                                rankRepo: IRankRepo, noteRepo: INoteRepo,
                                backupParser: IBackupParser, fileControl: IFileControl,
                                cipherControl: ICipherControl): IBackupInteractor {
        return BackupInteractor(
                preferenceRepo, alarmRepo, rankRepo, noteRepo, backupParser, fileControl,
                cipherControl
        )
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
    fun provideMainInteractor(activity: MainActivity, alarmRepo: IAlarmRepo): IMainInteractor {
        return MainInteractor(alarmRepo, activity)
    }

    @Provides
    @ActivityScope
    fun provideRankInteractor(preferenceRepo: IPreferenceRepo,
                              rankRepo: IRankRepo): IRankInteractor {
        return RankInteractor(preferenceRepo, rankRepo)
    }


    @Provides
    @ActivityScope
    fun provideNotesInteractor(fragment: NotesFragment, preferenceRepo: IPreferenceRepo,
                               noteRepo: INoteRepo, alarmRepo: IAlarmRepo,
                               rankRepo: IRankRepo): INotesInteractor {
        return NotesInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideBinInteractor(fragment: BinFragment, preferenceRepo: IPreferenceRepo,
                             noteRepo: INoteRepo): IBinInteractor {
        return BinInteractor(preferenceRepo, noteRepo, fragment)
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
    fun provideTextNoteInteractor(fragment: TextNoteFragment, preferenceRepo: IPreferenceRepo,
                                  alarmRepo: IAlarmRepo, rankRepo: IRankRepo,
                                  noteRepo: INoteRepo): ITextNoteInteractor {
        return TextNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, fragment)
    }

    @Provides
    @ActivityScope
    fun provideRollNoteInteractor(fragment: RollNoteFragment, preferenceRepo: IPreferenceRepo,
                                  alarmRepo: IAlarmRepo, rankRepo: IRankRepo,
                                  noteRepo: INoteRepo): IRollNoteInteractor {
        return RollNoteInteractor(preferenceRepo, alarmRepo, rankRepo, noteRepo, fragment)
    }

    //endregion

    @Provides
    @ActivityScope
    fun provideAlarmInteractor(activity: AlarmActivity, preferenceRepo: IPreferenceRepo,
                               alarmRepo: IAlarmRepo, noteRepo: INoteRepo): IAlarmInteractor {
        return AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, activity)
    }

    @Provides
    @ActivityScope
    fun provideNotificationInteractor(activity: NotificationActivity,
                                      preferenceRepo: IPreferenceRepo, noteRepo: INoteRepo,
                                      alarmRepo: IAlarmRepo,
                                      bindRepo: IBindRepo): INotificationInteractor {
        return NotificationInteractor(preferenceRepo, noteRepo, alarmRepo, bindRepo, activity)
    }


    @Provides
    @ActivityScope
    fun providePreferenceInteractor(resources: Resources,
                                    preferenceRepo: IPreferenceRepo): IPreferenceInteractor {
        return PreferenceInteractor(SummaryProvider(resources), preferenceRepo)
    }

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(developRepo: IDevelopRepo,
                                 preferenceRepo: IPreferenceRepo): IDevelopInteractor {
        return DevelopInteractor(developRepo, preferenceRepo)
    }
}
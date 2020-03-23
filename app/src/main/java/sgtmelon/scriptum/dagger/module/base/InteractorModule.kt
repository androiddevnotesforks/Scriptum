package sgtmelon.scriptum.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.AppInteractor
import sgtmelon.scriptum.interactor.BindInteractor
import sgtmelon.scriptum.interactor.IntroInteractor
import sgtmelon.scriptum.interactor.SplashInteractor
import sgtmelon.scriptum.interactor.callback.IAppInteractor
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.interactor.callback.ISplashInteractor
import sgtmelon.scriptum.interactor.callback.main.IBinInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.interactor.callback.main.INotesInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.interactor.callback.note.INoteInteractor
import sgtmelon.scriptum.interactor.callback.note.IRollNoteInteractor
import sgtmelon.scriptum.interactor.callback.note.ITextNoteInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.INotificationInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.interactor.main.BinInteractor
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.interactor.main.NotesInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.interactor.note.NoteInteractor
import sgtmelon.scriptum.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.interactor.notification.SignalInteractor
import sgtmelon.scriptum.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.repository.room.callback.IBindRepo
import sgtmelon.scriptum.repository.room.callback.INoteRepo
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.presentation.screen.ui.main.BinFragment
import sgtmelon.scriptum.presentation.screen.ui.main.MainActivity
import sgtmelon.scriptum.presentation.screen.ui.main.NotesFragment
import sgtmelon.scriptum.presentation.screen.ui.note.RollNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.note.TextNoteFragment
import sgtmelon.scriptum.presentation.screen.ui.notification.AlarmActivity
import sgtmelon.scriptum.presentation.screen.ui.notification.NotificationActivity

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
    fun provideSignalInteractor(context: Context,
                                preferenceRepo: IPreferenceRepo): ISignalInteractor {
        return SignalInteractor(context, preferenceRepo)
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
    fun provideRankInteractor(rankRepo: IRankRepo): IRankInteractor = RankInteractor(rankRepo)


    @Provides
    @ActivityScope
    fun provideNotesInteractor(fragment: NotesFragment, preferenceRepo: IPreferenceRepo,
                               noteRepo: INoteRepo, alarmRepo: IAlarmRepo,
                               rankRepo: IRankRepo): INotesInteractor {
        return NotesInteractor(preferenceRepo, noteRepo, alarmRepo, rankRepo, fragment)
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
    fun provideNotificationInteractor(activity: NotificationActivity, preferenceRepo: IPreferenceRepo,
                          alarmRepo: IAlarmRepo, bindRepo: IBindRepo): INotificationInteractor {
        return NotificationInteractor(preferenceRepo, alarmRepo, bindRepo, activity)
    }


    @Provides
    @ActivityScope
    fun provideAlarmInteractor(activity: AlarmActivity, preferenceRepo: IPreferenceRepo,
                               alarmRepo: IAlarmRepo, noteRepo: INoteRepo): IAlarmInteractor {
        return AlarmInteractor(preferenceRepo, alarmRepo, noteRepo, activity)
    }





}
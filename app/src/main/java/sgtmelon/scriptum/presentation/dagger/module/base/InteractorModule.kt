package sgtmelon.scriptum.presentation.dagger.module.base

import android.content.Context
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IAlarmRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.interactor.AppInteractor
import sgtmelon.scriptum.domain.interactor.BindInteractor
import sgtmelon.scriptum.domain.interactor.IntroInteractor
import sgtmelon.scriptum.domain.interactor.SplashInteractor
import sgtmelon.scriptum.domain.interactor.callback.IAppInteractor
import sgtmelon.scriptum.domain.interactor.callback.IBindInteractor
import sgtmelon.scriptum.domain.interactor.callback.IIntroInteractor
import sgtmelon.scriptum.domain.interactor.callback.ISplashInteractor
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
import sgtmelon.scriptum.domain.interactor.main.BinInteractor
import sgtmelon.scriptum.domain.interactor.main.MainInteractor
import sgtmelon.scriptum.domain.interactor.main.NotesInteractor
import sgtmelon.scriptum.domain.interactor.main.RankInteractor
import sgtmelon.scriptum.domain.interactor.note.NoteInteractor
import sgtmelon.scriptum.domain.interactor.note.RollNoteInteractor
import sgtmelon.scriptum.domain.interactor.note.TextNoteInteractor
import sgtmelon.scriptum.domain.interactor.notification.AlarmInteractor
import sgtmelon.scriptum.domain.interactor.notification.NotificationInteractor
import sgtmelon.scriptum.domain.interactor.notification.SignalInteractor
import sgtmelon.scriptum.presentation.dagger.ActivityScope
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
package sgtmelon.scriptum.dagger.module

import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IRankInteractor
import sgtmelon.scriptum.interactor.main.RankInteractor
import sgtmelon.scriptum.repository.room.callback.IRankRepo
import sgtmelon.scriptum.screen.ui.main.RankFragment
import sgtmelon.scriptum.screen.vm.callback.main.IRankViewModel
import sgtmelon.scriptum.screen.vm.main.RankViewModel

@Module
class RankModule {

    @Provides
    @ActivityScope
    fun provideInteractor(rankRepo: IRankRepo): IRankInteractor = RankInteractor(rankRepo)

    @Provides
    @ActivityScope
    fun provideViewModel(fragment: RankFragment, interactor: IRankInteractor,
                         bindInteractor: IBindInteractor): IRankViewModel {
        return ViewModelProvider(fragment).get(RankViewModel::class.java).apply {
            setCallback(fragment)
            setInteractor(interactor, bindInteractor)
        }
    }

}
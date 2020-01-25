package sgtmelon.scriptum.dagger.module

import android.content.Context
import androidx.lifecycle.ViewModelProvider
import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.dagger.ActivityScope
import sgtmelon.scriptum.dagger.module.base.InteractorModule
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.main.IMainInteractor
import sgtmelon.scriptum.interactor.main.MainInteractor
import sgtmelon.scriptum.screen.ui.callback.main.IMainActivity
import sgtmelon.scriptum.screen.ui.callback.main.IMainBridge
import sgtmelon.scriptum.screen.ui.main.MainActivity
import sgtmelon.scriptum.screen.vm.callback.main.IMainViewModel
import sgtmelon.scriptum.screen.vm.main.MainViewModel

@Module(includes = [InteractorModule::class])
class MainModule {

    @Provides
    @ActivityScope
    fun provideContext(activity: MainActivity): Context = activity.applicationContext

    @Provides
    @ActivityScope
    fun provideMainInteractor(context: Context, activity: MainActivity): IMainInteractor {
        return MainInteractor(context, activity)
    }

    @Provides
    @ActivityScope
    fun provideViewModel(activity: MainActivity,
                         iInteractor: IMainInteractor,
                         iBindInteractor: IBindInteractor): IMainViewModel {
        return ViewModelProvider(activity).get(MainViewModel::class.java).apply {
            setCallback(activity as? IMainActivity)
            setInteractor(iInteractor, iBindInteractor)
        }
    }

}
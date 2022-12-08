package sgtmelon.scriptum.cleanup.dagger.module

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.dagger.other.ActivityScope
import sgtmelon.scriptum.data.repository.database.DevelopRepo
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractor
import sgtmelon.scriptum.domain.interactor.preferences.DevelopInteractorImpl

@Deprecated("Convert to use cases if possible")
@Module
class InteractorModule {

    @Provides
    @ActivityScope
    fun provideDevelopInteractor(repository: DevelopRepo): DevelopInteractor {
        return DevelopInteractorImpl(repository)
    }
}
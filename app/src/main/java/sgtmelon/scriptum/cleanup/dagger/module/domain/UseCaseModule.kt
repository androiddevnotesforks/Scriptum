package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.data.dataSource.system.RingtoneDataSource
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase

@Module
class UseCaseModule {

    @Provides
    fun provideGetMelodyListUseCase(dataSource: RingtoneDataSource): GetMelodyListUseCase {
        return GetMelodyListUseCase(dataSource)
    }
}
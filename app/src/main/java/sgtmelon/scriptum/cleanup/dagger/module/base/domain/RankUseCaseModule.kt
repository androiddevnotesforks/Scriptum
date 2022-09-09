package sgtmelon.scriptum.cleanup.dagger.module.base.domain

import dagger.Module
import dagger.Provides
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.domain.useCase.rank.CorrectPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase

@Module
class RankUseCaseModule {

    @Provides
    fun getListRankUseCase(repository: RankRepo): GetRankListUseCase {
        return GetRankListUseCase(repository)
    }

    @Provides
    fun insertRankUseCase(repository: RankRepo): InsertRankUseCase {
        return InsertRankUseCase(repository)
    }

    @Provides
    fun deleteRankUseCase(repository: RankRepo): DeleteRankUseCase {
        return DeleteRankUseCase(repository)
    }

    @Provides
    fun updateRankUseCase(repository: RankRepo): UpdateRankUseCase {
        return UpdateRankUseCase(repository)
    }


    @Provides
    fun provideCorrectPositionsUseCase(): CorrectPositionsUseCase {
        return CorrectPositionsUseCase()
    }
}
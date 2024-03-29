package sgtmelon.scriptum.cleanup.dagger.module.domain

import dagger.Module
import dagger.Provides
import javax.inject.Named
import sgtmelon.scriptum.cleanup.data.repository.room.callback.RankRepo
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankDialogNamesUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankIdUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase

@Module
class RankUseCaseModule {

    @Provides
    fun provideGetListRankUseCase(repository: RankRepo): GetRankListUseCase {
        return GetRankListUseCase(repository)
    }

    @Provides
    fun provideInsertRankUseCase(repository: RankRepo): InsertRankUseCase {
        return InsertRankUseCase(repository)
    }

    @Provides
    fun provideDeleteRankUseCase(repository: RankRepo): DeleteRankUseCase {
        return DeleteRankUseCase(repository)
    }

    @Provides
    fun provideUpdateRankUseCase(repository: RankRepo): UpdateRankUseCase {
        return UpdateRankUseCase(repository)
    }

    @Provides
    fun provideCorrectRankPositionsUseCase(): CorrectRankPositionsUseCase {
        return CorrectRankPositionsUseCase()
    }

    @Provides
    fun provideUpdateRankPositionsUseCase(repository: RankRepo): UpdateRankPositionsUseCase {
        return UpdateRankPositionsUseCase(repository)
    }

    @Provides
    fun provideGetRankIdUseCase(repository: RankRepo): GetRankIdUseCase {
        return GetRankIdUseCase(repository)
    }

    @Provides
    fun provideGetRankDialogNamesUseCase(
        @Named("WithoutCategoryName") withoutCategoryName: String,
        repository: RankRepo
    ): GetRankDialogNamesUseCase {
        return GetRankDialogNamesUseCase(withoutCategoryName, repository)
    }
}
package sgtmelon.scriptum.infrastructure.screen.main.rank

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.domain.useCase.rank.CorrectRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.DeleteRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.GetRankListUseCase
import sgtmelon.scriptum.domain.useCase.rank.InsertRankUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankPositionsUseCase
import sgtmelon.scriptum.domain.useCase.rank.UpdateRankUseCase
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [RankViewModelImpl].
 */
class RankViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var getList: GetRankListUseCase
    @MockK lateinit var insertRank: InsertRankUseCase
    @MockK lateinit var deleteRank: DeleteRankUseCase
    @MockK lateinit var updateRank: UpdateRankUseCase
    @MockK lateinit var correctRankPositions: CorrectRankPositionsUseCase
    @MockK lateinit var updateRankPositions: UpdateRankPositionsUseCase

//    private val viewModel by lazy {
//        RankViewModelImpl(
//            getList, insertRank, deleteRank, updateRank, correctRankPositions, updateRankPositions
//        )
//    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            getList, insertRank, deleteRank, updateRank, correctRankPositions, updateRankPositions
        )
    }

    //endregion

    @Test fun todo() {
        TODO()
    }
}
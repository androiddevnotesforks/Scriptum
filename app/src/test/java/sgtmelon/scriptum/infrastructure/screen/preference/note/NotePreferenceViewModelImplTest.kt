package sgtmelon.scriptum.infrastructure.screen.preference.note

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.verifySequence
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test
import sgtmelon.scriptum.cleanup.parent.ParentViewModelTest
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.preferences.summary.GetSummaryUseCase
import sgtmelon.scriptum.infrastructure.model.key.preference.Color
import sgtmelon.scriptum.infrastructure.model.key.preference.SavePeriod
import sgtmelon.scriptum.infrastructure.model.key.preference.Sort
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest
import sgtmelon.test.common.nextString

/**
 * ViewModel for [NotePreferenceViewModelImpl].
 */
class NotePreferenceViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var getSortSummary: GetSummaryUseCase
    @MockK lateinit var getDefaultColorSummary: GetSummaryUseCase
    @MockK lateinit var getSavePeriodSummary: GetSummaryUseCase

    private val viewModel by lazy {
        NotePreferenceViewModelImpl(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, getSortSummary, getDefaultColorSummary, getSavePeriodSummary
        )
    }

    //endregion

    @Test fun getSort() {
        TODO()
    }

    @Test fun getSortSummary() {
        TODO()
    }

    @Test fun updateSort() {
        TODO()
    }

    @Test fun getDefaultColor() {
        TODO()
    }

    @Test fun getDefaultColorSummary() {
        TODO()
    }

    @Test fun updateDefaultColor() {
        TODO()
    }

    @Test fun getSavePeriod() {
        TODO()
    }

    @Test fun getSavePeriodSummary() {
        TODO()
    }

    @Test fun updateSavePeriod() {
        TODO()
    }
}
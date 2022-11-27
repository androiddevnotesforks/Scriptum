package sgtmelon.scriptum.domain.useCase.alarm

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.data.repository.database.AlarmRepo
import sgtmelon.scriptum.testing.parent.ParentTest

/**
 * Tests for [TidyUpAlarmUseCase].
 */
class TidyUpAlarmUseCaseTest : ParentTest() {

    @MockK lateinit var repository: AlarmRepo

    private val useCase by lazy { TidyUpAlarmUseCase(repository) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(repository)
    }

    @Test fun invoke() {
        TODO()
    }
}
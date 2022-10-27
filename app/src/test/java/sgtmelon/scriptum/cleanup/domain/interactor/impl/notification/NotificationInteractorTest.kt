package sgtmelon.scriptum.cleanup.domain.interactor.impl.notification

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.BindRepo
import sgtmelon.scriptum.cleanup.parent.ParentInteractorTest

/**
 * Test for [NotificationInteractor].
 */
@Deprecated("Don't use it")
@ExperimentalCoroutinesApi
class NotificationInteractorTest : ParentInteractorTest() {

    @MockK lateinit var bindRepo: BindRepo

    private val interactor by lazy { NotificationInteractor(bindRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(bindRepo)
    }

    @Test fun getCount() = startCoTest {
        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationsCount() } returns count
        assertEquals(count, interactor.getCount())

        coVerifySequence {
            bindRepo.getNotificationsCount()
        }
    }
}
package sgtmelon.scriptum.cleanup.presentation.screen.presenter.system

import io.mockk.coVerifySequence
import io.mockk.confirmVerified
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.spyk
import io.mockk.verifySequence
import java.util.Calendar
import kotlin.random.Random
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.After
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import sgtmelon.scriptum.cleanup.domain.interactor.callback.system.ISystemInteractor
import sgtmelon.scriptum.cleanup.presentation.screen.system.ISystemLogic
import sgtmelon.scriptum.parent.ParentPresenterTest

/**
 * Test for [SystemPresenter]
 */
@ExperimentalCoroutinesApi
class SystemPresenterTest : ParentPresenterTest() {

    //region Setup

    @MockK lateinit var callback: ISystemLogic
    @MockK lateinit var interactor: ISystemInteractor

    private val presenter by lazy { SystemPresenter(interactor) }
    private val spyPresenter by lazy { spyk(presenter) }

    @Before override fun setUp() {
        super.setUp()

        presenter.setCallback(callback)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor)
    }

    @Test override fun onDestroy() {
        assertNotNull(presenter.callback)
        presenter.onDestroy()
        assertNull(presenter.callback)

        verifySequence {
            interactor.onDestroy()
        }
    }


    @Test fun onSetup() {
        every { spyPresenter.tidyUpAlarm() } returns Unit
        every { spyPresenter.notifyAllNotes() } returns Unit
        every { spyPresenter.notifyCount(count = null) } returns Unit

        spyPresenter.onSetup()

        verifySequence {
            spyPresenter.onSetup()
            spyPresenter.tidyUpAlarm()
            spyPresenter.notifyAllNotes()
            spyPresenter.notifyCount(count = null)
        }
    }

    @Test fun tidyUpAlarm() {
        presenter.tidyUpAlarm()

        coVerifySequence {
            interactor.tidyUpAlarm()
        }
    }

    @Test fun setAlarm() {
        val id = Random.nextLong()
        val calendar = mockk<Calendar>()
        val showToast = Random.nextBoolean()

        presenter.setAlarm(id, calendar, showToast)

        verifySequence {
            callback.setAlarm(id, calendar, showToast)
        }
    }

    @Test fun cancelAlarm() {
        val id = Random.nextLong()

        presenter.cancelAlarm(id)

        verifySequence {
            callback.cancelAlarm(id)
        }
    }

    @Test fun notifyAllNotes() {
        presenter.notifyAllNotes()

        coVerifySequence {
            interactor.notifyNotesBind()
        }
    }

    @Test fun cancelNote() {
        val id = Random.nextLong()

        presenter.cancelNote(id)

        coVerifySequence {
            interactor.unbindNote(id)
            callback.cancelNoteBind(id)
        }
    }

    @Test fun notifyCount() {
        val count = Random.nextInt()

        presenter.notifyCount(count = null)
        presenter.notifyCount(count)

        coVerifySequence {
            interactor.notifyCountBind()

            callback.notifyCountBind(count)
        }
    }
}
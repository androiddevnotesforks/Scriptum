package sgtmelon.scriptum.screen.vm.notification

import io.mockk.impl.annotations.MockK
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.ObsoleteCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.R
import sgtmelon.scriptum.interactor.callback.IBindInteractor
import sgtmelon.scriptum.interactor.callback.notification.IAlarmInteractor
import sgtmelon.scriptum.interactor.callback.notification.ISignalInteractor
import sgtmelon.scriptum.model.annotation.Repeat
import sgtmelon.scriptum.screen.ui.callback.notification.IAlarmActivity
import kotlin.random.Random


/**
 * Test for [AlarmViewModel].
 */
@ObsoleteCoroutinesApi
@ExperimentalCoroutinesApi
class AlarmViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IAlarmActivity

    @MockK lateinit var interactor: IAlarmInteractor
    @MockK lateinit var signalInteractor: ISignalInteractor
    @MockK lateinit var bindInteractor: IBindInteractor

    private val viewModel by lazy { AlarmViewModel(application) }

    override fun setUp() {
        super.setUp()

        viewModel.setCallback(callback)
        assertEquals(callback, viewModel.callback)

        viewModel.setInteractor(interactor, signalInteractor, bindInteractor)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
//        viewModel.onDestroy()
//        assertNull(viewModel.callback)
    }


    @Test fun onClickDisable() {
        viewModel.onClickDisable()
        verify(exactly = 1) { callback.finish() }
    }


    @Test fun getRepeatById() {
        assertEquals(Repeat.MIN_10, viewModel.getRepeatById(R.id.item_repeat_0))
        assertEquals(Repeat.MIN_30, viewModel.getRepeatById(R.id.item_repeat_1))
        assertEquals(Repeat.MIN_60, viewModel.getRepeatById(R.id.item_repeat_2))
        assertEquals(Repeat.MIN_180, viewModel.getRepeatById(R.id.item_repeat_3))
        assertEquals(Repeat.MIN_1440, viewModel.getRepeatById(R.id.item_repeat_4))
        assertNull(viewModel.getRepeatById(Random.nextInt()))
    }
    
}
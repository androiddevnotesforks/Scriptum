package sgtmelon.scriptum.presentation.screen.vm.impl.preference

import android.os.Bundle
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.*
import org.junit.Test
import sgtmelon.scriptum.ParentViewModelTest
import sgtmelon.scriptum.domain.interactor.callback.preference.IPrintInteractor
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Default
import sgtmelon.scriptum.domain.model.data.IntentData.Print.Intent
import sgtmelon.scriptum.domain.model.item.PrintItem
import sgtmelon.scriptum.domain.model.key.PrintType
import sgtmelon.scriptum.extension.clearAdd
import sgtmelon.scriptum.getRandomSize
import sgtmelon.scriptum.presentation.screen.ui.callback.preference.IPrintActivity

/**
 * Test for [PrintViewModel].
 */
@ExperimentalCoroutinesApi
class PrintViewModelTest : ParentViewModelTest() {

    @MockK lateinit var callback: IPrintActivity

    @MockK lateinit var interactor: IPrintInteractor

    @MockK lateinit var bundle: Bundle

    private val viewModel by lazy { PrintViewModel(application) }

    @Before override fun setup() {
        super.setup()

        viewModel.setCallback(callback)
        viewModel.setInteractor(interactor)

        assertTrue(viewModel.itemList.isEmpty())
        assertNull(viewModel.type)
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(callback, interactor, bundle)
    }

    @Test override fun onDestroy() {
        assertNotNull(viewModel.callback)
        viewModel.onDestroy()
        assertNull(viewModel.callback)
    }


    @Test fun onSetup() {
        val type = PrintType.values().random()

        viewModel.onSetup()
        assertNull(viewModel.type)

        every { bundle.getInt(Intent.TYPE, Default.TYPE) } returns type.ordinal

        viewModel.onSetup(bundle)
        assertEquals(type, viewModel.type)

        verifySequence {
            bundle.getInt(Intent.TYPE, Default.TYPE)
            callback.setupView(type)
            callback.setupInsets()
        }
    }

    @Test fun onSaveData() {
        val type = PrintType.values().random()

        every { bundle.putInt(Intent.TYPE, type.ordinal) } returns Unit

        viewModel.type = type
        viewModel.onSaveData(bundle)

        verifySequence {
            bundle.putInt(Intent.TYPE, type.ordinal)
        }
    }

    @Test fun onUpdateData_startEmpty() = startCoTest {
        val type = PrintType.values().random()
        val itemList = List<PrintItem>(getRandomSize()) { mockk() }

        coEvery { interactor.getList(type) } returns itemList

        viewModel.itemList.clear()

        viewModel.type = type
        viewModel.onUpdateData()

        assertEquals(itemList, viewModel.itemList)

        coVerifySequence {
            callback.beforeLoad()
            callback.showProgress()
            interactor.getList(type)
            updateList(itemList)
        }
    }

    @Test fun onUpdateData_startNotEmpty() = startCoTest {
        val type = PrintType.values().random()
        val startList = List<PrintItem>(getRandomSize()) { mockk() }
        val returnList = startList.shuffled()

        coEvery { interactor.getList(type) } returns returnList

        viewModel.itemList.clearAdd(startList)
        assertEquals(startList, viewModel.itemList)

        viewModel.type = type
        viewModel.onUpdateData()

        assertEquals(returnList, viewModel.itemList)

        coVerifySequence {
            callback.beforeLoad()
            updateList(any())
            interactor.getList(type)
            updateList(returnList)
        }
    }

    private fun updateList(itemList: List<PrintItem>) = with(callback) {
        notifyList(itemList)
        onBindingList()
    }
}
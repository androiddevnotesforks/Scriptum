package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.domain.model.item.NoteItem
import sgtmelon.scriptum.presentation.control.system.BindControl
import kotlin.random.Random

/**
 * Test for [BindInteractor].
 */
@ExperimentalCoroutinesApi
class BindInteractorTest : ParentInteractorTest() {

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var bindRepo: IBindRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo

    @MockK lateinit var noteCallback: BindControl.NoteBridge.NotifyAll
    @MockK lateinit var infoCallback: BindControl.InfoBridge

    private val interactor by lazy { BindInteractor(preferenceRepo, bindRepo, rankRepo, noteRepo) }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(preferenceRepo, bindRepo, rankRepo, noteRepo, noteCallback, infoCallback)
    }

    @Test fun notifyNoteBind() = startCoTest {
        interactor.notifyNoteBind(callback = null)

        val sort = Random.nextInt()
        val rankIdVisibleList = mockk<List<Long>>()
        val itemList = mockk<MutableList<NoteItem>>()

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        coEvery {
            noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
        } returns itemList

        interactor.notifyNoteBind(noteCallback)

        coVerifySequence {
            preferenceRepo.sort
            rankRepo.getIdVisibleList()
            noteRepo.getList(sort, isBin = false, isOptimal = false, filterVisible = false)
            noteCallback.notifyNoteBind(itemList, rankIdVisibleList)
        }
    }

    @Test fun notifyInfoBind() = startCoTest {
        interactor.notifyInfoBind(callback = null)

        val count = Random.nextInt()
        coEvery { bindRepo.getNotificationCount() } returns count
        interactor.notifyInfoBind(infoCallback)

        coVerifySequence {
            bindRepo.getNotificationCount()
            infoCallback.notifyInfoBind(count)
        }
    }
}
package sgtmelon.scriptum.domain.interactor.impl

import io.mockk.coEvery
import io.mockk.coVerifySequence
import io.mockk.every
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Test
import sgtmelon.scriptum.ParentInteractorTest
import sgtmelon.scriptum.TestData
import sgtmelon.scriptum.data.repository.preference.IPreferenceRepo
import sgtmelon.scriptum.data.repository.room.callback.IBindRepo
import sgtmelon.scriptum.data.repository.room.callback.INoteRepo
import sgtmelon.scriptum.data.repository.room.callback.IRankRepo
import sgtmelon.scriptum.presentation.control.system.BindControl
import kotlin.random.Random

/**
 * Test for [BindInteractor].
 */
@ExperimentalCoroutinesApi
class BindInteractorTest : ParentInteractorTest() {

    private val data = TestData.Note

    @MockK lateinit var preferenceRepo: IPreferenceRepo
    @MockK lateinit var bindRepo: IBindRepo
    @MockK lateinit var rankRepo: IRankRepo
    @MockK lateinit var noteRepo: INoteRepo

    @MockK lateinit var noteCallback: BindControl.NoteBridge.NotifyAll
    @MockK lateinit var infoCallback: BindControl.InfoBridge

    private val interactor by lazy { BindInteractor(preferenceRepo, bindRepo, rankRepo, noteRepo) }

    @Test fun notifyNoteBind() = startCoTest {
        interactor.notifyNoteBind(callback = null)

        val rankIdVisibleList = data.rankIdVisibleList
        val itemList = data.itemList
        val sort = TestData.sort

        every { preferenceRepo.sort } returns null
        interactor.notifyNoteBind(noteCallback)

        every { preferenceRepo.sort } returns sort
        coEvery { rankRepo.getIdVisibleList() } returns null
        interactor.notifyNoteBind(noteCallback)

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        coEvery {
            noteRepo.getList(sort, bin = false, isOptimal = false, filterVisible = false)
        } returns null
        interactor.notifyNoteBind(noteCallback)

        coEvery {
            noteRepo.getList(sort, bin = false, isOptimal = false, filterVisible = false)
        } returns itemList
        interactor.notifyNoteBind(noteCallback)

        coVerifySequence {
            preferenceRepo.sort

            preferenceRepo.sort
            rankRepo.getIdVisibleList()

            preferenceRepo.sort
            rankRepo.getIdVisibleList()
            noteRepo.getList(sort, bin = false, isOptimal = false, filterVisible = false)

            preferenceRepo.sort
            rankRepo.getIdVisibleList()
            noteRepo.getList(sort, bin = false, isOptimal = false, filterVisible = false)
            noteCallback.notifyNoteBind(itemList, rankIdVisibleList)
        }
    }

    @Test fun notifyInfoBind() = startCoTest {
        interactor.notifyInfoBind(callback = null)

        coEvery { bindRepo.getNotificationCount() } returns null
        interactor.notifyInfoBind(infoCallback)

        val count = Random.nextInt()
        coEvery { bindRepo.getNotificationCount() } returns count
        interactor.notifyInfoBind(infoCallback)

        coVerifySequence {
            bindRepo.getNotificationCount()

            bindRepo.getNotificationCount()
            infoCallback.notifyInfoBind(count)
        }
    }

}
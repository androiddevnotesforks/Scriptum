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
        TODO("nullable")

        val rankIdVisibleList = data.rankIdVisibleList
        val itemList = data.itemList

        coEvery { rankRepo.getIdVisibleList() } returns rankIdVisibleList
        coEvery {
            noteRepo.getList(any(), bin = false, optimal = false, filterVisible = false)
        } returns itemList

        interactor.notifyNoteBind(callback = null)

        val firstSort = TestData.sort
        val secondSort = TestData.sort

        every { preferenceRepo.sort } returns firstSort
        interactor.notifyNoteBind(noteCallback)

        every { preferenceRepo.sort } returns secondSort
        interactor.notifyNoteBind(noteCallback)

        coVerifySequence {
            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            noteRepo.getList(firstSort, bin = false, optimal = false, filterVisible = false)

            noteCallback.notifyNoteBind(itemList, rankIdVisibleList)

            rankRepo.getIdVisibleList()
            preferenceRepo.sort
            noteRepo.getList(secondSort, bin = false, optimal = false, filterVisible = false)

            noteCallback.notifyNoteBind(itemList, rankIdVisibleList)
        }
    }

    @Test fun notifyInfoBind() = startCoTest {
        TODO("nullable")

        val count = Random.nextInt()

        coEvery { bindRepo.getNotificationCount() } returns count

        interactor.notifyInfoBind(callback = null)
        interactor.notifyInfoBind(infoCallback)

        coVerifySequence {
            infoCallback.notifyInfoBind(count)
        }
    }

}
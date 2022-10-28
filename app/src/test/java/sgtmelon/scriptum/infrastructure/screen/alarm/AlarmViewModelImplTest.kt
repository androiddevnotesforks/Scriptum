package sgtmelon.scriptum.infrastructure.screen.alarm

import io.mockk.confirmVerified
import io.mockk.impl.annotations.MockK
import org.junit.After
import org.junit.Test
import sgtmelon.scriptum.cleanup.data.repository.room.callback.NoteRepo
import sgtmelon.scriptum.data.repository.preferences.PreferencesRepo
import sgtmelon.scriptum.domain.useCase.alarm.DeleteNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.SetNotificationUseCase
import sgtmelon.scriptum.domain.useCase.alarm.ShiftDateIfExistUseCase
import sgtmelon.scriptum.domain.useCase.preferences.GetMelodyListUseCase
import sgtmelon.scriptum.testing.parent.ParentLiveDataTest

/**
 * Test for [AlarmViewModelImpl].
 */
class AlarmViewModelImplTest : ParentLiveDataTest() {

    //region Setup

    @MockK lateinit var preferencesRepo: PreferencesRepo
    @MockK lateinit var noteRepo: NoteRepo
    @MockK lateinit var getMelodyList: GetMelodyListUseCase
    @MockK lateinit var setNotification: SetNotificationUseCase
    @MockK lateinit var deleteNotification: DeleteNotificationUseCase
    @MockK lateinit var shiftDateIfExist: ShiftDateIfExistUseCase

    private val viewModel by lazy {
        AlarmViewModelImpl(
            preferencesRepo, noteRepo, getMelodyList, setNotification, deleteNotification,
            shiftDateIfExist
        )
    }

    @After override fun tearDown() {
        super.tearDown()
        confirmVerified(
            preferencesRepo, noteRepo, getMelodyList, setNotification, deleteNotification,
            shiftDateIfExist
        )
    }

    //endregion

    @Test fun setup() {
        TODO()
    }

    @Test fun getState() {
        TODO()
    }

    @Test fun getNoteItem() {
        TODO()
    }

    @Test fun getAlarmState() {
        TODO()
    }

    @Test fun postpone() {
        TODO()
    }

    @Test fun onReceiveUnbindNote() {
        TODO()
    }
}
package sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.develop

import kotlinx.coroutines.flow.Flow

interface DevelopViewModel {

    val randomNoteId: Flow<Long>

    fun resetPreferences()
}
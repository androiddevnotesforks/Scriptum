package sgtmelon.scriptum.cleanup.domain.interactor.callback.preference

import sgtmelon.scriptum.cleanup.domain.interactor.impl.preference.NotePreferenceInteractor
import sgtmelon.scriptum.cleanup.domain.model.annotation.Color
import sgtmelon.scriptum.cleanup.domain.model.annotation.SavePeriod
import sgtmelon.scriptum.cleanup.presentation.screen.vm.callback.preference.INotePreferenceViewModel

/**
 * Interface for communication [INotePreferenceViewModel] with [NotePreferenceInteractor].
 */
interface INotePreferenceInteractor {

    @Color val defaultColor: Int

    fun getDefaultColorSummary(): String?

    fun updateDefaultColor(@Color value: Int): String?


    @SavePeriod val savePeriod: Int

    fun getSavePeriodSummary(): String?

    fun updateSavePeriod(value: Int): String?

}
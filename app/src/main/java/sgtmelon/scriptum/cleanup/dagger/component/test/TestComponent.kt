package sgtmelon.scriptum.cleanup.dagger.component.test

import dagger.Subcomponent
import sgtmelon.scriptum.domain.useCase.note.GetCopyTextUseCase

/**
 * Component only for test usage.
 */
@Subcomponent
interface TestComponent {

    fun getCopyTextUseCase(): GetCopyTextUseCase

}
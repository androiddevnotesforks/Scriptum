package sgtmelon.scriptum.domain.model.key

import sgtmelon.scriptum.domain.model.key.DotAnimType.COUNT
import sgtmelon.scriptum.domain.model.key.DotAnimType.SPAN
import sgtmelon.scriptum.presentation.control.anim.DotAnimControl

/**
 * Class identifying type of animation for [DotAnimControl].
 *
 * Description:
 * [COUNT] - create strings with different count of dots.
 * [SPAN] - create fix length string but with different tinting dots.
 *
 * Disadvantages:
 * [COUNT] - if text gravity "center" it will looks ugly, due to different string length.
 * [SPAN] - it can't be set for preference summaries
 */
enum class DotAnimType { COUNT, SPAN }
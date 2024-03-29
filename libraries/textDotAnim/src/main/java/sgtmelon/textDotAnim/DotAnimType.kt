package sgtmelon.textDotAnim

import sgtmelon.textDotAnim.DotAnimType.COUNT
import sgtmelon.textDotAnim.DotAnimType.SPAN

/**
 * Class identifying type of animation for [DotAnimationImpl].
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
package sgtmelon.scriptum.infrastructure.model.exception

/**
 * Exception for detecting cases when use 2 arrays/lists and they have different sizes.
 *
 * Data class needed mainly for testing.
 */
data class DifferentSizeException(
    val firstSize: Int,
    val secondSize: Int
) : IndexOutOfBoundsException(
    "First array or list have size = $firstSize, second one have size = $secondSize"
)
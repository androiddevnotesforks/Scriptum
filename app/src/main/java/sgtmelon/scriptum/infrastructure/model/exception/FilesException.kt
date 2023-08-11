package sgtmelon.scriptum.infrastructure.model.exception

/**
 * Exception for detecting cases when work with files goes wrong.
 */
sealed class FilesException(message: String) : Throwable(message) {

    object OpenInputStream : FilesException("Can't open input stream")

    object ContentInsert : FilesException("Can't insert into contentResolver")

    object OpenOutputStream : FilesException("Can't open output stream")

}
package sgtmelon.scriptum.infrastructure.model.key

import androidx.annotation.StringRes
import sgtmelon.scriptum.R

/**
 * Class describes errors with messages to final user.
 */
sealed class AppError(@StringRes val messageId: Int) {

    object Unknown : AppError(R.string.error_unknown)

    object StartActivity : AppError(R.string.error_start_app)

    sealed class File(@StringRes messageId: Int) : AppError(messageId) {
        object NotFound : File(R.string.error_file_not_found)
        object Read : File(R.string.error_file_read)
        object Data : File(R.string.error_file_convert_data)
        object Decode : File(R.string.error_file_decode)
        object Damaged : File(R.string.error_file_damaged)
        object Create : File(R.string.error_file_create)
    }
}
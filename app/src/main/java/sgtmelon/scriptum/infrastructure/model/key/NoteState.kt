package sgtmelon.scriptum.infrastructure.model.key

/**
 * Class identifying result of permission request:
 *
 * [CREATE] - Note was created and not exists in database.
 * [EXIST] - Note already exists and data saved in database.
 * [DELETE] - Note exists, but placed in recycler bin.
 */
enum class NoteState { CREATE, EXIST, DELETE }
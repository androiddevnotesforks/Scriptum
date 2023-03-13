package sgtmelon.scriptum.infrastructure.model.key.preference

/**
 * Describes standard note types.
 *
 * Be careful with order.
 *
 * This model used inside xml/shortcuts.xml (serialization), be carefully with:
 * - Moving this class into another directory
 */
enum class NoteType { TEXT, ROLL }
package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

/**
 * Ключи для передачи данных между классами
 */
@StringDef({
        IntentDef.STATE_PAGE,
        IntentDef.STATE_OPEN,

        IntentDef.NOTE_CREATE,
        IntentDef.NOTE_ID,
        IntentDef.NOTE_TYPE,

        IntentDef.RANK_EMPTY
})
public @interface IntentDef {

    String STATE_PAGE = "INTENT_STATE_PAGE",
            STATE_OPEN = "INTENT_STATE_OPEN";

    String NOTE_CREATE = "INTENT_NOTE_CREATE",
            NOTE_ID = "INTENT_NOTE_ID",
            NOTE_TYPE = "INTENT_NOTE_TYPE";

    String RANK_EMPTY = "INTENT_RANK_EMPTY";

}

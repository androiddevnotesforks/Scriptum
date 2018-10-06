package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения фрагментов после поворота экрана
 */
@StringDef({
        FragmentDef.INFO,

        FragmentDef.RANK,
        FragmentDef.NOTES,
        FragmentDef.BIN,

        FragmentDef.TEXT,
        FragmentDef.ROLL
})
public @interface FragmentDef {

    String INFO = "FRG_INFO";

    String RANK = "FRG_RANK",
            NOTES = "FRG_NOTES",
            BIN = "FRG_BIN";

    String TEXT = "FRG_TEXT",
            ROLL = "FRG_ROLL";

}

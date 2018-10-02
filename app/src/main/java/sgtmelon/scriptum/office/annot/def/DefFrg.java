package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.StringDef;

/**
 * Ключи для нахождения фрагментов после поворота экрана
 */
@StringDef({
        DefFrg.INFO,

        DefFrg.RANK,
        DefFrg.NOTES,
        DefFrg.BIN,

        DefFrg.TEXT,
        DefFrg.ROLL
})
public @interface DefFrg {

    String INFO = "FRG_INFO";

    String RANK = "FRG_RANK",
            NOTES = "FRG_NOTES",
            BIN = "FRG_BIN";

    String TEXT = "FRG_TEXT",
            ROLL = "FRG_ROLL";

}

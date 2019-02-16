package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Ключи темы приложения
 */
@IntDef({
        ThemeDef.light,
        ThemeDef.dark
})
public @interface ThemeDef {

    int light = 0, dark = 1;

}
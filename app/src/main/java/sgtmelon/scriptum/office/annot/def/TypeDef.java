package sgtmelon.scriptum.office.annot.def;

import androidx.annotation.IntDef;

/**
 * Аннотация для различных типов
 */
@IntDef({TypeDef.text, TypeDef.roll})
public @interface TypeDef {

    // TODO: 01.11.2018 переделай
    int text = 0, roll = 1; //Вынесены в главный, для доступа dataBinding

    @IntDef({TypeDef.Roll.read, TypeDef.Roll.write})
    @interface Roll{
        int read = 0, write = 1;
    }

}

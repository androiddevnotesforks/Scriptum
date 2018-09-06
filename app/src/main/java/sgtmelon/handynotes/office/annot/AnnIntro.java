package sgtmelon.handynotes.office.annot;

import androidx.annotation.StringDef;
import sgtmelon.handynotes.R;

@StringDef({AnnIntro.PAGE})
public @interface AnnIntro {

    int[] icon = new int[]{
            R.drawable.ic_add,
            R.drawable.ic_cancel_off,
            R.drawable.ic_bind_roll,
            R.drawable.ic_bracket_right
    };

    int[] title = new int[]{
            R.string.info_intro_title_1,
            R.string.info_intro_title_2,
            R.string.info_intro_title_3,
            R.string.info_intro_title_4

    };

    int[] details = new int[]{
            R.string.info_intro_details_1,
            R.string.info_intro_details_2,
            R.string.info_intro_details_3,
            R.string.info_intro_details_4
    };

    String PAGE = "INTRO_PAGE";

}
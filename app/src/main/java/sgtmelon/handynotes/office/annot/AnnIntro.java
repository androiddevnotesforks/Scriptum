package sgtmelon.handynotes.office.annot;

import androidx.annotation.StringDef;
import sgtmelon.handynotes.R;

@StringDef({AnnIntro.PAGE})
public @interface AnnIntro {

    int[] icon = new int[]{
            R.drawable.ic_note_add,
            R.drawable.ic_palette,
            R.drawable.ic_bind_roll,
            R.drawable.ic_rank,
            R.drawable.ic_visible_off,
            R.drawable.ic_settings,
            R.drawable.ic_bin
    };

    int[] title = new int[]{
            R.string.info_intro_title_1,
            R.string.info_intro_title_2,
            R.string.info_intro_title_3,
            R.string.info_intro_title_4,
            R.string.info_intro_title_5,
            R.string.info_intro_title_6,
            R.string.info_intro_title_7

    };

    int[] details = new int[]{
            R.string.info_intro_details_1,
            R.string.info_intro_details_2,
            R.string.info_intro_details_3,
            R.string.info_intro_details_4,
            R.string.info_intro_details_5,
            R.string.info_intro_details_6,
            R.string.info_intro_details_7
    };

    int count = icon.length;

    String PAGE = "INTRO_PAGE";

}
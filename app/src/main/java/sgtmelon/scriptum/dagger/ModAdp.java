package sgtmelon.scriptum.dagger;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.app.adapter.AdpNote;
import sgtmelon.scriptum.app.adapter.AdpRank;
import sgtmelon.scriptum.app.adapter.AdpRoll;

@Module
public class ModAdp {

    @Provides
    public AdpRank provideAdpRank() {
        return new AdpRank();
    }

    @Provides
    public AdpNote provideAdpNote() {
        return new AdpNote();
    }

    @Provides
    public AdpRoll provideAdpRoll(Context context){
        return new AdpRoll(context);
    }

}

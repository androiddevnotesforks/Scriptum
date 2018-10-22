package sgtmelon.scriptum.dagger;

import dagger.Module;
import dagger.Provides;
import sgtmelon.scriptum.office.st.StCheck;
import sgtmelon.scriptum.office.st.StDrag;
import sgtmelon.scriptum.office.st.StOpen;
import sgtmelon.scriptum.office.st.StPage;

@Module
public class ModSt {

    @Provides
    public StOpen provideStOpen() {
        return new StOpen();
    }

    @Provides
    public StPage provideStPage() {
        return new StPage();
    }

    @Provides
    public StDrag provideStDrag() {
        return new StDrag();
    }

    @Provides
    public StCheck provideStCheck() {
        return new StCheck();
    }

}

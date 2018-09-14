package sgtmelon.scriptum.dagger.component;

import javax.inject.Singleton;

import dagger.Component;
import sgtmelon.scriptum.app.view.act.ActMain;
import sgtmelon.scriptum.dagger.module.ModMain;

@Singleton
@Component(modules = ModMain.class)
public interface ComMain {

    void inject(ActMain actMain);

}

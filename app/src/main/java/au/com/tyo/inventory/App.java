package au.com.tyo.inventory;

import android.content.Context;
import android.content.Intent;

import au.com.tyo.app.CommonApp;
import au.com.tyo.app.PageAgent;
import au.com.tyo.inventory.ui.UI;
import au.com.tyo.inventory.ui.page.PageCommon;
import au.com.tyo.inventory.ui.page.PageInitializer;
import au.com.tyo.services.HttpJavaNet;
import au.com.tyo.services.HttpPool;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 24/11/17.
 */

public class App extends CommonApp<UI, Controller> implements Controller {

    private AppData appData;

    static {
        PageAgent.setPagesPackage(PageCommon.class.getPackage().getName());
    }

    public App(Context context) {
        super(context);

        appData = new AppData(this, context);
    }

    @Override
    public boolean hasUserLoggedIn() {
        return appData.getApi().getAuthentication().hasConsumerKeyPair();
    }

    @Override
    public AppData getAppData() {
        return appData;
    }

    @Override
    public void initializeOnce() {
        HttpPool.setHttpConnectionClass(HttpJavaNet.class);

        super.initializeOnce();

        PageInitializer.setInstance(new PageInitializer());
    }

    @Override
    public void initializeInBackgroundThread(Context context) {
        super.initializeInBackgroundThread(context);

        appData.load();
    }

    @Override
    public void onMessageCustomOne() {
        getAppData().notifyDataCacheObservers();
    }

    @Override
    public void bindDataFromOtherApps(Intent intent) {

    }
}

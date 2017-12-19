package au.com.tyo.inventory.ui.page;

import android.app.Activity;

import au.com.tyo.app.ui.page.PageCommonList;
import au.com.tyo.inventory.Controller;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class PageMain extends PageCommonList<Controller> {

    /**
     * @param controller
     * @param activity
     */
    public PageMain(Controller controller, Activity activity) {
        super(controller, activity);
    }

    @Override
    public void onActivityStart() {
        super.onActivityStart();

        if (!getController().hasUserLoggedIn()) {
            getController().getUi().gotoLoginPage();

            finish();
        }

        startBackgroundTask();
    }

    @Override
    public void run() {
        //
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        // for debugging
        getController().getAppData().getApi().createCurlCommands();

        getController().getAppData().loadProducts();


    }
}

package au.com.tyo.inventory.ui.page;

import android.app.Activity;

import au.com.tyo.app.Controller;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class PageMain extends PageCommon {

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

        if (!controller.hasUserLoggedIn()) {
            controller.getUi().gotoLoginPage();

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
        controller.getAppData().getApi().createCurlCommands();

        controller.getAppData().loadProducts();


    }
}

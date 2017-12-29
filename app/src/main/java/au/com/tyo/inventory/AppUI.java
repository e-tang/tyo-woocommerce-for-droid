package au.com.tyo.inventory;

import au.com.tyo.app.ui.UIBase;
import au.com.tyo.inventory.ui.UI;
import au.com.tyo.inventory.ui.activity.ActivityLogin;
import au.com.tyo.inventory.ui.activity.ActivityProductDetails;
import au.com.tyo.inventory.ui.activity.ActivityStockIn;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class AppUI extends UIBase implements UI {

    public AppUI(Controller controller) {
        super(controller);
    }

    @Override
    public void gotoLoginPage() {
        gotoPage(ActivityLogin.class);
    }

    @Override
    public void gotoProductDetailsPage() {
        gotoPage(ActivityProductDetails.class);
    }

    @Override
    public void gotoProductStockInPage() {
        gotoPage(ActivityStockIn.class);
    }
}

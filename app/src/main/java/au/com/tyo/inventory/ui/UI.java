package au.com.tyo.inventory.ui;

import au.com.tyo.inventory.model.Product;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 24/11/17.
 */

public interface UI extends au.com.tyo.app.ui.UI {

    void gotoLoginPage();

    void gotoProductDetailsPage();

    void gotoProductStockInPage(Product product);

    void gotoBarcodeScannerPage();
}

package au.com.tyo.inventory.ui;

import android.net.Uri;

import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.model.ProductForm;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 24/11/17.
 */

public interface UI extends au.com.tyo.app.ui.UI {

    void gotoLoginPage();

    void gotoProductDetailsPage(Product product);

    void gotoProductDetailsPage(ProductForm productForm);

    void gotoProductStockInPage(Product product);

    void gotoBarcodeScannerPage();

    void gotoBarcodeInfoPage(ProductBarcode productBarcode);

    void openCloudPrintDialog(Uri docUri, String docMimeType, String title);
}

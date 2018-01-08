package au.com.tyo.inventory;

import java.util.HashMap;
import java.util.Map;

import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.model.ProductForm;
import au.com.tyo.inventory.model.ProductStockInForm;
import au.com.tyo.inventory.ui.UI;
import au.com.tyo.inventory.ui.activity.ActivityLogin;
import au.com.tyo.inventory.ui.activity.ActivityProductDetails;
import au.com.tyo.inventory.ui.activity.ActivityScan;
import au.com.tyo.inventory.ui.activity.ActivityStockIn;
import au.com.tyo.json.android.JsonFormUI;

import static au.com.tyo.app.Constants.DATA;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class AppUI extends JsonFormUI implements UI {

    private Controller controller;

    public AppUI(Controller controller) {
        super(controller);
        this.controller = controller;
    }

    @Override
    public void gotoLoginPage() {
        gotoPage(ActivityLogin.class);
    }

    @Override
    public void gotoProductDetailsPage(Product product) {
        gotoProductDetailsPage(new ProductForm(controller.getAppData(), product.getId()));
    }

    @Override
    public void gotoProductDetailsPage(ProductForm productForm) {
        controller.setParcel(productForm);
        gotoPage(ActivityProductDetails.class);
    }

    @Override
    public void gotoProductStockInPage(Product product) {
        Map map = new HashMap<>();
        map.put(DATA, new ProductStockInForm(controller.getAppData(), product.getId()));
        editForm(ActivityStockIn.class, map);
    }

    @Override
    public void gotoBarcodeScannerPage() {
        gotoPage(ActivityScan.class);
    }

    @Override
    public void gotoBarcodeInfoPage(ProductBarcode productBarcode) {

    }
}

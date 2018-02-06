package au.com.tyo.inventory;

import android.content.Context;
import android.net.Uri;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.model.ProductForm;
import au.com.tyo.inventory.model.ProductStockInForm;
import au.com.tyo.inventory.ui.UI;
import au.com.tyo.inventory.ui.activity.ActivityBarcode;
import au.com.tyo.inventory.ui.activity.ActivityImport;
import au.com.tyo.inventory.ui.activity.ActivityLogin;
import au.com.tyo.inventory.ui.activity.ActivityProductDetails;
import au.com.tyo.inventory.ui.activity.ActivityScan;
import au.com.tyo.inventory.ui.activity.ActivityStockIn;
import au.com.tyo.json.android.JsonFormUI;
import au.com.tyo.services.android.google.activity.CloudPrintDialog;

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
    public void gotoBarcodeScannerPage(int op) {
        gotoPage(ActivityScan.class, op);
    }

    @Override
    public void gotoBarcodeInfoPage(ProductBarcode productBarcode) {
        gotoPageWithData(ActivityBarcode.class, productBarcode);
    }

    @Override
    public void openCloudPrintDialog(Uri docUri, String docMimeType, String title, int widthMills, int heightMills) {
        Context context = getCurrentPage().getActivity();
        CloudPrintDialog.printDocument(context, docUri, docMimeType, title, widthMills, heightMills);
    }

    @Override
    public void gotoImportPage(List<File> files) {
//        Uri[] uris = new Uri[files.size()];
//        for (int i = 0; i < files.size(); ++i)
//            uris[i] = Uri.fromFile(files.get(i));
        List uris = new ArrayList();
        for (File file : files)
            uris.add(file.getAbsolutePath());

        gotoPageWithData(ActivityImport.class, uris);
    }
}

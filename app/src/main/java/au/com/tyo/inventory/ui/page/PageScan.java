/*
 * Copyright (c) 2017 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */

package au.com.tyo.inventory.ui.page;

import android.app.Activity;
import android.widget.Toast;

import com.google.zxing.Result;

import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.ui.fragment.ScannerFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 29/12/17.
 */

public class PageScan extends PageCommon implements ZXingScannerView.ResultHandler {

    private String barcode;

    ScannerFragment scannerFragment;

    /**
     * @param controller
     * @param activity
     */
    public PageScan(Controller controller, Activity activity) {
        super(controller, activity);
    }

    @Override
    public void setupComponents() {
        super.setupComponents();

        scannerFragment = new ScannerFragment();
        scannerFragment.setResultHandler(this);
        addFragmentToContainer(scannerFragment);
    }

    @Override
    public void handleResult(Result result) {
        showProgressBar("checking product");

        barcode = result.getText();

        startBackgroundTask();
    }

    @Override
    public void run() {
        if (null == barcode)
            return;

        ProductBarcode productBarcode = ProductBarcode.parse(barcode);

        Product product = getController().getAppData().lookupProductById(productBarcode.getId());

        setResult(product);
    }

    @Override
    protected void onPageBackgroundTaskFinished() {
        hideProgressBar();

        Product product = (Product) getResult();

        if (null != product)
            getController().getUi().gotoProductStockInPage(listItem.getProduct());
        else {
            Toast.makeText(getActivity(), "Can't find the product", Toast.LENGTH_SHORT).show();
            scannerFragment.resumePreview();
        }

    }
}

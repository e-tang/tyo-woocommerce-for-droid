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

        ScannerFragment fragment = new ScannerFragment();
        fragment.setResultHandler(this);
        addFragmentToContainer(fragment);
    }

    @Override
    public void handleResult(Result result) {
        String text = result.getText();

        ProductBarcode productBarcode = ProductBarcode.parse(text);

        Product product = getController().getAppData().lookupProductById(productBarcode.getId());
    }
}

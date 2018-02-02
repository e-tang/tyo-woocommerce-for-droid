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

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.Toast;

import com.google.zxing.Result;

import au.com.tyo.android.CommonPermission;
import au.com.tyo.app.Constants;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.Product;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.ui.fragment.ScannerFragment;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 29/12/17.
 */

public class PageScan extends PageCommon implements ZXingScannerView.ResultHandler {

    private final MediaPlayer player;
    private String barcode;

    ScannerFragment scannerFragment;

    private int op;

    /**
     * @param controller
     * @param activity
     */
    public PageScan(Controller controller, Activity activity) {
        super(controller, activity);

        setRequiredPermissions(CommonPermission.PERMISSIONS_CAMERA);

        player = MediaPlayer.create(activity, R.raw.bell);
    }

    @Override
    public void setupComponents() {
        super.setupComponents();

        scannerFragment = new ScannerFragment();
        scannerFragment.setResultHandler(this);
        addFragmentToContainer(scannerFragment);
    }

    @Override
    public void bindData(Intent intent) {
        super.bindData(intent);

        op = intent.getIntExtra(Constants.DATA, au.com.tyo.inventory.Constants.OPERATION_SCAN_PRODUCT);
    }

    @Override
    public void handleResult(Result result) {
        player.start();

        showProgressBar("checking product");

        barcode = result.getText();

        startBackgroundTask();
    }

    @Override
    public void run() {
        if (null == barcode)
            return;

        if (op == au.com.tyo.inventory.Constants.OPERATION_SCAN_PRODUCT) {
            String[] tokens = ProductBarcode.parse(barcode);

            if (null != tokens && tokens.length > 1) {
                Product product = getController().getAppData().lookupProductById(Integer.parseInt(tokens[1]));

                setResult(product);
            }
        }
    }

    @Override
    protected void onPageBackgroundTaskFinished() {
        hideProgressBar();

        if (op == au.com.tyo.inventory.Constants.OPERATION_SCAN_PRODUCT) {
            Product product = (Product) getResult();

            if (null != product) {
                getController().getUi().gotoProductStockInPage(product);
                finish();
            } else {
                Toast.makeText(getActivity(), "Can't find the product", Toast.LENGTH_SHORT).show();
                scannerFragment.resumePreview();
            }
        }
        else {
            if (barcode.contains("cs_") && barcode.contains("ck_")) {
                String tokens[] = barcode.split("\\|");
                String consumerKey = null, consumerSecret = null;
                for (int i = 0; i < tokens.length; ++i) {
                    if (tokens[i].startsWith("ck_"))
                        consumerKey = tokens[i];
                    else
                        consumerSecret = tokens[i];
                }
                getController().getAppData().getApi().getAuthentication().saveConsumerKeyPair(consumerKey, consumerSecret);
                getController().getAppData().getApi().createAuthorizationStrings();
            }

            if (getController().getAppData().getApi().getAuthentication().hasConsumerKeyPair())
                finish();
            else
                scannerFragment.resumePreview();
        }
    }

    @Override
    public void onRequestedPermissionsDenied(String permission) {
        if (permission.equals(Manifest.permission.CAMERA))
            Toast.makeText(getActivity(), getResources().getString(R.string.need_camera_permission), Toast.LENGTH_SHORT).show();
    }
}

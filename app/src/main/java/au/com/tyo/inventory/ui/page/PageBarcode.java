/*
 * Copyright (c) 2018 TYONLINE TECHNOLOGY PTY. LTD. (TYO Lab)
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
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

import au.com.tyo.android.utils.BitmapUtils;
import au.com.tyo.common.ui.CardBox;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.utils.StringUtils;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 8/1/18.
 */

public class PageBarcode extends PageCommon implements View.OnClickListener {

    private static final String TAG = "PageBarcode";

    private ProductBarcode productBarcode;

    private Bitmap qrCodeBitmap;

    /**
     * @param controller
     * @param activity
     */
    public PageBarcode(Controller controller, Activity activity) {
        super(controller, activity);
        setContentViewResId(R.layout.page_barcode);
    }

    @Override
    public void bindData() {
        super.bindData();

        productBarcode = (ProductBarcode) getController().getParcel();
        qrCodeBitmap = productBarcode.getQrCodeBitmap();
    }

    @Override
    public void onDataBound() {
        super.onDataBound();

        CardBox cardBox = (CardBox) findViewById(R.id.barcode_box);
        cardBox.addPreviewObject(qrCodeBitmap);

        Button button = (Button) findViewById(R.id.btn_print_barcode);
        button.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        String pngFile = getController().getAppData().getBarcodeImageCache().getCacheFilePathName(productBarcode.getProductId() + ".png");
        File file = new File(pngFile);
        Uri uri;
        if (!file.exists() || file.length() == 0) {
            try {
                BitmapUtils.toPNG(qrCodeBitmap, file);
            } catch (IOException e) {
                Log.e(TAG, StringUtils.exceptionStackTraceToString(e));
                file = null;
            }
        }
        if (null != file) {
            uri = Uri.fromFile(file);
            getController().getUi().openCloudPrintDialog(uri, "image/png", productBarcode.getProduct().getName());
        }
        else
            Toast.makeText(getActivity(), getResources().getString(R.string.error_cache_barcode), Toast.LENGTH_SHORT);
    }
}

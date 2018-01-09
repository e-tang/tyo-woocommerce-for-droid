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
import android.content.Context;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;

import java.util.Date;

import au.com.tyo.android.utils.SimpleDateUtils;
import au.com.tyo.app.ui.page.PageWebView;
import au.com.tyo.common.ui.CardBox;
import au.com.tyo.inventory.Controller;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.ProductBarcode;
import au.com.tyo.inventory.model.ProductForm;
import au.com.tyo.json.android.pages.PageForm;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/12/17.
 */

public class PageProductDetails extends PageForm<Controller> {

    private ProductForm productForm;

    /**
     * @param controller
     * @param activity
     */
    public PageProductDetails(Controller controller, Activity activity) {
        super(controller, activity);

        setSubpage(true);
    }

    @Override
    public void onDataBound() {
        super.onDataBound();
        productForm = (ProductForm) getForm();
    }

    @Override
    protected void onFormCheckFailed() {

    }

    @Override
    protected void saveFormData(Object form) {

    }

    @Override
    public void onFormClick(Context context, String key, String text) {

    }
    @Override
    protected void createMenu(MenuInflater menuInflater, Menu menu) {
        super.createMenu(menuInflater, menu);
        menuInflater.inflate(R.menu.barcode_only, menu);
    }

    @Override
    protected boolean onMenuCreated(Menu menu) {
        boolean ret = super.onMenuCreated(menu);

        getActionBarMenu().setMenuItemVisible(R.id.menuItemOne, false);

        return ret;
    }

    @Override
    public String formatDateTime(String key, Date date) {
        return SimpleDateUtils.toSlashDelimAussieDate(date);
    }

    @Override
    public void onFieldClick(View v) {
        if (v instanceof CardBox) {
            CardBox cardBox = (CardBox) v;
            final java.lang.Object imageUrl = cardBox.getPreviewItem();
            if (null != imageUrl)
                getController().getUi().viewHtmlPageFromAsset("html/center_image.html", "Product Image", null, new PageWebView.WebPageListener() {
                    @Override
                    public void onPageFinishedLoading(WebView webView, String url) {
                        PageWebView.call(webView, "updateImage", new String[] {imageUrl.toString()}, null);
                    }

                    @Override
                    public void onReceiveValue(String value) {

                    }
                });
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        if (item.getItemId() == R.id.menuItemBarcode) {
            getController().getUi().gotoBarcodeInfoPage(ProductBarcode.toParcel(productForm,
                    productForm.getProduct().getPermalink()));
            return true;
        }
        return super.onMenuItemClick(item);
    }

    protected void setMenuItemEdit() {
        // show no info
    }
}

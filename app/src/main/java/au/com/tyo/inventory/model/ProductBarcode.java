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

package au.com.tyo.inventory.model;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 29/12/17.
 */

import android.net.Uri;

/**
 * v1:
 *
 * link?code=1:id:sku
 *
 */
public class ProductBarcode extends ProductParcel {

    private static final String VERSION = "1";
    private static final String SEPARATOR = "|";

    private String url;

    public ProductBarcode(ProductParcel parcel, String url) {
        super(parcel);
        this.url = url;
    }

    public String createBarcodeText() {
        return url + "?code=" + VERSION + SEPARATOR + getProductId() + SEPARATOR + getProduct().getSku();
    }

    /**
     *
     * @param text
     * @return
     */
    public static String[] parse(String text) {
        try {
            Uri uri = Uri.parse(text);
            String barcode = uri.getQueryParameter("code");
            String[] tokens = barcode.split(SEPARATOR);
            String url = uri.getPath();
            return new String[] {VERSION, tokens[0], tokens[1], url};
        }
        catch (Exception e) {}
        return null;
    }

    public static ProductBarcode toParcel(ProductParcel parcel, String url) {
        return new ProductBarcode(parcel, url);
    }
}

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
public class ProductBarcode {

    private static final String VERSION = "1";
    private static final String SEPARATOR = "|";

    private int id;

    private int sku;

    private String url;

    public ProductBarcode(int id, int sku, String url) {
        this.id = id;
        this.sku = sku;
        this.url = url;
    }

    public String createBarcodeText() {
        return url + "?code=" + VERSION + SEPARATOR + id + SEPARATOR + sku;
    }

    public static ProductBarcode parse(String text) {
        try {
            Uri uri = Uri.parse(text);
            String barcode = uri.getQueryParameter("code");
            String[] tokens = barcode.split(SEPARATOR);
            String url = uri.getPath();
            return new ProductBarcode(Integer.parseInt(tokens[0]), Integer.parseInt(tokens[1]), url);
        }
        catch (Exception e) {}
        return null;
    }

    public int getId() {
        return id;
    }

    public int getSku() {
        return sku;
    }

    public void setSku(int sku) {
        this.sku = sku;
    }
}

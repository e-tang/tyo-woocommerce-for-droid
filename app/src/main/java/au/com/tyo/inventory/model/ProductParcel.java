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

package au.com.tyo.inventory.model;

import java.util.Map;

import au.com.tyo.json.android.utils.FormBase;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 2/1/18.
 */

public class ProductParcel extends FormBase {

    private int productId;

    private ProductContainer productContainer;

    public ProductParcel(ProductContainer productContainer, int productId) {
        this.productContainer = productContainer;
        this.productId = productId;
    }

    public ProductParcel(ProductParcel parcel) {
        this(parcel.getProductContainer(), parcel.getProductId());
    }

    public Product getProduct() {
        return productContainer.getProduct(productId);
    }

    @Override
    public Object getValue(String key) {
        if (key.equals("images"))
            return getProductImageUrl();
        return getProduct().get(key);
    }

    @Override
    public Map getFormKeyValueMap() {
        return getProduct();
    }

    public String getProductImageUrl() {
        return getProduct().getImageUrl();
    }

    @Override
    public void put(String key, Object value) {
        getProduct().put(key, value);
    }

    public ProductParcel toParcel() {
        return new ProductParcel(this);
    }

    public int getProductId() {
        return productId;
    }

    public ProductContainer getProductContainer() {
        return productContainer;
    }

    @Override
    public Map getFormMetaDataMap() {
        return null;
    }

    public String getProductSku() {
        return getProduct().getSku();
    }
}

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

import android.graphics.drawable.Drawable;
import android.view.View;

import java.util.Map;

import au.com.tyo.android.adapter.ListItem;
import au.com.tyo.inventory.AppData;
import au.com.tyo.json.FormBase;
import au.com.tyo.json.JsonForm;
import au.com.tyo.json.android.utils.FormHelper;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 19/12/17.
 */

public class ProductListItem extends FormBase implements ListItem {

    private Product product;

    public ProductListItem(Product product) {
        this.product = product;
    }

    @Override
    public int getViewType() {
        return 0;
    }

    @Override
    public CharSequence getAltText() {
        return null;
    }

    @Override
    public Drawable getImageViewDrawable() {
        return null;
    }

    @Override
    public CharSequence getText1() {
        return (CharSequence) product.get("name");
    }

    @Override
    public View.OnClickListener getImageButtonOnClickListener() {
        return null;
    }

    @Override
    public Drawable getImageButtonDrawable() {
        return null;
    }

    @Override
    public boolean shouldShowImageButton() {
        return false;
    }

    @Override
    public CharSequence getText2() {
        return null;
    }

    public Product getProduct() {
        return product;
    }

    public String getProductImageUrl() {
        return product.getImageUrl();
    }

    @Override
    public JsonForm toJsonForm() {
        return FormHelper.createForm(this);
    }

    @Override
    public Map getFormKeyValueMap() {
        return getProduct();
    }

    @Override
    public Map getFormMetaDataMap() {
        return AppData.getProductFormMetaData();
    }

    @Override
    public Object getValue(String key) {
        if (key.equals("images"))
            return getProductImageUrl();
        return product.get(key);
    }
}

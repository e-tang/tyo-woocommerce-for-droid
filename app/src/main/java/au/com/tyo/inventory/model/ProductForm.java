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

import au.com.tyo.inventory.AppData;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 19/12/17.
 */

public class ProductForm extends ProductFormBase implements ProductItem {

    public ProductForm(ProductContainer productContainer, int productId) {
        super(productContainer, productId);
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
        return (CharSequence) getValue("name");
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
        return getPriceString();
    }

    @Override
    public Map getFormMetaDataMap() {
        return AppData.getProductFormMetaData();
    }

    @Override
    public String getPriceString() {
        return String.valueOf(getProduct().getPrice());
    }

    @Override
    public String getStockString() {
        return String.valueOf(getProduct().getStock());
    }
}

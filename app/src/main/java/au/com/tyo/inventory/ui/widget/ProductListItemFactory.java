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

package au.com.tyo.inventory.ui.widget;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import au.com.tyo.android.adapter.InflaterFactory;
import au.com.tyo.android.adapter.ListItemFactory;
import au.com.tyo.common.ui.CardBox;
import au.com.tyo.inventory.R;
import au.com.tyo.inventory.model.ProductForm;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 20/12/17.
 */

public class ProductListItemFactory extends ListItemFactory {

    public ProductListItemFactory(Context context) {
        super(context, R.layout.image_text_list_cell_product);
    }

    @Override
    protected InflaterFactory.ViewHolder createViewHolder(View convertView, ViewGroup parent) {
        ViewHolder holder = (ViewHolder) super.createViewHolder(convertView, parent);

        findImageBox(holder);
        holder.tvStock = holder.view.findViewById(R.id.text3);
        return holder;
    }

    private void findImageBox(ViewHolder holder) {
        if (null == holder.imageBox && null != holder.view) {
            CardBox box = holder.view.findViewById(R.id.product_image_box);
            holder.imageBox = box;
        }
    }

    @Override
    public void bindData(InflaterFactory.ViewHolder holder, Object obj) {
        super.bindData(holder, obj);
        ViewHolder viewHolder = (ViewHolder) holder;

        findImageBox((ViewHolder) holder);

        if (null != viewHolder.imageBox) {
            ProductForm item = (ProductForm) obj;

            viewHolder.imageBox.addPreviewItem(item.getProductImageUrl());
            viewHolder.tvStock.setText(item.getStockString());
        }
    }

    protected InflaterFactory.ViewHolder newViewHolderInstance(){
        return new ViewHolder();
    }

    public static class ViewHolder extends InflaterFactory.ViewHolder {
        public CardBox imageBox;
        public TextView tvStock;
    }
}

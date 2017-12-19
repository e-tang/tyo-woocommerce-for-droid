package au.com.tyo.inventory;

import android.content.Context;
import android.util.Log;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import au.com.tyo.app.CommonAppData;
import au.com.tyo.inventory.model.Product;
import au.com.tyo.woocommerce.WooCommerceApi;
import au.com.tyo.woocommerce.WooCommerceJson;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 13/12/17.
 */

public class AppData extends CommonAppData {

    private static final String TAG = "AppData";
    //
    // API
    //
    private WooCommerceApi api;

    //
    // PARSER
    //
    private WooCommerceJson parser;

    //
    // MODELS
    //
    private List<Product> products;


    public AppData(Context context) {
        super(context);

        this.api = new WooCommerceApi(context);
        this.parser = new WooCommerceJson();
    }

    public WooCommerceApi getApi() {
        return api;
    }

    public void loadProducts() {
        String json = api.getProductsJsonString();

        Type collectionType = new TypeToken<List<Product>>(){}.getType();
        products = WooCommerceJson.getGson().fromJson(json, collectionType);

        Log.d(TAG, "products loaded: total " + products.size());
    }
}

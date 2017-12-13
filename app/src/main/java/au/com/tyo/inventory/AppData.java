package au.com.tyo.inventory;

import au.com.tyo.woocommerce.WooCommerceApi;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 13/12/17.
 */

public class AppData {

    private WooCommerceApi api;

    public AppData() {
        this.api = new WooCommerceApi();
    }

    public WooCommerceApi getApi() {
        return api;
    }
}

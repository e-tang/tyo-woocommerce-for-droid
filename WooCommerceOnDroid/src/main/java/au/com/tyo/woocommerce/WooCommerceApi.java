package au.com.tyo.woocommerce;

/**
 * Created by Eric Tang (eric.tang@tyo.com.au) on 27/11/17.
 */

public class WooCommerceApi {

    private WooCommerceAuthentication authentication;

    public WooCommerceApi() {
    }

    public WooCommerceAuthentication getAuthentication() {
        return authentication;
    }

    public void setAuthentication(WooCommerceAuthentication authentication) {
        this.authentication = authentication;
    }


}

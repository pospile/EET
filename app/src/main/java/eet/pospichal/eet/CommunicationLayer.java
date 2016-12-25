package eet.pospichal.eet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.google.zxing.client.android.CaptureActivity;
import com.siimkinks.sqlitemagic.ProductTable;
import com.siimkinks.sqlitemagic.Select;
import com.siimkinks.sqlitemagic.Update;

import eet.pospichal.eet.model.Product;

/**
 * Created by pospile on 21/12/2016.
 */
public class CommunicationLayer {
    private static CommunicationLayer ourInstance = new CommunicationLayer();

    public static CommunicationLayer getInstance() {
        return ourInstance;
    }

    private CommunicationLayer() {
    }

    private ProductsActivity product_activity;

    private AddProduktActivity add_product_activity;

    private AddCategoryActivity add_category_activity;

    private MainActivity main_activity;

    private Product prod;

    public void RegisterProductActivity(ProductsActivity activity) {
        this.product_activity = activity;
    }
    public void RegisterAddProdActivity(AddProduktActivity activity) {
        this.add_product_activity = activity;
    }
    public void RegisterMainActivity(MainActivity main) {

        main_activity = main;
    }
    public void RegisterAddCategoryActivity(AddCategoryActivity ac) {
        this.add_category_activity = ac;
    }
    public AddCategoryActivity GetLastRegisteredAddCategoryActivity(){
        return this.add_category_activity;}
    public ProductsActivity GetLastRegisteredProdActivity() {

        return product_activity;
    }
    public AddProduktActivity GetLastAddProductActivity() {

        return add_product_activity;
    }
    public MainActivity GetMainActivity() {

        return main_activity;
    }
    public void SetProductSugResult(Product prod) {

        this.prod = prod;
    }
    public Product GetProductSugResult() {

        return this.prod;
    }
    public void startScan(Context context, Activity activity) {

        Intent intent = new Intent(context,CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        activity.startActivityForResult(intent, 0);
    }

}
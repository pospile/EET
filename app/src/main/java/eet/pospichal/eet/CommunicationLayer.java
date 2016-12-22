package eet.pospichal.eet;

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

    private MainActivity main_activity;

    public void RegisterProductActivity(ProductsActivity activity) {
        this.product_activity = activity;
    }

    public void RegisterAddProdActivity(AddProduktActivity activity) {
        this.add_product_activity = activity;
    }

    public void RegisterMainActivity(MainActivity main) {
        main_activity = main;
    }

    public ProductsActivity GetLastRegisteredProdActivity() {
        return product_activity;
    }

    public AddProduktActivity GetLastAddProductActivity() {
        return add_product_activity;
    }

    public MainActivity GetMainActivity() {
        return main_activity;
    }

}
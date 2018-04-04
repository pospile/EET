package eet.pospichal.eet;

import eet.pospichal.eet.model.Category;
import eet.pospichal.eet.model.Product;
import com.siimkinks.sqlitemagic.ProductTable;

/**
 * Created by pospile on 20/12/2016.
 */
public class DatabaseHelper {
    private static DatabaseHelper ourInstance = new DatabaseHelper();

    public static DatabaseHelper getInstance() {
        return ourInstance;
    }

    private DatabaseHelper() {
    }


    public Category AddCategoryToDatabase(String name)
    {
        Category cate = new Category();
        cate.jmeno = name;
        cate.insert().execute();
        return cate;
    }

    public Product AddProduktToDatabase(String name, int cena_koruny, int cena_halere, int dan, int inter_id, String barcode, int cat_id){
        Product new_prd = new Product();
        new_prd.jmeno_produktu = name;
        new_prd.cena_koruny = cena_koruny;
        new_prd.cena_halere = cena_halere;
        new_prd.dan = dan;
        new_prd.inter_id = inter_id;
        new_prd.barcode = barcode;
        new_prd.category = cat_id;

        new_prd.insert().execute();
        return new_prd;
    }
}

package eet.pospichal.eet;

import android.util.Log;

import com.siimkinks.sqlitemagic.ProductTable;
import com.siimkinks.sqlitemagic.Select;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import eet.pospichal.eet.model.Cena;
import eet.pospichal.eet.model.Dan;
import eet.pospichal.eet.model.Product;

public class ProductHelper {

    private static ProductHelper ourInstance = new ProductHelper();

    public static ProductHelper getInstance() {
        return ourInstance;
    }

    private ProductHelper() {
        local_products = new ArrayList<Product>();
        /*
        for (int i = 0; i < 10; i++) {
            Product produkt = new Product(new Random().nextInt(10),new Random().nextInt(20), i, "bar"+i, Dan.Typ.dvacet_jedna);
            local_products.add(produkt);
        }
        */
    }


    private List<Product> local_products;

    public Product GetLocalProduct(int inter_id){
        List<Product> prod = Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.INTER_ID.is(inter_id)).limit(1).execute();
        if (!prod.isEmpty())
        {
            return prod.get(0);
        }
        return null;
    }
    public Product GetLocalProduct(String barcode){
        for (Product product:
                local_products) {
            if (barcode == product.barcode)
            {
                return product;
            }
        }
        return null;
    }

    public Cena VypoctiDanZProduktu(Cena cena){
        String to_double = cena.koruny + "." + cena.halere;
        double doubled = Double.parseDouble(to_double);
        double result = (doubled / 100) * 21;
        //result = (double)(Math.round(result)*100)/100;
        //String resulted = String.format("%.2f", result);
        result = round(result, 2);
        String res = result+"";
        String[] cen = res.split("\\.");

        cena.dan_koruny = Integer.parseInt(cen[0]);
        cena.dan_halere = Integer.parseInt(cen[1]);


        return cena;
    }

    public Cena VypoctiKorunyZHaleru(Cena cena)
    {
        if (cena.halere >= 100) {
            double result = (double) cena.halere / (double) 100;
            Log.e("INFO", result + "");
            String result_ready = String.valueOf(result);
            String koruny_add = result_ready.split("\\.")[0];
            String halere_add = result_ready.split("\\.")[1];

            Log.e("KORUNY", koruny_add + "");
            Log.e("HALERE", halere_add + "");


            cena.koruny += Integer.parseInt(koruny_add);
            cena.halere = Integer.parseInt(halere_add);
        }
        return cena;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

}

package eet.pospichal.eet;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.mancj.materialsearchbar.MaterialSearchBar;
import com.mancj.materialsearchbar.MaterialSearchBar.OnSearchActionListener;
import com.mindorks.placeholderview.PlaceHolderView;
import com.orhanobut.logger.Logger;
import com.siimkinks.sqlitemagic.CategoryTable;
import com.siimkinks.sqlitemagic.Select;

import java.util.ArrayList;
import java.util.List;

import eet.pospichal.eet.model.Category;
import eet.pospichal.eet.model.Product;
import com.siimkinks.sqlitemagic.ProductTable;


public class ProductsActivity extends SlidingActivity {

    PlaceHolderView productView;
    PlaceHolderView categoryView;
    Button add_new_produkt;
    Button add_new_category;
    MaterialSearchBar bar;
    List<Product> produkt;
    List<Category> kategorie;

    @Override
    public void init(Bundle savedInstanceState) {

        CommunicationLayer.getInstance().RegisterProductActivity(this);

        setTitle("Produkty");

        disableHeader();
        enableFullscreen();


        setPrimaryColors(
                getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.black)
        );

        setContent(R.layout.activity_products);

        produkt = new ArrayList<Product>();
        kategorie = new ArrayList<Category>();

        bar = (MaterialSearchBar)findViewById(R.id.searchBar);
        productView = (PlaceHolderView)findViewById(R.id.mrdkaView);
        productView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        productView.getBuilder().setHasFixedSize(true);

        categoryView = (PlaceHolderView)findViewById(R.id.categoryView);
        categoryView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 1));
        categoryView.getBuilder().setHasFixedSize(true);

        add_new_produkt = (Button)findViewById(R.id.btn_add_produkt);
        add_new_category = (Button)findViewById(R.id.btn_add_cat);



        add_new_produkt.setOnClickListener(addProd);
        add_new_category.setOnClickListener(addCat);
        bar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int j = produkt.size()-1; j >= 0; j--)
                {
                    produkt.remove(j);
                    productView.removeView(j);
                }

                if (charSequence.toString() == "")
                {
                    for (int j = produkt.size()-1; j >= 0; j--)
                    {
                        produkt.remove(j);
                        productView.removeView(j);
                    }
                    List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

                    Log.e("SELECT", select.size()+" velikost");

                    int index = 0;
                    for (Product prod :
                            select) {
                        Log.e("INFO", prod.jmeno_produktu);
                        ProduktType new_prod = new ProduktType(getApplicationContext(), productView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
                        produkt.add(prod);
                        productView.addView(new_prod);
                    }
                }
                else
                {
                    /*
                    View view = ProductsActivity.this.productView;
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    */

                    List<Product> select = Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.JMENO_PRODUKTU.like(charSequence.toString() + "%")).limit(100).execute();

                    Log.e("SELECT", select.size()+" velikost");

                    int index = 0;
                    for (Product prod :
                            select) {
                        Log.e("INFO", prod.jmeno_produktu);
                        ProduktType new_prod = new ProduktType(getApplicationContext(), productView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
                        produkt.add(prod);
                        productView.addView(new_prod);
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        bar.setOnSearchActionListener(new OnSearchActionListener() {
            @Override
            public void onSearchStateChanged(boolean b) {

                for (int i = produkt.size()-1; i >= 0; i--)
                {
                    produkt.remove(i);
                    productView.removeView(i);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {
                View view = ProductsActivity.this.productView;
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            @Override
            public void onButtonClicked(int i) {
                if(i == 0)
                {

                }
            }
        });

        /*
        ProduktType new_prod = new ProduktType(getApplicationContext(), productView, 0, "Produkt 1");
        productView.addView(new_prod);
        productView.addView(new ProduktType(getApplicationContext(), productView, 1, "Produkt 2"));
        */

        List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

        for (Product prod :
                select) {
            Log.e("INFO", prod.jmeno_produktu);
            produkt.add(prod);
            ProduktType new_prod = new ProduktType(getApplicationContext(), productView, produkt.size(), prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
            productView.addView(new_prod);
        }


        List<Category> categories = Select.from(CategoryTable.CATEGORY).execute();

        for (Category cat:
             categories) {
            Logger.i("Category:" + cat.jmeno);
            kategorie.add(cat);
            CategoryType catN = new CategoryType(getApplicationContext(), categoryView, kategorie.size(), cat.jmeno);
            categoryView.addView(catN);
        }

    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }

    View.OnClickListener addProd = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), AddProduktActivity.class);
            //intent.putExtra("View", );
            startActivity(intent);
        }
    };

    View.OnClickListener addCat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), AddCategoryActivity.class);
            //intent.putExtra("View", );
            startActivity(intent);
        }
    };


    public void RefreshProductList(){
        if (!produkt.isEmpty())
        {
            for (int j = produkt.size()-1; j >= 0; j--)
            {
                produkt.remove(j);
                productView.removeView(j);
            }
        }
        List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

        Log.e("SELECT", select.size()+" velikost");

        int index = 0;
        for (Product prod :
                select) {
            Log.e("INFO", prod.jmeno_produktu);
            ProduktType new_prod = new ProduktType(getApplicationContext(), productView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
            produkt.add(prod);
            productView.addView(new_prod);
        }
        enableFullscreen();
    }
    public void RefreshProductToCategory(String category){

        Logger.e("Looking for category: " + category);

        if (!produkt.isEmpty())
        {
            for (int j = produkt.size()-1; j >= 0; j--)
            {
                produkt.remove(j);
                productView.removeView(j);
            }
        }

        int cat_id = (int)Select.from(CategoryTable.CATEGORY).where(CategoryTable.CATEGORY.JMENO.is(category)).execute().get(0).id;
        Logger.e("Category id: " + cat_id);
        List<Product> select = Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.CATEGORY.is(cat_id)).limit(100).execute();

        Log.e("SELECT", select.size()+" velikost");

        int index = 0;
        for (Product prod :
                select) {
            Log.e("INFO", prod.jmeno_produktu);
            ProduktType new_prod = new ProduktType(getApplicationContext(), productView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
            produkt.add(prod);
            productView.addView(new_prod);
        }
    }
    public void RefreshCategoryList(){
        if(!kategorie.isEmpty())
        {
            Logger.e("Pocet kategorii -> " + kategorie.size());
            for (int j = kategorie.size()-1; j >= 0; j--)
            {
                kategorie.remove(j);
                categoryView.removeView(j);
            }
        }

        List<Category> select = Select.from(CategoryTable.CATEGORY).limit(100).execute();

        //Log.e("SELECT", select.size()+" velikost");

        int index = 0;
        for (Category prod :
                select) {
            Log.e("INFO", prod.jmeno);
            CategoryType new_prod = new CategoryType(getApplicationContext(), productView, index++, prod.jmeno);
            kategorie.add(prod);
            categoryView.addView(new_prod);
        }
    }

}
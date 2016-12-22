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
import com.siimkinks.sqlitemagic.Select;

import java.util.ArrayList;
import java.util.List;

import eet.pospichal.eet.model.Product;
import com.siimkinks.sqlitemagic.ProductTable;


public class ProductsActivity extends SlidingActivity {

    PlaceHolderView mrdkaView;
    Button add_new_produkt;
    MaterialSearchBar bar;
    List<Product> produkt;

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

        bar = (MaterialSearchBar)findViewById(R.id.searchBar);
        mrdkaView = (PlaceHolderView)findViewById(R.id.mrdkaView);
        mrdkaView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mrdkaView.getBuilder().setHasFixedSize(true);

        add_new_produkt = (Button)findViewById(R.id.btn_add_produkt);



        add_new_produkt.setOnClickListener(addProd);
        bar.addTextChangeListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                for (int j = produkt.size()-1; j >= 0; j--)
                {
                    produkt.remove(j);
                    mrdkaView.removeView(j);
                }

                if (charSequence.toString() == "")
                {
                    for (int j = produkt.size()-1; j >= 0; j--)
                    {
                        produkt.remove(j);
                        mrdkaView.removeView(j);
                    }
                    List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

                    Log.e("SELECT", select.size()+" velikost");

                    int index = 0;
                    for (Product prod :
                            select) {
                        Log.e("INFO", prod.jmeno_produktu);
                        ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
                        produkt.add(prod);
                        mrdkaView.addView(new_prod);
                    }
                }
                else
                {
                    /*
                    View view = ProductsActivity.this.mrdkaView;
                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    */

                    List<Product> select = Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.JMENO_PRODUKTU.like(charSequence.toString() + "%")).limit(100).execute();

                    Log.e("SELECT", select.size()+" velikost");

                    int index = 0;
                    for (Product prod :
                            select) {
                        Log.e("INFO", prod.jmeno_produktu);
                        ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
                        produkt.add(prod);
                        mrdkaView.addView(new_prod);
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
                    mrdkaView.removeView(i);
                }

            }

            @Override
            public void onSearchConfirmed(CharSequence charSequence) {
                View view = ProductsActivity.this.mrdkaView;
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
        ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, 0, "Produkt 1");
        mrdkaView.addView(new_prod);
        mrdkaView.addView(new ProduktType(getApplicationContext(), mrdkaView, 1, "Produkt 2"));
        */

        List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

        for (Product prod :
                select) {
            Log.e("INFO", prod.jmeno_produktu);
            produkt.add(prod);
            ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, produkt.size(), prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
            mrdkaView.addView(new_prod);
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

    public void RefreshFeed(){
        for (int j = produkt.size()-1; j >= 0; j--)
        {
            produkt.remove(j);
            mrdkaView.removeView(j);
        }
        List<Product> select = Select.from(ProductTable.PRODUCT).limit(100).execute();

        Log.e("SELECT", select.size()+" velikost");

        int index = 0;
        for (Product prod :
                select) {
            Log.e("INFO", prod.jmeno_produktu);
            ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, index++, prod.jmeno_produktu, prod.cena_koruny+"."+prod.cena_halere+" Kč", prod.cena_koruny, prod.cena_halere);
            produkt.add(prod);
            mrdkaView.addView(new_prod);
        }
    }
}
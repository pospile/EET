package eet.pospichal.eet;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.MaterialDialog;
import com.klinker.android.sliding.SlidingActivity;
import com.orhanobut.logger.Logger;
import com.sdsmdg.tastytoast.TastyToast;
import com.siimkinks.sqlitemagic.CategoryTable;
import com.siimkinks.sqlitemagic.Select;

import java.util.List;

import eet.pospichal.eet.model.Category;


public class AddProduktActivity extends SlidingActivity {

    Button addProdukt;
    String sel_cat = "";

    @Override
    public void init(Bundle savedInstanceState) {
        CommunicationLayer.getInstance().RegisterAddProdActivity(this);
        setTitle("Produkty");

        disableHeader();
        enableFullscreen();

        setPrimaryColors(
                getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.black)
        );

        setContent(R.layout.activity_add_produkt);

        addProdukt = (Button)findViewById(R.id.btn_add_produkt_to_list);
        addProdukt.setOnClickListener(add_prd_to_dat);
        Button caa = (Button)findViewById(R.id.btn_select_cat);

        caa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final List<Category> cat =  Select.from(CategoryTable.CATEGORY).execute();
                final String[] pole = new String[cat.size()];
                int i = 0;
                for (Category ct :
                        cat) {
                    pole[i] = ct.jmeno;
                    i++;
                }
                new MaterialDialog.Builder(AddProduktActivity.this)
                        .title("Vyberte kategorii")
                        .items(pole)
                        .itemsCallbackSingleChoice(-1, new MaterialDialog.ListCallbackSingleChoice() {
                            @Override
                            public boolean onSelection(MaterialDialog dialog, View view, int which, CharSequence text) {
                                sel_cat = pole[which];
                                return true;
                            }
                        })
                        .positiveText("Vybráno")
                        .show();
            }
        });

    }

    View.OnClickListener add_prd_to_dat = new View.OnClickListener() {
        @Override
        public void onClick(View view) {


            EditText name = (EditText)findViewById(R.id.editProductName);
            String nameStr = name.getText().toString();


            EditText priceKoruny = (EditText)findViewById(R.id.editProduktPriceKoruny);
            int korunyStr = 0;
            try {
                korunyStr = Integer.parseInt(priceKoruny.getText().toString());
            }
            catch (NumberFormatException e)
            {
                korunyStr = 0;
            }

            EditText priceHalere = (EditText)findViewById(R.id.editProduktPriceHalere);
            int halereStr = 0;
            try {
                halereStr = Integer.parseInt(priceHalere.getText().toString());
            }
            catch (NumberFormatException e)
            {
                halereStr = 0;
            }

            EditText id = (EditText)findViewById(R.id.editProductInternal);
            int idStr = 0;
            try {
                idStr = Integer.parseInt(id.getText().toString());
            }
            catch (NumberFormatException e)
            {
                idStr = -1;
            }

            EditText barcode = (EditText)findViewById(R.id.editProductBarcode);
            String barStr =barcode.getText().toString();



            if (!nameStr.isEmpty() && (korunyStr != 0 || halereStr != 0))
            {
                int cat_id = -1;
                if (!sel_cat.isEmpty())
                {
                    cat_id = (int) Select.from(CategoryTable.CATEGORY).where(CategoryTable.CATEGORY.JMENO.is(sel_cat)).execute().get(0).id;
                    Logger.e("Creating product in category:" + cat_id);
                }
                DatabaseHelper.getInstance().AddProduktToDatabase(nameStr, korunyStr , halereStr, 21, idStr, barStr, cat_id);
                TastyToast.makeText(AddProduktActivity.this, "Produkt " + nameStr + " úspěšně vytvořen!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                CommunicationLayer.getInstance().GetLastRegisteredProdActivity().RefreshProductList();
                AddProduktActivity.this.finish();
            }
            else
            {
                TastyToast.makeText(AddProduktActivity.this, "Vyplňte prosím povinné údaje!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

        }
    };
}
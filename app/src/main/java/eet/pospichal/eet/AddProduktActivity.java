package eet.pospichal.eet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.mindorks.placeholderview.PlaceHolderView;
import com.sdsmdg.tastytoast.TastyToast;


public class AddProduktActivity extends SlidingActivity {

    Button addProdukt;

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
                DatabaseHelper.getInstance().AddProduktToDatabase(nameStr, korunyStr , halereStr, 21, idStr, barStr);
                TastyToast.makeText(AddProduktActivity.this, "Produkt " + nameStr + " úspěšně vytvořen!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                CommunicationLayer.getInstance().GetLastRegisteredProdActivity().RefreshFeed();
                AddProduktActivity.this.finish();
            }
            else
            {
                TastyToast.makeText(AddProduktActivity.this, "Vyplňte prosím povinné údaje!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }

        }
    };
}
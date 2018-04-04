package eet.pospichal.eet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Space;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.zxing.client.android.CaptureActivity;
import com.mindorks.placeholderview.PlaceHolderView;
import com.orhanobut.logger.Logger;
import com.scandit.barcodepicker.BarcodePicker;
import com.scandit.barcodepicker.OnScanListener;
import com.scandit.barcodepicker.ScanSession;
import com.scandit.barcodepicker.ScanSettings;
import com.scandit.barcodepicker.ScanditLicense;
import com.scandit.recognition.Barcode;
import com.sdsmdg.tastytoast.TastyToast;
import com.siimkinks.sqlitemagic.ProductTable;
import com.siimkinks.sqlitemagic.Select;
import com.siimkinks.sqlitemagic.SqliteMagic;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import eet.pospichal.eet.model.Cena;
import eet.pospichal.eet.model.Dan;
import eet.pospichal.eet.model.Product;
import openeet.lite.EetRegisterRequest;
import openeet.lite.Main;

import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static openeet.lite.EetRegisterRequest.loadStream;

public class MainActivity extends Activity implements OnScanListener {

    //region properties
    public int koruny;
    public int halere;


    public int celk_kor;
    public int celk_hal;


    public enum CastMeny
    {
        Koruny, Halere;
    }

    public CastMeny castka;

    public Context cont;


    PlaceHolderView mGalleryView;


    List<UctenkaType> ucty;


    private boolean vynasob_posledni = false;
    private int vynasob_kolika = 1;


    private boolean zadani_inter = false;
    private BarcodePicker mPicker;

    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CommunicationLayer.getInstance().RegisterMainActivity(this);

        SqliteMagic.init(getApplication());

        ucty = new ArrayList<UctenkaType>();

        cont = MainActivity.this;
        castka = CastMeny.Koruny;

        /*
        AddItemToUcet();
        AddItemToUcet();
        AddItemToUcet();
        AddItemToUcet();
        */

        TextView monitor = (TextView)findViewById(R.id.txt_castka);
        monitor.setText(Celkem());

        //region btn setup

        Button one = (Button)findViewById(R.id.btn_1);
        Button two = (Button)findViewById(R.id.btn_2);
        Button three = (Button)findViewById(R.id.btn_3);
        Button four = (Button)findViewById(R.id.btn_4);
        Button five = (Button)findViewById(R.id.btn_5);
        Button six = (Button)findViewById(R.id.btn_6);
        Button seven = (Button)findViewById(R.id.btn_7);
        Button eight = (Button)findViewById(R.id.btn_8);
        Button nine = (Button)findViewById(R.id.btn_9);
        Button zero = (Button)findViewById(R.id.btn_0);

        Button dot = (Button)findViewById(R.id.btn_tecka);
        ImageButton remove = (ImageButton) findViewById(R.id.btn_delete);
        Button enter = (Button)findViewById(R.id.btn_enter);
        Button rem_all = (Button)findViewById(R.id.btn_rem_all);
        Button count = (Button)findViewById(R.id.btn_multiply);
        Button send_eet = (Button)findViewById(R.id.sendEET);
        Button open_list = (Button)findViewById(R.id.btn_open_product_list) ;
        Button inter_zad = (Button)findViewById(R.id.btn_inter_id);
        Button vypocist_dan = (Button)findViewById(R.id.btn_show_dan);
        CardView sug = (CardView)findViewById(R.id.card_suggestion);

        one.setOnClickListener(listener);
        two.setOnClickListener(listener);
        three.setOnClickListener(listener);
        four.setOnClickListener(listener);
        five.setOnClickListener(listener);
        six.setOnClickListener(listener);
        seven.setOnClickListener(listener);
        eight.setOnClickListener(listener);
        nine.setOnClickListener(listener);
        zero.setOnClickListener(listener);

        dot.setOnClickListener(dot_listener);
        remove.setOnClickListener(rem_listener);
        enter.setOnClickListener(enter_listener);
        rem_all.setOnClickListener(rem_all_listener);
        count.setOnClickListener(count_listener);
        send_eet.setOnClickListener(eet_listener);
        open_list.setOnClickListener(open_list_list);
        inter_zad.setOnClickListener(inter_zad_listener);
        vypocist_dan.setOnClickListener(show_dan_listener);
        sug.setOnClickListener(sug_add);

        //endregion


        ScanditLicense.setAppKey("PD+YwAUaXdmmhc3Eljfp2PeHXTYpuBwZMtlCNm9YkxA");
        ScanSettings settings = ScanSettings.create();
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_EAN13, true);
        settings.setSymbologyEnabled(Barcode.SYMBOLOGY_UPCA, true);
        /*
        // Instantiate the barcode picker by using the settings defined above.
        BarcodePicker picker = new BarcodePicker(this, settings);
        // Set the on scan listener to receive barcode scan events.
        picker.setOnScanListener(scan_list);
        setContentView(picker);
        */

//
//        Button scan = (Button)findViewById(R.id.btn_scan);
//        scan.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                CommunicationLayer.getInstance().startScan(MainActivity.this, MainActivity.this, scan_list);
//            }
//        });

    }
    @Override
    protected void onResume() {
        //mPicker.startScanning();
        super.onResume();
    }
    @Override
    protected void onPause() {
        //mPicker.stopScanning();
        super.onPause();
    }

    //region metody
    public void simpleRegistrationProcessTest(TextView status) throws MalformedURLException, IOException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //set minimal business data & certificate with key loaded from pkcs12 file

        TimeZone tz = TimeZone.getDefault();
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'"); // Quoted "Z" to indicate UTC, no timezone offset
        df.setTimeZone(tz);
        String nowAsISO = df.format(new Date());



        EetRegisterRequest request=EetRegisterRequest.builder()
                .dic_popl("CZ1212121218")
                .id_provoz("1")
                .id_pokl("POKLADNA01")
                .porad_cis("1")
                .dat_trzby(new Date())
                .celk_trzba(100.0)
                .rezim(1)
                .pkcs12(loadStream(getResources().openRawResource(R.raw.certificate)))
                .pkcs12password("eet")
                .build();

        //for receipt printing in online mode
        String bkp=request.formatBkp();
        assertNotNull(bkp);
        status.setText(status.getText()+" \n " + bkp);
        Log.i("INFO", "bkp " +  bkp);

        //for receipt printing in offline mode
        String pkp=request.formatPkp();
        assertNotNull(pkp);
        status.setText(status.getText()+" \n " + pkp);
        Log.i("INFO", "pkp " +  pkp);

        //the receipt can be now stored for offline processing

        //try send
        String requestBody=request.generateSoapRequest();
        assertNotNull(requestBody);


        String response= null;
        try {
            Log.i("INFO", requestBody);
            response = request.sendRequest(requestBody, new URL("https://pg.eet.cz:443/eet/services/EETServiceSOAP/v3"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        //extract FIK


        assertNotNull(response);
        assertTrue(response.contains("Potvrzeni fik="));
        status.setText(status.getText()+" \n " + "Potvrzeni: \n" + "Platba uspesne zaregistrovana u GFŘ");


        Log.i("INFO", response);
        //ready to print online receipt
    }


    public void IncrementKoruny(int amount)
    {
        if (GetKoruny() > 999999) {return;}
        koruny = Integer.parseInt(GetKoruny() + "" + amount);
    }
    public void IncrementHalere(int amount)
    {
        if (halere == 0)
        {
            halere = Integer.valueOf(amount + String.valueOf(halere).substring(0, 1));
        }
        else
        {
            halere = Integer.valueOf(String.valueOf(halere).substring(0, 1) + amount);
        }

    }
    public int GetKoruny()
    {
        return koruny;
    }
    public int GetHalere()
    {
        return halere;
    }
    public String Celkem()
    {
        String hal = String.valueOf(halere);
        if (halere < 10)
        {
            hal = hal + "0";
        }
        return GetKoruny() + "." + hal + " Kč ";
    }
    public void RemoveItemFromUcet(int index)
    {
        //TODO:// ZKONTROLOVAT MINUSOVANI KORUN POKUD JE POD NULOU S HALEREMA




        celk_kor -= ucty.get(index).kor;
        celk_hal -= ucty.get(index).hal;


        if (celk_hal < 0)
        {
            celk_kor -= 1;
            celk_hal = 100 + celk_hal;
        }

        TextView celkem = (TextView) findViewById(R.id.celkova_castka);
        celkem.setText(""+celk_kor+"."+celk_hal+" Kč");


        for (int i = index; i < ucty.size(); i++)
        {
            ucty.get(i).index -= 1;
        }
        ucty.remove(index);
        mGalleryView.removeView(index);
    }
    public void AddItemToUcet()
    {
        if (vynasob_posledni)
        {
            koruny *= vynasob_kolika;
            halere *= vynasob_kolika;

            if (halere >= 100)
            {
                double result = (double)halere / (double) 100;

                String result_ready = String.valueOf(result);
                String koruny_add = result_ready.split("\\.")[0];
                String halere_add = result_ready.split("\\.")[1];


                koruny += Integer.parseInt(koruny_add);
                halere = Integer.parseInt(halere_add);
            }


            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView,this, ucty.size(), Celkem(), koruny, halere, "Produkt *" + vynasob_kolika);
            ucty.add(new_ucet);

            celk_kor += koruny;
            celk_hal += halere;

            if (celk_hal >= 100)
            {
                celk_kor += 1;
                celk_hal -= 100;
            }

            TextView celkem = (TextView) findViewById(R.id.celkova_castka);
            celkem.setText(""+celk_kor+"."+celk_hal+" Kč");

            String celk = Celkem();

            halere = 0;
            koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            vynasob_posledni = false;
            vynasob_kolika = 1;
            Button btn = (Button)findViewById(R.id.btn_multiply);
            btn.setText("Množství");

            mGalleryView.addView(new_ucet);
        }
        else
        {
            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView,this, ucty.size(), Celkem(), koruny, halere, "Produkt");
            ucty.add(new_ucet);

            celk_kor += koruny;
            celk_hal += halere;

            if (celk_hal >= 100)
            {
                celk_kor += 1;
                celk_hal -= 100;
            }

            TextView celkem = (TextView) findViewById(R.id.celkova_castka);
            celkem.setText(""+celk_kor+"."+celk_hal+" Kč");

            String celk = Celkem();

            halere = 0;
            koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            vynasob_posledni = false;
            vynasob_kolika = 1;
            Button btn = (Button)findViewById(R.id.btn_multiply);
            btn.setText("Množství");

            mGalleryView.addView(new_ucet);
        }
    }

    public void AddItemToUcet(int koruny, int halere, String jmeno)
    {
        if (vynasob_posledni) {
            koruny *= vynasob_kolika;
            halere *= vynasob_kolika;

            if (halere >= 100) {
                double result = (double) halere / (double) 100;
                String result_ready = String.valueOf(result);
                String koruny_add = result_ready.split("\\.")[0];
                String halere_add = result_ready.split("\\.")[1];



                koruny += Integer.parseInt(koruny_add);
                halere = Integer.parseInt(halere_add);
            }


            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView, this, ucty.size(), koruny + "." + halere + " Czk", koruny, halere, jmeno +" *"+vynasob_kolika);
            ucty.add(new_ucet);

            celk_kor += koruny;
            celk_hal += halere;

            if (celk_hal >= 100) {
                celk_kor += 1;
                celk_hal -= 100;
            }

            TextView celkem = (TextView) findViewById(R.id.celkova_castka);
            celkem.setText("" + celk_kor + "." + celk_hal + " Kč");

            String celk = Celkem();

            halere = 0;
            koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView) findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            if (mGalleryView != null)
            {
                vynasob_posledni = false;
                vynasob_kolika = 1;
                Button btn = (Button)findViewById(R.id.btn_multiply);
                btn.setText("Množství");
                mGalleryView.addView(new_ucet);
            }
            else
            {
                Logger.e("FUCK CRASH");
            }
        }
        else
        {
            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView,this, ucty.size(), koruny + "." + halere+" Kč", koruny, halere, jmeno);
            ucty.add(new_ucet);

            celk_kor += koruny;
            celk_hal += halere;

            if (celk_hal >= 100)
            {
                celk_kor += 1;
                celk_hal -= 100;
            }

            TextView celkem = (TextView) findViewById(R.id.celkova_castka);
            celkem.setText(""+celk_kor+"."+celk_hal+" Kč");

            String celk = Celkem();

            this.halere = 0;
            this.koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());



            if (mGalleryView != null)
            {
                vynasob_posledni = false;
                vynasob_kolika = 1;
                Button btn = (Button)findViewById(R.id.btn_multiply);
                btn.setText("Množství");
                mGalleryView.addView(new_ucet);
            }
            else
            {
                Logger.e("FUCK CRASH");
                mGalleryView = (PlaceHolderView) findViewById(R.id.galleryView);
                mGalleryView.addView(new_ucet);
            }
        }
    }

    //endregion

    //region onclicklisteners


    public View.OnClickListener sug_add = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {
            Product add = CommunicationLayer.getInstance().GetProductSugResult();
            AddItemToUcet(add.cena_koruny, add.cena_halere, add.jmeno_produktu);


            Button btn = (Button)findViewById(R.id.btn_multiply);
            btn.setText("Množství");
            vynasob_kolika = 1;
            vynasob_posledni = false;

            halere = 0;
            koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            CardView card = (CardView) findViewById(R.id.card_suggestion);
            android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

            card.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);

        }
    };

    public View.OnClickListener show_dan_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Cena cena = new Cena(celk_kor, celk_hal, Dan.Typ.dvacet_jedna);
            cena = ProductHelper.getInstance().VypoctiDanZProduktu(cena);
            new MaterialDialog.Builder(MainActivity.this)
                    .title("Výpočet daně")
                    .content("Daň z této objednávky je: " + cena.dan_koruny + "." + cena.dan_halere + " Kč")
                    .theme(Theme.DARK)
                    .buttonsGravity(GravityEnum.CENTER)
                    .cancelable(true)
                    .show();
        }
    };

    public View.OnClickListener inter_zad_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (koruny != 0)
            {
                Product product = ProductHelper.getInstance().GetLocalProduct(koruny);
                if (product != null)
                {
                    TastyToast.makeText(MainActivity.this, "Přidávám produkt "+product.barcode, TastyToast.LENGTH_SHORT, TastyToast.DEFAULT);
                    if (product.jmeno_produktu.length() > 10)
                    {
                        AddItemToUcet(product.cena_koruny, product.cena_halere, product.jmeno_produktu.substring(0,10)+"...");
                    }
                    else
                    {
                        AddItemToUcet(product.cena_koruny, product.cena_halere, product.jmeno_produktu);
                    }
                    vynasob_posledni = false;
                    vynasob_kolika = 1;
                    Button btn = (Button)findViewById(R.id.btn_multiply);
                    btn.setText("Množství");
                    halere = 0;
                    koruny = 0;
                    castka = CastMeny.Koruny;
                    TextView monitor = (TextView)findViewById(R.id.txt_castka);
                    monitor.setText(Celkem());
                }
                else {
                    TastyToast.makeText(MainActivity.this, "Produkt nenalezen, můžete ho vytvořit...", TastyToast.LENGTH_LONG, TastyToast.WARNING);
                }
            }
        }
    };

    public View.OnClickListener open_list_list = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(getApplicationContext(), ProductsActivity.class);
            intent.putExtra("like_count", 10);
            startActivity(intent);
        }
    };

    public View.OnClickListener eet_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            new MaterialDialog.Builder(MainActivity.this)
                    .title("Počkejte prosím")
                    .cancelable(true)
                    .progressIndeterminateStyle(true)
                    .content("Odesílám účtenku ministerstvu")
                    .positiveColorRes(R.color.colorError)
                    .widgetColor(Color.RED)
                    .theme(Theme.DARK)
                    .progress(true, 0)
                    .show();
        }
    };

    public View.OnClickListener count_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            vynasob_posledni = true;
            new MaterialDialog.Builder(MainActivity.this)
                    .title("Množství produktu")
                    .content("Zadejte množství produktu a potvrďte.")
                    .theme(Theme.DARK)
                    .positiveColorRes(R.color.colorError)
                    .inputType(InputType.TYPE_CLASS_NUMBER)
                    .input("Množství", "", new MaterialDialog.InputCallback() {
                        @Override
                        public void onInput(MaterialDialog dialog, CharSequence input) {
                            if (input.length() != 0)
                            {
                                TastyToast.makeText(MainActivity.this, "Množství produktu:" + input, TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                vynasob_posledni = true;
                                vynasob_kolika = Integer.valueOf(input+"");
                                Button btn = (Button)findViewById(R.id.btn_multiply);
                                btn.setText("Množství: " + vynasob_kolika);
                            }
                        }
                    }).show();
        }
    };

    public View.OnClickListener rem_all_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            if (!ucty.isEmpty())
            {
                new MaterialDialog.Builder(MainActivity.this)
                        .title("Potvrzení")
                        .content("Jste si jistý, že chcete zrušit tuto objednávku?")
                        .positiveText("ANO")
                        .negativeText("ne")
                        .theme(Theme.DARK)
                        .positiveColorRes(R.color.colorPrimary)
                        .negativeColorRes(R.color.colorError)
                        .buttonsGravity(GravityEnum.CENTER)
                        .onPositive(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                int count = ucty.size();
                                for (int i = 0; i < count; i++)
                                {
                                    RemoveItemFromUcet(0);
                                    halere = 0;
                                    koruny = 0;
                                    castka = CastMeny.Koruny;
                                    TextView monitor = (TextView)findViewById(R.id.txt_castka);
                                    monitor.setText(Celkem());
                                }
                                vynasob_posledni = false;
                                vynasob_kolika = 1;

                                CardView card = (CardView) findViewById(R.id.card_suggestion);
                                android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

                                card.setVisibility(View.GONE);
                                space.setVisibility(View.VISIBLE);

                                Button btn = (Button)findViewById(R.id.btn_multiply);
                                btn.setText("Množství");
                                TastyToast.makeText(getApplicationContext(), "Objednávka zrušena!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        .onNegative(new MaterialDialog.SingleButtonCallback() {
                            @Override
                            public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                                TastyToast.makeText(getApplicationContext(), "Objednávka NEBYLA zrušena!", TastyToast.LENGTH_SHORT, TastyToast.INFO);
                            }
                        })
                        .show();
            }
            else
            {
                TastyToast.makeText(getApplicationContext(), "Nejde zrušit prázdnou objednávku!", TastyToast.LENGTH_SHORT, TastyToast.ERROR);
            }


        }
    };

    public View.OnClickListener enter_listener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.error_color));

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.cardview_dark_background));
                        }
                    },
                    100);
            //http://lorempixel.com/400/200/
            mGalleryView = (PlaceHolderView)findViewById(R.id.galleryView);
            /*
            mGalleryView.addView(new UctenkaType(cont, mGalleryView, "http://lorempixel.com/400/200/", Celkem()));
            */
            AddItemToUcet();
            Button btn = (Button)findViewById(R.id.btn_multiply);
            btn.setText("Množství");
            vynasob_kolika = 1;
            vynasob_posledni = false;

            CardView card = (CardView) findViewById(R.id.card_suggestion);
            android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

            card.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);
        }
    };

    public View.OnClickListener rem_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            halere = 0;
            koruny = 0;
            castka = CastMeny.Koruny;
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            CardView card = (CardView) findViewById(R.id.card_suggestion);
            android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

            card.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);
        }
    };

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {



            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.sucess));

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.cardview_dark_background));
                        }
                    },
                    100);


            Button clicked = (Button)view;
            /*
            TextView status = (TextView)findViewById(R.id.status);
            status.setText(clicked.getText());
            */
            if (castka == CastMeny.Koruny)
            {
                IncrementKoruny(Integer.parseInt(clicked.getText()+""));
            }
            else
            {
                IncrementHalere(Integer.parseInt(clicked.getText()+""));
            }

            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());

            List<Product> sel = Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.INTER_ID.like(koruny+"%")).execute();
            Logger.e(sel.size() + "počet suggestions");
            if (!sel.isEmpty())
            {
                CommunicationLayer.getInstance().SetProductSugResult(sel.get(0));

                CardView card = (CardView) findViewById(R.id.card_suggestion);
                android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

                TextView jmeno = (TextView)findViewById(R.id.product_name_sug);
                TextView cenovka = (TextView)findViewById(R.id.product_price_sug);

                jmeno.setText(sel.get(0).inter_id +": " + sel.get(0).jmeno_produktu);
                cenovka.setText(sel.get(0).cena_koruny+"."+sel.get(0).cena_halere+" Kč");

                card.setVisibility(View.VISIBLE);
                space.setVisibility(View.GONE);
            }
            else
            {
                CardView card = (CardView) findViewById(R.id.card_suggestion);
                android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

                card.setVisibility(View.GONE);
                space.setVisibility(View.VISIBLE);
            }

        }
    };

    public View.OnClickListener dot_listener = new View.OnClickListener() {
        @Override
        public void onClick(final View view) {

            CardView card = (CardView) findViewById(R.id.card_suggestion);
            android.support.v4.widget.Space space = (android.support.v4.widget.Space)findViewById(R.id.card_suggestion_space);

            card.setVisibility(View.GONE);
            space.setVisibility(View.VISIBLE);

            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.error_color));

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            view.setBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.cardview_dark_background));
                        }
                    },
                    100);


            if (castka == CastMeny.Koruny)
            {
                castka = CastMeny.Halere;
            }
            else
            {
                castka = CastMeny.Koruny;
            }
            TextView monitor = (TextView)findViewById(R.id.txt_castka);
            monitor.setText(Celkem());
        }
    };

    //endregions

    //region shity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 0) {
            if (resultCode == RESULT_OK) {
                String contents = data.getStringExtra("SCAN_RESULT");
                /*
                TextView status = (TextView)findViewById(R.id.status);
                status.setText(contents);
                */
                TastyToast.makeText(getApplicationContext(), "Hledám produkt:" + contents, TastyToast.LENGTH_LONG, TastyToast.CONFUSING);
                MyDynamicToast.informationMessage(MainActivity.this, "Vyhledávám produkt: "+contents);
            } else if (resultCode == RESULT_CANCELED) {
                //MyDynamicToast.errorMessage(MainActivity.this, "");
            }
        }

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // ignore orientation/keyboard change
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void didScan(ScanSession scanSession) {

    }

    public OnScanListener scan_list = new OnScanListener() {
        @Override
        public void didScan(ScanSession scanSession) {

        }
    };
    //endregion
}

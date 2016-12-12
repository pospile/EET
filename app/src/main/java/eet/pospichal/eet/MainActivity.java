package eet.pospichal.eet;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.StrictMode;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.GravityEnum;
import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.desai.vatsal.mydynamictoast.MyDynamicToast;
import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.common.StringUtils;
import com.mindorks.placeholderview.PlaceHolderView;
import com.sdsmdg.tastytoast.TastyToast;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import openeet.lite.EetRegisterRequest;
import static junit.framework.Assert.assertNotNull;
import static junit.framework.Assert.assertTrue;
import static openeet.lite.EetRegisterRequest.loadStream;

public class MainActivity extends Activity {

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

    //endregion

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        //endregion



        Button scan = (Button)findViewById(R.id.btn_scan);
        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startScan();
            }
        });
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

    private void startScan() {

        Intent intent = new Intent(getApplicationContext(),CaptureActivity.class);
        intent.setAction("com.google.zxing.client.android.SCAN");
        intent.putExtra("SAVE_HISTORY", false);
        startActivityForResult(intent, 0);

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

        Log.e("STAV_KORUNY", celk_kor+"");
        Log.e("STAV_HALERE", celk_hal+"");


        celk_kor -= ucty.get(index).kor;
        celk_hal -= ucty.get(index).hal;

        Log.e("KORUNY", ucty.get(index).kor+"");
        Log.e("HALERE", ucty.get(index).hal+"");

        Log.e("STAV_KORUNY", celk_kor+"");

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
                Log.e("INFO", result+"");
                String result_ready = String.valueOf(result);
                String koruny_add = result_ready.split("\\.")[0];
                String halere_add = result_ready.split("\\.")[1];

                Log.e("KORUNY", koruny_add+"");
                Log.e("HALERE", halere_add+"");


                koruny += Integer.parseInt(koruny_add);
                halere = Integer.parseInt(halere_add);
            }


            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView,this, ucty.size(), Celkem(), koruny, halere);
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


            mGalleryView.addView(new_ucet);
        }
        else
        {
            UctenkaType new_ucet = new UctenkaType(MainActivity.this, mGalleryView,this, ucty.size(), Celkem(), koruny, halere);
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


            mGalleryView.addView(new_ucet);
        }
    }
    //endregion

    //region onclicklisteners

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
    };

    public View.OnClickListener enter_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
        }
    };

    public View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Button clicked = (Button)view;
            TextView status = (TextView)findViewById(R.id.status);
            status.setText(clicked.getText());

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
        }
    };

    public View.OnClickListener dot_listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
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
    //endregion
}

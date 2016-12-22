package eet.pospichal.eet;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.siimkinks.sqlitemagic.Delete;

import eet.pospichal.eet.R;
import eet.pospichal.eet.model.Product;
import openeet.lite.Main;

import com.siimkinks.sqlitemagic.ProductColumn;
import com.siimkinks.sqlitemagic.ProductTable;
import com.siimkinks.sqlitemagic.Select;

@Animate(Animation.ANIM_DURATION)
@Layout(R.layout.produkt_layout)
public class ProduktType {

    public String prod;

    public int index;

    @View(R.id.productView)
    private TextView tag;

    @View(R.id.productPrice)
    private TextView price;

    @View(R.id.card)
    private CardView card;

    @View(R.id.productAdded)
    private TextView status;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;

    //private MainActivity activity;

    private String priceStr;

    private int koruny;

    private int halere;


    public ProduktType(Context context, PlaceHolderView placeHolderView, int ind, String produkt, String price, int koruny, int halere) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        index = ind;
        prod = produkt;
        priceStr = price;
        this.koruny = koruny;
        this.halere = halere;
    }

    @Resolve
    private void onResolved() {
        tag.setText(ShortenName(20));
        price.setText(priceStr);
    }

    @LongClick(R.id.card)
    private void onLongClick(){
        Delete.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.JMENO_PRODUKTU.is(prod)).execute();
        //activity.RemoveItemFromUcet(index);
        mPlaceHolderView.removeView(this);
    }
    @Click(R.id.card)
    private void OnClick() {
        Log.e("INFO", "Kliknul jsi");
        /*
        Toast toast = Toast.makeText(mContext, "Kliknul jsi", Toast.LENGTH_SHORT);
        toast.show();
        */
        //tag.setText(tag.getText() + " přidáno");
        status.setText("Přidáno");
        card.setCardBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.sucess));
        CommunicationLayer.getInstance().GetMainActivity().AddItemToUcet(koruny, halere, ShortenName(10));
        //CommunicationLayer.getInstance().UpdateProduct(Select.from(ProductTable.PRODUCT).where(ProductTable.PRODUCT.JMENO_PRODUKTU.is(prod)).execute().get(0));
        new android.os.Handler().postDelayed(
                new Runnable() {
                    public void run() {

                        card.setCardBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.sucess1));

                        new android.os.Handler().postDelayed(
                                new Runnable() {
                                    public void run() {
                                        card.setCardBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.sucess2));

                                        new android.os.Handler().postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        Log.i("tag", "This'll run 300 milliseconds later");
                                                        card.setCardBackgroundColor(CommunicationLayer.getInstance().GetMainActivity().getResources().getColor(R.color.cardview_light_background));
                                                        status.setText("");
                                                    }
                                                },
                                                300);
                                    }
                                },
                                150);
                    }
                },
                150);
        //mPlaceHolderView.addView(new ProduktType(mContext, mPlaceHolderView, index+1, "Produkt"+index));
    }

    public String ShortenName(int shorten_to)
    {
        if (this.prod.length() > shorten_to)
        {
            return prod.substring(0, shorten_to) + "...";
        }
        else
            return prod;
    }


}
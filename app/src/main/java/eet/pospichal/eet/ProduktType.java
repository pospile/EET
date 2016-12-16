package eet.pospichal.eet;

import android.content.Context;
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

import eet.pospichal.eet.R;
import openeet.lite.Main;

@Animate(Animation.ANIM_DURATION)
@Layout(R.layout.produkt_layout)
public class ProduktType {

    public String prod;

    public int index;

    @View(R.id.productView)
    private TextView tag;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;

    private MainActivity activity;



    public ProduktType(Context context, PlaceHolderView placeHolderView, int ind, String produkt) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        index = ind;
        prod = produkt;
    }

    @Resolve
    private void onResolved() {
        tag.setText(prod);
    }

    @LongClick(R.id.card)
    private void onLongClick(){

        //activity.RemoveItemFromUcet(index);
        //mPlaceHolderView.removeView(this);
    }
    @Click(R.id.card)
    private void OnClick() {
        Log.e("INFO", "Kliknul jsi");
        Toast toast = Toast.makeText(mContext, "Kliknul jsi", Toast.LENGTH_SHORT);
        toast.show();
        mPlaceHolderView.addView(new ProduktType(mContext, mPlaceHolderView, index+1, "Produkt"+index));
    }


}
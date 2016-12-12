package eet.pospichal.eet;

import android.content.Context;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.NonReusable;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;

import eet.pospichal.eet.R;
import openeet.lite.Main;

@Animate(Animation.ANIM_DURATION)
@NonReusable
@Layout(R.layout.uctenka_item)
public class UctenkaType {


    public int index;

    @View(R.id.priceView)
    private TextView tag;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;

    private MainActivity activity;

    public String pricetag;

    public int kor;
    public int hal;

    public UctenkaType(Context context, PlaceHolderView placeHolderView, MainActivity _act, int ind, String price_tag, int koruny, int halere) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        pricetag = price_tag;
        activity = _act;
        index = ind;
        kor = koruny;
        hal = halere;
    }

    @Resolve
    private void onResolved() {
        tag.setText(pricetag);
    }

    @LongClick(R.id.card)
    private void onLongClick(){
        activity.RemoveItemFromUcet(index);
        //mPlaceHolderView.removeView(this);
    }

}
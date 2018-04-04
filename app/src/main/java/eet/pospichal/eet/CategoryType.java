package eet.pospichal.eet;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.TextView;

import com.mindorks.placeholderview.Animation;
import com.mindorks.placeholderview.PlaceHolderView;
import com.mindorks.placeholderview.annotations.Animate;
import com.mindorks.placeholderview.annotations.Click;
import com.mindorks.placeholderview.annotations.Layout;
import com.mindorks.placeholderview.annotations.LongClick;
import com.mindorks.placeholderview.annotations.Resolve;
import com.mindorks.placeholderview.annotations.View;
import com.orhanobut.logger.Logger;
import com.siimkinks.sqlitemagic.BooleanTransformerColumn;
import com.siimkinks.sqlitemagic.CategoryTable;
import com.siimkinks.sqlitemagic.Delete;

import com.siimkinks.sqlitemagic.ProductTable;

@Animate(Animation.ANIM_DURATION)
@Layout(R.layout.category_layout)
public class CategoryType {

    public String jmeno;

    public int index;

    @View(R.id.categoryNameText)
    private TextView tag;

    @View(R.id.cardCategory)
    private CardView card;

    private Context mContext;
    private PlaceHolderView mPlaceHolderView;

    private boolean clicked = false;



    public CategoryType(Context context, PlaceHolderView placeHolderView, int ind, String jmeno) {
        mContext = context;
        mPlaceHolderView = placeHolderView;
        index = ind;
        this.jmeno = jmeno;
    }

    @Resolve
    private void onResolved() {
        tag.setText(ShortenName(10));
    }

    @LongClick(R.id.cardCategory)
    private void onLongClick(){
        Logger.e("Removing from category from database");
        Delete.from(CategoryTable.CATEGORY).where(CategoryTable.CATEGORY.JMENO.is(jmeno)).execute();
        //activity.RemoveItemFromUcet(index);
        mPlaceHolderView.removeView(this);
    }
    @Click(R.id.cardCategory)
    private void OnClick() {
        //TODO://FILTER TO SPECIFIC CATEGORY
        if (!clicked)
        {
            CommunicationLayer.getInstance().GetLastRegisteredProdActivity().RefreshProductToCategory(this.jmeno);
            clicked = true;
            card.setCardBackgroundColor(mContext.getResources().getColor(R.color.successs_color));
        }
        else
        {
            CommunicationLayer.getInstance().GetLastRegisteredProdActivity().RefreshProductList();
            clicked = false;
            card.setCardBackgroundColor(mContext.getResources().getColor(R.color.cardview_light_background));
        }
    }

    public String ShortenName(int shorten_to)
    {
        if (this.jmeno.length() > shorten_to)
        {
            return jmeno.substring(0, shorten_to) + "...";
        }
        else
            return jmeno;
    }


}
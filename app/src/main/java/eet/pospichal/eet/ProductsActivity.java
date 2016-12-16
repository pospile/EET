package eet.pospichal.eet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.mindorks.placeholderview.PlaceHolderView;


public class ProductsActivity extends SlidingActivity {

    PlaceHolderView mrdkaView;

    @Override
    public void init(Bundle savedInstanceState) {
        setTitle("Activity Title");

        disableHeader();
        //enableFullscreen();

        setPrimaryColors(
                getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.black)
        );

        setContent(R.layout.activity_products);

        mrdkaView = (PlaceHolderView)findViewById(R.id.mrdkaView);
        mrdkaView.setLayoutManager(new GridLayoutManager(getApplicationContext(), 4));
        mrdkaView.getBuilder().setHasFixedSize(true);
        ProduktType new_prod = new ProduktType(getApplicationContext(), mrdkaView, 0, "Produkt 1");
        mrdkaView.addView(new_prod);
        mrdkaView.addView(new ProduktType(getApplicationContext(), mrdkaView, 1, "Produkt 2"));
        Log.e("INFO", mrdkaView.getViewResolverCount()+"");

    }

    @Override
    protected void configureScroller(MultiShrinkScroller scroller) {
        super.configureScroller(scroller);
        scroller.setIntermediateHeaderHeightRatio(1);
    }
}
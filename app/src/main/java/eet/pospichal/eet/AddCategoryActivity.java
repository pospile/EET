package eet.pospichal.eet;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.klinker.android.sliding.MultiShrinkScroller;
import com.klinker.android.sliding.SlidingActivity;
import com.mindorks.placeholderview.PlaceHolderView;
import com.sdsmdg.tastytoast.TastyToast;

import eet.pospichal.eet.model.Category;


public class AddCategoryActivity extends SlidingActivity {

    Button addProdukt;

    @Override
    public void init(Bundle savedInstanceState) {
        disableHeader();
        enableFullscreen();

        setPrimaryColors(
                getResources().getColor(R.color.cardview_dark_background),
                getResources().getColor(R.color.black)
        );

        setContent(R.layout.activity_add_category);


        CommunicationLayer.getInstance().RegisterAddCategoryActivity(this);

        Button add = (Button)findViewById(R.id.btn_add_category);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextView cat = (TextView)findViewById(R.id.editCategoryName);
                if (cat.length() != 0)
                {
                    DatabaseHelper.getInstance().AddCategoryToDatabase(cat.getText().toString());
                    TastyToast.makeText(AddCategoryActivity.this, "Kategorie " + cat.getText().toString() + " úspěšně vytvořena!", TastyToast.LENGTH_SHORT, TastyToast.SUCCESS);
                    CommunicationLayer.getInstance().GetLastRegisteredProdActivity().RefreshCategoryList();
                    AddCategoryActivity.this.finish();
                }
            }
        });
    }
}
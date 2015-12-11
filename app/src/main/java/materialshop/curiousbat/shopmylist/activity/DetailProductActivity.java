package materialshop.curiousbat.shopmylist.activity;



import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;

import java.util.ArrayList;
import java.util.List;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.utils.ImageLoader;

/**
 * Activity for loading detail product layout
 * Created by Zied on 13/11/2015
 */
public class DetailProductActivity extends AppCompatActivity implements ISimpleDialogListener{

    DetailProductActivity c = this;

    private static final int REQUEST_SIMPLE_DIALOG = 42;
    private static final int SCALE_DELAY = 30;

    private LinearLayout rowContainer;

    private Toolbar mToolbar;

    List<Product> listfavori = new ArrayList<>();
    List<Product> listhotel = new ArrayList<>();


    private String INTENT_PUTEXTRA_ID = "id";
    private String INTENT_PUTEXTRA_NAME = "name";
    private String INTENT_PUTEXTRA_UNICOST = "unicost";
    private String INTENT_PUTEXTRA_BAR = "barcode";
    private String INTENT_PUTEXTRA_IMG = "image";
    private String INTENT_PUTEXTRA_CAT = "category";




    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_product);
        setupToolbar();

        initView();

    }

    private void initView() {

        // Row Container
        rowContainer = (LinearLayout) findViewById(R.id.row_container);

        for (int i = 0; i < rowContainer.getChildCount(); i++) {
            View rowView = rowContainer.getChildAt(i);
            rowView.animate().setStartDelay(100 + i * SCALE_DELAY).scaleX(1)
                    .scaleY(1);
        }

        initData();
    }

    /**
     * Initialize the data with getExtra
     */
    private void initData() {

        Intent intent = getIntent();
        final int idd = intent.getIntExtra(INTENT_PUTEXTRA_ID, 1);
        final String name = intent.getStringExtra(INTENT_PUTEXTRA_NAME);
        final String unicost = intent.getStringExtra(INTENT_PUTEXTRA_UNICOST);
        final String barcode = intent.getStringExtra(INTENT_PUTEXTRA_BAR);
        final String image = intent.getExtras().getString(INTENT_PUTEXTRA_IMG);
        final String category = intent.getStringExtra(INTENT_PUTEXTRA_CAT);

        // toolbar.setTitle(item.name);

        Log.i("Hotels", "id = " + idd);
        Log.i("Hotels", "link = " + name);
        Log.i("Hotels", "desc = " + unicost);
        Log.i("Hotels", "price = " + barcode);
        Log.i("Hotels", "image = " + image);
        Log.i("Hotels", "category = " + category);


        View view = rowContainer.findViewById(R.id.row_image);

        ImageView iv = (ImageView) view.findViewById(R.id.img_details);


        ImageLoader imageLoader = new ImageLoader(this);
        imageLoader.DisplayImage(image, iv);

        Log.d("Log imaaaaage ", image.toString());

        view = rowContainer.findViewById(R.id.row_name);
        fillRow(view, "Name : ", name);

        view = rowContainer.findViewById(R.id.row_detail);
        fillRow(view, "Price : ", unicost + "");

        view = rowContainer.findViewById(R.id.row_price);
        fillRow(view, "BarCode : ", barcode);

        view = rowContainer.findViewById(R.id.row_category);
        fillRow(view, "Category : ", category);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab_fav);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SimpleDialogFragment.createBuilder(
                        getApplicationContext(),
                        getSupportFragmentManager())
                            .setTitle(R.string.add_fav_dialog)
                            .setMessage(R.id.msg_dialog_fav)
                        .setPositiveButtonText(R.string.positive_button)
                        .setNegativeButtonText(R.string.negative_button)
                        .setRequestCode(REQUEST_SIMPLE_DIALOG)
                        .show();

            }
        });
    }

    private void fillRow(View view, final String title, final String description) {
        TextView titleView = (TextView) view.findViewById(R.id.title);
        titleView.setText(title);

        TextView descriptionView = (TextView) view
                .findViewById(R.id.description);
        descriptionView.setText(description);
    }

    private void setupToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
    }


    @Override
    public void onNegativeButtonClicked(int requestCode) {
        if (requestCode == REQUEST_SIMPLE_DIALOG) {

            Intent intent = getIntent();
            final int idd = intent.getIntExtra(INTENT_PUTEXTRA_ID, 1);
            final String name = intent.getStringExtra(INTENT_PUTEXTRA_NAME);
            final String unicost = intent.getStringExtra(INTENT_PUTEXTRA_UNICOST);
            final String barcode = intent.getStringExtra(INTENT_PUTEXTRA_BAR);
            final String image = intent.getStringExtra(INTENT_PUTEXTRA_IMG);
            final String category = intent.getStringExtra(INTENT_PUTEXTRA_CAT);

            ProductDatabase mDatabase = new ProductDatabase(c);

            mDatabase.open();
            Product product = new Product();
            String id = String.valueOf(idd);
            product.setId(id);
            product.setName(name);
            product.setUnitCost(unicost);
            product.setBarCode(barcode);
            product.setImage(image);
            product.setCategory(category);

            Log.i("Favorite Product", "id = " + product.getId());
            Log.i("Favorite Product", "name = " + product.getName());
            Log.i("Favorite Product", "unicost = " + product.getUnitCost());
            Log.i("Favorite Product", "barcode = " + product.getBarCode());
            Log.i("Favorite Product", "image = " + product.getImage());
            Log.i("Favorite Product", "category = " + product.getCategory());



            if (mDatabase.selectfavorite(Integer.valueOf(product.getId())).getName() == null
            || mDatabase.selectfavorite(Integer.valueOf(product.getId())).getName() != product.getName()) {

                mDatabase.insertFavorites(product);

            }else {
                Toast.makeText(c, "Can't add product ", Toast.LENGTH_SHORT).show();
            }

            Product favProd = mDatabase.selectfavorite(Integer.valueOf(product.getId()));
            Log.i("Favorite Added", "id = " + favProd.getId());
            Log.i("Favorite Added", "name = " + favProd.getName());
            Log.i("Favorite Added", "unicost = " + favProd.getUnitCost());
            Log.i("Favorite Added", "barcode = " + favProd.getBarCode());
            Log.i("Favorite Added", "image = " + favProd.getImage());
            Log.i("Favorite Added", "category = " + favProd.getCategory());

            mDatabase.close();
        }
    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if (requestCode == REQUEST_SIMPLE_DIALOG) {
            //Toast.makeText(this.getApplicationContext(), "Positive button clicked", Toast.LENGTH_SHORT).show();
        }
    }
}

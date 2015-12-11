package materialshop.curiousbat.shopmylist.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;

import java.util.ArrayList;

import materialshop.curiousbat.shopmylist.R;
import materialshop.curiousbat.shopmylist.database.ProductDatabase;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.network.VolleySingleton;


/**
 * Created by Zied on 14/11/2015.
 */
public class FavoritAdapter extends RecyclerView.Adapter<FavoritAdapter.ViewHolderFav>{

    private ArrayList<Product> listProduct = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private static Context context;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public FavoritAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    /**
     * Set The List of products
     * @param listProduct
     */
    public void setProductList(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
        notifyItemRangeChanged(0, listProduct.size());
    }

    public void delete(int position) {
        listProduct.remove(position);
        notifyItemRangeChanged(0, listProduct.size());
    }


    /**
     * Represent Items of the given type
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public ViewHolderFav onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_prod_row, parent, false);

        ViewHolderFav viewHolderOrder = new ViewHolderFav(view);

        return viewHolderOrder;
    }

    /**
     * Display the data at a specific position
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(final ViewHolderFav holder, final int position) {

        final Product currentProduct = listProduct.get(position);

        holder.prodId.setText(currentProduct.getId());
        holder.prodName.setText(currentProduct.getName());
        holder.prodUnicost.setText((currentProduct.getUnitCost()));
        holder.prodBarcode.setText(currentProduct.getBarCode());
        holder.prodCat.setText(currentProduct.getCategory());

        String prodImgUrl = currentProduct.getImage();
        loadImage(prodImgUrl, holder);

        holder.prodImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ProductDatabase mDatabase = new ProductDatabase(context);
                mDatabase.open();

                mDatabase.removeFavorite(Integer.valueOf(currentProduct.getId()));

                delete(position);

                mDatabase.close();
            }
        });

    }

    /**
     * Loading the image
     * @param prodImgUrl
     * @param holder
     */
    private void loadImage(String prodImgUrl, final ViewHolderFav holder) {
        if (!prodImgUrl.equals(null)) {

            imageLoader.get(prodImgUrl.replace("localhost", "192.168.1.66"), new ImageLoader.ImageListener() {
                @Override
                public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
                    holder.prodImg.setImageBitmap(response.getBitmap());
                }

                @Override
                public void onErrorResponse(VolleyError error) {

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return listProduct.size();
    }


    /**
     * View Holder Class
     */
    class ViewHolderFav extends RecyclerView.ViewHolder{

        private TextView prodId;
        private TextView prodName;
        private TextView prodUnicost;
        private TextView prodBarcode;
        private ImageView prodImg;
        private TextView prodCat;

        public ViewHolderFav(View itemView) {
            super(itemView);

            prodId = (TextView) itemView.findViewById(R.id.id);
            prodName = (TextView) itemView.findViewById(R.id.prodName);
            prodUnicost = (TextView) itemView.findViewById(R.id.prodUnicost);
            prodBarcode = (TextView) itemView.findViewById(R.id.prodBarcode);
            prodImg = (ImageView) itemView.findViewById(R.id.prodImg);
            prodCat = (TextView) itemView.findViewById(R.id.prodCat);

        }

    }
}

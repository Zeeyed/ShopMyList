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
import materialshop.curiousbat.shopmylist.activity.DetailProductActivity;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.network.VolleySingleton;

/**
 * Order Adapter
 * Created by Zied on 01/11/2015.
 */
public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolderOrder> {


    private ArrayList<Product> listProduct = new ArrayList<>();
    private LayoutInflater layoutInflater;
    private static Context context;
    private VolleySingleton volleySingleton;
    private ImageLoader imageLoader;

    public OrderAdapter(Context context) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        volleySingleton = VolleySingleton.getInstance();
        imageLoader = volleySingleton.getImageLoader();
    }

    public void setProductList(ArrayList<Product> listProduct) {
        this.listProduct = listProduct;
        notifyItemRangeChanged(0, listProduct.size());
    }

    /**
     * Represent Items of the given type
     * @param parent parent
     * @param viewType type of the view
     * @return view holder order
     */
    @Override
    public ViewHolderOrder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = layoutInflater.inflate(R.layout.custom_prod_row, parent, false);

        ViewHolderOrder viewHolderOrder = new ViewHolderOrder(view);

        return viewHolderOrder;
    }

    /**
     * Display the data at a specific position
     * @param holder holder
     * @param position position
     */
    @Override
    public void onBindViewHolder(final ViewHolderOrder holder, final int position) {
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
                final int id = Integer.valueOf(currentProduct.getId());
                final String name = currentProduct.getName();
                final String uniCost = currentProduct.getUnitCost();
                final String barcode = currentProduct.getBarCode();
                final String image = currentProduct.getImage();
                final String category = currentProduct.getCategory();

                Intent intent = new Intent(v.getContext().getApplicationContext(), DetailProductActivity.class);
                intent.putExtra("id", id);
                intent.putExtra("name", name);
                intent.putExtra("unicost", uniCost);
                intent.putExtra("barcode", barcode);
                intent.putExtra("image", image);
                intent.putExtra("category", category);
                v.getContext().startActivity(intent);

                Log.i("Hotels list", "id = " + id);
                Log.i("Hotels list", "name = " + name);
                Log.i("Hotels list", "unicost = " + uniCost);
                Log.i("Hotels list", "barcode = " + barcode);
                Log.i("Hotels list", "image = " + image);
                Log.i("Hotels list", "category = " + category);

            }
        });
    }

    /**
     * Loading the image
     * @param prodImgUrl url image
     * @param holder holder
     */
    private void loadImage(String prodImgUrl, final ViewHolderOrder holder) {
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
    class ViewHolderOrder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView prodId;
        private TextView prodName;
        private TextView prodUnicost;
        private TextView prodBarcode;
        private ImageView prodImg;
        private TextView prodCat;

        public ViewHolderOrder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            prodId = (TextView) itemView.findViewById(R.id.id);
            prodName = (TextView) itemView.findViewById(R.id.prodName);
            prodUnicost = (TextView) itemView.findViewById(R.id.prodUnicost);
            prodBarcode = (TextView) itemView.findViewById(R.id.prodBarcode);
            prodImg = (ImageView) itemView.findViewById(R.id.prodImg);
            prodCat = (TextView) itemView.findViewById(R.id.prodCat);

            prodImg.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
        }
    }
}

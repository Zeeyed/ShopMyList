package materialshop.curiousbat.shopmylist.extra;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import materialshop.curiousbat.shopmylist.model.Product;

/**
 * Sort List Products
 * Created by Zied on 15/11/2015.
 */
public class ProductSorter {

    public void sortNewsTechByName(ArrayList<Product> newsTeches){
        Collections.sort(newsTeches, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    public void sortNewsTechByDate(ArrayList<Product> newsTeches){
        Collections.sort(newsTeches, new Comparator<Product>() {
            @Override
            public int compare(Product lhs, Product rhs) {
                return lhs.getUnitCost().compareTo(rhs.getUnitCost());
            }
        });
    }
}

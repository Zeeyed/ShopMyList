package materialshop.curiousbat.shopmylist.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.util.Date;

/**
 * Product Class
 * Created by Zied on 01/11/2015.
 */
public class Product implements Parcelable {

    private String id;
    private String name;
    private String unitCost;
    private String barCode;
    private String image;
    private String category;

    public Product() {
    }

    public Product(String name, String unitCost, String barCode, String image, String category) {
        this.name = name;
        this.unitCost = unitCost;
        this.barCode = barCode;
        this.image = image;
        this.category = category;
    }

    protected Product(Parcel in) {
        id = in.readString();
        name = in.readString();
        unitCost = in.readString();
        barCode = in.readString();
        image = in.readString();
        category = in.readString();

    }

    public static final Creator<Product> CREATOR = new Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel in) {
            return new Product(in);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUnitCost(String unitCost) {
        this.unitCost = unitCost;
    }

    public void setBarCode(String barCode) {
        this.barCode = barCode;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getId() {

        return id;
    }

    public String getImage() {
        return image;
    }

    public String getBarCode() {
        return barCode;
    }

    public String getUnitCost() {
        return unitCost;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    @Override
    public String toString() {
        return "Name " + name +
                " / uniCost " + unitCost +
                " / barCode " + barCode +
                " / image " + image;

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(unitCost);
        dest.writeString(barCode);
        dest.writeString(image);
    }
}

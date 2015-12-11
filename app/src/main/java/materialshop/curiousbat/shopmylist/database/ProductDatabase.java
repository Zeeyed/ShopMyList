package materialshop.curiousbat.shopmylist.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import materialshop.curiousbat.shopmylist.logging.L;
import materialshop.curiousbat.shopmylist.model.Product;
import materialshop.curiousbat.shopmylist.model.User;

/** SQLite Product, Favorite, User Database
 * Created by Zied on 01/11/2015.
 */
public class ProductDatabase {

    private static final int VERSION_BDD = 4;
    private static final String NAME_BDD = "shopMyList.db";

    public static final String TABLE_PRODUCT = "product";
    public static final String TABLE_FAVORITE = "favorite";
    public static final String TABLE_USER = "user";


    public static final String ID_PRODUCT = "id";
    public static final String NAME_PRODUCT = "name";
    public static final String UNICOST_PRODUCT = "unicost";
    public static final String BARCODE_PRODUCT = "barcode";
    public static final String IMG_PRODUCT = "img";
    public static final String CAT_PRODUCT = "category";

    public static final String ID_FAVORIT = "id";
    public static final String NAME_FAVORIT = "name";
    public static final String UNICOST_FAVORIT = "unicost";
    public static final String BARCODE_FAVORIT = "barcode";
    public static final String IMG_FAVORIT = "img";
    public static final String CAT_FAVORIT = "category";

    public static final String ID_USER = "id";
    public static final String NAME_USER = "name";
    public static final String EMAIL_USER = "email";
    public static final String PASS_USER = "password";


    private ProductHelper mHelper;
    private SQLiteDatabase mDatabase;


    public ProductDatabase(Context context) {
        mHelper = new ProductHelper(context, NAME_BDD, null, VERSION_BDD);
    }

    public void open(){
        mDatabase = mHelper.getWritableDatabase();
    }

    public void close(){
        mDatabase.close();
    }

    public SQLiteDatabase getDb(){
        return mDatabase;
    }

    /**
     * Insert Products into Sqlite
     * @param product product object
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertTop(Product product) {
        Cursor cursor = mDatabase.rawQuery("SELECT MAX(" + ProductHelper.ID_PRODUCT
                + ") FROM " + ProductHelper.TABLE_PRODUCT, null);
        if (cursor.moveToFirst())
            product.setId(String.valueOf(cursor.getInt(0) + 1));
        else {
            product.setId("1");
        }
        ContentValues values = new ContentValues();

        values.put(ID_PRODUCT, product.getId());
        values.put(NAME_PRODUCT, product.getName());
        values.put(UNICOST_PRODUCT, product.getUnitCost());
        values.put(BARCODE_PRODUCT, product.getBarCode());
        values.put(IMG_PRODUCT, product.getImage());
        values.put(CAT_PRODUCT, product.getCategory());

        return mDatabase.insert(TABLE_PRODUCT, null, values);

    }

    /**
     * Remove All products
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int removeAllProducts() {
        return mDatabase.delete(TABLE_PRODUCT, null, null);
    }

    /**
     * Remove a specific product
     * @param index index
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int removeProduct(int index) {
        return mDatabase.delete(TABLE_PRODUCT, ID_PRODUCT + " = " + index, null);
    }


    /**
     * Select from Sqlite all products from product table
     * @return List of products , otherwise null
     */
    public List<Product> selectAll() {
        open();
        List<Product> list = new ArrayList<Product>();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                Product a = new Product();
                a.setId(String.valueOf(cursor.getInt(0)));
                a.setName(cursor.getString(1));
                a.setUnitCost(cursor.getString(2));
                a.setBarCode(cursor.getString(3));
                a.setImage(cursor.getString(4));
                a.setCategory(cursor.getString(5));
                list.add(a);
                cursor.moveToNext();
            }
        }

        return list;
    }

    /**
     * Insert User into Sqlite
     * @param user user object
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertUser(User user) {
        Cursor cursor = mDatabase.rawQuery("SELECT MAX(" + ProductHelper.ID_USER
                + ") FROM " + ProductHelper.TABLE_USER, null);
        if (cursor.moveToFirst())
            user.setId(cursor.getInt(0) + 1);
        else {
            user.setId(1);
        }
        ContentValues values = new ContentValues();

        values.put(ID_USER, user.getId());
        values.put(NAME_USER, user.getName());
        values.put(EMAIL_USER, user.getEmail());
        values.put(PASS_USER, user.getPassword());

        return mDatabase.insert(TABLE_USER, null, values);

    }

    /**
     * Get a user from his email and password
     * @param email user email
     * @param pass user password
     * @return user object, otherwise null
     */
    public User searchPassword(String email, String pass){
        open();
        Log.d("Pass ", email);
        User user = new User();
        String selectQuery = "SELECT  * FROM " + TABLE_USER +" "+" where "+NAME_USER +" = \'"+ email+"\' AND "+PASS_USER+"= \'"+ pass +"\'";
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                user.setId(cursor.getInt(0));
                user.setName(cursor.getString(1));
                user.setEmail(cursor.getString(2));
                user.setPassword(cursor.getString(3));

                cursor.moveToNext();
            }
        }
        cursor.close();
        return user;
    }

    /**
     * Insert favorites product into Sqlite
     * @param product product object
     * @return the row ID of the newly inserted row, or -1 if an error occurred
     */
    public long insertFavorites(Product product) {

        Cursor cursor = mDatabase.rawQuery("SELECT MAX(" + ProductHelper.ID_FAVORIT
                + ") FROM " + ProductHelper.TABLE_FAVORITE, null);
        if (cursor.moveToFirst())
            product.setId(String.valueOf(cursor.getInt(0) + 1));
        else {
            product.setId("1");
        }

        ContentValues values = new ContentValues();

        values.put(ID_FAVORIT, product.getId());
        values.put(NAME_FAVORIT, product.getName());
        values.put(UNICOST_FAVORIT, product.getUnitCost());
        values.put(BARCODE_FAVORIT, product.getBarCode());
        values.put(IMG_FAVORIT, product.getImage());
        values.put(CAT_FAVORIT, product.getCategory());

        return mDatabase.insert(TABLE_FAVORITE, null, values);

    }

    /**
     * Remove all favorite products
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int removeAllFavorites() {
        return mDatabase.delete(TABLE_FAVORITE, null, null);
    }

    /**
     * Remove a specific favorite product
     * @param index index
     * @return the number of rows affected if a whereClause is passed in, 0 otherwise
     */
    public int removeFavorite(int index) {
        return mDatabase.delete(TABLE_FAVORITE, ID_FAVORIT + " = " + index, null);
    }

    /**
     * Get All favorites products
     * @return List of favorites products
     */
    public ArrayList<Product> selectAllFavorites() {
        open();
        ArrayList<Product> list = new ArrayList<>();

        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {

            while (!cursor.isAfterLast()) {
                Product a = new Product();
                a.setId(String.valueOf(cursor.getInt(0)));
                a.setName(cursor.getString(1));
                a.setUnitCost(cursor.getString(2));
                a.setBarCode(cursor.getString(3));
                a.setImage(cursor.getString(4));
                a.setCategory(cursor.getString(5));
                list.add(a);
                cursor.moveToNext();
            }
        }

        return list;
    }

    /**
     * Get a specific favorite product
     * @param id id favorite
     * @return Favorite Product
     */
    public Product selectfavorite(int id ) {
        open();
        Product product =new Product();
        String selectQuery = "SELECT  * FROM " + TABLE_FAVORITE +" "+" where "+ID_FAVORIT +" = "+id ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                product.setId(String.valueOf(cursor.getInt(0)));
                product.setName(cursor.getString(1));
                product.setUnitCost(cursor.getString(2));
                product.setBarCode(cursor.getString(3));
                product.setImage(cursor.getString(4));
                product.setCategory(cursor.getString(5));
                cursor.moveToNext();
            }
        }

        return product;
    }


    /**
     * Get the selected product by its barcode
     * @param id product id
     * @return product object, otherwise null
     */
    public Product selectProductByBarCode(int id ) {
        open();
        Product product =new Product();
        String selectQuery = "SELECT  * FROM " + TABLE_PRODUCT +" "+" where "+ID_PRODUCT +" = "+id ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                product.setId(String.valueOf(cursor.getInt(0)));
                product.setName(cursor.getString(1));
                product.setUnitCost(cursor.getString(2));
                product.setBarCode(cursor.getString(3));
                product.setImage(cursor.getString(4));
                product.setCategory(cursor.getString(5));

                cursor.moveToNext();
            }
        }

        return product;
    }

    /**
     * Overriding toString method
     * @param product product object
     * @return Product String details
     */
    public String toString(Product product) {
        return "ID " +  product.getId()+
                " / Name " +  product.getName()+
                " / uniCost " + product.getUnitCost() +
                " / barCode " + product.getBarCode() +
                " / image " + product.getImage() +
                " / Category " + product.getCategory();
    }
}

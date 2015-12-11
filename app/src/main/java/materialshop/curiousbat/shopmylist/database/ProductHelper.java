package materialshop.curiousbat.shopmylist.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/** SQLite Statements Product, Favorite and User
 * Created by Zied on 12/11/2015.
 */
public class ProductHelper extends SQLiteOpenHelper{

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

    private static final String CREATE_PRODUCT = "CREATE TABLE " + TABLE_PRODUCT + " ("+
            ID_PRODUCT + " INTEGER, "+
            NAME_PRODUCT + " TEXT, "+
            UNICOST_PRODUCT + " TEXT, "+
            BARCODE_PRODUCT + " TEXT, "+
            IMG_PRODUCT + " TEXT, "+
            CAT_PRODUCT + " TEXT);";

    private static final String CREATE_FAVORITES = "CREATE TABLE " + TABLE_FAVORITE + " ("+
            ID_FAVORIT + " INTEGER, "+
            NAME_FAVORIT + " TEXT, "+
            UNICOST_FAVORIT + " TEXT, "+
            BARCODE_FAVORIT + " TEXT, "+
            IMG_FAVORIT + " TEXT, "+
            CAT_FAVORIT + " TEXT);";

    private static final String CREATE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_USER + " ("+
            ID_USER + " INTEGER PRIMARY KEY NOT NULL, "+
            NAME_USER + " TEXT, "+
            EMAIL_USER + " TEXT, "+
            PASS_USER + " TEXT);";

    Context ctx;

    String dbname;
    String dbpath;

    public ProductHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
        this.ctx = context;
        this.dbname = name;
        this.dbpath = this.ctx.getDatabasePath(dbname).getAbsolutePath();
        Log.e("Path 1", dbpath);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_PRODUCT);
        db.execSQL(CREATE_FAVORITES);
        db.execSQL(CREATE_USER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE " + TABLE_PRODUCT + ";");
        db.execSQL("DROP TABLE " + TABLE_FAVORITE + ";");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER + ";");
        if(newVersion>oldVersion)
            onCreate(db);
        }

}

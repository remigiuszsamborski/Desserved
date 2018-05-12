package net.swierkowski.cookbook4;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

public class RecipesDbAdapter {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "Recipes.db";

    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + Produkty.TABLE_NAME + " (" +
                    Produkty._ID + " INTEGER PRIMARY KEY," +
                    Produkty.COLUMN_NAME_NAZWA + " TEXT," +
                    Produkty.COLUMN_NAME_RESTRYKCJE + " INTEGER);";

    public static class Produkty implements BaseColumns {
        public static final String TABLE_NAME = "produkty";
        public static final String COLUMN_NAME_NAZWA = "nazwa";
        public static final String COLUMN_NAME_RESTRYKCJE = "restrykcje";
    }

    private static final String TAG = "RecipesDbAdapter";
    private DatabaseHelper mDbHelper;
    private SQLiteDatabase mDb;
    private final Context mCtx;

    private String orderBy = null;

    public class DatabaseHelper extends SQLiteOpenHelper {
        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + Produkty.TABLE_NAME;

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(SQL_CREATE_ENTRIES);
        }
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL(SQL_DELETE_ENTRIES);
            onCreate(db);
        }
        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }
    }

    public RecipesDbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    public RecipesDbAdapter open() throws SQLException {
        mDbHelper = new RecipesDbAdapter.DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        if (mDbHelper != null) {
            mDbHelper.close();
        }
    }

    public long createProduct(String nazwa, int restrykcjeDefault) {
        ContentValues initialValues = new ContentValues();
        initialValues.put(Produkty.COLUMN_NAME_NAZWA, nazwa);
        initialValues.put(Produkty.COLUMN_NAME_RESTRYKCJE, restrykcjeDefault);
        return mDb.insert(Produkty.TABLE_NAME, null, initialValues);
    }

    public boolean deleteAllProducts() {
        int doneDelete = 0;
        doneDelete = mDb.delete(Produkty.TABLE_NAME, null , null);
        Log.w(TAG, Integer.toString(doneDelete));
        return doneDelete > 0;
    }

    public Cursor fetchProductsByName(String inputText) throws SQLException {
        Log.w(TAG, "Szukamy: " + inputText);

        Cursor mCursor = null;

        if (inputText == null || inputText.length () == 0) {
            mCursor = mDb.query(Produkty.TABLE_NAME, new String[] {Produkty._ID,Produkty.COLUMN_NAME_NAZWA,Produkty.COLUMN_NAME_RESTRYKCJE}, null, null, null, null, orderBy, null);

        } else {
            mCursor = mDb.query(Produkty.TABLE_NAME, new String[] {Produkty._ID,Produkty.COLUMN_NAME_NAZWA,Produkty.COLUMN_NAME_RESTRYKCJE},
                    Produkty.COLUMN_NAME_NAZWA + " like '%" + inputText + "%'",
                    null, null, null, orderBy, null);
        }
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public Cursor fetchAllProducts() {
        Cursor mCursor = mDb.query(Produkty.TABLE_NAME, new String[]{Produkty._ID,Produkty.COLUMN_NAME_NAZWA,Produkty.COLUMN_NAME_RESTRYKCJE}, null, null, null, null, orderBy, null);
        if (mCursor != null) {
            mCursor.moveToFirst();
        }
        return mCursor;
    }

    public void insertSomeProducts() {
        createProduct("mleko krowie",0);
        createProduct("mleko migdałowe",0);
        createProduct("mąka pszenna",0);
        createProduct("mąka kokosowa",0);
        createProduct("mąka ryżowa",0);
        createProduct("mąka gryczana",0);
        createProduct("orzechy włoskie",0);
        createProduct("orzechy ziemne",0);
        createProduct("orzechy nerkowca",0);
    }

    public void setOrderBy(String txt) {
        orderBy = txt;
    }

    public boolean updateProduct(long productID, int isExcluded) {
        ContentValues args = new ContentValues();
        args.put(Produkty.COLUMN_NAME_RESTRYKCJE, isExcluded);
        return mDb.update(Produkty.TABLE_NAME, args, Produkty._ID + " = " + productID, null) > 0;
    }


}

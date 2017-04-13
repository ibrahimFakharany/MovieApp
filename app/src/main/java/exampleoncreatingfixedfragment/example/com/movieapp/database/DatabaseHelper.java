package exampleoncreatingfixedfragment.example.com.movieapp.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import exampleoncreatingfixedfragment.example.com.movieapp.Adapters.TrailerAdapter;
import exampleoncreatingfixedfragment.example.com.movieapp.database.Contract.DetailEntry;
import exampleoncreatingfixedfragment.example.com.movieapp.database.Contract.ReviewEntry;
import exampleoncreatingfixedfragment.example.com.movieapp.database.Contract.TrailerEntry;


/**
 * Created by 450 G1 on 14/11/2016.d
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATAbASE_NAME = "favourit.db";

    private static final int DATABASE_VERSION = 1;

    public DatabaseHelper(Context context) {
        super(context, DATAbASE_NAME, null,DATABASE_VERSION);
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_REVIEW_TABLE = "CREATE TABLE " + ReviewEntry.TABLE_NAME + " (" +
                ReviewEntry.COLUMN_M_ID + " INTEGER PRIMARY KEY," +
                ReviewEntry.COLUMN_AUTHOR + " TEXT  NOT NULL, " +
                ReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL," +
                " FOREIGN KEY (" + ReviewEntry.COLUMN_M_ID + ") REFERENCES " +
                DetailEntry.TABLE_NAME + " (" + DetailEntry.COLUMN_ID + "))";

        final String SQL_CREATE_Trailer_TABLE = "CREATE TABLE " + TrailerEntry.TABLE_NAME + " (" +
                TrailerEntry.COLUMN_M_ID + " INTEGER PRIMARY KEY," +
                TrailerEntry.COLUMN_LINK + " TEXT  NOT NULL, " +
                " FOREIGN KEY (" + TrailerEntry.COLUMN_M_ID + ") REFERENCES " +
                DetailEntry.TABLE_NAME + " (" + DetailEntry.COLUMN_ID + "))";

        final String SQL_CREATE_Detail_TABLE = "CREATE TABLE " + DetailEntry.TABLE_NAME + " (" +

                DetailEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                // the Review ID of the review entry associated with this detail data
                DetailEntry.COLUMN_BACKDROP+ " VARCHAR NOT NULL, " +
                DetailEntry.COLUMN_POSTER + " VARCHAR NOT NULL, " +
                DetailEntry.COLUMN_FILM_NAME + " TEXT NOT NULL, " +
                DetailEntry.COLUMN_YEAR + " INTEGER NOT NULL," +
                DetailEntry.COLUMN_VOTE + " REAL NOT NULL, " +
                DetailEntry.COLUMN_OVER_VIEW + " VARCHAR NOT NULL, " +
                DetailEntry.COLUMN_TRIALER_KEY+ " INTEGER NOT NULL)";

                db.execSQL(SQL_CREATE_Detail_TABLE);
                db.execSQL(SQL_CREATE_Trailer_TABLE);
                db.execSQL(SQL_CREATE_REVIEW_TABLE);


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXIST " + DetailEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + TrailerEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXIST " + ReviewEntry.TABLE_NAME);
        onCreate(db);
    }

   public boolean insertMovie  (Integer id, String back_drop, String poster, String film_name, Integer year, String vote, String over_view)
   {
       SQLiteDatabase db = this.getWritableDatabase();
       ContentValues contentValues = new ContentValues();
       contentValues.put("id", id);
       contentValues.put("back_drop", back_drop);
       contentValues.put("poster", poster);
       contentValues.put("film_name", film_name);
       contentValues.put("year", year);
       contentValues.put("vote", vote);
       contentValues.put("over_view", over_view);
       db.insert("Detail", null, contentValues);
       return true;
   }
    public Cursor getData(int id){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res =  db.rawQuery( "select * from Detail where id="+id+"", null );
        return res;
    }


}

package org.cclab.microsoft_gpsreceiver;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;

public class GpsDBHelper {


	private static final String DATABASE_NAME = "msgps.db";
	private static final String TABLE_NAME = "msgps";
    private static final int DATABASE_VERSION = 1 ;
    
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_LATITUDE = "lat";
    private static final String COLUMN_LONGITUDE = "lng";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_nSATEELITES = "num_of_sattelintes";
    private static final String COLUMN_HDOP = "hdop";
    private static final String COLUMN_SENT = "is_sent";
    
    private Context context;
    public SQLiteDatabase sqlDb;
   
    
    public GpsDBHelper(Context context) {
    	this.context = context;
    }
    
    public void open() {
    	
    	DbHelper dbHelper = new DbHelper(context, DATABASE_NAME, null, DATABASE_VERSION);
    	sqlDb = dbHelper.getWritableDatabase();
    }
    
    public void close() {
    	sqlDb.close();
    }
    
    public void insertGps(GpsData gps) {
    	
    	String _INSERT = "insert into " + TABLE_NAME + "("
    			+ COLUMN_LATITUDE + ", " + COLUMN_LONGITUDE + ", " + COLUMN_TIMESTAMP + ", " + COLUMN_nSATEELITES + ", "
    			+ COLUMN_HDOP + ", " + COLUMN_SENT + ") values("
    			+ gps.getLat() + ", " + gps.getLng() + ", " + gps.getTimestamp() + ", " + gps.getNStatellite() + ", "
    			+ gps.getHdop() + ", " + "0);"; 
    			
    	sqlDb.execSQL(_INSERT);
    }
    
    public ArrayList<GpsData> getGpsListNotSent() {
    
    	ArrayList<GpsData> GpsListToReturn = new ArrayList<GpsData>();
    	
    	String _SELECT = "SELECT * FROM " + TABLE_NAME + " WHERE "+ COLUMN_SENT + "=0";
    	
    	Cursor cursor = sqlDb.rawQuery(_SELECT, null);
    	
    	while(cursor.moveToNext()) {
    		GpsData data = new GpsData(
    				cursor.getLong(1), cursor.getLong(2), cursor.getLong(3), cursor.getInt(4), cursor.getDouble(5)
    				);
    		GpsListToReturn.add(data);
    	}


    	if(GpsListToReturn.size() == 0)
    		return null;
    	
    	String _UPDATE = "UPDATE " + TABLE_NAME + " SET " + COLUMN_SENT + "=1 WHERE " + COLUMN_SENT + "=0;";
    	sqlDb.execSQL(_UPDATE);
    	
		return GpsListToReturn;
    	
    }
    

    
	
    private class DbHelper extends SQLiteOpenHelper {

		public DbHelper(Context context, String name, CursorFactory factory,
				int version) {
			super(context, name, factory, version);
			// TODO Auto-generated constructor stub
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			String _CREATE = "CREATE TABLE " + TABLE_NAME + "("
					+ COLUMN_ID + " integer primary key autoincrement, "
					+ COLUMN_LATITUDE + " real not null, "
					+ COLUMN_LONGITUDE + " real not null, "
					+ COLUMN_TIMESTAMP + " real not null, "
					+ COLUMN_nSATEELITES + " integer not null, "
					+ COLUMN_HDOP + " real not null, "
					+ COLUMN_SENT + " integer not null default 0"
					+ ");";
			
			db.execSQL(_CREATE);
			
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			 db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
	         onCreate(db);
		}
    	
    }
}

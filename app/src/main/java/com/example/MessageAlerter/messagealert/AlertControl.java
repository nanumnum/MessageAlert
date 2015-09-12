package com.example.MessageAlerter.messagealert;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//--------------------------------------------------------------------------------------------------
// Holds all the server details and handles database insert/updates/deletes/selects.
//--------------------------------------------------------------------------------------------------
public class AlertControl {

    // To prevent someone from accidentally instantiating the contract class, give it an empty constructor.
    public AlertControl(Context pContext) {
        context = pContext;
    }

    //Set from action
    public Context context;

    public List<AlertSettingDetails> alertSettingDetails = new ArrayList<AlertSettingDetails>();

    //Tables to store saved data
    private final String SQL_CREATE_ENTRIES = "CREATE TABLE AlertSettings (asID INTEGER PRIMARY KEY, asMatchingText TEXT, asSoundFileLocation TEXT, asOrder INTEGER )";
    private final String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS AlertSettings";

    private SavedDataDbHelper dbHelper;

    public class SavedDataDbHelper extends SQLiteOpenHelper {
        // If you change the database schema, you must increment the database version.
        public static final int DATABASE_VERSION = 1;
        public static final String DATABASE_NAME = "SavedData.db";

        public SavedDataDbHelper(Context context) {
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

    public void InitiateDatabase(){
        dbHelper = new SavedDataDbHelper(context);
    }

    public void InsertNewAlertSettingDetails(String matchingText, String fileLocation, Integer settingOrder){
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put("asMatchingText", matchingText);
        values.put("asSoundFileLocation", fileLocation);
        values.put("asOrder", settingOrder);

        long newRowId;
        newRowId = db.insert("AlertSettings","null",values);
    }

    public void UpdateAlertSettingDetails(int idToUpdate, String matchingText, String fileLocation, Integer settingOrder){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        //Columns to update
        ContentValues values = new ContentValues();
        values.put("asMatchingText", matchingText);
        values.put("asSoundFileLocation", fileLocation);
        values.put("asOrder", settingOrder);

        // Which row to update, based on the ID
        String selection = "asID = ?";
        String[] selectionArgs = { String.valueOf(idToUpdate) };

        int count = db.update("AlertSettings",values,selection,selectionArgs);
    }

    public void DeleteConnectionDetails(int idToDelete){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "asID = ?";

        String[] selectionArgs = { String.valueOf(idToDelete) };

        db.delete("AlertSettings", selection, selectionArgs);
    }

    //----------------------------------------------------------------------------------------------
    // Returns all the saved down connection details as a list of class ConnectionDetail.
    //----------------------------------------------------------------------------------------------
    public List<AlertSettingDetails> GetSavedConnectionDetails(){
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] select = {"asID","asMatchingText","asSoundFileLocation"};
        String sortOrder = "asOrder DESC";

        //Put selection statement results into cursor.
        Cursor c = db.query("AlertSettings", select, null, null, null, null, sortOrder);

        int id ;
        String matchingText;
        String asSoundFileLocation;
        int order;

        //Remove existing connection details in class.
        alertSettingDetails.clear();

        //Iterate over cursor to build list of connection details.
        if(c.moveToFirst()){
            do{
                id = c.getInt(c.getColumnIndexOrThrow("asID"));
                matchingText =  c.getString(c.getColumnIndexOrThrow("asMatchingText"));
                asSoundFileLocation =  c.getString(c.getColumnIndexOrThrow("asSoundFileLocation"));
                order =  c.getInt(c.getColumnIndexOrThrow("asOrder"));

                AlertSettingDetails asD  = new AlertSettingDetails(id, matchingText, asSoundFileLocation, order);


                alertSettingDetails.add(asD);

            }while (c.moveToNext());
        }

        c.close();

        return alertSettingDetails;
    }

    //----------------------------------------------------------------------------------------------
    // Update/Delete/Rename methods.
    //----------------------------------------------------------------------------------------------
    public void rename(int position, String newMatchingText){
        AlertSettingDetails asD = alertSettingDetails.get(position);
        asD.matchingText = newMatchingText;
        UpdateAlertSettingDetails(asD.id, asD.matchingText, asD.soundFileLocation,  asD.order);
    }

    public void delete(int position){
        AlertSettingDetails asD = alertSettingDetails.get(position);
        alertSettingDetails.remove(position);
        DeleteConnectionDetails(asD.id);
    }

    public AlertSettingDetails getAlertSettingDetails(int position){
        return  alertSettingDetails.get(position);
    }


}

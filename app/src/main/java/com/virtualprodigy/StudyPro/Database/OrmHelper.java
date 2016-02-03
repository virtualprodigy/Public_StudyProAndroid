package com.virtualprodigy.studypro.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.virtualprodigy.studypro.Models.Note;
import com.virtualprodigy.studypro.R;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by virtualprodigyllc on 02/01/16.
 */
public class OrmHelper extends OrmLiteSqliteOpenHelper {
    //relics from the old db. Will slow remove later. I don't want fix what isn't broken(aka break something...too much happening in this current update)
    private final static String DB_Name = "studyProDatabase";
    private final String DB_Table_Memoirs = "notesTable";

    private final String TAG = this.getClass().getSimpleName();
    private Context context;

    public OrmHelper(Context context) {
        super(context, DB_Name, null, context.getResources().getInteger(R.integer.DB_Version));
        this.context = context;
        getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            Log.i(TAG, "Creating database");
            TableUtils.createTable(connectionSource, Note.class);
        } catch (SQLException e) {
            Log.e(TAG, "Error creating database", e);
        }

    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        int DB_Version = context.getResources().getInteger(R.integer.DB_Version);
    }

    /**
     * this genric method is for grabbing the Dao for any ormlite table
     */
    public <T, V> Dao<T, V> getTypeDao(Class<T> classType, Class<V> idType) throws SQLException {
        return getDao(classType);
    }

}

package net.toilet.my.is.where.com.whereismytoilet.Tools;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import net.toilet.my.is.where.com.whereismytoilet.View.Toilette;

import java.util.ArrayList;

/**
 * Created by quentinbaillet on 03/03/15.
 */
public class DataBase extends SQLiteOpenHelper {

    protected static String dataBaseName = "ToiletteDataBase";
    protected static String dataBaseTableName = "Toilette";
    protected static String selectOneInformation = "SELECT * FROM %s WHERE %s='%s";
    protected static String selectAllInformation = "SELECT * FROM %s";
    protected static String searchInTable = "SELECT * FROM %s WHERE Nom='*%s*'";
    protected static SQLiteDatabase database;

    public DataBase(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, dataBaseName, factory, 1);
    }

    public void openWritableDataBase(){
        database = this.getWritableDatabase();
    }

    public void openReadableDatabase(){ database = this.getReadableDatabase();}

    public void closeDataBase(){
        database.close();
    }

    public boolean clearTable(){
        try{
            database.execSQL(String.format("DELETE FROM %s", dataBaseTableName));
            return true;
        }catch (Exception ex){
            return false;
        }
    }

    public void insertToiletteInDB(Toilette toilette){
        try{
            database.execSQL(String.format("INSERT INTO Toilette VALUES(%s,%s,%s,%s,%s)", toilette.getGid(), toilette.getIdentifiant(), toilette.getAdresse(), toilette.getVille(), toilette.getObservation()));
        }catch (Exception ex){
            throw ex;
        }
    }

    public ArrayList<Toilette> getAllToilette(){
        try{
            this.openWritableDataBase();

            String selectQuery = String.format(selectAllInformation, dataBaseTableName);
            Cursor c = database.rawQuery(selectQuery, null);
            ArrayList<Toilette> toilettes = new ArrayList<Toilette>();

            if(c.moveToFirst()){

                do{
                    Toilette toilette = new Toilette();
                    toilette.setGid(c.getString(c.getColumnIndex("gid")));
                    toilette.setGid(c.getString(c.getColumnIndex("identifiant")));
                    toilette.setGid(c.getString(c.getColumnIndex("adresse")));
                    toilette.setGid(c.getString(c.getColumnIndex("ville")));
                    toilette.setGid(c.getString(c.getColumnIndex("observation")));
                    toilettes.add(toilette);
                }while(c.moveToNext());

            }else{
                c.close();
                return new ArrayList<Toilette>();
            }
            return toilettes;

        }catch (Exception ex){
            throw ex;
        }
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS Toilette(gid VARCHAR,identifiant VARCHAR,adresse VARCHAR,ville VARCHAR,observation VARCHAR);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}

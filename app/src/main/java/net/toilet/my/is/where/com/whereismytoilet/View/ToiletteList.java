package net.toilet.my.is.where.com.whereismytoilet.View;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import net.toilet.my.is.where.com.whereismytoilet.Tools.DataBase;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Created by quentinbaillet on 03/03/15.
 */
public class ToiletteList extends AsyncTask<String, Void, JSONArray>{

    private String WebServiceURL = "https://download.data.grandlyon.com/ws/grandlyon/gin_nettoiement.gintoilettepublique/all.json";
    private ArrayList<Toilette> toilettes;
    private ToiletteListEvent _event;
    private Context _context;

    public interface ToiletteListEvent{
        public void onToiletteListFinished(ArrayList<Toilette> toilettes);
        public void onToiletteListFailed(Exception ex);
    }

    public ToiletteList(ToiletteListEvent event, Context context){
        _event = event;
        _context = context;
    }

    public ArrayList<Toilette> getToilettes() {
        return toilettes;
    }

    public void setToilettes(ArrayList<Toilette> toilettes) {
        this.toilettes = toilettes;
    }


    @Override
    protected JSONArray doInBackground(String... params) {
        InputStream inputStream = null;
        String result = "";
        JSONArray jsonArray;
        try{
            Log.i("Adresse", WebServiceURL);

            HttpGet httpGet = new HttpGet(WebServiceURL);

            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse httpResponse = httpClient.execute(httpGet);

            inputStream = httpResponse.getEntity().getContent();

            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";

            JSONObject objet = new JSONObject(result);
            jsonArray = objet.getJSONArray("values");

        }catch (Exception ex){
            _event.onToiletteListFailed(new Exception("Une erreur s'est produite pendant la récupération des informations"));
            jsonArray = new JSONArray();
        }
        return jsonArray;
    }

    @Override
    protected void onPostExecute(JSONArray json) {
        try{
            toilettes = new ArrayList<Toilette>();
            for(int i = 0; i < json.length(); i++){
                String numerovoie = "";
                String observation = "";
                if(!json.getJSONArray(i).getString(2).equals("None"))
                    numerovoie = json.getJSONArray(i).getString(2);

                if(!json.getJSONArray(i).getString(4).equals("None"))
                    observation = json.getJSONArray(i).getString(4);

                toilettes.add(new Toilette(json.getJSONArray(i).getString(6), json.getJSONArray(i).getString(5),
                        String.format("%s %s",numerovoie,json.getJSONArray(i).getString(1)),
                        json.getJSONArray(i).getString(0), observation));
            }
            //InsertToiletteInDataBase();
            _event.onToiletteListFinished(toilettes);
        }catch (Exception ex){
            _event.onToiletteListFailed(ex);
        }
    }


    private void InsertToiletteInDataBase(){
        DataBase dataBase = new DataBase(_context, "", null, 1);
        dataBase.openWritableDataBase();

        for(int i = 0; i < toilettes.size(); i++){
            dataBase.insertToiletteInDB(toilettes.get(i));
        }

        dataBase.closeDataBase();
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }

    public List<String> GetListAddresses(){
        List<String> addresses = new ArrayList<String>();
           for(Toilette toilette : toilettes){
               addresses.add(toilette.getAdresse());
           }
        return addresses;
    }
}

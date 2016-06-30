package net.toilet.my.is.where.com.whereismytoilet.Models;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import net.toilet.my.is.where.com.whereismytoilet.Tools.DataBase;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by quentinbaillet on 03/03/15.
 */
public class ToiletteList extends AsyncTask<String, Void, JSONArray>{

    private String WebServiceURL = "https://download.data.grandlyon.com/ws/grandlyon/gin_nettoiement.gintoilettepublique/all.json";
    private ArrayList<Toilette> toilettes;
    private HashMap<String, Toilette> toiletsByCity = new HashMap<String, Toilette>();
    private ToiletteListEvent _event;
    private Context _context;

    public ArrayList<Toilette> toilettesToShow;

    public interface ToiletteListEvent{
        public void onToiletteListFinished(ArrayList<Toilette> toilettes);
        public void onToiletteListFailed(Exception ex);
    }

    public ToiletteList(ToiletteListEvent event, Context context){
        _event = event;
        _context = context;
    }

    public HashMap<String, Toilette> getToiletsByCity() {
        try {
            toiletsByCity = new HashMap<String, Toilette>();
            for (Toilette toilet : toilettesToShow)
                toiletsByCity.put(toilet.getVille(), toilet);
        }catch (Exception ex){
            return null;
        }

        return toiletsByCity;

    }

    public void setToiletsByCity(HashMap<String, Toilette> toiletsByCity) {
        this.toiletsByCity = toiletsByCity;
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

            URL url = new URL(WebServiceURL);
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("GET");

            inputStream = connection.getInputStream();

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
                    numerovoie = json.getJSONArray(i).getString(2) + " ";

                if(!json.getJSONArray(i).getString(4).equals("None"))
                    observation = json.getJSONArray(i).getString(4);

                toilettes.add(new Toilette(json.getJSONArray(i).getString(6), json.getJSONArray(i).getString(5),
                        String.format("%s%s",numerovoie,json.getJSONArray(i).getString(1)),
                        json.getJSONArray(i).getString(0), observation));
            }
            toilettesToShow = toilettes;
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
        BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line+"\n");
        }
        br.close();
        return sb.toString();

    }

    public List<String> GetListAddresses(){
        List<String> addresses = new ArrayList<String>();
           for(Toilette toilette : toilettes){
               addresses.add(toilette.getAdresse());
           }
        return addresses;
    }

    public void retablishList(){
        toilettesToShow = toilettes;
    }

    public void findWordInList(String[] words){
        toilettesToShow = new ArrayList<Toilette>();
        for(Toilette toilet : toilettes){
            if(toilet.findWord(words)){
                toilettesToShow.add(toilet);
            }
        }
    }
}

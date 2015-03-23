package net.toilet.my.is.where.com.whereismytoilet.View;

/**
 * Created by quentinbaillet on 03/03/15.
 */
public class Toilette {
    private String gid;
    private String identifiant;
    private String adresse;
    private String ville;
    private String observation;

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getIdentifiant() {
        return identifiant;
    }

    public void setIdentifiant(String identifiant) {
        this.identifiant = identifiant;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getVille() {
        return ville;
    }

    public void setVille(String ville) {
        this.ville = ville;
    }

    public String getObservation() {
        return observation;
    }

    public void setObservation(String observation) {
        this.observation = observation;
    }

    public Toilette(){}

    public Toilette(String _gid, String _identifiant, String _adresse, String _ville, String _observation){
        gid = _gid;
        identifiant = _identifiant;
        adresse = _adresse;
        ville = _ville;
        observation = _observation;
    }

    public Boolean findWord(String[] words){
        for(String word : words) {
            if (adresse.contains(word) || ville.contains(word) || observation.contains(word))
                return true;
        }
        return false;
    }
}

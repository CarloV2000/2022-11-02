/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.itunes;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.itunes.model.Genre;
import it.polito.tdp.itunes.model.Model;
import it.polito.tdp.itunes.model.Track;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {

	private Model model;
	
    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnPlaylist"
    private Button btnPlaylist; // Value injected by FXMLLoader

    @FXML // fx:id="cmbGenere"
    private ComboBox<String> cmbGenere; // Value injected by FXMLLoader

    @FXML // fx:id="txtDTOT"
    private TextField txtDTOT; // Value injected by FXMLLoader

    @FXML // fx:id="txtMax"
    private TextField txtMax; // Value injected by FXMLLoader

    @FXML // fx:id="txtMin"
    private TextField txtMin; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader

    @FXML
    void doCalcolaPlaylist(ActionEvent event) {

    	String dTOT = this.txtDTOT.getText();
    	Integer durataTot;
    	if(dTOT == null){
    		this.txtResult.setText("Inserire una valore nelcampo DTOT!");
    		return;
    	}
    	
    	try {
    		durataTot = Integer.parseInt(dTOT);
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo DTOT");
    		return;
    	}
    	
    	List<Track>res = new ArrayList<>(model.calcolaPercorso(durataTot));
    	if(res.isEmpty()) {
    		this.txtResult.setText("Nessun percorso troato!");
    		return;
    	}
    	String s = "Percorso trovato: \n";
    	for(Track x : res) {
    		s += x.getName()+"\n";
    	}
    	this.txtResult.setText(s);
    }

    @FXML
    void doCreaGrafo(ActionEvent event) {
    	String genere = this.cmbGenere.getValue();
    	String smin = this.txtMin.getText();
    	String smax = this.txtMax.getText();
    	Integer min;
    	Integer max;
    	if(smin == null){
    		this.txtResult.setText("Inserire una valore nelcampo min!");
    		return;
    	}
    	if(smax == null){
    		this.txtResult.setText("Inserire un valore nel campo max!");
    		return;
    	}
    	try {
    		min = Integer.parseInt(smin);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo min");
    		return;
    	}
    	try {
    		max = Integer.parseInt(smax);
    		
    	}catch(NumberFormatException e) {
    		this.txtResult.setText("Inserire un valore numerico nel campo max");
    		return;
    	}
    	String s = model.creaGrafo(min, max, genere);
    	this.txtResult.setText(s);
    	
    	String s2 = model.getConnectedComponents();
    	this.txtResult.appendText(s2);
    }

    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnPlaylist != null : "fx:id=\"btnPlaylist\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbGenere != null : "fx:id=\"cmbGenere\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtDTOT != null : "fx:id=\"txtDTOT\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMax != null : "fx:id=\"txtMax\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtMin != null : "fx:id=\"txtMin\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	for(Genre x : model.getAllGenres()) {
    		this.cmbGenere.getItems().add(x.getName());
    	}
    }

}

package it.polito.tdp.country;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.country.model.Country;
import it.polito.tdp.country.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class CountryController {
	

	private Model model ;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Country> boxPartenza;

    @FXML
    private ComboBox<Country> boxDestinazione;

    @FXML
    private TextArea txtResult;

    @FXML
    void handlePercorso(ActionEvent event) {
    	
    	Country destinazione = boxDestinazione.getValue() ;
    	
    	List<Country> percorso = model.getPercorso(destinazione) ;
    	
    	txtResult.appendText(percorso.toString()+"\n");

    }

    @FXML
    void handleRaggiungibili(ActionEvent event) {
    	Country partenza = boxPartenza.getValue() ;
    	if(partenza==null) {
    		txtResult.appendText("Errore: devi selezionare lo stato di partenza\n") ;
    		return ;
    	}
    	
    	List<Country> raggiungibili = model.getRaggiungibili(partenza) ;
    	
    	txtResult.appendText(raggiungibili.toString()+"\n");
    	
    	boxDestinazione.getItems().clear(); 
    	boxDestinazione.getItems().addAll(raggiungibili) ;
    }

    @FXML
    void initialize() {
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Country.fxml'.";
        assert boxDestinazione != null : "fx:id=\"boxDestinazione\" was not injected: check your FXML file 'Country.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Country.fxml'.";

    }

	/**
	 * @param model the model to set
	 */
	public void setModel(Model model) {
		this.model = model;
		
		// riempi la prima tendina con l'elenco completo delle nazioni
		boxPartenza.getItems().addAll(this.model.getCountries()) ;
	}

}

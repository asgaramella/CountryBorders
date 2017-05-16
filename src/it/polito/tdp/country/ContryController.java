package it.polito.tdp.country;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import it.polito.tdp.country.model.Country;
import it.polito.tdp.country.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;

public class ContryController {
	
	private Model model;

    /**
     * nel SetModel punto in cui posso inizializzare le parti dell'interfaccia
     * che dipendono dai dati del DB !!!
	 */
	public void setModel(Model model) {
		this.model = model;
		boxPartenza.getItems().addAll(model.getCountries());
		
		
		
	}

	@FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Country> boxPartenza;

    @FXML
    private Button btnRaggiungibili;

    @FXML
    private ComboBox<Country> boxDestinazione;

    @FXML
    private Button btnPercorsi;

    @FXML
    private TextArea txtResult;

    @FXML
    void handlePercorso(ActionEvent event) {
    	
    	Country destinazione= boxDestinazione.getValue();
    	List<Country> percorso= model.getPercorso(destinazione);
    	
    	for(Country ctemp:percorso){
    		txtResult.appendText(ctemp.toString()+"\n");
    	}
    }

    @FXML
    void handleRaggiungibili(ActionEvent event) {
    	Country partenza=boxPartenza.getValue();
    	if(partenza==null){
    		txtResult.appendText("Selezionare stato di partenza! \n");
    	}
    	List<Country> raggiungibili=model.getRaggiungibili(partenza);
    	txtResult.clear();
    	for(Country ctemp:raggiungibili){
    		txtResult.appendText(ctemp.toString()+"\n");
    	}
    	//potrei richiamare il metodo più volte devo cancellare lista già inserita 
    	boxDestinazione.getItems().clear();
    	boxDestinazione.getItems().addAll(raggiungibili);
    	
    }

    @FXML
    void initialize() {
        assert boxPartenza != null : "fx:id=\"boxPartenza\" was not injected: check your FXML file 'Country.fxml'.";
        assert btnRaggiungibili != null : "fx:id=\"btnRaggiungibili\" was not injected: check your FXML file 'Country.fxml'.";
        assert boxDestinazione != null : "fx:id=\"boxDestinazione\" was not injected: check your FXML file 'Country.fxml'.";
        assert btnPercorsi != null : "fx:id=\"btnPercorsi\" was not injected: check your FXML file 'Country.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Country.fxml'.";

    }
}

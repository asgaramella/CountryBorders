package it.polito.tdp.country.model;

import java.util.HashMap;
import java.util.Map;

//classe che funge da wrapper di una mappa
public class CountryIdMap {
	
	private Map<Integer,Country> map;

	
	public CountryIdMap() {
		super();
		map=new HashMap<>();
	}
	
	//costruisco l'eq del metodo get delle mappa
	
	public Country get(Integer code){
		return map.get(code);
	}
	/**
	 * Controlliamo se oggetto che è stata appena creato esiste già nella mappa
	 * no) allora devo aggiungerlo, ritorno rif dell'ogg appena creato
	 * si) ritorno riferimento dell'ogg esistente 
	 * 
	 * @param country elemento da aggiungere
	 * @return references dell'ogg
	 */
	
	public Country put(Country country){
		Country old= map.get(country.getcCode());
		
		if(old==null){
			map.put(country.getcCode(), country);
			return country;
		}
		else
			return old;
		
	}
	

}

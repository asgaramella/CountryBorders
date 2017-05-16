package it.polito.tdp.country.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graphs;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;
import org.jgrapht.traverse.BreadthFirstIterator;

import it.polito.tdp.db.CountryDao;

public class Model {
	
	private UndirectedGraph<Country, DefaultEdge> graph  ;
	private List<Country> countries;
	private Map<Country,Country> alberoVisita;
	private CountryIdMap countryIdMap= new CountryIdMap();
			
		public	Model() {
	}
	
	//chiedi differenza rispetto al tuo metodo
	public List<Country> getCountries(){
		//faccio query solo se lista ancora vuota altrimenti sfrutto l'aver fatto caching
		if(this.countries==null){
			CountryDao dao=new CountryDao();
			//Dubbio cosi non sto facendo solo copia dei references ??
			//sì, ma se ti sta bene che vedi sempre versione nello stack che tutti possono modiciare ok
			// se invece vuoi una fotografia istantanea devi passarti lista come par del costruttore
			//in questo caso se qualcuno farà modifiche tu continuerai a vedere versione passata nel costruttore
			this.countries=dao.listCountry();
			}
		return this.countries;
	}
	
	//OSS: nei metodi creaGrafo devi richiamare f getCountries e non la lista diretamente percè potrebbe essere ancora null, non conosco ordine co cui i metodi saranno invocati
	
	public List<Country> getRaggiungibili(Country partenza){
		// non uso this.graph perchè non sono sicuro sia inizializzato
		UndirectedGraph<Country,DefaultEdge> g =this.getGrafo();
		
		BreadthFirstIterator<Country,DefaultEdge> bfi=new BreadthFirstIterator<Country,DefaultEdge>(g,partenza);
		
		List<Country> list= new ArrayList<Country>();
		Map<Country,Country> albero=new HashMap<Country,Country>();
		albero.put(partenza, null);
		
		
		bfi.addTraversalListener(new CountryTraversalListener(albero,graph));
		
			while(bfi.hasNext()){
				list.add(bfi.next());
			}
		
		this.alberoVisita=albero;
			
			
		return list;
		}
	
	
	
	private UndirectedGraph<Country, DefaultEdge> getGrafo(){
		//affinchè grafo sia nullo non puoi fare new graph() nel model altrimenti 
		//il grafo sarà vuoto con 0 vertici ma non nullo !!!
		//metti istruzione in ciascuno dei metodi crea Grafico !
		if(this.graph==null){
			this.creaGrafo3();
		}
		return this.graph;
		
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Prima versione: per ogni coppia di vertici, chiedo al database se esiste un arco.
	 * Poco efficiente, query molto semplice (confinanti) eseguita n*n volte
	 */
	public void creaGrafo1() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 1
		for(Country c1: graph.vertexSet()) {
			for(Country c2: graph.vertexSet()) {
				if(!c1.equals(c2)) {
					if( dao.confinanti(c1, c2) ) {
						graph.addEdge(c1, c2) ;
					}
				}
			}
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Seconda versione: per ogni vertice, chiedo al database la lista dei vertici ad esso confinanti.
	 * query più complessa eseguita n volte
	 */
	public void creaGrafo2() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 2
		for(Country c: graph.vertexSet()) {
			List<Country> adiacenti = dao.listAdiacenti(c) ;
			for(Country c2: adiacenti)
				graph.addEdge(c, c2) ;
		}
	}
	
	/**
	 * Creazione del grafo CountryBorders.
	 * Terza versione: una sola volta, chiedo al database l'elenco delle coppie di vertici confinanti.
	 * query complessa eseguita 1 sola volta
	 */
	public void creaGrafo3() {
		this.graph = new SimpleGraph<>(DefaultEdge.class) ;
		
		CountryDao dao = new CountryDao() ;
		
		// crea i vertici del grafo
		Graphs.addAllVertices(graph, this.getCountries()) ;
	
		// crea gli archi del grafo -- versione 3
		for(CountryPair cp : dao.listCoppieCountryAdiacenti()) {
			graph.addEdge(cp.getC1(), cp.getC2()) ;
		}
	}

	public void printStats() {
		System.out.format("Grafo: Vertici %d, Archi %d\n", graph.vertexSet().size(), graph.edgeSet().size());
	}

	public List<Country> getPercorso(Country destinazione) {
		List<Country> percorso=new ArrayList<Country>();
		
		Country c=destinazione;
		//parto dal fondo perchè ogni nodo ha sicuro un solo predecessore !
		//finchè non arrivo al nodo origine che avevo inserito nella mappa con value null !!
		while(c!=null){
			percorso.add(c);
			c=alberoVisita.get(c);
			
		}
		return percorso;
	}

}

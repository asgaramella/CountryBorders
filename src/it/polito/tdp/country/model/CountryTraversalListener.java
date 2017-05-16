package it.polito.tdp.country.model;

import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.event.ConnectedComponentTraversalEvent;
import org.jgrapht.event.EdgeTraversalEvent;
import org.jgrapht.event.TraversalListener;
import org.jgrapht.event.VertexTraversalEvent;
import org.jgrapht.graph.DefaultEdge;

public class CountryTraversalListener implements TraversalListener<Country, DefaultEdge> {
	
	private Graph<Country,DefaultEdge> graph;
	private Map<Country,Country> map;

	public CountryTraversalListener(Map<Country, Country> map, UndirectedGraph<Country, DefaultEdge> g) {
		this.graph=g;
		this.map=map;
	}

	@Override
	public void connectedComponentFinished(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void connectedComponentStarted(ConnectedComponentTraversalEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void edgeTraversed(EdgeTraversalEvent<DefaultEdge> evento) {
		//grafo non orientato devo contemplare le due possibilità: io voglio sempre arco da lontano verso il vicino
		// uno dei due vertici ce l'ho già nella mappa come K 
		Country c1=graph.getEdgeSource(evento.getEdge());
		Country c2=graph.getEdgeTarget(evento.getEdge());
		
		//controllo necessario perchè algoritmo di visita può attraversare un arco più volte,
		// coppia di vertici già visti e inseriti nella mappa
		if(map.containsKey(c1)&& map.containsKey(c2))
			return;
		
		if(!map.containsKey(c1)){
			//ci è il nuovo
			map.put(c1,c2);
		}
		else{
			//c2 è il nuovo
			map.put(c2,c1);
			}
		
		
	}

	@Override
	public void vertexFinished(VertexTraversalEvent<Country> arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void vertexTraversed(VertexTraversalEvent<Country> arg0) {
		// TODO Auto-generated method stub
		
	}

}

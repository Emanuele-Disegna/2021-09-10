package it.polito.tdp.yelp.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	private Map<String, Business> idMap;
	private YelpDao dao;
	private List<Adiacenza> adiacenze;
	private Graph<Business, DefaultWeightedEdge> grafo;
	
	public Model() {
		dao = new YelpDao();
	}
	
	public void creaGrafo(String citta) {
		grafo = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		dao.getVertici(citta, idMap);
		
		Graphs.addAllVertices(grafo, idMap.values());
		
		adiacenze = dao.getArchi(citta, idMap);
		for(Adiacenza a : adiacenze) {
			Graphs.addEdgeWithVertices(grafo, a.getB1(), a.getB2(), a.getDistanza());
		}
		
		System.out.println(grafo.vertexSet().size());
		System.out.println(grafo.edgeSet().size());
	}
	
	public Business getPiuDistante(Business b1) {
		Business b2 = null;
		double distanza = 0;
		
		for(Business b : Graphs.neighborListOf(grafo, b1)) {
			if(grafo.containsEdge(b1, b)) {
				double peso = grafo.getEdgeWeight(grafo.getEdge(b1, b));
				if(distanza<peso) {
					distanza = peso;
					b2 = b;
				}
			}
			
		}
		
		System.out.println(b2);
		
		return b2;
	}
	
	public Set<Business> getVertici() {
		return grafo.vertexSet();
	}
	
	public List<String> getCitta(){
		return dao.getCitta();
	}
}

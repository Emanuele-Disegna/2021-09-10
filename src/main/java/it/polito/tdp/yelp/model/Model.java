package it.polito.tdp.yelp.model;

import java.util.ArrayList;
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
	private List<Business> solOttima;
	
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

	public List<Business> cerca(Business b1, Business b2, double soglia) {
		List<Business> parziale = new ArrayList<>();
		solOttima = new ArrayList<>();
		parziale.add(b1);
		
		cercaRicorsiva(parziale, b2, soglia, controllo(Graphs.neighborListOf(grafo, b1), soglia, b2, parziale));
		
		return solOttima;
	}

	private void cercaRicorsiva(List<Business> parziale, Business b2, double soglia, List<Business> possibili) {
		if(parziale.size()>solOttima.size() && parziale.contains(b2)) {
			solOttima = new ArrayList<>(parziale);
			return;
		}
		
		for(Business b : possibili) {
			parziale.add(b);
			cercaRicorsiva(parziale, b2, soglia, controllo(Graphs.neighborListOf(grafo, b), soglia, b2, parziale));
			parziale.remove(b);
		}
	}

	private List<Business> controllo(List<Business> vicini, double soglia, Business b2, List<Business> parziale) {
		List<Business> ret = new ArrayList<>();
		
		for(Business b : vicini) {
			if((b.equals(b2) || b.getStars()>=soglia) && !parziale.contains(b)) {
				ret.add(b);
			}
		}

		return ret;
	}

	public double getKm() {
		double somma = 0;
		
		for(int i=0; i<solOttima.size()-1; i++) {
			if(grafo.containsEdge(grafo.getEdge(solOttima.get(i), solOttima.get(i+1)))) {
				somma += grafo.getEdgeWeight(grafo.getEdge(solOttima.get(i), solOttima.get(i+1)));
			}
		}
		
		return somma;
	}

}

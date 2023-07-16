package it.polito.tdp.itunes.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

import it.polito.tdp.itunes.db.ItunesDAO;

public class Model {
	private List<Genre>allGenres;
	private Graph<Track, DefaultEdge>grafo;
	private List<Track>allTracks;
	private ItunesDAO dao;
	
	private List<Track> migliore;
	private Integer numMassimo;
	
	public Model() {
		this.dao = new ItunesDAO();
		this.allGenres = new ArrayList<>(dao.getAllGenres());
		this.allTracks = new ArrayList<>();		
		
	}

	public String creaGrafo(Integer minSEC, Integer maxSEC, String genre) {
		Integer minMS = minSEC*1000;
		Integer maxMS = maxSEC*1000;
		this.grafo = new SimpleGraph<Track, DefaultEdge>(DefaultEdge.class);
		
		this.allTracks = dao.getAllTracks(minMS, maxMS, genre);
		Graphs.addAllVertices(grafo, this.allTracks);
		
		/*for(Retailers r : grafo.vertexSet()) {
			this.idMapRetailers.put(r.getCode(), r);
		}*/
		
		for(Track x : this.allTracks) {
			for(Track y : this.allTracks) {
				if(!x.equals(y)) {
					int nX = dao.getnumPlaylist(x);
					int nY = dao.getnumPlaylist(y);
					if(nX == nY) {
						grafo.addEdge(x, y);
					}
				}
			}	
		}
		return "Grafo creato con "+grafo.vertexSet().size()+" vertici e "+grafo.edgeSet().size()+" archi.";
	}
	
	/*Al termine della costruzione del grafo, determinarne le componenti connesse e, per ciascuna di esse,
		stampare il numero di vertici presenti, ed il numero di playlist distinte che corrispondono agli archi di tale
		componente.
	*/
	public String getConnectedComponents() {
		// Trova componenti connesse  (Connectivity Inspector)
		String s = "\nComponenti connesse :\n";
		ConnectivityInspector<Track, DefaultEdge> inspector = new ConnectivityInspector<Track, DefaultEdge>(this.grafo);
		List<Set<Track>> connessi = inspector.connectedSets();
		for(Set<Track> x : connessi) {
			s += "Componente connessa contenente "+x.size()+" vertici e "+this.calcolaNPlaylistDistinte(x)+" playlist\n";
		}
		return s;
	}
	
	public int calcolaNPlaylistDistinte(Set<Track>t) {
		Integer n = 0;
		List<Playlist>playlistComparse = new ArrayList<>();
		for(Track x : t) {
			List<Playlist>y = new ArrayList<>(dao.getPlaylistInCuiCompareX(x));
			for(Playlist z : y) {
				if(!playlistComparse.contains(z)) {
					playlistComparse.add(z);
				}
			}
		}
		n = playlistComparse.size();
		return n;
	}
	
	//metodo base per ricorsione
	
		public List<Track>calcolaPercorso(Integer dMaxMIN) {
			this.numMassimo = 0;
			this.migliore = new ArrayList<Track>();
			List<Track> rimanenti = new ArrayList<>(this.getComponenteConnessaPiuNumerosa());
			List<Track> parziale = new ArrayList<>();
			
			
			ricorsione(parziale, rimanenti, 0, 0, dMaxMIN*60000);
			return migliore;
		}

		private void ricorsione(List<Track> parziale, List<Track> rimanenti, Integer livello, Integer durataParz, Integer dMaxMS){
			
			// Condizione Terminale
			if (rimanenti.isEmpty()) {
				//calcolo dimensione
				Integer dimensione = parziale.size();
				if (dimensione > this.numMassimo && durataParz <= dMaxMS) {
					this.numMassimo = dimensione;
					this.migliore = new ArrayList<>(parziale);
				}
				return;
			}
			
			
	       	for (Track p : rimanenti) {
	       		if(p.getMilliseconds()+durataParz < dMaxMS) {
	 			List<Track> currentRimanenti = new ArrayList<>(rimanenti);
	 				parziale.add(p);
	 				currentRimanenti.remove(p);
	 				ricorsione(parziale, currentRimanenti, livello+1, p.getMilliseconds()+durataParz, dMaxMS);
	 				parziale.remove(parziale.size()-1);
	 			}
	 		}
			
		}
		
		
		private Set<Track> getComponenteConnessaPiuNumerosa() {
			Integer dimMax = 0;
			Set<Track>res = new HashSet<>();
			ConnectivityInspector<Track, DefaultEdge> inspector = new ConnectivityInspector<Track, DefaultEdge>(this.grafo);
			if(inspector!= null) {
			
			List<Set<Track>> connessi = inspector.connectedSets();
			for(Set<Track> x : connessi) {
				if(x.size()> dimMax) {
					dimMax = x.size();
					res = new HashSet<>(x);
				}
			}	
		}
			return res;
		}

	public List<Genre> getAllGenres() {
		return allGenres;
	}

	public Graph<Track, DefaultEdge> getGrafo() {
		return grafo;
	}

	public List<Track> getAllTracks() {
		return allTracks;
	}

	public ItunesDAO getDao() {
		return dao;
	}

	public List<Track> getMigliore() {
		return migliore;
	}

	public Integer getNumMassimo() {
		return numMassimo;
	}


	
	
}

package bowling;

import java.util.HashMap;
import java.util.Map;

public class PartieMultiJoueurs implements IPartieMultiJoueurs {
	
	private String[] nomsDesJoueurs;
	private Map<String, PartieMonoJoueur> partiesParJoueur;
	private int indexJoueurCourant;
	private boolean partieEnCours;

	public PartieMultiJoueurs() {
		this.partieEnCours = false;
	}
	
	@Override
	public String demarreNouvellePartie(String[] nomsDesJoueurs) throws IllegalArgumentException {
		if (nomsDesJoueurs == null || nomsDesJoueurs.length <1) {
			throw new IllegalArgumentException("Il faut au moins un joueur pour démarrer une partie");
		}
		
		this.nomsDesJoueurs = nomsDesJoueurs;
		this.partiesParJoueur = new HashMap<>();
		this.indexJoueurCourant = 0;
		this.partieEnCours = true;
		
		// Créer une partie mono-joueur pour chaque joueur
		for (String nom : nomsDesJoueurs) {
			partiesParJoueur.put(nom, new PartieMonoJoueur());
		}
		
		return genererMessageProchainTir();
	}
	
	@Override
	public boolean enregistreLancer(int nombreDeQuillesAbattues) throws IllegalStateException {
		if (!partieEnCours) {
			throw new IllegalStateException("La partie n'est pas démarrée");
		}
		
		String nomJoueurCourant = nomsDesJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieCourante = partiesParJoueur.get(nomJoueurCourant);
		
		// Enregistrer le lancer pour le joueur courant
		boolean continuerTour = partieCourante.enregistreLancer(nombreDeQuillesAbattues);
		
		// Si le tour n'est pas terminé (continuerTour == true), on reste sur le même joueur
		if (!continuerTour) {
			// Le tour est terminé, on passe au joueur suivant
			passerAuJoueurSuivant();
		}
		
		// Vérifier si toutes les parties sont terminées
		if (toutesLesPartiesSontTerminees()) {
			partieEnCours = false;
			return "Partie terminée" != null;
		}
		
		return genererMessageProchainTir() != null;
	}
	
	@Override
	public int scorePour(String nomDuJoueur) throws IllegalArgumentException {
		if (!partiesParJoueur.containsKey(nomDuJoueur)) {
			throw new IllegalArgumentException("Joueur inconnu");
		}
		
		return partiesParJoueur.get(nomDuJoueur).score();
	}
	
	/**
	 * Passe au joueur suivant dans l'ordre de rotation
	 */
	private void passerAuJoueurSuivant() {
		indexJoueurCourant = (indexJoueurCourant + 1) % nomsDesJoueurs.length;
	}
	
	/**
	 * Vérifie si toutes les parties sont terminées
	 */
	private boolean toutesLesPartiesSontTerminees() {
		for (PartieMonoJoueur partie : partiesParJoueur.values()) {
			if (!partie.estTerminee()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * Génère le message indiquant le prochain tir
	 */
	private String genererMessageProchainTir() {
		String nomJoueurCourant = nomsDesJoueurs[indexJoueurCourant];
		PartieMonoJoueur partieCourante = partiesParJoueur.get(nomJoueurCourant);
		
		int numeroTour = partieCourante.numeroTourCourant();
		int numeroBoule = partieCourante.numeroProchainLancer();
		
		return String.format("Prochain tir : joueur %s, tour n° %d, boule n° %d", 
		                     nomJoueurCourant, numeroTour, numeroBoule);
	}
}

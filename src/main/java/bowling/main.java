package bowling;

public class main {
    public static void main(String[] args) {
        IPartieMultiJoueurs partie = new PartieMultiJoueurs();

        String[] joueurs = { "Pierre", "Paul" };
        System.out.println(partie.demarreNouvellePartie(joueurs));

        System.out.println(partie.enregistreLancer(3));
        System.out.println(partie.enregistreLancer(5));
        System.out.println(partie.enregistreLancer(10));

        System.out.println("Score Pierre : " + partie.scorePour("Pierre"));
        System.out.println("Score Paul : " + partie.scorePour("Paul"));
    }
}

class Personne {
    private int cin;
    private String nom;
    private String prenom;

    public Personne(int cin, String nom, String prenom) {
        this.cin = cin;
        this.nom = nom;
        this.prenom = prenom;
    }

    public int getCin() {
        return cin;
    }

    public void setCin(int cin) {
        this.cin = cin;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String toString() {
        return "Personne{cin=" + cin + ", nom='" + nom + "', prenom='" + prenom + "'}";
    }
}

abstract class Propriete {
    protected int id;
    protected Personne responsable;
    protected String adresse;
    protected double surface;

    public Propriete(int id, Personne responsable, String adresse, double surface) {
        this.id = id;
        this.responsable = responsable;
        this.adresse = adresse;
        this.surface = surface;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Personne getResponsable() {
        return responsable;
    }

    public void setResponsable(Personne responsable) {
        this.responsable = responsable;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public abstract double calculImpot();

    public String toString() {
        return "Propriete{id=" + id + ", responsable=" + responsable + ", adresse='" + adresse + "', surface=" + surface + "}";
    }
}

interface GestionPropriete {
    int MAX_PROPRIETES = 10;

    void afficherProprietes();

    boolean ajouter(Propriete p);

    boolean supprimer(Propriete p);
}

class Lotissement implements GestionPropriete {
    protected Propriete[] tabProp;
    protected int nombre;

    public Lotissement(int capacite) {
        tabProp = new Propriete[capacite];
        nombre = 0;
    }

    public void afficherProprietes() {
        for (int i = 0; i < nombre; i++) {
            System.out.println(tabProp[i]);
            System.out.println("Impôt à payer: " + tabProp[i].calculImpot() + " DT");
        }
    }

    public boolean ajouter(Propriete p) {
        if (nombre < MAX_PROPRIETES) {
            tabProp[nombre++] = p;
            return true;
        }
        return false;
    }

    public boolean supprimer(Propriete p) {
        for (int i = 0; i < nombre; i++) {
            if (tabProp[i].getId() == p.getId()) {
                for (int j = i; j < nombre - 1; j++) {
                    tabProp[j] = tabProp[j + 1];
                }
                tabProp[nombre - 1] = null;
                nombre--;
                return true;
            }
        }
        return false;
    }

    public Propriete getProprieteByIndex(int i) {
        if (i >= 0 && i < nombre) {
            return tabProp[i];
        }
        return null;
    }

    public int getNbPieces() {
        int total = 0;
        for (int i = 0; i < nombre; i++) {
            if (tabProp[i] instanceof ProprietePrivée) {
                total += ((ProprietePrivée) tabProp[i]).getNbPieces();
            }
        }
        return total;
    }
}

class ProprietePrivée extends Propriete {
    private int nbPieces;

    public ProprietePrivée(int id, Personne responsable, String adresse, double surface, int nbPieces) {
        super(id, responsable, adresse, surface);
        this.nbPieces = nbPieces;
    }

    public int getNbPieces() {
        return nbPieces;
    }

    public double calculImpot() {
        return 50 * surface / 100 + 10 * nbPieces;
    }

    public String toString() {
        return super.toString() + ", nbPieces=" + nbPieces + '}';
    }
}


class Villa extends ProprietePrivée {
    private boolean avecPiscine;

    public Villa(int id, Personne responsable, String adresse, double surface, int nbPieces, boolean avecPiscine) {
        super(id, responsable, adresse, surface, nbPieces);
        this.avecPiscine = avecPiscine;
    }

    public boolean isAvecPiscine() {
        return avecPiscine;
    }

    public double calculImpot() {
        double impots = super.calculImpot();
        if (avecPiscine) {
            impots += 200;
        }
        return impots;
    }

    public String toString() {
        return super.toString() + ", avecPiscine=" + avecPiscine + '}';
    }
}

class Appartement extends ProprietePrivée {
    private int numEtage;

    public Appartement(int id, Personne responsable, String adresse, double surface, int nbPieces, int numEtage) {
        super(id, responsable, adresse, surface, nbPieces);
        this.numEtage = numEtage;
    }

    public int getNumEtage() {
        return numEtage;
    }

    public String toString() {
        return super.toString() + ", numEtage=" + numEtage + '}';
    }
}

class ProprieteProfessionnelle extends Propriete {
    private int nbEmployes;
    private boolean estEtatique;

    public ProprieteProfessionnelle(int id, Personne responsable, String adresse, double surface, int nbEmployes, boolean estEtatique) {
        super(id, responsable, adresse, surface);
        this.nbEmployes = nbEmployes;
        this.estEtatique = estEtatique;
    }

    public double calculImpot() {
        if (estEtatique) {
            return 0;
        } else {
            return 100 * surface / 100 + 30 * nbEmployes;
        }
    }

    public String toString() {
        return super.toString() + ", nbEmployes=" + nbEmployes + ", estEtatique=" + estEtatique + '}';
    }
}

public class Fiscalite {
    public static void main(String[] args) {
        Personne p1 = new Personne(12345, "Founes", "Aya");
        Personne p2 = new Personne(67890, "Merzoug", "Salma");
        Personne p3 = new Personne(11223, "Kachabia", "Hazem");

        Lotissement lotissement = new Lotissement(10);

        lotissement.ajouter(new ProprietePrivée(1, p1, "Corniche", 350, 4));
        lotissement.ajouter(new Villa(2, p2, "Dar Chaabane", 400, 6, true));
        lotissement.ajouter(new Appartement(3, p2, "Hammamet", 1200, 8, 3));
        lotissement.ajouter(new ProprieteProfessionnelle(4, p3, "Korba", 1000, 50, true));
        lotissement.ajouter(new ProprieteProfessionnelle(5, p1, "Bir Bouragba", 2500, 400, false));

        lotissement.afficherProprietes();

        System.out.println("Nombre global de pièces: " + lotissement.getNbPieces());

        ProprietePrivée proprieteMoinsImpots = null;
        double minImpot = Double.MAX_VALUE;
        for (int i = 0; i < 10; i++) {
            if (lotissement.tabProp[i] instanceof ProprietePrivée) {
                ProprietePrivée pPrivée = (ProprietePrivée) lotissement.tabProp[i];
                double impot = pPrivée.calculImpot();
                if (impot < minImpot) {
                    minImpot = impot;
                    proprieteMoinsImpots = pPrivée;
                }
            }
        }

        if (proprieteMoinsImpots != null) {
            System.out.println("Propriété privée qui paye le moins d'impôt : " + proprieteMoinsImpots);
        }
    }
}

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class InterfaceUtilisateur extends JFrame implements ActionListener {
    private JButton btnAjouterUtilisateur;
    private JButton btnSupprimerUtilisateur;
    private JButton btnRechercherUtilisateur;
    private JButton btnRetourMenuPrincipal;
    private JButton btnListerUtilisateurs;

    private Utilisateur utilisateur;

    public InterfaceUtilisateur() {
        setTitle("Menu de gestion des utilisateurs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 200);
        setLocationRelativeTo(null);

        btnAjouterUtilisateur = new JButton("Ajouter un utilisateur");
        btnSupprimerUtilisateur = new JButton("Supprimer un utilisateur");
        btnRechercherUtilisateur = new JButton("Rechercher un utilisateur");
        btnRetourMenuPrincipal = new JButton("Retour au menu principal");
        btnListerUtilisateurs = new JButton("Lister les utilisateurs");

        btnAjouterUtilisateur.addActionListener(this);
        btnSupprimerUtilisateur.addActionListener(this);
        btnRechercherUtilisateur.addActionListener(this);
        btnListerUtilisateurs.addActionListener(this);
        btnRetourMenuPrincipal.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(btnAjouterUtilisateur);
        panel.add(btnSupprimerUtilisateur);
        panel.add(btnRechercherUtilisateur);
        panel.add(btnListerUtilisateurs);
        panel.add(btnRetourMenuPrincipal);

        add(panel);

        setVisible(true);

        utilisateur = new Utilisateur();
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAjouterUtilisateur) {
            String nom = JOptionPane.showInputDialog("Donner le nom : ");
            String prenom = JOptionPane.showInputDialog("Donner le prénom : ");
            String dateNaissance = "";
            boolean dateValide = false;
            while (!dateValide) {
                dateNaissance = JOptionPane.showInputDialog("Donner la date de naissance (format JJ/MM/AAAA) : ");
                if (dateNaissance.matches("\\d{2}/\\d{2}/\\d{4}")) {
                    dateValide = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Le format de la date de naissance doit être JJ/MM/AAAA.");
                }
            }
            String tel = "";
            boolean telephoneValide = false;
            while (!telephoneValide) {
                tel = JOptionPane.showInputDialog("Donner votre numéro de téléphone (format 772145963) : ");
                if (tel.matches("7[0-9]{8}")) {
                    telephoneValide = true;
                } else {
                    JOptionPane.showMessageDialog(null, "Le numéro de téléphone doit commencer par 7 et être suivi de 8 chiffres.");
                }
            }
            int nbEmp = Integer.parseInt(JOptionPane.showInputDialog("Donner le nombre d'ouvrages empruntés : "));

            Utilisateur nouvelUtilisateur = new Utilisateur(nom, prenom, dateNaissance, tel, nbEmp);
            Utilisateur.ajouterUtilisateur(nouvelUtilisateur);
        } else if (e.getSource() == btnSupprimerUtilisateur) {
            String numeroIdentification = JOptionPane.showInputDialog("Entrez le numéro d'identification de l'utilisateur à supprimer : ");
            if (numeroIdentification != null && !numeroIdentification.isEmpty()) {
                Utilisateur.supprimerUtilisateur(numeroIdentification);
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro d'identification valide.");
            }
        } else if (e.getSource() == btnRechercherUtilisateur) {
            String numeroIdentification = JOptionPane.showInputDialog("Entrez le numéro d'identification de l'utilisateur à rechercher : ");
            if (numeroIdentification != null && !numeroIdentification.isEmpty()) {
                rechercherUtilisateur(numeroIdentification);
            } else {
                JOptionPane.showMessageDialog(null, "Veuillez entrer un numéro d'identification valide.");
            }
        } else if (e.getSource() == btnListerUtilisateurs) {
            listerUtilisateurs();
        } else if (e.getSource() == btnRetourMenuPrincipal) {
            dispose(); // Ferme l'interface actuelle
            new Bibliotheque(); // Ouvre l'interface de la bibliothèque
        }
    }

    private void rechercherUtilisateur(String numeroIdentification) {
        Utilisateur utilisateurTrouve = null;
        for (Utilisateur user : Utilisateur.getUtilisateurs()) {
            if (user.getNumeroIdentification().equals(numeroIdentification)) {
                utilisateurTrouve = user;
                break;
            }
        }
        if (utilisateurTrouve != null) {
            JOptionPane.showMessageDialog(null, utilisateurTrouve.toString(), "Informations de l'utilisateur", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Utilisateur introuvable !");
        }
    }

    private void listerUtilisateurs() {
        List<Utilisateur> utilisateurs = Utilisateur.getUtilisateurs();
        String[][] data = new String[utilisateurs.size()][6];

        for (int i = 0; i < utilisateurs.size(); i++) {
            Utilisateur utilisateur = utilisateurs.get(i);
            data[i][0] = utilisateur.getNom();
            data[i][1] = utilisateur.getPrenom();
            data[i][2] = utilisateur.getDateNaissance();
            data[i][3] = utilisateur.getTel();
            data[i][4] = utilisateur.getNumeroIdentification();
            data[i][5] = String.valueOf(utilisateur.getNbEmp());
        }

        String[] columnNames = {"Nom", "Prénom", "Date de naissance", "Téléphone", "Numéro d'identification", "Nombre d'ouvrages empruntés"};

        JTable table = new JTable(data, columnNames);
        JScrollPane scrollPane = new JScrollPane(table);
        JPanel panel = new JPanel();
        panel.add(scrollPane);
        JFrame frame = new JFrame("Liste des utilisateurs");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(panel);
        frame.setSize(800, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    // Méthodes de gestion des utilisateurs

    public static void main(String[] args) {
        new InterfaceUtilisateur();
    }
}











import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class InterfaceGestionLivres extends JFrame implements ActionListener {
    private JButton btnAjouterLivre;
    private JButton btnModifierLivre;
    private JButton btnSupprimerLivre;
    private JButton btnRechercherLivre;
    private JButton btnListerLivres;
    private JButton btnRetour;

    private DefaultTableModel tableModel;
    private JTable table;

    public InterfaceGestionLivres() {
        setTitle("Gestion des livres");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        btnAjouterLivre = new JButton("Ajouter un livre");
        btnModifierLivre = new JButton("Modifier un livre");
        btnSupprimerLivre = new JButton("Supprimer un livre");
        btnRechercherLivre = new JButton("Rechercher un livre");
        btnListerLivres = new JButton("Lister les livres");
        btnRetour = new JButton("Retour");

        btnAjouterLivre.addActionListener(this);
        btnModifierLivre.addActionListener(this);
        btnSupprimerLivre.addActionListener(this);
        btnRechercherLivre.addActionListener(this);
        btnListerLivres.addActionListener(this);
        btnRetour.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(6, 1));
        panel.add(btnAjouterLivre);
        panel.add(btnModifierLivre);
        panel.add(btnSupprimerLivre);
        panel.add(btnRechercherLivre);
        panel.add(btnListerLivres);
        panel.add(btnRetour);

        add(panel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnAjouterLivre) {
            ajouterLivre();
        } else if (e.getSource() == btnModifierLivre) {
            // Modifier un livre
        } else if (e.getSource() == btnSupprimerLivre) {
            supprimerLivre();
        } else if (e.getSource() == btnRechercherLivre) {
            rechercherLivre();
        } else if (e.getSource() == btnListerLivres) {
            listerLivres();
        } else if (e.getSource() == btnRetour) {
            new Bibliotheque();
            dispose(); // Fermer la fenêtre de gestion des livres
        }
    }

    private void ajouterLivre() {
        String titre = JOptionPane.showInputDialog("Donner le titre du livre : ");
        String auteur = JOptionPane.showInputDialog("Donner l'auteur du livre : ");
        int anneePublication = Integer.parseInt(JOptionPane.showInputDialog("Donner l'année de publication du livre : "));
        int nbExemplaires = Integer.parseInt(JOptionPane.showInputDialog("Donner le nombre d'exemplaires du livre à ajouter : "));

        // Génération automatique du numéro ISBN
        String numeroISBN = generateISBN();

        Livres nouveauLivre = new Livres(titre, auteur, anneePublication, nbExemplaires);
        ajouterLivre(nouveauLivre);
    }

    private void ajouterLivre(Livres livre) {
        List<Livres> listeLivres = chargerLivres();
        listeLivres.add(livre);
        sauvegarderLivres(listeLivres);
        JOptionPane.showMessageDialog(null, "Livre ajouté avec succès !");

        // Mettre à jour le modèle de tableau avec les données mises à jour
        Object[] rowData = {livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getNbExemplaires(), livre.getNumeroISBN()};
        tableModel.addRow(rowData);

        // Rafraîchir le tableau
        table.setModel(tableModel);
    }

    private void listerLivres() {
        List<Livres> listeLivres = chargerLivres();

        String[] columnNames = {"Titre", "Auteur", "Année de publication", "Nombre d'exemplaires", "ISBN"};
        Object[][] data = new Object[listeLivres.size()][5];

        for (int i = 0; i < listeLivres.size(); i++) {
            Livres livre = listeLivres.get(i);
            data[i][0] = livre.getTitre();
            data[i][1] = livre.getAuteur();
            data[i][2] = livre.getAnneePublication();
            data[i][3] = livre.getNbExemplaires();
            data[i][4] = livre.getNumeroISBN();
        }

        tableModel = new DefaultTableModel(data, columnNames);
        table = new JTable(tableModel);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setPreferredSize(new Dimension(580, 350));

        JOptionPane.showMessageDialog(null, scrollPane, "Liste des Livres", JOptionPane.PLAIN_MESSAGE);
    }

    private List<Livres> chargerLivres() {
        List<Livres> livres = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("livres.ser"))) {
            livres = (List<Livres>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            // En cas d'erreur de lecture du fichier ou s'il n'existe pas, on retourne une liste vide
        }
        return livres;
    }

    private void sauvegarderLivres(List<Livres> livres) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("livres.ser"))) {
            oos.writeObject(livres);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generateISBN() {
        Random random = new Random();
        StringBuilder isbn = new StringBuilder();
        for (int i = 0; i < 13; i++) {
            isbn.append(random.nextInt(10));
        }
        return isbn.toString();
    }

    private void rechercherLivre() {
        String choixRecherche = JOptionPane.showInputDialog("Voulez-vous rechercher par auteur (A) ou par ISBN (I) ?");
        if (choixRecherche != null && !choixRecherche.isEmpty()) {
            if (choixRecherche.equalsIgnoreCase("A")) {
                String auteurRecherche = JOptionPane.showInputDialog("Entrez le nom de l'auteur :");
                if (auteurRecherche != null && !auteurRecherche.isEmpty()) {
                    rechercherParAuteur(auteurRecherche);
                }
            } else if (choixRecherche.equalsIgnoreCase("I")) {
                String isbnRecherche = JOptionPane.showInputDialog("Entrez le numéro ISBN :");
                if (isbnRecherche != null && !isbnRecherche.isEmpty()) {
                    rechercherParISBN(isbnRecherche);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Choix invalide !");
            }
        }
    }

    private void rechercherParAuteur(String auteurRecherche) {
        List<Livres> listeLivres = chargerLivres();
        DefaultTableModel resultModel = new DefaultTableModel(new String[]{"Titre", "Auteur", "Année de publication", "Nombre d'exemplaires", "ISBN"}, 0);

        for (Livres livre : listeLivres) {
            if (livre.getAuteur().equalsIgnoreCase(auteurRecherche)) {
                resultModel.addRow(new Object[]{livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getNbExemplaires(), livre.getNumeroISBN()});
            }
        }

        if (resultModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Aucun livre trouvé pour cet auteur.");
        } else {
            JTable resultTable = new JTable(resultModel);
            JScrollPane scrollPane = new JScrollPane(resultTable);
            JOptionPane.showMessageDialog(null, scrollPane, "Résultat de la recherche", JOptionPane.PLAIN_MESSAGE);
        }
    }

    private void rechercherParISBN(String isbnRecherche) {
        List<Livres> listeLivres = chargerLivres();
        DefaultTableModel resultModel = new DefaultTableModel(new String[]{"Titre", "Auteur", "Année de publication", "Nombre d'exemplaires", "ISBN"}, 0);

        for (Livres livre : listeLivres) {
            if (livre.getNumeroISBN().equalsIgnoreCase(isbnRecherche)) {
                resultModel.addRow(new Object[]{livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getNbExemplaires(), livre.getNumeroISBN()});
                break; // On a trouvé le livre, pas besoin de continuer la recherche
            }
        }

        if (resultModel.getRowCount() == 0) {
            JOptionPane.showMessageDialog(null, "Aucun livre trouvé avec cet ISBN.");
        } else {
            JTable resultTable = new JTable(resultModel);
            JScrollPane scrollPane = new JScrollPane(resultTable);
            JOptionPane.showMessageDialog(null, scrollPane, "Résultat de la recherche", JOptionPane.PLAIN_MESSAGE);
        }
    }
    
    private void supprimerLivre() {
        String choixRecherche = JOptionPane.showInputDialog("Voulez-vous supprimer un livre par auteur (A) ou par ISBN (I) ?");
        if (choixRecherche != null && !choixRecherche.isEmpty()) {
            if (choixRecherche.equalsIgnoreCase("A")) {
                String auteurRecherche = JOptionPane.showInputDialog("Entrez le nom de l'auteur :");
                if (auteurRecherche != null && !auteurRecherche.isEmpty()) {
                    supprimerParAuteur(auteurRecherche);
                }
            } else if (choixRecherche.equalsIgnoreCase("I")) {
                String isbnRecherche = JOptionPane.showInputDialog("Entrez le numéro ISBN :");
                if (isbnRecherche != null && !isbnRecherche.isEmpty()) {
                    supprimerParISBN(isbnRecherche);
                }
            } else {
                JOptionPane.showMessageDialog(null, "Choix invalide !");
            }
        }
    }
    
    private void supprimerParAuteur(String auteurRecherche) {
        List<Livres> listeLivres = chargerLivres();
        List<Livres> livresASupprimer = new ArrayList<>();

        for (Livres livre : listeLivres) {
            if (livre.getAuteur().equalsIgnoreCase(auteurRecherche)) {
                livresASupprimer.add(livre);
            }
        }

        if (!livresASupprimer.isEmpty()) {
            listeLivres.removeAll(livresASupprimer);
            sauvegarderLivres(listeLivres);
            JOptionPane.showMessageDialog(null, "Livre(s) supprimé(s) avec succès !");
            rafraichirTableau();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun livre trouvé pour cet auteur.");
        }
    }
    
    private void supprimerParISBN(String isbnRecherche) {
        List<Livres> listeLivres = chargerLivres();
        boolean livreTrouve = false;

        for (Livres livre : listeLivres) {
            if (livre.getNumeroISBN().equalsIgnoreCase(isbnRecherche)) {
                listeLivres.remove(livre);
                livreTrouve = true;
                break; // On a trouvé le livre, pas besoin de continuer la recherche
            }
        }

        if (livreTrouve) {
            sauvegarderLivres(listeLivres);
            JOptionPane.showMessageDialog(null, "Livre supprimé avec succès !");
            rafraichirTableau();
        } else {
            JOptionPane.showMessageDialog(null, "Aucun livre trouvé avec cet ISBN.");
        }
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0); // Effacer toutes les lignes du tableau
        List<Livres> listeLivres = chargerLivres();

        for (Livres livre : listeLivres) {
            Object[] rowData = {livre.getTitre(), livre.getAuteur(), livre.getAnneePublication(), livre.getNbExemplaires(), livre.getNumeroISBN()};
            tableModel.addRow(rowData);
        }
    }

    public static void main(String[] args) {
        new InterfaceGestionLivres();
    }
}













import javax.swing.JOptionPane;
import java.io.Serializable;

public class Livres implements Ouvrages, Serializable {
    private String titre;
    private String auteur;
    private int anneePublication;
    private int nbExemplaires;
    private String numeroISBN;

    private static int compteurISBN = 1;

    public Livres() {}

    public Livres(String titre, String auteur, int anneePublication, int nbExemplaires) {
        this.titre = titre;
        this.auteur = auteur;
        this.anneePublication = anneePublication;
        this.nbExemplaires = nbExemplaires;
        this.numeroISBN = generateISBN();
    }

    // Getters and setters...

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getAuteur() {
        return auteur;
    }

    public void setAuteur(String auteur) {
        this.auteur = auteur;
    }

    public int getAnneePublication() {
        return anneePublication;
    }

    public void setAnneePublication(int anneePublication) {
        this.anneePublication = anneePublication;
    }

    public int getNbExemplaires() {
        return nbExemplaires;
    }

    public void setNbExemplaires(int nbExemplaires) {
        this.nbExemplaires = nbExemplaires;
    }

    public String getNumeroISBN() {
        return numeroISBN;
    }

    public void setNumeroISBN(String numeroISBN) {
        this.numeroISBN = numeroISBN;
    }

    private String generateISBN() {
        return "ISBN-" + String.format("%03d", compteurISBN++);
    }

    @Override
    public void ajouter() {
        // L'ajout est désormais géré dans l'interface graphique
    }

    @Override
    public void supprimer() {
        // La suppression est désormais gérée dans l'interface graphique
    }

    @Override
    public void modifier() {
        // La modification est désormais gérée dans l'interface graphique
    }

    public void afficheLivre() {
        JOptionPane.showMessageDialog(null,
            "Titre : " + titre + "\n" +
            "Auteur : " + auteur + "\n" +
            "Année de publication : " + anneePublication + "\n" +
            "Nombre d'exemplaires : " + nbExemplaires + "\n" +
            "Numéro ISBN : " + numeroISBN);
    }

    public String[][] getLivreData() {
        String[][] data = new String[1][5]; // Un seul livre, donc un seul tableau de données
        data[0][0] = getTitre();
        data[0][1] = getAuteur();
        data[0][2] = String.valueOf(getAnneePublication());
        data[0][3] = String.valueOf(getNbExemplaires());
        data[0][4] = getNumeroISBN();
        return data;
    }

    @Override
    public String toString() {
        return "Titre: " + titre + "\n" +
                "Auteur: " + auteur + "\n" +
                "Année de publication: " + anneePublication + "\n" +
                "Nombre d'exemplaires: " + nbExemplaires + "\n" +
                "Numéro ISBN: " + numeroISBN;
    }

    // Méthode pour rechercher un livre par auteur
    public boolean rechercherParAuteur(String auteurRecherche) {
        return this.auteur.equalsIgnoreCase(auteurRecherche);
    }

    // Méthode pour rechercher un livre par numéro ISBN
    public boolean rechercherParISBN(String isbnRecherche) {
        return this.numeroISBN.equals(isbnRecherche);
    }
}






import javax.swing.JOptionPane;
import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Utilisateur implements Serializable {
    private String nom;
    private String prenom;
    private String dateNaissance;
    private String tel;
    private String numeroIdentification;
    private int nbEmp;

    private static List<Utilisateur> utilisateurs;

    static {
        chargerUtilisateurs();
    }

    public Utilisateur() {}

    public Utilisateur(String nom, String prenom, String dateNaissance, String tel, int nbEmp) {
        this.nom = nom;
        this.prenom = prenom;
        this.dateNaissance = dateNaissance;
        this.tel = tel;
        this.nbEmp = nbEmp;
        this.numeroIdentification = genererNumeroIdentification();
    }

    public String getNom() {
        return nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public String getDateNaissance() {
        return dateNaissance;
    }

    public String getTel() {
        return tel;
    }

    public String getNumeroIdentification() {
        return numeroIdentification;
    }

    public int getNbEmp() {
        return nbEmp;
    }

    public static List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    public static void ajouterUtilisateur(Utilisateur utilisateur) {
        utilisateurs.add(utilisateur);
        sauvegarderUtilisateurs();
    }

    public static void supprimerUtilisateur(String numeroIdentification) {
        Utilisateur utilisateurTrouve = null;
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getNumeroIdentification().equals(numeroIdentification)) {
                utilisateurTrouve = utilisateur;
                break;
            }
        }
        if (utilisateurTrouve != null) {
            utilisateurs.remove(utilisateurTrouve);
            sauvegarderUtilisateurs();
            JOptionPane.showMessageDialog(null, "Utilisateur supprimé avec succès !");
        } else {
            JOptionPane.showMessageDialog(null, "Utilisateur introuvable !");
        }
    }

    private static void chargerUtilisateurs() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream("utilisateurs.ser"))) {
            utilisateurs = (List<Utilisateur>) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            utilisateurs = new ArrayList<>();
        }
    }

    private static void sauvegarderUtilisateurs() {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream("utilisateurs.ser"))) {
            oos.writeObject(utilisateurs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return "Nom : " + nom + "\n" +
                "Prenom : " + prenom + "\n" +
                "Date de naissance : " + dateNaissance + "\n" +
                "Numero de telephone : " + tel + "\n" +
                "Numero d'identification : " + numeroIdentification + "\n" +
                "Nombre d'ouvrages empruntes : " + nbEmp;
    }

    private String genererNumeroIdentification() {
        return String.valueOf(utilisateurs.size() + 1);
    }
}












import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Bibliotheque extends JFrame implements ActionListener {
    private JButton btnGestionLivres;
    private JButton btnGestionUtilisateurs;
    private JButton btnGestionEmprunts; // Nouveau bouton pour la gestion des emprunts et retours
    private JButton btnQuitter;

    public Bibliotheque() {
        setTitle("Bibliothèque");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(300, 200);
        setLocationRelativeTo(null);

        btnGestionLivres = new JButton("Gestion des Livres");
        btnGestionUtilisateurs = new JButton("Gestion des Utilisateurs");
        btnGestionEmprunts = new JButton("Gestion des Emprunts"); // Initialisation du nouveau bouton
        btnQuitter = new JButton("Quitter");

        btnGestionLivres.addActionListener(this);
        btnGestionUtilisateurs.addActionListener(this);
        btnGestionEmprunts.addActionListener(this); // Ajout de l'ActionListener pour le nouveau bouton
        btnQuitter.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(4, 1)); // Modification de la disposition pour inclure le nouveau bouton
        panel.add(btnGestionLivres);
        panel.add(btnGestionUtilisateurs);
        panel.add(btnGestionEmprunts); // Ajout du nouveau bouton au panel
        panel.add(btnQuitter);

        add(panel);

        setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnGestionLivres) {
            new InterfaceGestionLivres();
            dispose();
        } else if (e.getSource() == btnGestionUtilisateurs) {
            new InterfaceUtilisateur();
            dispose();
        } else if (e.getSource() == btnGestionEmprunts) { // Condition pour le nouveau bouton
            new InterfaceGestionEmprunts(); // Crée une nouvelle instance de l'interface de gestion des emprunts
            dispose(); // Ferme la fenêtre actuelle
        } else if (e.getSource() == btnQuitter) {
            System.exit(0);
        }
    }

    public static void main(String[] args) {
        new Bibliotheque();
    }
}








import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.time.LocalDate;

public class InterfaceGestionEmprunts extends JFrame implements ActionListener {
    private JButton btnEmprunter;
    private JButton btnRetourner;
    private JButton btnRechercherEmprunt;
    private JButton btnListerEmprunts;
    private JButton btnRetour;

    private List<Emprunt> listeEmprunts; // Liste pour stocker les emprunts

    public InterfaceGestionEmprunts() {
        setTitle("Gestion des Emprunts et Retours");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        btnEmprunter = new JButton("Emprunter un livre");
        btnRetourner = new JButton("Retourner un livre");
        btnRechercherEmprunt = new JButton("Rechercher un emprunt");
        btnListerEmprunts = new JButton("Lister les emprunts");
        btnRetour = new JButton("Retour");

        btnEmprunter.addActionListener(this);
        btnRetourner.addActionListener(this);
        btnRechercherEmprunt.addActionListener(this);
        btnListerEmprunts.addActionListener(this);
        btnRetour.addActionListener(this);

        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(5, 1));
        panel.add(btnEmprunter);
        panel.add(btnRetourner);
        panel.add(btnRechercherEmprunt);
        panel.add(btnListerEmprunts);
        panel.add(btnRetour);

        add(panel);

        setVisible(true);

        listeEmprunts = new ArrayList<>(); // Initialisation de la liste des emprunts
    }

    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == btnEmprunter) {
            gererEmprunt();
        } else if (e.getSource() == btnRetourner) {
             gererRetour();
        } else if (e.getSource() == btnRechercherEmprunt) {
             rechercherEmprunt();
        } else if (e.getSource() == btnListerEmprunts) {
            listerEmprunts();
        } else if (e.getSource() == btnRetour) {
            new Bibliotheque();
            dispose();
        }
    }

    private void listerEmprunts() {
        // Créer un tableau pour stocker les données de la table
        Object[][] data = new Object[listeEmprunts.size()][3];
        int i = 0;
        for (Emprunt emprunt : listeEmprunts) {
            data[i][0] = emprunt.getNomUtilisateur();
            data[i][1] = emprunt.getTitreLivre();
            data[i][2] = emprunt.getDateEmprunt();
            i++;
        }

        // Créer les titres des colonnes
        String[] columnNames = {"Nom Utilisateur", "Titre Livre", "Date Emprunt"};

        // Créer le modèle de table avec les données et les titres des colonnes
        DefaultTableModel model = new DefaultTableModel(data, columnNames);

        // Créer une JTable avec le modèle de table
        JTable table = new JTable(model);

        // Ajouter la table à une JScrollPane pour permettre le défilement si nécessaire
        JScrollPane scrollPane = new JScrollPane(table);

        // Créer une nouvelle fenêtre pour afficher la table
        JFrame frame = new JFrame("Liste des Emprunts");
        frame.setLayout(new BorderLayout());
        frame.add(scrollPane, BorderLayout.CENTER);
        frame.setSize(600, 400);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private void gererEmprunt() {
        String nomUtilisateur = JOptionPane.showInputDialog(null, "Entrez le nom de l'utilisateur :");
        String titreLivre = JOptionPane.showInputDialog(null, "Entrez le titre du livre à emprunter :");

         LocalDate dateEmprunt = LocalDate.now();
         String dateEmpruntString = dateEmprunt.toString();

        // Vérification si l'utilisateur existe
        if (utilisateurExiste(nomUtilisateur)) {
            // Vérification si le livre existe
            if (livreExiste(titreLivre)) {
                // Demander la somme de cotisation
                int cotisation = demanderCotisation();
                if (cotisation == 2000) {
                    JOptionPane.showMessageDialog(null, "L'utilisateur et le livre existent. Livre emprunté !");
                    // Simulation de l'emprunt (remplacez ceci par votre propre logique)
                    Emprunt emprunt = new Emprunt(nomUtilisateur, titreLivre, dateEmpruntString);
                    listeEmprunts.add(emprunt); // Ajouter l'emprunt à la liste
                } else {
                    JOptionPane.showMessageDialog(null, "La cotisation doit être de 2000 FCFA.");
                }
            } else {
                JOptionPane.showMessageDialog(null, "Le livre n'existe pas.");
            }
        } else {
            JOptionPane.showMessageDialog(null, "L'utilisateur n'existe pas.");
        }
    }

    // Méthode pour demander la cotisation
    private int demanderCotisation() {
        String montant = JOptionPane.showInputDialog(null, "Entrez la cotisation (2000 FCFA) :");
        try {
            int cotisation = Integer.parseInt(montant);
            return cotisation;
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    // Méthode pour vérifier si l'utilisateur existe
    private boolean utilisateurExiste(String nomUtilisateur) {
        // Remplacer cette méthode avec votre propre logique
        return true; // Pour l'exemple, toujours retourner true
    }

    // Méthode pour vérifier si le livre existe
    private boolean livreExiste(String titreLivre) {
        // Remplacer cette méthode avec votre propre logique
        return true; // Pour l'exemple, toujours retourner true
    }


    private void gererRetour() {
    String nomUtilisateur = JOptionPane.showInputDialog(null, "Entrez le nom de l'utilisateur :");
    String titreLivre = JOptionPane.showInputDialog(null, "Entrez le titre du livre à retourner :");

    // Vérification si l'utilisateur existe
    if (utilisateurExiste(nomUtilisateur)) {
        // Vérification si le livre existe
        if (livreExiste(titreLivre)) {
            // Simulation du retour (remplacez ceci par votre propre logique)
            for (Emprunt emprunt : listeEmprunts) {
                if (emprunt.getNomUtilisateur().equals(nomUtilisateur) && emprunt.getTitreLivre().equals(titreLivre)) {
                    listeEmprunts.remove(emprunt); // Retirer l'emprunt de la liste
                    JOptionPane.showMessageDialog(null, "Livre retourné avec succès !");
                    return;
                }
            }
            JOptionPane.showMessageDialog(null, "Ce livre n'a pas été emprunté par cet utilisateur.");
        } else {
            JOptionPane.showMessageDialog(null, "Le livre n'existe pas.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "L'utilisateur n'existe pas.");
    }
}



private void rechercherEmprunt() {
    String recherche = JOptionPane.showInputDialog(null, "Entrez le nom de l'utilisateur ou le titre du livre à rechercher :");

    if (recherche != null && !recherche.isEmpty()) {
        boolean empruntTrouve = false;
        StringBuilder resultat = new StringBuilder();

        for (Emprunt emprunt : listeEmprunts) {
            if (emprunt.getNomUtilisateur().equalsIgnoreCase(recherche) || emprunt.getTitreLivre().equalsIgnoreCase(recherche)) {
                resultat.append("Nom Utilisateur: ").append(emprunt.getNomUtilisateur())
                        .append(", Titre Livre: ").append(emprunt.getTitreLivre())
                        .append(", Date Emprunt: ").append(emprunt.getDateEmprunt()).append("\n");
                empruntTrouve = true;
            }
        }

        if (empruntTrouve) {
            JOptionPane.showMessageDialog(null, "Résultats de la recherche :\n" + resultat.toString());
        } else {
            JOptionPane.showMessageDialog(null, "Aucun emprunt trouvé pour cette recherche.");
        }
    } else {
        JOptionPane.showMessageDialog(null, "Veuillez saisir un nom d'utilisateur ou un titre de livre pour effectuer la recherche.");
    }
}

    public static void main(String[] args) {
        new InterfaceGestionEmprunts();
    }
}

class Emprunt implements Serializable {
    private String nomUtilisateur;
    private String titreLivre;
    private String dateEmprunt;

    public Emprunt(String nomUtilisateur, String titreLivre, String dateEmprunt) {
        this.nomUtilisateur = nomUtilisateur;
        this.titreLivre = titreLivre;
        this.dateEmprunt = dateEmprunt;
    }

    public String getNomUtilisateur() {
        return nomUtilisateur;
    }

    public String getTitreLivre() {
        return titreLivre;
    }

    public String getDateEmprunt() {
        return dateEmprunt;
    }
}


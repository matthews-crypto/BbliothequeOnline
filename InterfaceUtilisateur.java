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
    private JButton btnRapportUtilisateursActifs;

    private Utilisateur utilisateur;

    public InterfaceUtilisateur() {
        setTitle("Menu de gestion des utilisateurs");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        // Ajout d'un arrière-plan d'image à la fenêtre
        setContentPane(new JLabel(new ImageIcon("image/user1.jpg")));
        setLayout(new BorderLayout());

        // Création d'un JPanel pour les boutons
        JPanel panelBoutons = new JPanel(new GridLayout(6, 1, 5, 5));
        panelBoutons.setOpaque(false); // Rendre le panel transparent

        btnAjouterUtilisateur = new JButton("Ajouter un utilisateur");
        btnSupprimerUtilisateur = new JButton("Supprimer un utilisateur");
        btnRechercherUtilisateur = new JButton("Rechercher un utilisateur");
        btnRetourMenuPrincipal = new JButton("Menu principal");
        btnListerUtilisateurs = new JButton("Lister les utilisateurs");
        btnRapportUtilisateursActifs = new JButton("Utilisateurs Actifs");

        // Ajout des écouteurs d'événements aux boutons
        btnAjouterUtilisateur.addActionListener(this);
        btnSupprimerUtilisateur.addActionListener(this);
        btnRechercherUtilisateur.addActionListener(this);
        btnListerUtilisateurs.addActionListener(this);
        btnRetourMenuPrincipal.addActionListener(this);
        btnRapportUtilisateursActifs.addActionListener(this);

        // Ajout des boutons au panel
        panelBoutons.add(btnAjouterUtilisateur);
        panelBoutons.add(btnSupprimerUtilisateur);
        panelBoutons.add(btnRechercherUtilisateur);
        panelBoutons.add(btnListerUtilisateurs);
        panelBoutons.add(btnRapportUtilisateursActifs);
        panelBoutons.add(btnRetourMenuPrincipal);

        // Ajout du panel de boutons au content pane
        add(panelBoutons, BorderLayout.WEST);

        // Rendre la fenêtre visible
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
            // Supprimez la demande pour le nombre d'ouvrages empruntés
            Utilisateur nouvelUtilisateur = new Utilisateur(nom, prenom, dateNaissance, tel, 0); // Utilisateur créé avec 0 ouvrages empruntés
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
        } else if (e.getSource() == btnRapportUtilisateursActifs) {
            afficherRapportUtilisateursActifs();
        } else if (e.getSource() == btnRetourMenuPrincipal) {
            dispose();
            new Bibliotheque();
        }
    }

    private void afficherRapportUtilisateursActifs() {
        List<Utilisateur> utilisateurs = Utilisateur.getUtilisateurs();

        // Vous pouvez trier les utilisateurs par une autre propriété ici

        StringBuilder rapport = new StringBuilder("Utilisateurs les plus actifs :\n");
        for (int i = 0; i < Math.min(10, utilisateurs.size()); i++) {
            Utilisateur utilisateur = utilisateurs.get(i);
            rapport.append("Nom: ").append(utilisateur.getNom()).append(", ");
            rapport.append("Prénom: ").append(utilisateur.getPrenom()).append(", ");
            // Vous pouvez ajouter d'autres propriétés ici
            rapport.append("\n");
        }

        JOptionPane.showMessageDialog(this, rapport.toString(), "Rapport Utilisateurs Actifs", JOptionPane.INFORMATION_MESSAGE);
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
    String[][] data = new String[utilisateurs.size()][5]; // Réduisez la taille du tableau à 5 colonnes

    for (int i = 0; i < utilisateurs.size(); i++) {
        Utilisateur utilisateur = utilisateurs.get(i);
        data[i][0] = utilisateur.getNom();
        data[i][1] = utilisateur.getPrenom();
        data[i][2] = utilisateur.getDateNaissance();
        data[i][3] = utilisateur.getTel();
        data[i][4] = utilisateur.getNumeroIdentification();
    }

    String[] columnNames = {"Nom", "Prénom", "Date de naissance", "Téléphone", "Numéro d'identification"}; // Supprimez le nom de la colonne "Nombre d'ouvrages empruntés"

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


    public static void main(String[] args) {
        new InterfaceUtilisateur();
    }
}

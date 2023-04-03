package controller;

import application.ChargerGrille;
import application.Main;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import javafx.scene.Node;
import javafx.scene.control.RadioButton;

public class AccueilController implements Initializable {

	@FXML
	private RadioButton choisirGrilleRB;
	@FXML
	private RadioButton GrilleAleatoireRB;
	@FXML
	private ComboBox<String> listeGrillesCB;

	@FXML
	private Button jouerButton;

	@FXML
	private Button quitter;

	@FXML
	private ImageView imageBackG;

	@FXML
	private ImageView logoBackG;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jouerButton.setDisable(true);
		listeGrillesCB.setVisible(false);
	}

	// Cette m�thode est appel� en fonction du choix de l'utilisateur
	// Et fixe une variable de classe la classe Main
	// Qui sera utilis� par le controleur de la vue suivante pour l'affichage
	@FXML
	public void clicRadioButton(MouseEvent e) throws SQLException {
		if (e.getSource() == GrilleAleatoireRB) {
			System.out.println("ça fonctionne");
			jouerButton.setDisable(false);
			listeGrillesCB.setVisible(false);

			// G�n�rer un num�ro de grille al�atoire entre 1 et 11 pour les grilles de la BD
			int numAleatoire = 1 + (int) (Math.random() * 11);
			Main.choixTypeMC = numAleatoire;

		} else if (e.getSource() ==choisirGrilleRB) {
			jouerButton.setDisable(false);
			// On rend le combo box visible et
			// On y ajoute les grilles disponibles dans la BD
			listeGrillesCB.setVisible(true);
			ChargerGrille cG = new ChargerGrille();
			Map<Integer, String> mapGrille = cG.grillesDisponibles();
			ObservableList<String> grillesDispos = FXCollections.observableArrayList(mapGrille.values());
			listeGrillesCB.setItems(grillesDispos);
			listeGrillesCB.getSelectionModel().select(8);
		}
	}






}

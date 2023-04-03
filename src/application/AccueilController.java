package application;

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
	
		//public static int choixTypeMC;
	@FXML
	private RadioButton choisirGrilleRB;
	@FXML
	private RadioButton GrilleAleatoireRB;
	@FXML
	private ComboBox<String> listeGrillesCB;

	@FXML
	private Button jouerButton;

	@FXML
	private Button quitterButton1;

	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		jouerButton.setDisable(true);
		listeGrillesCB.setVisible(false);
		
	}

	// Cette m�thode est appel� en fonction du choix de l'utilisateur
	// Et fixe une variable de classe la classe Main
	// Qui sera utilis� par le controleur de la vue suivante pour l'affichage
	@FXML
	public void clicRadioButton(ActionEvent e) throws SQLException {
		if (e.getSource() == GrilleAleatoireRB) {
			System.out.println("ça fonctionne");
			jouerButton.setDisable(false);
			choisirGrilleRB.setDisable(false);
			listeGrillesCB.setVisible(false);
           // G�n�rer un num�ro de grille al�atoire entre 1 et 11 pour les grilles de la BD
			int numAleatoire = 1 + (int) (Math.random() * 11);
			Main.choixTypeMC = numAleatoire;

		} else if (e.getSource() ==choisirGrilleRB) {
			jouerButton.setDisable(false);
			//GrilleAleatoireRB.setDisable(false);
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
	//Pour charger la vue suivante
		@FXML
		public void clicButton(ActionEvent e) {
			if (e.getSource() == jouerButton) {
				if (listeGrillesCB.visibleProperty().getValue()) {
					Main.choixTypeMC = listeGrillesCB.getSelectionModel().getSelectedIndex();
					System.out.println(Main.choixTypeMC);
				}
				nextvue(e,"VueTP6.fxml");
				
			} else if (e.getSource() == quitterButton1) {
				Stage stage = (Stage) quitterButton1.getScene().getWindow();
				stage.close();
			}
		}
		// Cette m�thode est apell�e pour charger la vue suivante
		
		public static void nextvue(ActionEvent e, String urlFXML) {
			try {
				FXMLLoader loader = new FXMLLoader(AccueilController.class.getResource(urlFXML));
				Parent root = (Parent) loader.load();
				Scene next_scene = new Scene(root);
				Stage next_stage = (Stage) ((Node) e.getSource()).getScene().getWindow();
				next_stage.setScene(next_scene);
				next_stage.show();
			} catch (IOException ex) {
				System.out.println("Impossible de charger le fichier FXML");
				System.out.println(ex.getMessage());
			}
		}





}

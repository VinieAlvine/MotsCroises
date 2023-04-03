package application;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javafx.animation.ScaleTransition;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.util.Duration;
import models.MotsCroisesTP6;






public class Tp6Controller {

	@FXML
	private GridPane grilleMC;

	MotsCroisesTP6 modeleMC;

	TextField textF;
	@FXML
	private Label GrilleLabel;
	@FXML
	private Button Menu;
	TextField mTF; // Pour r�affecter les propri�t�s de base partout o� on souhaite
	@FXML
	private Label nomGrilleLabel;


	@FXML
	public void initialize() throws SQLException{
		System.out.println(Main.choixTypeMC);
		dispositionG(Main.choixTypeMC + 1);
		Modele();
		TextField tf = (TextField) grilleMC.getChildren().get(0);
		// Placer le curseur sur la premi�re case
		// avec la police ad�quate
		tf.requestFocus();
		Police(null);
	}

	// Cette fonction se charge de disposer une grille sur la fen�tre � partir de
	// son num�ro de grille
	private void dispositionG(int numeroGrille) throws SQLException {
		ChargerGrille grille = new ChargerGrille();
		modeleMC = grille.extraireGrille(numeroGrille);
		// Afficher le nom  de la grille
		nomGrilleLabel.setText(grille.grillesDisponibles().get(numeroGrille));
		// On récupère la première case de la grille
		mTF = (TextField) grilleMC.getChildren().get(0);
		mTF.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-size: 1px");
		// On vide la grille
		grilleMC.getChildren().clear();
		// On ajoute les cases =! noires dans la grille
		TextField textF;

		for (int ligne = 0; ligne < modeleMC.getHauteur(); ligne++) {

			for (int colonne = 0; colonne < modeleMC.getLargeur(); colonne++) {
				if (!modeleMC.estCaseNoire(ligne + 1, colonne + 1)) {
					textF = new TextField();
					textF.setPrefWidth(mTF.getPrefWidth());
					textF.setPrefHeight(mTF.getPrefHeight());
					for (Object cle : mTF.getProperties().keySet()) {
						textF.getProperties().put(cle, mTF.getProperties().get(cle));
					}
					textF.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-size: 1px");
					grilleMC.add(textF, colonne, ligne);
				}
			}
		}

	}

	// QUESTION 1.1
	private void disposerGrille3x6() {
		modeleMC = MotsCroisesFactory.creerMotsCroises2x3();
		Modele();
	}

	// QUESTIONS 1.2, 1.3, 1.4, 1.5 et l'animation de la QUESTION 3

	private void Modele() {
		StringProperty modelSP;
		String defH, defV;
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = ((int) n.getProperties().get("gridpane-row")) + 1;
				int col = ((int) n.getProperties().get("gridpane-column")) + 1;

				// Initialisation du TextField tf ayant pour coordonn�es (lig, col)
				modelSP = modeleMC.propositionProperty(lig, col);
				tf.textProperty().bindBidirectional(modelSP);
				defH = modeleMC.getDefinition(lig, col, true);
				defV= modeleMC.getDefinition(lig, col, false);
				if (defH != null && defV != null) {
					tf.setTooltip(new Tooltip(defH + "/" + defV));
				} else if (defH != null) {
					tf.setTooltip(new Tooltip(defH));
				} else if (defV != null) {
					tf.setTooltip(new Tooltip(defV));
				}

				// Association du textField � la m�thode montrerSolution
				// setPolice va permettre de changer le style de la case puissqu'elle a le focus
				tf.setOnMouseClicked((e) -> {
					clicLettre(e);
					Police(e);
				});
				tf.setOnKeyReleased((e) -> {
					releaseKey(e);
				});
			}
		}
	}
	// Ref QUESTION 3 : Cette m�thode met une bordure bleue � la case avec le focus
	// Etune bordure standard pour toutes les autres cases
	private void Police(MouseEvent event) {
		TextField tf;
		if (event == null) {
			tf = (TextField) grilleMC.getChildren().get(0);
			tf.setStyle("-fx-border-color: blue; -fx-border-style: solid; -fx-border-width: 3px");
		} else {
			tf = (TextField) event.getSource();
			tf.setStyle("-fx-border-color: blue; -fx-border-style: solid; -fx-border-width: 3px");
			for (Node n : grilleMC.getChildren()) {
				if (n instanceof TextField) {
					tf = (TextField) n;
					if (!tf.focusedProperty().getValue()) {
						tf.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-size: 1px");

					}

				}
			}
		}

	}




	@FXML
	public void clicLettre(MouseEvent e) {
		if (e.getButton() == MouseButton.MIDDLE) {
			// C'est un clic "central"
			TextField caseMC = (TextField) e.getSource();
			int lig = ((int) caseMC.getProperties().get("gridpane-row")) + 1;
			int col = ((int) caseMC.getProperties().get("gridpane-column")) + 1;
			// appel de montrerSolution() sur le mod le
			modeleMC.montrerSolution(lig, col);
		}
	}
	// Permet de r�cup�rer un noeud (textfield) du gridpane
	private Node getNodeFromGridPane(GridPane gridPane, int col, int lig) {
		for (Node node : gridPane.getChildren()) {
			if (GridPane.getColumnIndex(node) == col && GridPane.getRowIndex(node) == lig) {
				return node;
			}
		}
		return null;
	}

	// Ref QUESTION 3 :Cette fonction permet de d�placer le curseur selon la
	// direction donn�e
	private void advanceCursor(TextField tf, int lig, int col, KeyCode kC) {

		switch (kC) {
		case LEFT:
			do {
				if (col == 0 && lig == 0) {
					// Se positionner sur la derni�re case de la derni�re colonne
					// de la derni�re ligne
					col = modeleMC.getLargeur() - 1;
					lig = modeleMC.getHauteur() - 1;
				} else if (col == 0) {
					// Remonter d'une ligne et se positionner sur la derni�re case
					lig--;
					col = modeleMC.getLargeur() - 1;
				} else {
					// Revenir une case en avant
					col--;
				}
			} while (modeleMC.estCaseNoire(lig + 1, col + 1));
			break;

		case RIGHT:
			do {
				if (col == modeleMC.getLargeur() - 1 && lig == modeleMC.getHauteur() - 1) {
					col = 0;
					lig = 0;
				} else if (col == modeleMC.getLargeur() - 1) {
					// Deplacer vers la droite
					lig++;
					col = 0;
				} else {
					// Ou revenir � la ligne
					col++;
				}
			} while (modeleMC.estCaseNoire(lig + 1, col + 1));
			break;

		case DOWN:
			do {
				if (lig == modeleMC.getHauteur() - 1) {
					// Revenir sur la 1ère ligne
					lig = 0;
				} else {
					// Deplacer vers le bas
					lig++;
				}
			} while (modeleMC.estCaseNoire(lig + 1, col + 1));

			break;

		case UP:
			do {
				if (lig == 0) {
					// Revenir sur la premi�re ligne
					lig = modeleMC.getHauteur() - 1;
				} else {
					// Deplacer vers le bas
					lig--;
				}
			} while (modeleMC.estCaseNoire(lig + 1, col + 1));
			break;

		default:
			break;

		}

		tf.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-size: 1px");
		tf = (TextField) getNodeFromGridPane(grilleMC, col, lig);
		tf.requestFocus();
		tf.setStyle("-fx-border-color: blue; -fx-border-style: solid; -fx-border-width: 3px");
	}

	@FXML
	public void releaseKey(KeyEvent event) {
		TextField tf;
		tf = (TextField) event.getSource();
		styliserCases(tf);
		int lig = ((int) tf.getProperties().get("gridpane-row"));
		int col = ((int) tf.getProperties().get("gridpane-column"));

		KeyCode eventKC = event.getCode();
		switch (eventKC) {
		case ENTER:
			// Montrer les cases correctes
			afficherCasesCorrectes();
			break;

		case DOWN:
			advanceCursor(tf, lig, col, KeyCode.DOWN);
			break;

		case UP:
			advanceCursor(tf, lig, col, KeyCode.UP);
			break;

		case LEFT:
			advanceCursor(tf, lig, col, KeyCode.LEFT);
			break;

		case BACK_SPACE:
			// Supprimer et faire reculer le curseur ( avancer vers la gauche)
			tf.setText("");
			advanceCursor(tf, lig, col, KeyCode.LEFT);
			break;

		case RIGHT:
			advanceCursor(tf, lig, col, KeyCode.RIGHT);
			break;

		case TAB:
			break;

		default:verifycharacters(tf,event,lig,col);


		break;

		}
	}

	public void verifycharacters(TextField tf,KeyEvent event,int lig, int col) {
		// analyse du caractère saisi et s'il s'agit d'une lettre min ou maj on l'affiche sinon on ne l'accepte pas
		String contenu = tf.getText();
		System.out.println(event.getCharacter() + " txf :" + tf.getText());

		if (contenu.length() == 1 && !isConforming(contenu.charAt(0), "[A-Z]")
				&& !isConforming(contenu.charAt(0), "[a-z]")) {
			tf.setText("");
		} else if (contenu.length() == 1 &&isConforming(contenu.charAt(0), "[A-Z]")
				|| isConforming(contenu.charAt(0), "[a-z]")) {
			tf.setText(Character.toString(contenu.charAt(0)).toUpperCase());
			advanceCursor(tf, lig, col, KeyCode.RIGHT);
		} else if (contenu.length() > 1) {

			char c = contenu.charAt(contenu.length() - 1);
			if (isConforming(c, "[A-Z]")) {
				tf.setText(Character.toString(c));
				advanceCursor(tf, lig, col, KeyCode.RIGHT);
			} else if (isConforming(c, "[a-z]")) {
				tf.setText(Character.toString(c).toUpperCase());
				advanceCursor(tf, lig, col, KeyCode.RIGHT);
			} else {
				c = contenu.charAt(0);
				if (isConforming(c, "[A-Z]")) {
					tf.setText(Character.toString(c));
				} else if (isConforming(c, "[a-z]")) {
					tf.setText(Character.toString(c).toUpperCase());
				} else {
					tf.setText("");
				}
			}

		}

	}

	//Cette méthode permet de styliser une case de grille, en lui donnant une bordure bleue si elle a le focus et une bordure noire sinon
	private void styliserCases(TextField tf) {
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				tf = (TextField) n;
				if (tf.focusedProperty().getValue()) {
					tf.setStyle("-fx-border-color: blue; -fx-border-style: solid; -fx-border-width: 3px");
				} else {
					tf.setStyle("-fx-border-color: black; -fx-border-style: solid; -fx-border-size: 1px");
				}
			}
		}
	}

	// cette methode accepte ou  refuser un caractere saisi
	private boolean isConforming(char chaine, String reg) {
		Pattern p = Pattern.compile(reg);
		Matcher m = p.matcher(chaine + "");
		return m.find();
	}

	private void afficherCasesCorrectes() {
		for (Node n : grilleMC.getChildren()) {
			if (n instanceof TextField) {
				TextField tf = (TextField) n;
				int lig = GridPane.getRowIndex(n) + 1;
				int col = GridPane.getColumnIndex(n) + 1;
				char solution = modeleMC.getSolution(lig, col);
				String couleur = tf.getText().equalsIgnoreCase(Character.toString(solution))
						? "-fx-background-color:palegreen;"
								: "-fx-background-color:salmon;";
				tf.setStyle(couleur);
			}
		}
	}
	public void MenuButton(ActionEvent e) {
		if (e.getSource() == Menu) {
			AccueilController.nextvue(e,"VueAccueilTP6.fxml");
		}
	}




}

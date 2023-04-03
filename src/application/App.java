package application;

import java.sql.SQLException;
import java.util.HashMap;

import models.*;

public class App {

	public static void main(String[] args) throws SQLException {
		// TODO Auto-generated method stub
		ChargerGrille c_grille=new ChargerGrille();
		//System.out.println( c_grille.grillesDisponibles());
		System.out.println( c_grille.extraireGrille(1).toString());

	}

}

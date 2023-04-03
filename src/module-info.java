module TP6_MotsCroises {
	requires javafx.controls;
	requires javafx.fxml;
	requires javafx.base;
	requires java.sql;
	requires javafx.graphics;
	opens application to javafx.graphics, javafx.fxml;
}

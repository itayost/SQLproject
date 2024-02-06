package application;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import test.controller.QuestionController;
import test.controller.QuestionControllerSQL;
import test.model.Answer;
import test.model.Manager;
import test.model.ManagerSQL;
import test.model.MultiChoiceQuestion;
import test.model.OpenQuestion;
import test.model.Question;
import test.model.Set;
import test.view.TestGUI;
import test.view.Testable;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {



	public void start(Stage primaryStage) {
		ManagerSQL m = new ManagerSQL();
		Testable view = new TestGUI(primaryStage);
		QuestionControllerSQL controller = new QuestionControllerSQL(m, view);
		m.LoadSourceFromSQL();
		/*try {
			m.inFile();
		} catch (ClassNotFoundException | IOException e) {
			System.out.println("Didnt load");
			m.initial();
		}*/
	}

	public static void main(String[] args) {
		
		/*try {
	        Class.forName("com.mysql.cj.jdbc.Driver"); // start the driver
	    }catch (Exception ex1) {
	        	while (ex1 != null) {
	        		System.out.println("SQL exception: + " + ex1.getMessage());
	    }
	     }
	    try {
	    	Connection conn1;
	        String dbUrl = "jdbc:mysql://localhost:3306/test"; 
	        conn1 = DriverManager.getConnection(dbUrl, "root", "Asraf0714");//set up a connection
	        Statement stmt2 = conn1.createStatement();
	        String s2 = ("INSERT INTO Question (Qtext) VALUES('what day is this?')");
	        int numAffectedRows = stmt2.executeUpdate(s2);
	    	System.out.println("numAffectedRows=" + numAffectedRows); 
			}catch (SQLException ex) {
			    	while (ex != null) {
			    		System.out.println("SQL exception: + " + ex.getMessage());
			}
			
			}*/
	    launch(args);
			}
	
	
	}

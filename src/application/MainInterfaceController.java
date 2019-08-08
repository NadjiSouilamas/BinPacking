package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXRadioButton;
import com.jfoenix.controls.JFXTabPane;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.StackedBarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Toggle;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Background;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;

 
  

public class MainInterfaceController implements Initializable{

 @FXML TextField capacite;
 @FXML TextArea listObjets;
 @FXML JFXButton resoudre,hresoudre,mheuristic; 
 @FXML StackedBarChart <String,Integer> chart;
 @FXML CategoryAxis x;
 @FXML NumberAxis y;
 @FXML Label nbins;
 @FXML JFXButton testBench; 
 @FXML JFXTextField filepath; 
 @FXML JFXButton tester; 
 @FXML JFXButton heuristic;
 @FXML Label N; 
 @FXML Label C;
 @FXML Label tmp; 
 @FXML BorderPane borderPane; 
 static javafx.scene.Node node;
 @FXML JFXTabPane tabpane;
 @FXML Tab mexacteTab;
 @FXML Tab heuristicTab;
 @FXML Tab mheuristicTab;
 @FXML JFXButton mexacte;
 @FXML Label title;
 @FXML JFXTextField nbNFD,nbBFD,nbFFD,tmpNFD,tmpBFD,tmpFFD,nbKorf,tmpKorf;
 @FXML Label best_method,type_method,result,tmpexec,param; 
 

 Opt tp =new Opt();
 String objets_mexacte,objets_heuristic="";
 String c_mexacte,c_heuristics="";
 String eFilepath,hFilepath="";
 int n,c;
 
 private List<Integer> R=new ArrayList<>();
 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	}
   

}

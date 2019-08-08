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
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleGroup;
import javafx.stage.FileChooser;
import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import java.io.File;
import java.io.IOException;

public class MainInterfaceController2 implements Initializable {


   @FXML JFXButton testBench; 
   @FXML JFXButton tester;
   @FXML JFXTextField filepath;
   @FXML JFXToggleButton ffdButton,bfdButton,nfdButton,agButton,rsButton; 
   @FXML JFXButton resoudre; 
   String benchpath; 
   String benchname; 
   String excelSheet="E:\\\\MyFirstExcel.xls"; 
   @FXML Label best_method,type_method,result,tmpexec,param;
   @FXML JFXTextField rsNbItr,rsTinit,rsSinit,alpha;
   @FXML JFXTextField sizepop,pm,sizemut,agNbiter;
   @FXML Label nbBins,tmp; 
   //@FXML Label rsNbItr,rsTinit,rsSinit,alpha; 
   private List<Integer> R=new ArrayList<>();
   int n,c; 
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		ToggleGroup group=new ToggleGroup(); 
	    ffdButton.setToggleGroup(group);
	    bfdButton.setToggleGroup(group);
	    nfdButton.setToggleGroup(group);
	    agButton.setToggleGroup(group);
	    rsButton.setToggleGroup(group);
	}
	
	@FXML  void handdletestBench(ActionEvent e)
	{   
		filepath.clear(); benchpath=""; 
		benchname=""; 
		FileChooser fc=new FileChooser();
    	File selectedFile=fc.showOpenDialog(null);
    	if(selectedFile!=null) 
    	{   
    		String str = selectedFile.getName();
    		filepath.setText(str);
    		int index = str.indexOf (".");
    		benchname=str.substring(0,index);
    		benchpath=selectedFile.getAbsolutePath(); 
    	}
	}
	
	@FXML  void handleTester(ActionEvent e)
	{
		boolean found; 
		Excel.result=10000000; 
		Excel.methode=""; 
		Excel.type=""; 
		Excel.param=""; 
		Excel.tmp=""; 
		Excel sheet=new Excel(); 
	  found=sheet.getBestSolution(benchname+".txt");	
	  if(!found)
	  {   
		  long startTime,endTime; 
		  List<Bin> l=new ArrayList<>(); 
		  int ffd,rs;
		  /************* Resoudre manuellement ********/ 
		  //nbBins.setText("");
		  //tmp.setText("");
		  System.out.println("instance not found");
		  setlist(benchpath);
		  int bestResult=n+1; 
		  startTime = System.currentTimeMillis();
		  l=Heuristics.FFD(R, n, c);
		  endTime = System.currentTimeMillis();
		  ffd=l.size();
		  if(ffd<bestResult)
		  {
			  best_method.setText("FFD");
			  type_method.setText("Heuristique");
			  result.setText(Integer.toString(ffd));
	          tmpexec.setText(Long.toString(endTime-startTime));
	          param.setText("pas de paramètres"); 
		  }
		  //*****************//
		  l.clear(); R.clear(); 
		  Instance i=new Instance(benchpath); 
		  SA s=new SA(i);
		  startTime = System.currentTimeMillis();
		  l=s.simulatedAnnealing(1000000, 1000, 2, 0.99);
		  endTime = System.currentTimeMillis();
		  rs=l.size();
		  if(rs<bestResult)
		  {
			  best_method.setText("Récuit simulé");
			  type_method.setText("métaheuristique");
			  result.setText(Integer.toString(rs));
	          tmpexec.setText(Long.toString(endTime-startTime));
	          param.setText("Nb_iter = 1000000  S_initial = 2, alpha = 0.99");
		  }
		  
	  }
	  else {
		  best_method.setText(Excel.methode);
		  type_method.setText(Excel.type);
		  result.setText(Integer.toString(Excel.result));
          tmpexec.setText(Excel.tmp);
          param.setText(Excel.param);
	  }
		  
	}
	
	@FXML void handleResoudre(ActionEvent e)
	{
	  /*** FFD ***/ 
		
	 List<Bin> solution=new ArrayList<Bin>();
	 long startTime,endTime; 
	 nbBins.setText("Nb Bins =");
	 tmp.setText("Temp d'éxecution(ms) =");
	  if(ffdButton.isSelected())
	  {
		  setlist(benchpath);  
		  startTime = System.currentTimeMillis();
		  solution= Heuristics.FFD(R, n, c);
		  endTime = System.currentTimeMillis();
		  nbBins.setText("Nb Bins = "+solution.size());
		  tmp.setText("Temp d'éxecution(ms) = "+(endTime-startTime));
		  afficherSol(solution);
		  
		  
	  }
	  if(bfdButton.isSelected())
	  {
		  setlist(benchpath); 
		  startTime = System.currentTimeMillis();
		  solution= Heuristics.BFD(R, n, c);
		  endTime = System.currentTimeMillis();
		  nbBins.setText("Nb Bins = "+solution.size());
		  tmp.setText("Temp d'éxecution(ms) = "+(endTime-startTime));
		  afficherSol(solution);
		  	  
	  }
	  if(nfdButton.isSelected())
	  {
		  setlist(benchpath); 
		  startTime = System.currentTimeMillis();
		  solution= Heuristics.NFD(R, n, c);
		  endTime = System.currentTimeMillis();
		  nbBins.setText("Nb Bins = "+solution.size());
		  tmp.setText("Temp d'éxecution(ms) = "+(endTime-startTime));
		  afficherSol(solution);
		  	  
	  }
	  if(rsButton.isSelected())
	  {   //setlist(benchpath);
		  int kmax=Integer.valueOf(rsNbItr.getText().trim()); 
		  double t=Double.parseDouble(rsTinit.getText().trim()); 
		  int s=Integer.valueOf(rsSinit.getText().trim());
		  double a=Double.parseDouble(alpha.getText().trim()); 
		  Instance i=new Instance(benchpath); 
		  SA sa =new SA(i); 
		  startTime = System.currentTimeMillis();
		  solution= sa.simulatedAnnealing(kmax, t, s, a);
		  endTime = System.currentTimeMillis();
		  nbBins.setText("Nb Bins = "+solution.size());
		  tmp.setText("Temp d'éxecution(ms) = "+(endTime-startTime));
		  afficherSol(solution);
	  }
	  if(agButton.isSelected())
	  {
		  setlist(benchpath);  
		  int population =Integer.valueOf(sizepop.getText().trim()); 
		  int mutate_size=Integer.valueOf(sizemut.getText().trim()); 
		  int pmm =Integer.valueOf(pm.getText().trim());
		  int k=Integer.valueOf(agNbiter.getText().trim());
		  
		  Generation gen=new Generation(); 
		  startTime = System.currentTimeMillis();
          gen.setPopulation_size(population);

          //3.GENERATE THE FIRST GENERATION
          gen =Generation.initFirstGeneration(R);

          //3.SET THE BEST BINS COUNT TO N+1 WHERE N IS THE NUMBER OF ELEMENTS
          gen.setBestBinsCount(n+ 1);

          //4.SET THE SELECTION SIZE
          gen.setSelectionSize(population / 2);

          //5.COUNT THE FITNESST OF THE FIRST GENERATION' S INDIVIDUALS
          gen.countFistness();

          //6.SET THE MUTATION SIZE
          gen.setMutation_size(mutate_size);

          //7.SET THE MUTATION PROBA (PROBA*100)
          gen.setMutation_proba(pmm);

    //      System.out.println("BEST NB BINS BEFORE :\t" +gen.getBestBinsCount());
          for(int j = 0 ; j < k; j++)
          {
              gen.generateNextGeneration();
              gen.addMutations();
              gen.countFistness();
          }
          endTime = System.currentTimeMillis();
          nbBins.setText("Nb Bins = "+gen.getBestBinsCount());
		  tmp.setText("Temp d'éxecution(ms) = "+(endTime-startTime));
	  }
	}

	private void setlist(String filepath)
	{
		n = 0; c = 100;
		int i=1;  
		this.R.clear();
        if(!filepath.isEmpty())
        {
		try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {

			String sCurrentLine;

            if ((sCurrentLine = br.readLine()) != null)
                n = Integer.parseInt(sCurrentLine);

            if ((sCurrentLine = br.readLine()) != null)
			    c = Integer.parseInt(sCurrentLine);
            Bin.c=c;
			while ((sCurrentLine = br.readLine()) != null) {

				this.R.add(Integer.parseInt(sCurrentLine));
			}

		} catch (IOException exp) {
			System.out.println("ERROR WHILE OPENING THE FILE !");
		}
        System.out.println("N =\t"+n);
        System.out.println("C =\t"+c);
        Collections.sort(R,Collections.reverseOrder()); 
  }
        else {
        	
        	/************ SHOW ALERT ************/ 
        }
	}
	
	private void afficherSol(List<Bin>l)
	{   int j=1; 
		for(int i=0;i<l.size();i++)
		{
			System.out.print("Bin "+j+":");
			l.get(i).afficher();
			j++; 
		}
	}
	
	
}

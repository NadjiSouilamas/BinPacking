package application;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

public class Excel {

    
	 
	 static String methode; 
	 static String type; 
	 static int result=100000; 
	 static String tmp; 
	 static String param; 
	 
	  boolean getBestSolution(String instance)
	    {
         boolean stop=false; 
	        Workbook workbook = null;
	        try {

	            workbook = Workbook.getWorkbook(new File(getClass().getResource("SA_Results.xls").getPath() ));
	            Sheet sheet = workbook.getSheet(0);
	            int ffdCol,ffdtmpCol,bfdCol,bfdtmpCol,nfdCol,nfdtmpCol; 
	            int agCol,agtmpCol,agConfCol; 
	            int rsCol,rstmpCol,rsConfCol;
	            bfdCol=1; bfdtmpCol=2;
	            ffdCol=3; ffdtmpCol=4; 
	            nfdCol=5; nfdtmpCol=6; 
	            rsCol=7;rstmpCol=8; rsConfCol=9; 
	            agCol=10; agtmpCol=11; agConfCol=12; 
	            
	            int nbrows=sheet.getRows(); 
	            System.out.println("Nymber of rows is"+nbrows);        // 1
                int i=1; 
                while(i<nbrows && !stop )
                {
                	if(instance.equals(sheet.getCell(0, i).getContents().trim()))
                     stop=true; 
                	else i++; 
                }
                if(i==nbrows)

                return false ; 
                else {
                	System.out.println("i="+i);
                	int ffd = Integer.valueOf(sheet.getCell(ffdCol, i).getContents().trim()); 
                	System.out.println("ffd="+ffd);
                	if(ffd<result)
                	{
                		methode="FFD"; 
                		type="Heuristique";
                		result=ffd;
                		tmp=sheet.getCell(ffdtmpCol, i).getContents().trim();
                		param="Pas de paramètres"; 
                	}
                	int bfd = Integer.valueOf(sheet.getCell(bfdCol, i).getContents().trim()); 
                	System.out.println("bfd="+bfd);
                	if(bfd<result)
                	{
                		methode="BFD"; 
                		type="Heuristique";
                		result=bfd;
                		tmp=sheet.getCell(bfdtmpCol, i).getContents().trim();
                		param="Pas de paramètres"; 
                	}
                	int nfd = Integer.valueOf(sheet.getCell(nfdCol, i).getContents().trim());
                	System.out.println("nfd="+nfd);
                	if(nfd<result)
                	{
                		methode="NFD"; 
                		type="Heuristique";
                		result=nfd;
                		tmp=sheet.getCell(nfdtmpCol, i).getContents().trim();
                		param="Pas de paramètres"; 
                	}
                	int rs = Integer.valueOf(sheet.getCell(rsCol, i).getContents().trim()); 
                	System.out.println("ag="+rs);
                	if(rs<result)
                	{
                		methode="Récuit simulé"; 
                		type="métaeuristique";
                		result=rs;
                		tmp=sheet.getCell(rstmpCol, i).getContents().trim();
                		FileManager m=new FileManager(); 
                		m.initConfigSA(); 
                		int k=Integer.valueOf(sheet.getCell(rsConfCol, i).getContents().trim()); 
                		ConfigSA c=m.csa.get(k); 
                		param="Nb_iter = "+c.kmax+", T_init = "+c.t+", S_initial = "+c.initSol+", alpha = "+c.alpha; 
                		//param="Pas de paramètres"; 
                	}
                	int ag=Integer.valueOf(sheet.getCell(agCol, i).getContents().trim()); 
                	if(ag<result)
                	{
                		methode="Algorithme génétique"; 
                		type="métaeuristique";
                		result=ag;
                		tmp=sheet.getCell(agtmpCol, i).getContents().trim();
                		int k=Integer.valueOf(sheet.getCell(agConfCol, i).getContents().trim()); 
                		FileManager m=new FileManager(); 
                		int []l=m.cAG[k];
                		param="Nb_iter = 10000" +"taille_pop = "+l[0]+"taile_mut = "+l[1]+"pm = "+l[2]; 
                	}
                }

	        } catch (IOException e) {
	            e.printStackTrace();
	        } catch (BiffException e) {
	            e.printStackTrace();
	        } finally {

	            if (workbook != null) {
	                workbook.close();
	            }

	        }
		 
		 return true; 
	      
	    }
}

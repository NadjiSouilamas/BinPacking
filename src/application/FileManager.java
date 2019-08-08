package application;

import jxl.Workbook;
import jxl.write.*;
import jxl.write.Number;
import jxl.write.biff.RowsExceededException;


import java.io.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class FileManager {

    private static final String EXCEL_FILE_LOCATION = "E:\\2CS-SIQ-S2\\OPT-Projet\\tests\\SA_Results.xls";
    private String folderPath = "E:\\2CS-SIQ-S2\\OPT-Projet\\Benchmarks\\ForDemo";

    private double[] optim = {40, 83, 167, 20, 399, 48, 99, 198, 56, 57, 56};

    private final int instCol = 0;
    private final int bfdSol = 1;
    private final int bfdTime = 2;
    private final int ffdSol = 3;
    private final int ffdTime = 4;
    private final int nfdSol = 5;
    private final int nfdTime = 6;
    private final int saSol = 7;
    private final int saTime = 8;
    private final int saConf = 9;
    private final int agSol = 10;
    private final int agTime = 11;
    private final int agConf = 12;

    int[] kmax = {10_000, 50_000, 800_000};
    int[] t = { 10_000, 100_000};
    double[] alpha = { 0.9, 1};
    int[] init = {1, 2, 3};

    /** AG config */

    int[][] cAG =
    {       //population-size____mutation-size____mutation-proba
        {100, 8  , 70},   //0
        {100, 8  , 50},   //1
    //    {100, 8  , 30},   //2
   //     {100, 6  , 70},   //3
   //     {100, 6  , 50},   //4
   //     {100, 6  , 30},   //5
        {100, 4  , 70},   //6
        {100, 4  , 50},   //7
    //    {100, 4  , 30},   //8
        {100, 2  , 70},   //9
    //    {100, 2  , 50},   //10
        {100, 2  , 30},   //11
        {50 , 8  , 70},   //12
     //   {50 , 8  , 50},   //13
        {50 , 8  , 30},   //14
        {50 , 6  , 70},   //15
    //    {50 , 6  , 50},   //16
        {50 , 6  , 30},   //17
        {50 , 4  , 70},   //18
    //    {50 , 4  , 50},   //19
        {50 , 4  , 30},   //20
        {50 , 2  , 70},   //21
            //  {50 , 2  , 50},   //22
    //    {50 , 2  , 30},   //23
    };

    private final int popSize = 0;
    private final int mutSize = 1;
    private final int mutProba = 2;

    HashMap<Integer, ConfigSA> csa = new HashMap<>();
    List<Bin> sol = new ArrayList<>();

    public int initConfigSA(){
        int k = 0;
        List<ConfigSA> configsSA = new ArrayList<>();
        for ( int iKmax = 0; iKmax < kmax.length; iKmax++)
            for (int iT = 0; iT < t.length; iT++)
                for(int iAlpha = 0; iAlpha < alpha.length; iAlpha++)
                    for (int iInit = 0; iInit < init.length; iInit++){
                        this.csa.put(k, new ConfigSA(kmax[iKmax], t[iT], init[iInit], alpha[iAlpha] ));
                        k++;
                    }
        return 0;
    }

    public int showConfigSA(){
        ConfigSA configSA;
        for(int i = 0; i < csa.keySet().size(); i++){
            System.out.print("key = "+i);
            configSA = csa.get(i);
            System.out.println("kmax = "+configSA.kmax+"t = "+configSA.t+"init = "+configSA.initSol+"alpha = "+configSA.alpha);
        }
        return 0;
    }

    public int testSA() {
        File folder = new File(folderPath);
        String files[] = folder.list();

            for (int i = 0; i < files.length; i++) {
                System.out.println((i + 1) + "\t" + files[i]);
            }
        return 0;
    }


    public int makeTestFile(){

        File  folder = new File(folderPath);
        String files[] = folder.list();

        Label label;
        Number number;

        //1. Create an Excel file
        WritableWorkbook myFirstWbook = null;
        try {
            myFirstWbook = Workbook.createWorkbook(new File(EXCEL_FILE_LOCATION));
            WritableSheet excelSheet = myFirstWbook.createSheet("demo", 0);

            /** HEADER */

            label = new Label(instCol, 0, "Instance");
            excelSheet.addCell(label);
            label = new Label(bfdSol, 0, "Cout BFD");
            excelSheet.addCell(label);
            label = new Label(bfdTime, 0, "Temps BFD");
            excelSheet.addCell(label);

            label = new Label(ffdSol, 0, "Cout FFD");
            excelSheet.addCell(label);
            label = new Label(ffdTime, 0, "Temps FFD");
            excelSheet.addCell(label);

            label = new Label(nfdSol, 0, "Cout NFD");
            excelSheet.addCell(label);
            label = new Label(nfdTime, 0, "Temps NFD");
            excelSheet.addCell(label);

            label = new Label(saSol, 0, "Cout RS");
            excelSheet.addCell(label);
            label = new Label(saTime, 0, "Temps RS");
            excelSheet.addCell(label);
            label = new Label(saConf, 0, "Config RS");
            excelSheet.addCell(label);

            label = new Label(agTime, 0, "Temps AG");
            excelSheet.addCell(label);
            label = new Label(agSol, 0, "Cout AG");
            excelSheet.addCell(label);
            label = new Label(agConf, 0, "Config AG");
            excelSheet.addCell(label);

            for(int i = 0; i < 1; i++){
                System.out.println(files[i]);
                 long timeBest = Long.MAX_VALUE;
                 double  valueBest = Double.MAX_VALUE;
                 int configBest = 0;

                 long startTime;
                 long executionTime;

                Instance instance = new Instance(folderPath+"\\"+files[i]);

                label = new Label(instCol, i + 1, files[i]);
                excelSheet.addCell(label);

                /** Heuristics */


                /** BFD */

                startTime = executionTime = System.currentTimeMillis();
                sol = Heuristics.BFD(instance.getItemsList(), instance.getN(), instance.getC());
                executionTime = System.currentTimeMillis() - startTime;

                number = new Number(bfdTime, i + 1, executionTime);
                excelSheet.addCell(number);

                number = new Number(bfdSol, i + 1, sol.size());
                excelSheet.addCell(number);
                System.out.println("BFD ="+sol.size());

                /** FFD */

                startTime = executionTime = System.currentTimeMillis();
                sol = Heuristics.FFD(instance.getItemsList(), instance.getN(), instance.getC());
                executionTime = System.currentTimeMillis() - startTime;

                System.out.println("printing FFD sol ");
                for ( Bin bin :sol
                ) {
                    bin.print();
                }
                number = new Number(ffdTime, i + 1, executionTime);
                excelSheet.addCell(number);
                number = new Number(ffdSol, i + 1, sol.size());
                excelSheet.addCell(number);
                System.out.println("FFD ="+sol.size());

                /** NFD */

                startTime = executionTime = System.currentTimeMillis();
                sol = Heuristics.NFD(instance.getItemsList(), instance.getN(), instance.getC());
                executionTime = System.currentTimeMillis() - startTime;

                number = new Number(nfdTime, i + 1, executionTime);
                excelSheet.addCell(number);

                number = new Number(nfdSol, i + 1, sol.size());
                excelSheet.addCell(number);
                System.out.println("NFD ="+sol.size());
                /** Simulated Annealing */

                SA sa = new SA(instance);
                 for ( int keyConfig: csa.keySet()
                      ) {
                                 double value =0;
                                ConfigSA configSA = csa.get(keyConfig);
                                for (int j = 0; j < 3; j++) {
                                    startTime = System.currentTimeMillis();
                                    sol = sa.simulatedAnnealing(configSA.kmax, configSA.t, configSA.initSol, configSA.alpha);
                                    executionTime += System.currentTimeMillis() - startTime;
                                    value += sol.size();
                                }
                                executionTime /= 3;
                                value /= 3;

                                if( value < valueBest
                                        || ((value == valueBest) && (executionTime < timeBest))){
                                    configBest = keyConfig;
                                    valueBest = value;
                                    timeBest = executionTime;
                                    if (valueBest == optim[i]){
                                        break;
                                    }
                                }

               //                 System.out.println("\nSOLUTION SIZE = "+valueBest+"\n");
                 }

                 number = new Number(saSol, i + 1, (int) valueBest);
                 excelSheet.addCell(number);

                number = new Number(saTime, i + 1, timeBest);
                excelSheet.addCell(number);

                number = new Number(saConf, i + 1,configBest);
                excelSheet.addCell(number);


                /** Genetic Algorithm */
                timeBest = Long.MAX_VALUE;
                valueBest = Integer.MAX_VALUE;
                configBest = 0;

                for( int configAG = 0; configAG < cAG.length; configAG++){
                    int[] conf = cAG[configAG];

                    Generation gen = new Generation();

                    /** COPIED FROM MAIN CLASS */

                    startTime = System.currentTimeMillis();
                    gen.setPopulation_size(conf[popSize]);

                    //3.GENERATE THE FIRST GENERATION
                    gen =Generation.initFirstGeneration(instance.getItemsList());

                    //3.SET THE BEST BINS COUNT TO N+1 WHERE N IS THE NUMBER OF ELEMENTS
                    gen.setBestBinsCount(instance.getN() + 1);

                    //4.SET THE SELECTION SIZE
                    gen.setSelectionSize(conf[popSize] / 2);

                    //5.COUNT THE FITNESST OF THE FIRST GENERATION' S INDIVIDUALS
                    gen.countFistness();

                    //6.SET THE MUTATION SIZE
                    gen.setMutation_size(conf[mutSize]);

                    //7.SET THE MUTATION PROBA (PROBA*100)
                    gen.setMutation_proba(conf[mutProba]);

              //      System.out.println("BEST NB BINS BEFORE :\t" +gen.getBestBinsCount());
                    for(int k = 0 ; k < 50_000; k++)
                    {
                        gen.generateNextGeneration();
                        gen.addMutations();
                        gen.countFistness();
                    }

                    int valueFound = gen.getBestBinsCount();
                    executionTime = System.currentTimeMillis() - startTime;
                    if(valueFound < valueBest
                            || ((valueFound == valueBest) && (executionTime < timeBest))){
                        configBest = configAG;
                        valueBest = valueFound;
                        timeBest = executionTime;
                    }
                    number = new Number(agSol, i + 1, (int) valueBest);
                    excelSheet.addCell(number);

                    number = new Number(agTime, i + 1, timeBest);
                    excelSheet.addCell(number);

                    number = new Number(agConf, i + 1, configBest);
                    excelSheet.addCell(number);
                    /** END COPIED*/

                }
            }
            myFirstWbook.write();
        }
        catch (IOException e) {
            System.out.println("ERROR OPENING THE SHEET");
            e.printStackTrace();
        } catch (RowsExceededException e) {
            e.printStackTrace();
        } catch (WriteException e) {
            e.printStackTrace();
        }
        finally {

            if (myFirstWbook != null) {
                try {
                    myFirstWbook.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WriteException e) {
                    e.printStackTrace();
                }
            }
        }
        return 0;
    }

    public int testBFD(){
        File  folder = new File(folderPath);
        String files[] = folder.list();
        List<Bin> sol;

        for(int i = 0; i < files.length; i++) {
            System.out.println("INSTANCE : " + files[i]);
            Instance instance = new Instance(folderPath + "\\" + files[i]);
            sol = Heuristics.BFD(instance.getItemsList(), instance.getN(), instance.getC());
            System.out.println("BFD SIZE = " + sol.size());
        }
        return 0;
    }
}

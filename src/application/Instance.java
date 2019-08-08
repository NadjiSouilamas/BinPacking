package application;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Instance {
    private List<Integer> itemsList = new ArrayList<>();
    private int N;
    private int C;

    public Instance(String filePath){
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {

            String sCurrentLine;

            if ((sCurrentLine = br.readLine()) != null)
                N = Integer.parseInt(sCurrentLine);

            if ((sCurrentLine = br.readLine()) != null)
                C = Integer.parseInt(sCurrentLine);
                Bin.c = C;

            while ((sCurrentLine = br.readLine()) != null) {

                itemsList.add(Integer.parseInt(sCurrentLine));
            }

        } catch (IOException e) {
            System.out.println("ERROR WHILE OPENING THE FILE !");
        }
    }

    public int getC() {
        return C;
    }

    public int getN() {
        return N;
    }

    public List<Integer> getItemsList() {
        return itemsList;
    }
}

package application;


public class ConfigSA {
    int kmax;
    int t;
    int initSol;
    double alpha;

    public ConfigSA(int kmax, int t, int initSol, double alpha){
        this.kmax = kmax;
        this.t = t;
        this.initSol = initSol;
        this.alpha = alpha;
    }

}

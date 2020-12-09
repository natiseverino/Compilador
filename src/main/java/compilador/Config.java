package compilador;


public class Config {


    private static Config instance = null;

    private boolean mostrarL = false;
    private boolean mostrarS = false;
    private boolean mostrarTS = false;
    private boolean mostrarP = false;

    private String ultimoArg = "";
    private boolean ultimoReconocido = true;

    public Config(){}

    public static Config getInstance() {
        if (instance == null)
            instance = new Config();
        return instance;
    }

    public boolean mostrarL() {
        return mostrarL;
    }

    public boolean mostrarS() {
        return mostrarS;
    }

    public boolean mostrarTS() {
        return mostrarTS;
    }

    public boolean mostrarP() {
        return mostrarP;
    }

    public String getUltimoArg() {
        return ultimoArg;
    }

    public boolean isUltimoReconocido() {
        return ultimoReconocido;
    }

    public void setConfig(String config){
        ultimoArg = config;
        config = config.toUpperCase();
        switch (config){
            case "-L":
                mostrarL = true;
                break;
            case "-S":
                mostrarS = true;
                break;
            case "-TS":
                mostrarTS = true;
                break;
            case "-P":
                mostrarP = true;
                break;
            case "-ALL":
                mostrarL = true;
                mostrarS = true;
                mostrarTS = true;
                mostrarP = true;
                break;
            default:
                ultimoReconocido = false;
                break;
        }
    }
}


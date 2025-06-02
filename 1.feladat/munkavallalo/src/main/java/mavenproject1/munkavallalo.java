
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class munkavallalo {
     private int azonosito;
    private String keresztnev;
    private String vezeteknev;
    private LocalDate szuletesiDatum;
    private String szint;
    private String munkakor;
    private int haviFizetes;
    private boolean egyeniVallalkozo;

   
    public munkavallalo(String sor) throws IllegalArgumentException {
       
        String[] adatok = sor.split(";");

        if (adatok.length != 8) {
            throw new IllegalArgumentException("Hibás adatsor a fájlban: " + sor + " (nem 8 adatot tartalmaz)");
        }

        try {
            this.azonosito = Integer.parseInt(adatok[0]);
            this.keresztnev = adatok[1];
            this.vezeteknev = adatok[2];
           
            DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE; 
            this.szuletesiDatum = LocalDate.parse(adatok[3], formatter);
            this.szint = adatok[4];
            this.munkakor = adatok[5];
            this.haviFizetes = Integer.parseInt(adatok[6]);
          
            this.egyeniVallalkozo = "1".equals(adatok[7]);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Hiba a szám formátumú adat konvertálásakor a sorban: " + sor, e);
        } catch (java.time.format.DateTimeParseException e) {
            throw new IllegalArgumentException("Hiba a dátum formátumú adat konvertálásakor a sorban: " + sor, e);
        }
    }

    
    public int getAzonosito() {
        return azonosito;
    }

    public String getKeresztnev() {
        return keresztnev;
    }

    public String getVezeteknev() {
        return vezeteknev;
    }

    public LocalDate getSzuletesiDatum() {
        return szuletesiDatum;
    }

    public String getSzint() {
        return szint;
    }

    public String getMunkakor() {
        return munkakor;
    }

    public int getHaviFizetes() {
        return haviFizetes;
    }

    public boolean isEgyeniVallalkozo() {
        return egyeniVallalkozo;
    }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE;
        return azonosito + ";" +
               keresztnev + ";" +
               vezeteknev + ";" +
               szuletesiDatum.format(formatter) + ";" +
               szint + ";" +
               munkakor + ";" +
               haviFizetes + ";" +
               (egyeniVallalkozo ? "1" : "0");
    }

    
}

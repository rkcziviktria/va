import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class main {
          public static void main(String[] args) {
        String dataFilePath = "data.txt";
        List<munkavallalo> munkavallalokListaja = beolvasMunkavallalok(dataFilePath);

        if (munkavallalokListaja.isEmpty()) {
            System.out.println("Nincsenek feldolgozható adatok a listában, vagy hiba történt a beolvasás során.");
            return;
        }

       
        kiirDolgozokSzama(munkavallalokListaja);

       
        kiirEgyeniVallalkozokSzama(munkavallalokListaja);

       
        String keresettMunkakor = "Fejlesztő";
        kiirMunkakorSzerintiLetszam(munkavallalokListaja, keresettMunkakor);

       

        System.out.println("\nA program végzett.");
    }

    
    
    private static List<munkavallalo> beolvasMunkavallalok(String filePath) {
        List<munkavallalo> munkavallalok = new ArrayList<>();
          try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            br.readLine();
            String sor;
            while ((sor = br.readLine()) != null) {
                try {
                    munkavallalok.add(new munkavallalo(sor));
                } catch (IllegalArgumentException e) {
                    System.err.println("Figyelmeztetés: Hibás adatsor feldolgozása sikertelen - " + e.getMessage());
                }
            }
            System.out.println("Adatok sikeresen beolvasva a " + filePath + " fájlból.");
        } catch (FileNotFoundException e) {
            System.err.println("Hiba: A '" + filePath + "' fájl nem található.");
            System.err.println("Kérlek, ellenőrizd, hogy a fájl a megfelelő helyen van-e.");
            return new ArrayList<>(); 
        } catch (IOException e) {
            System.err.println("Hiba történt a fájl beolvasása során: " + e.getMessage());
            e.printStackTrace();
            return new ArrayList<>();
        }
        System.out.println("---");
        return munkavallalok;
    }

   
    private static void kiirDolgozokSzama(List<munkavallalo> munkavallalok) {
        System.out.println("2. feladat:");
        System.out.println("A rendszerben regisztrált dolgozók száma: " + munkavallalok.size());
        System.out.println("---");
    }

    
    private static void kiirEgyeniVallalkozokSzama(List<munkavallalo> munkavallalok) {
        long egyeniVallalkozokSzama = munkavallalok.stream()
                .filter(munkavallalo::isEgyeniVallalkozo)
                .count();
        System.out.println("3. feladat:");
        System.out.println("Egyéni vállalkozók száma: " + egyeniVallalkozokSzama);
        System.out.println("---");
    }

   
    private static void kiirMunkakorSzerintiLetszam(List<munkavallalo> munkavallalok, String munkakor) {
        long dolgozokSzamaAdottMunkakorben = munkavallalok.stream()
                .filter(m -> munkakor.equalsIgnoreCase(m.getMunkakor()))
                .count();
        System.out.println("4. feladat:");
        System.out.println("A '" + munkakor + "' munkakörben dolgozók száma: " + dolgozokSzamaAdottMunkakorben);
        System.out.println("---");
    }
}

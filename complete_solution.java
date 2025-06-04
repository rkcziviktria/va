import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

// Munkavállalót reprezentáló osztály
class munkavallalo {
    private int id;
    private String nev;
    private LocalDate szuletesiDatum;
    private String munkakor;
    private String szint; // Junior, Medior, Senior
    private boolean egyeniVallalkozo;
    
    // Konstruktor a fájlból beolvasott sor alapján
    public munkavallalo(String sor) {
        String[] adatok = sor.split(";");
        if (adatok.length < 6) {
            throw new IllegalArgumentException("Nem megfelelő adatsor: " + sor);
        }
        
        try {
            this.id = Integer.parseInt(adatok[0].trim());
            this.nev = adatok[1].trim();
            this.szuletesiDatum = LocalDate.parse(adatok[2].trim(), DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            this.munkakor = adatok[3].trim();
            this.szint = adatok[4].trim();
            this.egyeniVallalkozo = Boolean.parseBoolean(adatok[5].trim());
        } catch (Exception e) {
            throw new IllegalArgumentException("Hibás adatformátum: " + sor + " - " + e.getMessage());
        }
    }
    
    // Getterek
    public int getId() { return id; }
    public String getNev() { return nev; }
    public LocalDate getSzuletesiDatum() { return szuletesiDatum; }
    public String getMunkakor() { return munkakor; }
    public String getSzint() { return szint; }
    public boolean isEgyeniVallalkozo() { return egyeniVallalkozo; }
    
    // Kor számítása adott dátumhoz képest
    public int getKor(LocalDate referenceDatum) {
        return Period.between(szuletesiDatum, referenceDatum).getYears();
    }
    
    @Override
    public String toString() {
        return String.format("ID: %d, Név: %s, Születési dátum: %s, Munkakör: %s, Szint: %s, Egyéni vállalkozó: %s",
                id, nev, szuletesiDatum, munkakor, szint, egyeniVallalkozo ? "Igen" : "Nem");
    }
}

public class main {
    private static final LocalDate VIZSGA_DATUMA = LocalDate.of(2024, 6, 1); // Példa vizsga dátum
    
    public static void main(String[] args) {
        String dataFilePath = "data.txt";
        List<munkavallalo> munkavallalokListaja = beolvasMunkavallalok(dataFilePath);
        
        if (munkavallalokListaja.isEmpty()) {
            System.out.println("Nincsenek feldolgozható adatok a listában, vagy hiba történt a beolvasás során.");
            return;
        }
        
        // 3. feladat: Dolgozók száma
        kiirDolgozokSzama(munkavallalokListaja);
        
        // 4. feladat: Egyéni vállalkozók száma
        kiirEgyeniVallalkozokSzama(munkavallalokListaja);
        
        // 5. feladat: Software Engineer munkakörű dolgozók száma
        String keresettMunkakor = "Software Engineer";
        kiirMunkakorSzerintiLetszam(munkavallalokListaja, keresettMunkakor);
        
        // 6. feladat: Legfiatalabb dolgozó
        kiirLegfiatalabbDolgozo(munkavallalokListaja);
        
        // 7. feladat: Legidősebb dolgozó
        kiirLegidosebbDolgozo(munkavallalokListaja);
        
        // 8. feladat: Medior Project Manager keresése
        keresesMediorProjectManager(munkavallalokListaja);
        
        // 9. feladat: Statisztika szenioritási szint alapján
        kiirSzintStatisztika(munkavallalokListaja);
        
        System.out.println("\nA program végzett.");
    }
    
    private static List<munkavallalo> beolvasMunkavallalok(String filePath) {
        List<munkavallalo> munkavallalok = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath), StandardCharsets.UTF_8))) {
            
            br.readLine(); // Fejléc átlépése
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
        System.out.println("3. feladat:");
        System.out.println("A forrásállományban szereplő dolgozók száma: " + munkavallalok.size());
        System.out.println("---");
    }
    
    private static void kiirEgyeniVallalkozokSzama(List<munkavallalo> munkavallalok) {
        long egyeniVallalkozokSzama = munkavallalok.stream()
                .filter(munkavallalo::isEgyeniVallalkozo)
                .count();
        System.out.println("4. feladat:");
        System.out.println("Egyéni vállalkozók száma: " + egyeniVallalkozokSzama);
        System.out.println("---");
    }
   
    private static void kiirMunkakorSzerintiLetszam(List<munkavallalo> munkavallalok, String munkakor) {
        long dolgozokSzamaAdottMunkakorben = munkavallalok.stream()
                .filter(m -> munkakor.equalsIgnoreCase(m.getMunkakor()))
                .count();
        System.out.println("5. feladat:");
        System.out.println("A '" + munkakor + "' munkakörben dolgozók száma: " + dolgozokSzamaAdottMunkakorben);
        System.out.println("---");
    }
    
    private static void kiirLegfiatalabbDolgozo(List<munkavallalo> munkavallalok) {
        Optional<munkavallalo> legfiatalabb = munkavallalok.stream()
                .max(Comparator.comparing(munkavallalo::getSzuletesiDatum));
        
        System.out.println("6. feladat:");
        if (legfiatalabb.isPresent()) {
            munkavallalo dolgozo = legfiatalabb.get();
            System.out.println("Legfiatalabb dolgozó:");
            System.out.println("  Név: " + dolgozo.getNev());
            System.out.println("  Születési dátum: " + dolgozo.getSzuletesiDatum());
            System.out.println("  Kora a vizsga időpontjában: " + dolgozo.getKor(VIZSGA_DATUMA) + " év");
            System.out.println("  ID: " + dolgozo.getId());
        } else {
            System.out.println("Nincs dolgozó az adatállományban.");
        }
        System.out.println("---");
    }
    
    private static void kiirLegidosebbDolgozo(List<munkavallalo> munkavallalok) {
        Optional<munkavallalo> legidosebb = munkavallalok.stream()
                .min(Comparator.comparing(munkavallalo::getSzuletesiDatum));
        
        System.out.println("7. feladat:");
        if (legidosebb.isPresent()) {
            munkavallalo dolgozo = legidosebb.get();
            System.out.println("Legidősebb dolgozó:");
            System.out.println("  Név: " + dolgozo.getNev());
            System.out.println("  Születési dátum: " + dolgozo.getSzuletesiDatum());
            System.out.println("  Kora a vizsga időpontjában: " + dolgozo.getKor(VIZSGA_DATUMA) + " év");
            System.out.println("  ID: " + dolgozo.getId());
        } else {
            System.out.println("Nincs dolgozó az adatállományban.");
        }
        System.out.println("---");
    }
    
    private static void keresesMediorProjectManager(List<munkavallalo> munkavallalok) {
        boolean vanMediorProjectManager = munkavallalok.stream()
                .anyMatch(m -> "Project Manager".equalsIgnoreCase(m.getMunkakor()) && 
                              "Medior".equalsIgnoreCase(m.getSzint()));
        
        System.out.println("8. feladat:");
        if (vanMediorProjectManager) {
            System.out.println("Van a cégnél Medior project manager");
        } else {
            System.out.println("Nincs a cégnél Medior project manager");
        }
        System.out.println("---");
    }
    
    private static void kiirSzintStatisztika(List<munkavallalo> munkavallalok) {
        HashMap<String, Integer> szintStatisztika = new HashMap<>();
        
        for (munkavallalo dolgozo : munkavallalok) {
            String szint = dolgozo.getSzint();
            szintStatisztika.put(szint, szintStatisztika.getOrDefault(szint, 0) + 1);
        }
        
        System.out.println("9. feladat:");
        System.out.println("Dolgozók statisztikája szenioritási szint alapján:");
        System.out.println("HashMap alapértelmezett formátuma: " + szintStatisztika);
        
        // Részletes kiírás
        System.out.println("Részletes breakdown:");
        System.out.println("  Junior: " + szintStatisztika.getOrDefault("Junior", 0) + " fő");
        System.out.println("  Medior: " + szintStatisztika.getOrDefault("Medior", 0) + " fő");
        System.out.println("  Senior: " + szintStatisztika.getOrDefault("Senior", 0) + " fő");
        System.out.println("---");
    }
}
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import javax.swing.*;
 
 public class Table extends JPanel implements ActionListener{

     public Table(String table) {

         setLayout(new GridLayout());
         if(table.equals("osoba") || table.equals("rezerwacje") || table.equals("pokoj_info") || table.equals("podsumowanie_rachunku")) {      //dopasowanie rozmiaru tworzonej tabeli wynikowej do
             // ilosci danych w poszczegolnych tabelach, kosmetyka

             if (table.equals("rezerwacje") || table.equals("podsumowanie_rachunku"))
                 setPreferredSize(new Dimension(1200, 300));
             else
                 setPreferredSize(new Dimension(950, 250));
         }
         else setPreferredSize(new Dimension(600, 250));

         String[] columns = getFieldsName(table);
         ArrayList<ArrayList<String>> datas = DB.selectQuery(table, columns);  //wynik polecenia select dla podanej jako argument tablicy

         Object[][] data = new Object[datas.size()][datas.get(0).size()];
         for(int i = 0; i < datas.size(); i++)
            for(int j = 0; j < datas.get(i).size(); j++)
                data[i][j] = datas.get(i).get(j);

         if(table.equals("sprzatanie"))
             for (int i = 0; i < data.length; i++)
                data[i][1] = data[i][1].equals("t") ? "tak" : "nie";

         if(table.equals("rezerwacje"))
             for (int i = 0; i < data.length; i++)
                 data[i][data[0].length-1] = data[i][data[0].length-1].equals("t") ? "tak" : "nie";

        JTable jTable = new JTable(data, columns);
        JScrollPane scrollPane = new JScrollPane(jTable);
        jTable.setFillsViewportHeight(true);
        jTable.setEnabled(false);
        add(scrollPane);
 	}

     public static String[] getFieldsName(String table) {
         return switch (table) {
             //tables
             case "lista_gosci" -> new String[]{"id_gosc", "imie", "nazwisko", "id_osoba"};
             case "wyzywienie" -> new String[]{"id_wyzywienie", "id_osoba", "kategoria", "cena"};
             case "dodatkowa_oplata" -> new String[]{"id_dodatkowa_oplata", "id_rezerwacja", "opis", "oplata"};
             case "osoba" -> new String[]{"id_osoba", "imie", "nazwisko", "email", "numer_dowodu", "telefon"};
             case "adres" -> new String[]{"id_adres", "id_osoba", "kod_pocztowy", "miejscowosc", "ulica", "numer"};
             case "sprzatanie" -> new String[]{"id_sprzatanie", "czy_codziennie", "cena", "id_rezerwacja"};
             case "rezerwacja" -> new String[]{"id_rezerwacja", "id_osoba", "id_kategoria", "id_termin"};
             case "kategoria" -> new String[]{"id_kategoria", "cena", "opis_kategorii", "liczba_pokoi"};
             case "pokoj" -> new String[]{"numer", "id_kategoria"};
             case "termin" -> new String[]{"id_termin", "termin_poczatkowy", "termin_koncowy"};
             case "termin_pokoj" -> new String[]{"id_termin", "numer"};
             //views
             case "rezerwacje" -> new String[]{"id_rezerwacja", "imie", "nazwisko", "kod","miasto","ulica","numer_domu", "numer_pokoju","opis_kategorii","poczatek", "koniec", "wyzywienie_kategoria", "codzienne_sprzatanie"};
             case "pokoj_info" -> new String[]{"id_osoba", "id_rezerwacja", "imie", "nazwisko", "numer_dowodu", "czas_pobytu", "kwota_za_dzien", "dodatkowe_koszty"};
             case "pokoj_rachunek" -> new String[]{"id_osoba", "imie", "nazwisko", "numer_dowodu", "suma"};
             case "wyzywienie_rachunek","sprzatanie_rachunek" -> new String[]{"id_osoba", "imie", "nazwisko", "suma"};
             case "podsumowanie_rachunku" -> new String[]{"id_osoba", "imie", "nazwisko", "oplata_za_pokoj", "oplata_za_wyzywienie", "oplata_za_sprzatanie", "dodatkowe_oplaty", "suma"};
             case "kupon" -> new String[]{"id_osoba", "imie", "nazwisko", "ile_pobytow"};
             case "goscie" -> new String[]{"id_gosc", "imie", "nazwisko", "rezerwujacy", "nazwisko_rez"};
             case "ranking" -> new String[]{"id_osoba", "imie", "nazwisko", "koszt"};
             case "wolne_pokoje" -> new String[]{"id_kategoria", "numer", "opis_kategorii", "cena"};
             default -> null;
         };
      }

     @Override
     public void actionPerformed(ActionEvent e) {}
 }


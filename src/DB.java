import java.sql.*;
import java.util.ArrayList;

public class DB {
public static boolean isAdmin = false;

    public static Connection connectDB() {  //laczenie z baza danych
        try {
            Class.forName("org.postgresql.Driver");
        }
        catch (ClassNotFoundException e) {System.out.println(e.getMessage());}

        Connection c = null;
        try {
            String url = "jdbc:postgresql://castor.db.elephantsql.com:5432/";       //database url, possible local too
            String username = "gqzqdtyw";                                           //databases username
            String password = "MsUAw36uAu3XMb36KPR-25y_6_OTBeG9";                   //password
            c = DriverManager.getConnection(url, username, password);
        } catch (SQLException e) {
            System.out.println("Brak polaczenia z baza danych, wydruk logu sledzenia i koniec.");
            e.printStackTrace();
            System.exit(1);
        }
        return c;
    }


    public static ArrayList< ArrayList<String> > selectQuery(String tab, String[] columns) {         //create select query to database

        Connection c = connectDB(); //nazwiązanie połaczenia z bazą
        ArrayList< ArrayList<String> > arraySelect = new ArrayList<>(); //do przechowywania wyniku zapytania select
        if (c != null) {
            System.out.println("Polaczenie z baza danych OK ! ");   //info
            try{
                PreparedStatement pst = c.prepareStatement("SELECT * FROM hotel." + tab +" order by 1", ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
                ResultSet rs = pst.executeQuery();

                int i = 0;
                while (rs.next()) {
                    arraySelect.add(new ArrayList<>()); //2nd dimension

                    for(var x: columns)
                        arraySelect.get(i).add(rs.getString(x));

                    i++;
                }
                rs.close();
                pst.close();
                c.close();
            }
            catch(SQLException e){System.out.println("Blad podczas przetwarzania danych: "+e) ;}
        }
        else
            System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");

        return arraySelect;
    }


  public static String insertQuery(ArrayList<String> data) {

      Connection c = connectDB();

        if (c != null) {
            System.out.println("Polaczenie z baza danych OK !");
            try {
                PreparedStatement pst = prepareInsertQuery(data, c);
                int rows = pst.executeUpdate();
                System.out.println("INSERT-ilosc dodanych rekordow: "+rows) ;   //info
                String info = null;
                if(rows == 0) {
                    System.out.println(pst.getWarnings().toString());
                    info = pst.getWarnings().toString();
                }
                pst.close();
                c.close();
                return info;
             }
             catch(SQLException | NumberFormatException e){
               System.out.println("Blad "+e) ;
               return "Dane nie zostaly wprowadzone! \nOpis błędu:\t " + e;
             }
        } else{
            System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana."); //console info
            return "Brak polaczenia z baza! Sprobuj nawiazac ponownie polaczenie.";
        }
    }


    public static String updateQuery(ArrayList<String> data) {

        Connection c = connectDB();

        if (c != null) {
            System.out.println("Polaczenie z baza danych OK !");
            try {
                PreparedStatement pst = prepareUpdateQuery(data, c);
                int rows = pst.executeUpdate();
                System.out.println("UPDATE-ilosc zmienionych rekordow: "+rows) ;   //info
                String info=null;
                if(rows == 0) {
                    System.out.println(pst.getWarnings().toString());
                    info = pst.getWarnings().toString();
                }
                pst.close();
                c.close();
                return info;
            }
            catch(SQLException | NumberFormatException e){
                System.out.println("Blad "+e) ;
                return "Dane nie zostaly zmienione! \nOpis błędu:\t " + e;
            }
        } else{
            System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
            return "Brak polaczenia z baza! Sprobuj nawiazac ponownie polaczenie.";
        }
    }


    public static String[] getTypesOfDaTa(String tab) {  //tworzy tablice zawierajaca info o typach danych w poszczegolnych tabelach bazy
        return switch (tab) {
            case "lista_gosci" -> new String[]{"s", "s", "i"};
            case "wyzywienie" -> new String[]{"i", "i", "d"};
            case "dodatkowa_oplata" -> new String[]{"i", "s", "d"};
            case "osoba" -> new String[]{"s", "s", "s", "s", "i"};
            case "adres" -> new String[]{"i", "s", "s", "s", "s"};
            case "sprzatanie" -> new String[]{"b", "d", "i"};
            case "rezerwacja" -> new String[]{"i", "i", "i"};
            case "kategoria" -> new String[]{"d", "s", "i"};
            case "pokoj" -> new String[]{"i"};
            case "termin" -> new String[]{"dt", "dt"};
            case "termin_pokoj" -> new String[]{"i", "i"};
            default -> null;
        };
    }


    public static PreparedStatement prepareInsertQuery(ArrayList<String> data, Connection c) {
      PreparedStatement pst = null;
      try {
        String table = data.get(0); //table name
        String[] fields = Table.getFieldsName(table);
        StringBuilder query = new StringBuilder("INSERT INTO hotel." + table + "(");
        String values = "";
        int i = 1;
        if(table.equals("termin_pokoj")) i = 0;        //tabela ta (jako jedyna) nie ma auto increment PK
        if(table.equals("osoba") && data.get(5).equals(""))    data.add(5, String.valueOf(0)); //dla nieobowiązkowego pola numer_telefonu

        if(table.equals("sprzatanie"))
            if(((data.get(1).toUpperCase()).equals("TAK")) || ((data.get(1).toUpperCase()).equals("T")))      //w przeciwnym wypadku (nawet po podaniu glupoty) false  // equalsIgnoreCase
                data.set(1, String.valueOf(true));

          System.out.println(data);

        for(; i<fields.length; i++) {
          query.append(fields[i]);
            values += "?";
          if(i != fields.length-1) {
             query.append(", "); //tabela(?,?,?)
             values += ",";
          }
        }
        query.append(") VALUES(").append(values).append(")");
        pst = c.prepareStatement(query.toString());

        String[] types = getTypesOfDaTa(table);
        for(int k = 0; k < types.length; k++) {     //budowanie calosci wyniku zapytania
            switch (types[k]) {
                case "s" -> pst.setString(k + 1, data.get(k + 1));
                case "i" -> pst.setInt(k + 1, Integer.parseInt(data.get(k + 1)));
                case "d" -> pst.setDouble(k + 1, Double.parseDouble(data.get(k + 1)));
                case "b" -> pst.setBoolean(k + 1, Boolean.parseBoolean(data.get(k + 1)));
                case "dt" -> pst.setDate(k+1, Date.valueOf(data.get(k+1)));
            }
        }
      }
      catch(SQLException e)  {
        System.out.println("Blad podczas przetwarzania danych:"+e);
      }
      return pst;
    }


    public static PreparedStatement prepareUpdateQuery(ArrayList<String> data, Connection c) {
        PreparedStatement pst = null;
        try {
            String table = data.get(0); //table name
            String[] fields = Table.getFieldsName(table);
            StringBuilder query = new StringBuilder("UPDATE hotel." + table + " SET ");
            String values = "";
            int i = 1;
            if(table.equals("termin_pokoj")) i = 0;        //tabela ta (jako jedyna) nie ma auto increment PK
            if(table.equals("osoba") && data.get(6)==null) data.add(6, String.valueOf(0)); //dla nieobowiązkowego pola numer_telefonu

            for(; i<fields.length; i++) {
                query.append(fields[i]).append("=?");

                if(i != fields.length-1)
                    query.append(", ");
            }
            query.append(" WHERE ").append(fields[0]).append("=").append(Integer.parseInt(data.get(1))).append(";");        //index to change
            pst = c.prepareStatement(query.toString());

            String[] types = getTypesOfDaTa(table);
            for(int k =0; k < types.length; k++) {     //budowanie calosci wyniku zapytania
                switch (types[k]) {
                    case "s" -> pst.setString(k+1 , data.get(k + 2));
                    case "i" -> pst.setInt(k+1 , Integer.parseInt(data.get(k + 2)));
                    case "d" -> pst.setDouble(k+1, Double.parseDouble(data.get(k + 2)));
                    case "b" -> pst.setBoolean(k+1 , Boolean.parseBoolean(data.get(k + 2)));
                    case "dt" -> pst.setDate(k+1 , Date.valueOf(data.get(k + 2)));
                }
            }
        }
        catch(SQLException e)  {
            System.out.println("Blad podczas przetwarzania danych:"+e);
        }
        return pst;
    }


    public static boolean addWorker(String name, String surname, String login, String password) {

        Connection c = connectDB();

        if (c != null) {
            System.out.println("Polaczenie z baza danych OK !");
            try {
                PreparedStatement pst = c.prepareStatement("select * from hotel.dodaj_pracownika(?, ?, ?, ?);");        //dodaj_pracownika -> PostgreSQL function to adding workers
                pst.setString(1, name);
                pst.setString(2, surname);
                pst.setString(3, login);
                pst.setString(4, password);

                ResultSet rs = pst.executeQuery();

                int i = 0;
                while (rs.next())
                    i++;

                rs.close();
                pst.close();
                c.close();
                return i!=0;
            }
            catch(SQLException | NumberFormatException e){
                System.out.println("Blad!") ;
            }
        } else
            System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
        return false;
    }


    public static boolean toLogged(String login, String password) {

        Connection c = connectDB(); //nazwiązanie połaczenia z bazą

        if (c != null) {
            System.out.println("Polaczenie z baza danych OK ! ");   //info
            try{
                PreparedStatement pst = c.prepareStatement("select * from hotel.logowanie(?, ?);");
                pst.setString(1, login);
                pst.setString(2, password);
                ResultSet rs = pst.executeQuery();

                int i = 0;
                while (rs.next()) {
                    i++;

                    if(rs.getString("loggin").equals("admin"))      //only admin
                        DB.isAdmin = true;
                }

                System.out.println(DB.isAdmin);

                rs.close();
                pst.close();
                c.close();

                return i != 0;
            }
            catch(SQLException e){System.out.println("Blad podczas przetwarzania danych: "+e) ;}
        }
        else
            System.out.println("Brak polaczenia z baza, dalsza czesc aplikacji nie jest wykonywana.");
        return false;
    }

}


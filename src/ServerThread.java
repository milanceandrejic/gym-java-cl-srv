import javax.xml.crypto.dsig.dom.DOMValidateContext;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

public class ServerThread extends Thread {

    Socket socket = null;

    public ServerThread(Socket s) {
        socket = s;
        start();
    }

    @Override
    public void run() {
        String request = "";

        try {
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                    ,true);

            request = in.readLine();

            String odServera = "";

            String niz[] = request.split(":");
            if(niz.length>1){
                Server.textArea.appendText("Zahtev: " + request + "\n");
                int i=0;
                while (i<niz.length){
                    System.out.println(niz[i]);
                    i++;
                }

                if(niz[0].equals("PROVERA")){
                    String query = "SELECT ime, prezime, datum_isteka FROM `clanovi` WHERE `idclanovi`=" + niz[1];
                    ResultSet resultSet = DBUtil.dbExecuteQuery(query);
                    System.out.println(resultSet.toString());
                    resultSet.last();
                    int numberOfRows = resultSet.getRow();
                    resultSet.beforeFirst();

                    if(numberOfRows!=0){
                        while(resultSet.next()){
                            //odServera += resultSet.toString();
                            String ime = resultSet.getString(1);
                            String prezime = resultSet.getString(2);
                            String datumIs = resultSet.getString(3);
                            Date datumIsteka = null;
                            if(datumIs!=null)
                                datumIsteka = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(datumIs);
                            SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy.");
                            if(datumIsteka!=null) {
                                //datumIsteka = new Date();
                                Date danas = new Date();
                                System.out.println(danas);
                                System.out.println(datumIsteka.after(danas));
                                System.out.println(datumIsteka.before(danas));
                                if(datumIsteka.before(danas))
                                    odServera += "NE:" + ime + " "+ prezime + "(" + niz[1] + ")" + " - ISTEKLA CLANARINA " + dateFormat.format(datumIsteka) + "\n";
                                else
                                    odServera += "DA:" + ime + " "+ prezime + "(" + niz[1] + ")" + " - " + dateFormat.format(datumIsteka) + "\n";

                            }
                            else {
                                odServera += "NE:" + ime + " "+ prezime + "(" + niz[1] + ")" + " - ISTEKLA CLANARINA\n";
                            }

                        }
                    }
                    else {
                        odServera += "NE:Ne postoji korisnik sa unetim članskim brojem";
                    }
                    resultSet.close();
                }

                if(niz[0].equals("PRODUZI")){
                    String pattern = "YYYY-MM-dd";
                    SimpleDateFormat dateFormat = new SimpleDateFormat(pattern);
                    Date datum;
                    datum = Date.from((LocalDate.now()).atStartOfDay(ZoneId.systemDefault()).plusMonths(1).toInstant());
                    String query = "UPDATE `clanovi` SET `datum_isteka`= '" + dateFormat.format(datum) +"' ,`datum_izmene`= CURRENT_TIMESTAMP WHERE `idclanovi` = " + niz[1];

                    DBUtil.dbExecuteUpdate(query);

                    query = "SELECT ime, prezime, datum_isteka FROM `clanovi` WHERE `idclanovi`=" + niz[1];
                    ResultSet resultSet = DBUtil.dbExecuteQuery(query);
                    System.out.println(resultSet.toString());
                    resultSet.last();
                    int numberOfRows = resultSet.getRow();
                    resultSet.beforeFirst();

                    if(numberOfRows!=0){
                        while(resultSet.next()){
                            //odServera += resultSet.toString();
                            String ime = resultSet.getString(1);
                            String prezime = resultSet.getString(2);
                            String datumIs = resultSet.getString(3);
                            Date datumIsteka = null;
                            if(datumIs!=null)
                                datumIsteka = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm").parse(datumIs);
                            Date jucerasnji = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).minusDays(1).toInstant());
                            if(datumIsteka==null || datumIsteka.before(jucerasnji)) {
                                //datumIsteka = new Date();

                                odServera = "NE:" + ime + " "+ prezime + "(" + niz[1] + ")" + " - NEUSPESNO PRODUZAVANJE CLANARINE\n";
                            }
                            else {
                                SimpleDateFormat df = new SimpleDateFormat("dd.MM.YYYY.");
                                odServera = "DA:" + ime + " "+ prezime + "(" + niz[1] + ")" + " je produžena članarina do " + df.format(datumIsteka) + "\n";
                            }

                        }
                    }
                    else {
                        odServera = "NE:Ne postoji korisnik sa unetim članskim brojem!";
                    }
                    resultSet.close();
                }

                if(niz[0].equals("DODAJ")){
                    String values = "";

                    String _ime = niz[1];
                    String _prezime = niz[2];
                    String _mail = niz[3];
                    String _telefon = niz[4];
                    String _jmbg = niz[5];
                    String _datumRodjenja = niz[6];

                    values = "'" + _ime + "', " +
                            "'" + _prezime + "', " +
                            "'" + _mail + "', " +
                            "'" + _jmbg + "', " +
                            "'" + _telefon + "', " +
                            "'" + _datumRodjenja + "', CURRENT_TIMESTAMP";

                    String query = "INSERT INTO `clanovi`(`ime`, `prezime`, `mail`, `jmbg`, `telefon`, `datum_rodjenja`, `datum_izmene`) VALUES (" + values + ")";

                    DBUtil.dbExecuteUpdate(query);

                    query = "SELECT idclanovi, ime, prezime FROM `clanovi` ORDER BY idclanovi DESC LIMIT 1";
                    ResultSet resultSet = DBUtil.dbExecuteQuery(query);
                    System.out.println(resultSet.toString());
                    resultSet.last();
                    int numberOfRows = resultSet.getRow();
                    resultSet.beforeFirst();

                    if(numberOfRows!=0){
                        while(resultSet.next()){
                            //odServera += resultSet.toString();
                            String id = resultSet.getString(1);
                            String ime = resultSet.getString(2);
                            String prezime = resultSet.getString(3);

                            odServera = "DA:Uspesno ste dodali " + ime + " " + prezime + "(" + id + ")";

                        }
                    }
                    else {
                        odServera = "NE:Neuspesno dodavanje korisnika!";
                    }
                    resultSet.close();
                }
            }

            out.println(odServera);
            Server.textArea.appendText("ODGOVOR:  " + odServera + "\n");

            in.close();
            out.close();
            socket.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

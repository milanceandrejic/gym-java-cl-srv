import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.Border;
import javafx.scene.layout.GridPane;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;
import java.time.LocalDate;


public class Client extends Application {
    Scene scene;
    TextField brojKartice;
    Button proveri;
    Button produzi;
    Label odgovor;
    Button dodajClana;

    TextField ime;
    TextField prezime;
    TextField mail;
    TextField telefon;
    TextField jmbg;
    DatePicker datumRodjenja;
    Button dodaj;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        stage.setTitle("GYM Application");
        stage.setWidth(600);
        stage.setHeight(800);

        GridPane root = new GridPane();
        root.setPadding(new Insets(10, 10, 10, 10));
        root.setVgap(10);
        root.setHgap(5);

        brojKartice = new TextField();
        proveri = new Button("Proveri članarinu");
        produzi = new Button("Produži članarinu");
        dodajClana = new Button("Dodaj člana");
        odgovor = new Label("Pozdrav!");

        root.setAlignment(Pos.CENTER);
        root.setPrefWidth(stage.getWidth());

        root.add(new Label("Članski broj"), 0, 0);
        root.add(brojKartice, 1, 0);
        root.add(proveri, 0, 1);
        root.add(produzi, 1, 1);
        root.add(dodajClana, 0, 4);
        odgovor.setMinWidth(200);
        odgovor.setMinHeight(40);
        odgovor.setPadding(new Insets(10, 10, 10, 10));
        root.add(odgovor, 0, 2);

        scene = new Scene(root);

        proveri.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                int brKartice;
                try {
                    brKartice = Integer.parseInt(brojKartice.getText());
                } catch (Exception e) {
                    brKartice = -1;
                }

                if (brKartice != -1) {
                    odgovor.setText("Kliknuto na dugme");
                    odgovor.setBackground(Background.EMPTY);

                    try {
                        Socket socket = new Socket("localhost", 9000);
                        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                                , true);

                        String zaServer = "PROVERA:" + brKartice;

                        out.println(zaServer);
                        String odg = in.readLine();

                        if (odg.split(":")[0].equals("NE"))
                            odgovor.setBackground(Background.fill(Color.rgb(255, 110, 110, 0.55)));
                        if (odg.split(":")[0].equals("DA"))
                            odgovor.setBackground(Background.fill(Color.rgb(150, 255, 150, 0.55)));

                        odgovor.setText(odg.split(":")[1]);

                        in.close();
                        out.close();
                        socket.close();

                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }

                } else {
                    odgovor.setText("Unesite broj clanske kartice");
                    odgovor.setBackground(Background.fill(Color.rgb(255, 110, 110, 0.55)));
                    //odgovor.
                }

            }
        });

        produzi.setOnAction(
                new EventHandler<ActionEvent>() {
                    public void handle(ActionEvent event) {
                        int brKartice;
                        try {
                            brKartice = Integer.parseInt(brojKartice.getText());
                        } catch (Exception e) {
                            brKartice = -1;
                        }

                        if (brKartice != -1) {
                            odgovor.setText("Kliknuto na dugme");
                            odgovor.setBackground(Background.EMPTY);

                            try {
                                Socket socket = new Socket("localhost", 9000);
                                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                                        , true);

                                String zaServer = "PRODUZI:" + brKartice;

                                out.println(zaServer);
                                String odg = in.readLine();

                                if (odg.split(":")[0].equals("NE"))
                                    odgovor.setBackground(Background.fill(Color.rgb(255, 110, 110, 0.55)));
                                if (odg.split(":")[0].equals("DA"))
                                    odgovor.setBackground(Background.fill(Color.rgb(150, 255, 150, 0.55)));

                                odgovor.setText(odg.split(":")[1]);

                                in.close();
                                out.close();
                                socket.close();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }

                        } else {
                            odgovor.setText("Unesite broj clanske kartice");
                            odgovor.setBackground(Background.fill(Color.rgb(255, 110, 110, 0.55)));
                            //odgovor.
                        }

                    }
                }
        );

        GridPane gp = new GridPane();
        Scene dodajKorisnika = new Scene(gp);

        ime = new TextField();
        prezime = new TextField();
        mail = new TextField();
        telefon = new TextField();
        jmbg = new TextField();
        datumRodjenja = new DatePicker();
        dodaj = new Button("Dodaj Clana");
        Label poruka = new Label("");

        Button vratiSe = new Button("Vrati se nazad!");
        //Adding elements on gridPanel gp
        {
        gp.setPadding(new Insets(10, 10, 10, 10));
        gp.setVgap(10);
        gp.setHgap(5);
        gp.setAlignment(Pos.CENTER);
        gp.add(new Label("Ime"), 0, 0);
        gp.add(ime, 1, 0);
        gp.add(new Label("Prezime"), 0, 1);
        gp.add(prezime, 1, 1);
        gp.add(new Label("E-mail"), 0, 2);
        gp.add(mail, 1, 2);
        gp.add(new Label("Telefon"), 0, 3);
        gp.add(telefon, 1, 3);
        gp.add(new Label("JMBG"), 0, 4);
        gp.add(jmbg, 1, 4);
        gp.add(new Label("Datum rodjenja"), 0, 5);
        gp.add(datumRodjenja, 1, 5);
        gp.add(vratiSe, 0, 6);
        gp.add(dodaj, 1, 6);
        gp.add(poruka, 0, 7);
    }

        dodajClana.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        ime.clear();
                        prezime.clear();
                        mail.clear();
                        telefon.clear();
                        jmbg.clear();
                        poruka.setBackground(Background.EMPTY);
                        poruka.setText("");
                        datumRodjenja.setValue(null);

                        stage.setScene(dodajKorisnika);
                    }
                }
        );

        vratiSe.setOnAction(
                new EventHandler<ActionEvent>(){
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        brojKartice.clear();
                        odgovor.setText("Pozdrav!");
                        odgovor.setBackground(Background.EMPTY);
                        stage.setScene(scene);
                    }
                }
        );

        dodaj.setOnAction(
                new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent actionEvent) {
                        if(proveraPodataka())
                        {
                            try {
                                Socket socket = new Socket("localhost", 9000);
                                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                                PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))
                                        , true);

                                String _ime = ime.getText();
                                String _prezime = prezime.getText();
                                String _mail = mail.getText();
                                String _telefon = telefon.getText();
                                String _jmbg = jmbg.getText();
                                String _datumRodjenja = datumRodjenja.getValue().toString();

                                String zaServer = "DODAJ:"
                                        + _ime + ":"
                                        + _prezime + ":"
                                        + _mail + ":"
                                        + _telefon + ":"
                                        + _jmbg + ":"
                                        + _datumRodjenja;

                                out.println(zaServer);
                                String odg = in.readLine();

                                if (odg.split(":")[0].equals("NE"))
                                    odgovor.setBackground(Background.fill(Color.rgb(255, 110, 110, 0.55)));
                                if (odg.split(":")[0].equals("DA"))
                                    odgovor.setBackground(Background.fill(Color.rgb(150, 255, 150, 0.55)));

                                odgovor.setText(odg.split(":")[1]);

                                in.close();
                                out.close();
                                socket.close();

                            } catch (IOException ex) {
                                ex.printStackTrace();
                            }
                            brojKartice.setText("");
                            stage.setScene(scene);
                        }
                        else {
                            poruka.setPadding(new Insets(10,10,10,10));
                            poruka.setBackground(Background.fill(Color.rgb(255,110,110,0.55)));
                            poruka.setText("Unesite sve podatke validno!");
                        }
                        System.out.println(ime.getText());
                        System.out.println(prezime.getText());
                        System.out.println(mail.getText());
                        System.out.println(telefon.getText());
                        System.out.println(jmbg.getText());
                        System.out.println(datumRodjenja.getValue());
                        System.out.println(proveraPodataka());
                    }
                }
        );

        stage.setScene(scene);

        stage.show();
    }

    private boolean proveraPodataka(){

        if(ime.getText().trim().equals("")) {
            System.out.println("puca na ime");
            return false;
        }
        if(prezime.getText().trim().equals("")) {
            System.out.println("puca na prezime");
            return false;
        }
        if(mail.getText().trim().equals("") ||
                !mail.getText().contains("@") ||
                !mail.getText().split("@")[1].contains(".")
                ) {
            System.out.println("puca na mail");
            return false;
        }
        if(jmbg.getText().trim().equals("") || jmbg.getText().length()!=13) {
            System.out.println("puca na jmbg");
            return false;
        }
        for (char k:jmbg.getText().toCharArray()
             ) {
            if(!Character.isDigit(k)) {
                System.out.println("puca na cifre jmbg");
                return false;
            }
        }

        if(telefon.getText().trim()=="") {
            System.out.println("puca na telefon");
            return false;
        }
        for (char k:telefon.getText().toCharArray()
        ) {
            if(k!='+' && !Character.isDigit(k)) {
                System.out.println("puca na cifre telefona");
                return false;
            }
        }
        if(datumRodjenja.getValue()==null || datumRodjenja.getValue().isAfter(LocalDate.now().minusYears(10))) {
            System.out.println("puca na datum rodjenja");
            return false;
        }

        return true;
    }
}

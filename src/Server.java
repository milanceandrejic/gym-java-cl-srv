import javafx.application.Application;
import javafx.event.Event;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.SQLException;

public class Server extends Application {

    public Scene scene;
    public static TextArea textArea = new TextArea("Server je pokrenut!\n");

    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        //DBUtil.dbExecuteUpdate("INSERT INTO `clanovi`(`ime`, `prezime`, `mail`, `jmbg`, `telefon`, `datum_rodjenja`, `datum_izmene`) VALUES ('Milan','Gavrilov','milannrt2320@gs.viser.edu.rs','0907001860025','0658824295','2001-07-09',CURRENT_TIMESTAMP)");
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        scene = new Scene(textArea);
        stage.setTitle("GYM Server");
        stage.setWidth(400);
        stage.setHeight(800);
        stage.setOnCloseRequest(event -> {
            System.exit(1);
        });
        stage.setScene(scene);

        stage.show();


        new Thread(()->{
            ServerSocket ss = null;
            try {
                ss = new ServerSocket(9000);
            } catch (IOException e) {
                e.printStackTrace();
            }

            while (true) {
                try {
                    Socket s = ss.accept();
                    new ServerThread(s);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}

package sample;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.event.EventType;
import javafx.fxml.FXMLLoader;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.ComboBoxListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;


import javax.swing.*;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;

import static javafx.scene.paint.Color.*;

/* TODO: VECTOR DE CONEXIUNII...SCHIMBA */

public class Main extends Application {


    //elemente
    private Button readyButton = new Button();
    private Button spectateButton = new Button();
    private Pane mPane = new Pane();
    private TextArea mTextArea = new TextArea();
    private TextField mTypo = new TextField();
    private Label readyLabel = new Label();
    private Label spectateLabel = new Label();
    //private Button[] playCards = new Button[45];

    ObjectOutputStream output;
    ObjectInputStream input;
    String message = "";

    ArrayList<String> items = new ArrayList<>();
    ArrayList<Pane> labelsOfCards = new ArrayList<>();


    private Group root = new Group();

    TextArea mListLobby = new TextArea();
    TextArea mListViewReady = new TextArea();

    private String nume = "TEST";
    private PlayersState playersState = new PlayersState();

    private final String serverIP = "188.173.49.219";
    public ArrayList<Socket> serverSockets;
    private Socket connection;


    @Override
    public void start(Stage primaryStage) throws Exception{


        //Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Mac&Bear");
        primaryStage.setScene(new Scene(root, 1000, 500));
        primaryStage.show();
        primaryStage.setResizable(false);
        primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override public void handle(WindowEvent t) {
                try {
                    sendMessage("/left");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.exit(0);
            }
        });
        setup();
    }

    private void setup() throws IOException {



        Controller controller = new Controller();

        URL main = Main.class.getResource("Main.class");
        if (!"file".equalsIgnoreCase(main.getProtocol()))
            throw new IllegalStateException("Main class is not stored in a file.");
        String path = new File(main.getPath()).toString();

        root.getChildren().add(mPane);
        mPane.setPrefWidth(1100);
        mPane.setPrefHeight(600);
        mPane.setStyle("-fx-background-color: #" + "A90637");

        mPane.getChildren().add(mTextArea);
        mTextArea.setEditable(false);
        mTextArea.setLayoutX(550);
        mTextArea.setLayoutY(10);
        mTextArea.setPrefHeight(450);
        mTextArea.setPrefWidth(250);
        mTextArea.setStyle("-fx-background-color: #" + "A90637");

        mPane.getChildren().add(mTypo);
        mTypo.setLayoutX(550);
        mTypo.setLayoutY(460);
        mTypo.setPrefHeight(40);
        mTypo.setPrefWidth(250);
        mTypo.setPromptText("Type a message...");
        mTypo.setOnKeyPressed((KeyEvent keyEvent) -> {
            if(keyEvent.getCode() == KeyCode.ENTER)  {
                String text = mTypo.getText();
                if(text != null && !text.trim().isEmpty())
                    try {
                        sendMessage(text);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                mTypo.setText("");
            }
        });
        //mTypo.setStyle("-fx-background-color: #" + "A90637");



        mPane.getChildren().add(mListLobby);
        mListLobby.setLayoutX(810);
        mListLobby.setLayoutY(50);
        mListLobby.setPrefHeight(150);
        mListLobby.setPrefWidth(180);
        mListLobby.setEditable(false);
        mListLobby.setText("");

        mPane.getChildren().add(mListViewReady);
        mListViewReady.setLayoutX(810);
        mListViewReady.setLayoutY(300);
        mListViewReady.setPrefHeight(150);
        mListViewReady.setPrefWidth(180);
        mListViewReady.setEditable(false);
        mListViewReady.setText("");


        //Image img = new Image(getClass().getResource("images/down.png").toExternalForm(), 20, 20, false, true);
        readyButton = new Button(
                //null, new ImageView(img)
        );
        mPane.getChildren().add(readyButton);
        readyButton.setLayoutX(845);
        readyButton.setLayoutY(225);
        readyButton.setPrefWidth(30);
        readyButton.setPrefHeight(30);
        //TODO modifica in true
        readyButton.setDisable(false);
        readyButton.setOnAction((ActionEvent event) ->
                {
                    addCard(1);
//                    spectateButton.setDisable(false);
//                    playersState.removeFrom(nume,playersState.getLobbyPlayers());
//                    playersState.addTo(nume, playersState.getReadyPlayer());
//                    try {
//                        sendMessage(playersState);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
                }
        );


        //Image img2 = new Image(getClass().getResource("images/up.png").toExternalForm(), 20, 20, false, true);

        spectateButton = new Button(

                // new ImageView(img2)
        );
        mPane.getChildren().add(spectateButton);
        spectateButton.setLayoutX(915);
        spectateButton.setLayoutY(225);
        spectateButton.setPrefWidth(30);
        spectateButton.setPrefHeight(30);
        spectateButton.setDisable(true);
        spectateButton.setOnAction((ActionEvent event) ->
                {

                    spectateButton.setDisable(true);
                    playersState.addTo(nume,playersState.getLobbyPlayers());
                    playersState.removeFrom(nume, playersState.getReadyPlayer());
                    try {
                        sendMessage(playersState);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
        );


        mPane.getChildren().add(readyLabel);
        readyLabel.setLayoutX(810);
        readyLabel.setLayoutY(280);
        readyLabel.setPrefHeight(20);
        readyLabel.setPrefWidth(290);
        readyLabel.setText("Ready Players: (" + String.valueOf(items.size()) + "/8)");
        readyLabel.setTextFill(web("#ffffff"));

        mPane.getChildren().add(spectateLabel);
        spectateLabel.setLayoutX(810);
        spectateLabel.setLayoutY(30);
        spectateLabel.setPrefHeight(20);
        spectateLabel.setPrefWidth(290);
        spectateLabel.setText("Players in Lobby: (" + String.valueOf(items.size()) + "/100)");
        spectateLabel.setTextFill(web("#ffffff"));



        nume = JOptionPane.showInputDialog("Enter your name. Leave empty or close for a random name");
        if(nume == null || nume.equals("") || nume.trim().isEmpty())
            nume = "user" + String.valueOf(new Random().nextInt(999)+1);
        else
            nume = nume + String.valueOf(new Random().nextInt(999)+1);



        //new Thread(new startClient(2000)).start();
    }



    private void addCard(int index){
        for(int i=1;i<=index;i++) {
            //TODO initiaza cartea cu toate datele necesare si trimite serverului

            Pane pane = new Pane();
            mPane.getChildren().add(pane);
            labelsOfCards.add(pane);
            //pane.setStyle("-fx-background-color: #fff");

            ImageView bk = new ImageView(getClass().getResource("Test_cards/" + String.valueOf(new Random().nextInt(14)+1) + ".jpg").toExternalForm());
            pane.getChildren().add(bk);
            pane.setLayoutY(350);
            pane.setPrefHeight(190);
            pane.setPrefWidth(90);
            pane.addEventFilter(MouseEvent.MOUSE_ENTERED, e -> pane.setLayoutY(pane.getLayoutY()-40));
            pane.addEventFilter(MouseEvent.MOUSE_EXITED, e -> pane.setLayoutY(pane.getLayoutY()+40));

        }
        resetPositionOfCards(labelsOfCards.size());
    }


    private void resetPositionOfCards(int index){
        if(index>6)
            for(Pane pane: labelsOfCards){
                pane.setLayoutX((450/(index-1))*(labelsOfCards.indexOf(pane))+5);
            }
        else
            for(Pane pane: labelsOfCards){
                pane.setLayoutX(90*labelsOfCards.indexOf(pane)+5);
            }

    }



    public class startClient implements Runnable{


        private int mPortNumber;

        public startClient(int portNumber){
            mPortNumber = portNumber;
        }

        @Override
        public void run() {
            try {
                connectToServer();
                setupStreams();
                whileChatting();
            } catch (EOFException var6) {
                showMessage("\n Client terminated the connection");
            } catch (IOException var7) {

            } finally {
                closeConnection();
            }
        }

        private void connectToServer() throws IOException {
            showMessage("Attempting connection... \n");
            connection = new Socket(InetAddress.getByName(serverIP), 2000);
            showMessage("Connection Established! Connected to: " + connection.getInetAddress().getHostName());

        }

        private void setupStreams() throws IOException {
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            showMessage("\n The streams are now set up! \n");
        }

        private void whileChatting() throws IOException {
            List<String> readyList = new ArrayList<>();
            readyList.add("ceva");

            sendMessage("&nume"+nume);
            do {
                try {
                    Object object = input.readObject();

                    if(object instanceof String)
                        showMessage("\n" + (String) object);

                    else if(object instanceof PlayersState){
                        playersState = (PlayersState) object;
                        resetLists(playersState.getLobbyPlayers(),playersState.getReadyPlayer());
                    }
                    else if(object instanceof Map){
                        if(((Map) object).containsKey("lobby")){
                            resetLists((List<String>) ((Map) object).get("lobby"), readyList);
                        }
                    }

                } catch (ClassNotFoundException var2) {
                    showMessage("Unknown data received!");
                }
            } while(!message.equals("SERVER - END"));
        }


        private void closeConnection() {
            showMessage("\n Closing the connection!");

            try {
                output.close();
                input.close();
                connection.close();
            } catch (IOException var2) {
                var2.printStackTrace();
            }
        }


    }


    private void showMessage(String s) {
        mTextArea.appendText(s);
    }

    public void sendMessage(String message) throws IOException {
        output.writeObject(message);
        output.flush();
    }

    public void sendMessage(PlayersState playersState) throws IOException {
        output.reset();
        output.writeObject(playersState);
        output.flush();
    }

    public void resetLists(List<String> lobbyList, List<String> readyList){
        Platform.runLater(() -> {
            spectateLabel.setText("Players in Lobby: ("+String.valueOf(playersState.getLobbyPlayers().size())+"/100)");
            readyLabel.setText(("Ready Players: ("+String.valueOf(playersState.getReadyPlayer().size())+"/8)"));
            if(playersState.getReadyPlayer().size() < 8 && playersState.getLobbyPlayers().contains(nume)){
                readyButton.setDisable(false);
            }else if(playersState.getReadyPlayer().size() == 8 || playersState.getReadyPlayer().contains(nume)){
                readyButton.setDisable(true);
            }

        });
        mListLobby.clear();
        mListViewReady.clear();

        for(String person: lobbyList){
            mListLobby.appendText(person + "\n");
        }

        for(String person: readyList){
            mListViewReady.appendText(person + "\n");
        }
    }


    public static void main(String[] args) {
        launch(args);
    }


}

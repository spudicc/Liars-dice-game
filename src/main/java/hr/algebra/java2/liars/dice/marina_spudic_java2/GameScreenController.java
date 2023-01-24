package hr.algebra.java2.liars.dice.marina_spudic_java2;

import hr.algebra.java2.liars.dice.marina_spudic_java2.model.*;
import hr.algebra.java2.liars.dice.marina_spudic_java2.rmi.RemoteService;
import hr.algebra.java2.liars.dice.marina_spudic_java2.utils.AlertUtils;
import hr.algebra.java2.liars.dice.marina_spudic_java2.utils.DocumentationUtils;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;

public class GameScreenController {
    Random random = new Random();
    @FXML
    private Button btnPlayerBidHigher;
    @FXML
    private Button btnCallBluff;
    @FXML
    private Button btnSendMessage;
    @FXML
    private Button btnShowMoves;
    @FXML
    private Label lbOpponentsMove;
    @FXML
    private ImageView playerDiceImageOne;
    @FXML
    private ImageView playerDiceImageTwo;
    @FXML
    private ImageView playerDiceImageThree;
    @FXML
    private ImageView playerDiceImageFour;
    @FXML
    private ImageView playerDiceImageFive;
    @FXML
    private AnchorPane apPlayer;
    @FXML
    private AnchorPane apGameMoves;

    @FXML
    private ChoiceBox playerNumberChoiceBox;
    @FXML
    private ChoiceBox playerDiceChoiceBox;
    @FXML
    private TextArea taChatArea;
    @FXML
    private TextField tfChatMessage;

    private boolean playerOneTurn;
    private Map<Integer, Integer> numberOfDices;
    private ArrayList<Integer> playerDices;
    private ImageView[] playerDiceIW;
    List<ImageView> allDices;

    private ArrayList<Node> apNodes;
    private int lastNumberBid;
    private int lastDiceBid;
    private RemoteService stub = null;
    private static final int CHOICES = 10;
    private static final int DICES = 6;
    private static final String xmlPath = "D:\\java2xmlfile.xml";
    public static Client client;

    private GameMetaData gameMetaData;

    public void initialize() throws NotBoundException, RemoteException {
        client.listenForGameReady(new TransferData(btnCallBluff, btnPlayerBidHigher, lbOpponentsMove, playerNumberChoiceBox));
        gameMetaData = client.getGameMetaData();
        playerDices = new ArrayList<>();
        playerDiceIW = new ImageView[DICES];

        allDices = new ArrayList<>();
        initNodes();
        numberOfDices = new HashMap<>();

        numberOfDices.put(1, 0);
        numberOfDices.put(2, 0);
        numberOfDices.put(3, 0);
        numberOfDices.put(4, 0);
        numberOfDices.put(5, 0);
        numberOfDices.put(6, 0);

        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i <= DICES; i++) {
            numbers.add(i);
        }
        List<Integer> numberOfChoices = new ArrayList<>();
        for (int i = 1; i <= CHOICES; i++) {
            numberOfChoices.add(i);
        }
        playerDiceChoiceBox.setItems(FXCollections.observableList(numbers));
        playerNumberChoiceBox.setItems(FXCollections.observableList(numberOfChoices));

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        new Thread(() -> {
            try {
                refreshChat();
            } catch (InterruptedException | RemoteException e) {
                throw new RuntimeException(e);
            }
        }).start();
        stub = (RemoteService) registry.lookup(RemoteService.REMOTE_OBJECT_NAME);

        rollDice();
    }

    private void initNodes() {
        apNodes = new ArrayList<>();
        apNodes.addAll(apPlayer.getChildren());
        for (int i = 0; i < apNodes.size(); i++) {
            if (apNodes.get(i) instanceof ImageView)
            {
                playerDiceIW[i] = (ImageView) apNodes.get(i);
                allDices.add((ImageView) apNodes.get(i));
            }
        }
    }
    public void rollDice() {
        for (ImageView dice : allDices) {
            int randomDice = random.nextInt(6) + 1;
            File file = new File("src/main/resources/assets/dice" + randomDice + ".png");
            dice.setImage(new Image(file.toURI().toString()));
            numberOfDices.put(randomDice, numberOfDices.get(randomDice) + 1);
            playerDices.add(randomDice);
        }
    }

    private void setStage() {
        apGameMoves.setVisible(true);

        int lastNumberBid = client.getGameMetaData().getNumberBids().get(client.getGameMetaData().getNumberBids().size() - 1);
        int lastDiceBid = client.getGameMetaData().getDiceBids().get(client.getGameMetaData().getDiceBids().size() - 1);

        lbOpponentsMove.setText(lastNumberBid + " dices of number " + lastDiceBid);
    }

    private void setChoiceBoxValues() {
        playerNumberChoiceBox.getItems().clear();

        int lastNumberBid = client.getGameMetaData().getNumberBids().get(client.getGameMetaData().getNumberBids().size() - 1);
        List<Integer> availableChoices = new ArrayList<>();
        for (int i = lastNumberBid; i < CHOICES; i++){
            availableChoices.add(i);
        }
        playerNumberChoiceBox.setItems(FXCollections.observableList(availableChoices));
    }

    public void onBidHigher(){
        if (playerNumberChoiceBox.getValue() == null || playerDiceChoiceBox.getValue() == null) {
            System.out.println("U gotta pick");
            return;
        }
        lastNumberBid = (int) playerNumberChoiceBox.getValue();
        lastDiceBid = (int) playerDiceChoiceBox.getValue();

        try {
            client.getGameMetaData().setNumberOfDices(numberOfDices);
            client.sendTurn(lastNumberBid, lastDiceBid);
            disableUI();
            System.out.println(client.getGameMetaData().getNumberOfDices());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void disableStaticUI(TransferData transferData) {
        Platform.runLater(() -> {
            transferData.getBtnBidHigher().setDisable(true);
            transferData.getBtnBidHigher().setText("Waiting for another player...");
            transferData.getLbOpponentsMove().setText(" ");
            transferData.getBtnCallBluff().setDisable(true);
        });
    }

    private void disableUI(){
        btnPlayerBidHigher.setDisable(true);
        btnPlayerBidHigher.setText("Waiting for another player...");
        lbOpponentsMove.setText(" ");
        btnCallBluff.setDisable(true);
        playerDiceChoiceBox.setValue(" ");
    }

    public static void enableUI(TransferData transferData){
        Platform.runLater(() -> {
            transferData.getBtnBidHigher().setDisable(false);
            transferData.getBtnBidHigher().setText("Bid higher");
            if (client.getGameMetaData().getNumberBids().size() != 0){
                int lastNumberBid = client.getGameMetaData().getNumberBids().get(client.getGameMetaData().getNumberBids().size() - 1);
                int lastDiceBid = client.getGameMetaData().getDiceBids().get(client.getGameMetaData().getDiceBids().size() - 1);
                transferData.getLbOpponentsMove().setText(lastNumberBid + " dices of number " + lastDiceBid);
                List<Integer> availableChoices = new ArrayList<>();
                for (int i = lastNumberBid++; i < CHOICES; i++){
                    availableChoices.add(i);
                }
                transferData.getCbLastNumberBid().setItems(FXCollections.observableList(availableChoices));
            }
            else if (client.getGameMetaData().getNumberBids().size() - 1 == 0)
            {
                transferData.getLbOpponentsMove().setText(" ");
                List<Integer> availableChoices = new ArrayList<>();
                for (int i = 1; i < CHOICES; i++){
                    availableChoices.add(i);
                }
                transferData.getCbLastNumberBid().setItems(FXCollections.observableList(availableChoices));

            }
            transferData.getBtnCallBluff().setDisable(false);
        });
    }

    public void onCallBluff(){
        checkWinner();
    }

    private void checkWinner() {
        if (gameMetaData.getNumberOfDices().get(lastDiceBid) < lastNumberBid)
        {
            AlertUtils.displayVictoryDialog(HelloController.getPlayerName());
        }
        else
        {
            AlertUtils.displayVictoryDialog(client.getGameMetaData().isPlayerOneTurn() ? client.getGameMetaData().getPlayerTwoData().getPlayerName() : client.getGameMetaData().getPlayerOneData().getPlayerName());
        }
        if (client.getGameMetaData().isPlayerOneTurn()) {
            client.getGameMetaData().getPlayerOneData().recordNewVictory();
        } else {
            client.getGameMetaData().getPlayerTwoData().recordNewVictory();
        }
        btnShowMoves.setDisable(false);
    }

    public void startGame() throws IOException {
        //client = new Client(new PlayerData(client.getGameMetaData().isPlayerOneTurn() ? client.getGameMetaData().getPlayerOneData().getPlayerName() : client.getGameMetaData().getPlayerTwoData().getPlayerName()));

        /*FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        HelloApplication.getMainStage().setTitle("Liar's dice");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();*/
        //clearScreen();
        rollDice();
        lastNumberBid = 0;
        lastDiceBid = 0;
        lbOpponentsMove.setText(" ");
    }

    private void clearScreen() {
        for (ImageView dice: playerDiceIW) {
            File file = new File("src/main/resources/assets/dice1.png");
            dice.setImage(new Image(file.toURI().toString()));
        }
    }

    public void showMoves() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameMoves.fxml"));
        GameMovesController gameMovesController = new GameMovesController();
        gameMovesController.setNumberMoves(client.getGameMetaData().getNumberBids());
        gameMovesController.setDiceMoves(client.getGameMetaData().getDiceBids());
        fxmlLoader.setController(gameMovesController);
        Scene scene = new Scene(fxmlLoader.load(), 600, 650);
        HelloApplication.getMainStage().setTitle("Game moves");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void goBack() throws IOException {
        client.getSocket().close();
        client.getOos().close();
        client.getOis().close();
        client = new Client(new PlayerData(HelloController.getPlayerName()));
        Stage stage = HelloApplication.getMainStage();
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("hello-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 700, 600);
        stage.setTitle("Liar's dice");
        stage.setScene(scene);
        stage.show();
    }

    public void saveGame() throws IOException {
        List<SerializableDice> playerSerializedDices = new ArrayList<>();

        for (Integer dice : playerDices) {
            playerSerializedDices.add(new SerializableDice(dice.toString()));
        }

        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream("savedGame.ser"))) {
            serializator.writeObject(playerSerializedDices);
            serializator.writeObject(lastNumberBid);
            serializator.writeObject(lastDiceBid);
        };
    }

    public void loadGame() throws IOException, ClassNotFoundException {
            try (ObjectInputStream deserializator = new ObjectInputStream(new FileInputStream("savedGame.ser"))) {
                ArrayList<SerializableDice> playerDeserializedDices = (ArrayList<SerializableDice>) deserializator.readObject();
                lastNumberBid = (int) deserializator.readObject();
                lastDiceBid = (int) deserializator.readObject();

                for (int i = 0; i < playerDeserializedDices.size(); i++) {
                    File file = new File("src/main/resources/assets/dice" + playerDeserializedDices.get(i).toString() + ".png");
                    playerDiceIW[i].setImage(new Image(file.toURI().toString()));
                }
            };
            setStage();
            setChoiceBoxValues();
    }

    private void refreshChat() throws InterruptedException, RemoteException {
        while(true){
            Thread.sleep(2000);
            taChatArea.clear();

            setMessages();
        }
    }

    private void setMessages() throws RemoteException {
        String conversation = stub.receiveConversation();
        taChatArea.setText(conversation);
    }

    public void sendMessage() throws RemoteException {
        String message = tfChatMessage.getText();
        stub.sendMessage(message, HelloController.getPlayerName());
        setMessages();

        tfChatMessage.clear();
    }

    public void generateDocumentation(){
        DocumentationUtils.GenerateDocumentation();
    }

    public void generateXML(){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("liarsDiceMoves");
            document.appendChild(root);

            for (int i = 0; i < client.getGameMetaData().getNumberBids().size(); i++)
            {
               int numberBidResult = client.getGameMetaData().getNumberBids().get(i);
               int diceBidResult = client.getGameMetaData().getDiceBids().get(i);

                Element move = document.createElement("move");
                root.appendChild(move);

                Element numberBid = document.createElement("numberBid");
                numberBid.appendChild(document.createTextNode(String.valueOf(numberBidResult)));
                move.appendChild(numberBid);

                Element diceBid = document.createElement("DiceBid");
                diceBid.appendChild(document.createTextNode(String.valueOf(diceBidResult)));
                move.appendChild(diceBid);
            }

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource domSource = new DOMSource(document);
            StreamResult streamResult = new StreamResult(new File(xmlPath));

            transformer.transform(domSource, streamResult);

            System.out.println("XML successfully made");

        } catch (ParserConfigurationException | TransformerException e) {
            e.printStackTrace();
        }

    }
}


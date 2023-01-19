package hr.algebra.java2.liars.dice.marina_spudic_java2;

import hr.algebra.java2.liars.dice.marina_spudic_java2.model.Client;
import hr.algebra.java2.liars.dice.marina_spudic_java2.model.SerializableDice;
import hr.algebra.java2.liars.dice.marina_spudic_java2.rmi.RemoteService;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
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
import java.lang.reflect.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.*;
import java.util.stream.Collectors;

public class GameScreenController {
    Random random = new Random();
    @FXML
    private Button btnRollDice;
    @FXML
    private Button btnPlayerBidHigher;
    @FXML
    private Button btnCallBluff;
    @FXML
    private Button btnStartNewGame;
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
    @FXML
    private Button btnSendMessage;

    private boolean playerOneTurn;
    private Map<Integer, Integer> numberOfDices;
    private ArrayList<Integer> playerDices;
    private ImageView[] playerDiceIW;
    private ArrayList<ImageView> allDices;

    private ArrayList<Node> apNodes;
    private int lastNumberBid;
    private int lastDiceBid;
    private RemoteService stub = null;
    private static final int CHOICES = 10;
    private static final int DICES = 6;
    private static final String SERIALIZATION_FILE_NAME = "documentation.html";
    private static final String xmlPath = "D:\\java2xmlfile.xml";
    public static Client client;

    public void initialize() throws NotBoundException, RemoteException {
        client.listenForGameReady();


        lastNumberBid = 0;
        playerDices = new ArrayList<>();
        playerDiceIW = new ImageView[DICES];

        allDices = new ArrayList<>();
        initNodes();
        numberOfDices = new HashMap<>(){{
            put(1, 0);
            put(2, 0);
            put(3, 0);
            put(4, 0);
            put(5, 0);
            put(6, 0);
        }};
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
        apGameMoves.setVisible(false);

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        stub = (RemoteService) registry.lookup(RemoteService.REMOTE_OBJECT_NAME);
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
    public void onRollDicePressed() {
        for (ImageView dice:allDices) {
            int randomDice = random.nextInt(6) + 1;
            File file = new File("src/main/resources/assets/dice" + randomDice + ".png");
            dice.setImage(new Image(file.toURI().toString()));
            numberOfDices.put(randomDice, numberOfDices.get(randomDice) + 1);
                playerDices.add(randomDice);
        }
        btnRollDice.setVisible(false);
        setStage();
    }

    private void setStage() {
        /*
        if (playerOneTurn){
            apPlayerOne.setVisible(true);
            apPlayerTwo.setVisible(false);
        }
        else{
            apPlayerOne.setVisible(false);
            apPlayerTwo.setVisible(true);
        }*/
        if (lastNumberBid == 0 && lastDiceBid == 0)
        {
            apGameMoves.setVisible(false);
        }
        else{
            apGameMoves.setVisible(true);
        }
        lbOpponentsMove.setText(lastNumberBid + " dices of number " + lastDiceBid);
    }

    public void onBidHigher(){
        //if (playerOneTurn) {
            if (playerNumberChoiceBox.getValue() == null || playerDiceChoiceBox.getValue() == null)
            {
                System.out.println("U gotta pick");
                return;
                // dodati onaj info window
            }
            lastNumberBid = (int) playerNumberChoiceBox.getValue();
            lastDiceBid = (int) playerDiceChoiceBox.getValue();
        //}
        /*else{
            if (playerTwoNumberChoiceBox.getValue() == null || playerTwoDiceChoiceBox.getValue() == null)
            {
                System.out.println("U gotta pick");
                return;
                // dodati onaj info window
            }
            lastNumberBid = (int) playerTwoNumberChoiceBox.getValue();
            lastDiceBid = (int) playerTwoDiceChoiceBox.getValue();
        }*/
        setChoiceBoxValues();
        try {
            client.sendTurn();
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerOneTurn = !playerOneTurn;
        setStage();
    }

    private void setChoiceBoxValues() {
        playerNumberChoiceBox.getItems().clear();
        List<Integer> availableChoices = new ArrayList<>();
        for (int i = lastNumberBid+1; i < CHOICES; i++) {
            availableChoices.add(i);
        }
        playerNumberChoiceBox.setItems(FXCollections.observableList(availableChoices));
    }

    public void onCallBluff(){
        if (numberOfDices.get(lastDiceBid) < lastNumberBid)
        {
            if (playerOneTurn) {
                displayVictoryDialog(HelloController.getPlayerInfo().getPlayerName());
                HelloController.getPlayerInfo().recordNewVictory();
            } else {
                displayVictoryDialog(HelloController.getPlayerInfo().getPlayerName());
                HelloController.getPlayerInfo().recordNewVictory();
            }
        }
        else
        {
            if (playerOneTurn) {
                displayDefeatDialog(HelloController.getPlayerInfo().getPlayerName());
                HelloController.getPlayerInfo().recordNewVictory();
            } else {
                displayDefeatDialog(HelloController.getPlayerInfo().getPlayerName());
                HelloController.getPlayerInfo().recordNewVictory();
            }
        }
    }

    private static void displayVictoryDialog(String winnerName){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("They really bluffed!");
        alert.setContentText("Player " + winnerName + " wins!");

        alert.showAndWait();
    }

    private static void displayDefeatDialog(String winnerName){
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("It seems like they didn't lie.");
        alert.setContentText("Player " + winnerName + " wins!");

        alert.showAndWait();
    }

    public void startGame() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("gameScreen.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 700);
        HelloApplication.getMainStage().setTitle("Liar's dice");
        HelloApplication.getMainStage().setScene(scene);
        HelloApplication.getMainStage().show();
    }

    public void saveGame() throws IOException, ClassNotFoundException {
        List<SerializableDice> playerOneSerializedDices = new ArrayList<>();
        List<SerializableDice> playerTwoSerializedDices = new ArrayList<>();
        /*
        for (Integer dice : playerOnedices) {
            playerOneSerializedDices.add(new SerializableDice(dice.toString()));
        }
        for (Integer dice : playerTwodices) {
            playerTwoSerializedDices.add(new SerializableDice(dice.toString()));
        }*/

        try (ObjectOutputStream serializator = new ObjectOutputStream(new FileOutputStream("savedGame.ser"))) {
            serializator.writeObject(playerOneSerializedDices);
            serializator.writeObject(playerTwoSerializedDices);
            serializator.writeObject(lastNumberBid);
            serializator.writeObject(lastDiceBid);
        };
    }

    public void loadGame() throws IOException, ClassNotFoundException {
            try (ObjectInputStream deserializator = new ObjectInputStream(new FileInputStream("savedGame.ser"))) {
                ArrayList<SerializableDice> playerOneDeserializedDices = (ArrayList<SerializableDice>) deserializator.readObject();
                ArrayList<SerializableDice> playerTwoDeserializedDices = (ArrayList<SerializableDice>) deserializator.readObject();
                lastNumberBid = (int) deserializator.readObject();
                lastDiceBid = (int) deserializator.readObject();

                for (int i = 0; i < playerOneDeserializedDices.size(); i++) {
                    File file = new File("src/main/resources/assets/dice" + playerOneDeserializedDices.get(i).toString() + ".png");
                    //playerOneDiceIW[i].setImage(new Image(file.toURI().toString()));
                }
                for (int i = 0; i < playerTwoDeserializedDices.size(); i++) {
                    File file = new File("src/main/resources/assets/dice" + playerTwoDeserializedDices.get(i).toString() + ".png");
                    //playerTwoDiceIW[i].setImage(new Image(file.toURI().toString()));
                }
            };
            setStage();
            setChoiceBoxValues();
            btnRollDice.setVisible(false);
    }


    public void sendMessage() throws RemoteException {
        String message = tfChatMessage.getText();
        stub.sendMessage(message, HelloController.getPlayerInfo().getPlayerName());
        String conversation = stub.receiveConversation();
        taChatArea.setText(conversation);
    }

    public void generateDocumentation(){
        StringBuilder builder = new StringBuilder();

        builder.append("<!DOCTYPE html>");
        builder.append("<html>");
        builder.append("<head>");
        builder.append("<title>Documentation</title>");
        builder.append("</head>");
        builder.append("<body>");
        builder.append("<h1>HTML dokumentacija projektnog zadatka iz Jave 2</h1>");
        builder.append("<p>Class list:</p>");

        try {
            List<Path> pathsList = Files.walk(Paths.get("."))
                    .filter(path -> path.getFileName().toString().endsWith(".class"))
                    //.forEach(System.out::println);
                    .collect(Collectors.toList());


            for (Path path : pathsList) {

                String fileName = path.getFileName().toString();

                StringTokenizer tokenizer = new StringTokenizer(path.toString(), "\\");

                String fullQualifiedName = "";

                Boolean packageStart = false;

                while (tokenizer.hasMoreTokens()) {
                    String token = tokenizer.nextToken();

                    if ("classes".equals(token)) {
                        packageStart = true;
                        continue;
                    }

                    if (packageStart == false) {
                        continue;
                    }

                    if (token.endsWith(".class")) {
                        token = token.substring(0, token.indexOf("."));
                        fullQualifiedName += token;
                        break;
                    }

                    fullQualifiedName += token;
                    fullQualifiedName += ".";
                }

                if ("module-info".equals(fullQualifiedName)) {
                    continue;
                }

                builder.append("Class:");

                Class<?> klasa = Class.forName(fullQualifiedName);

                builder.append("<h2>" + Modifier.toString(klasa.getModifiers()) + " " + fullQualifiedName);
                builder.append("</h2>");
                builder.append("<br \\>");
                builder.append("<br \\>");

                builder.append("   Variables:");

                Field[] classFields = klasa.getDeclaredFields();

                StringBuilder fieldsStringBuilder = new StringBuilder();

                for (Field f : classFields) {
                    fieldsStringBuilder.append(Modifier.toString(f.getModifiers()));
                    fieldsStringBuilder.append(" ");
                    fieldsStringBuilder.append(f.getType().getSimpleName());
                    fieldsStringBuilder.append(" ");
                    fieldsStringBuilder.append(f.getName());
                    fieldsStringBuilder.append("<br />");
                }

                fieldsStringBuilder.append("<br />");

                builder.append("<h3>   " + fieldsStringBuilder + "</h3>");

                builder.append("   Constructors:");

                Constructor[] constructors = klasa.getConstructors();

                for (Constructor c : constructors) {

                    String constructorParamString = generateExecutableDocumentation(c);

                    builder.append("<h3>   " + c.getName() + "(" + constructorParamString + ")");
                    builder.append("</h3>");
                    builder.append("<br \\>");
                }

                builder.append("   Methods:");

                Method[] methods = klasa.getMethods();

                for (Method method : methods) {

                    String methodMetaDataString = generateExecutableDocumentation(method);

                    String methodMetaData = "";

                    methodMetaData += Modifier.toString(method.getModifiers()) + " ";
                    methodMetaData += method.getReturnType().getSimpleName() + " ";

                    builder.append("<h3>   " + methodMetaData + " " + method.getName() + "(" + methodMetaDataString + ")");
                    builder.append("</h3>");
                    builder.append("<br \\>");
                }

                System.out.println(path.getFileName());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        builder.append("</body>");
        builder.append("</html>");

        try (FileWriter fw = new FileWriter(SERIALIZATION_FILE_NAME)) {
            fw.write(builder.toString());

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Documentation generated successfuly!");
            alert.setContentText("The file \"" + SERIALIZATION_FILE_NAME + "\" has been generated!");

            alert.showAndWait();

        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error while creating documentation file");
            alert.setHeaderText("Cannot generate documentation");
            alert.setContentText("Details: " + e.getMessage());

            alert.showAndWait();
        }
    }

    private <T extends Executable> String generateExecutableDocumentation(T executable) {
        Parameter[] constructorParams = executable.getParameters();

        String paramString = "";

        for (int i = 0; i < constructorParams.length; i++) {
            Parameter p = constructorParams[i];
            paramString += Modifier.toString(p.getModifiers());
            paramString += p.getType().getSimpleName() + " ";
            paramString += p.getName();

            if (i < (constructorParams.length - 1)) {
                paramString += ", ";
            }
        }

        return paramString;
    }

    public void generateXML(){
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            Document document = documentBuilder.newDocument();

            Element root = document.createElement("liarsDiceMoves");
            document.appendChild(root);

            //foreach kroz mapu (client.getGameMetaData().getGameMoves().forEach()
            Element move = document.createElement("move");
            root.appendChild(move);

            Element numberBid = document.createElement("numberBid");
            //numberBid.appendChild(document.createTextNode());
            move.appendChild(numberBid);

            Element diceBid = document.createElement("DiceBid");
            //numberBid.appendChild(document.createTextNode());
            move.appendChild(diceBid);

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


package controller;

/*-
 * #%L
 * BlackJack
 * %%
 * Copyright (C) 2018 University of Debrecen
 * %%
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 * #L%
 */

import dao.AiEntity;
import dao.PlayerEntity;
import dao.DBManager;
import javafx.scene.control.Alert;
import modell.GameMaster;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static java.lang.Integer.parseInt;

public class MainFXMLController implements Initializable {

    private static final DBManager DB_MANAGER = DBManager.getDpInstance();

    static PlayerEntity playerEntity = new PlayerEntity();
    static AiEntity aiEntity = new AiEntity();

    private Stage stage = new Stage();

    private GameMaster gameMaster = new GameMaster();

    private int seged = 0;

    private int playerPosition = 0;
    private int aiPosition = 0;
    private boolean endofgame = false;

    @FXML
    private Button ConcedeButton;

    @FXML
    private AnchorPane Desk;

    @FXML
    private Button HintButton;

    @FXML
    private Button StartButton;

    @FXML
    private Button PassButton;

    @FXML
    private ImageView img01;

    @FXML
    private ImageView img02;

    @FXML
    private ImageView img03;

    @FXML
    private ImageView img04;

    @FXML
    private ImageView img05;

    @FXML
    private ImageView img06;

    @FXML
    private ImageView img07;

    @FXML
    private ImageView img08;

    @FXML
    private ImageView img11;

    @FXML
    private ImageView img12;

    @FXML
    private ImageView img13;

    @FXML
    private ImageView img14;

    @FXML
    private ImageView img15;

    @FXML
    private ImageView img16;

    @FXML
    private ImageView img17;

    @FXML
    private ImageView img18;

    @FXML
    private Label myScore;

    @FXML
    private Label aiScore;

    @FXML
    private Label myCredit;

    @FXML
    private Label Bets;

    @FXML
    private Label aiCredit;

    @FXML
    private ImageView Credit01;

    @FXML
    private ImageView Credit02;

    @FXML
    private TextField bettingField;

    @FXML
    private Button BetButton;

    @FXML
    private Button reMatch;

    @FXML
    private Button mainMenuButton;
    
    private final Integer BASE_CREDIT = 5000;

    public void initialize(URL location, ResourceBundle resources) {
        HintButton.setDisable(true);
        ConcedeButton.setDisable(false);
        PassButton.setDisable(true);
        StartButton.setDisable(true);
        reMatch.setVisible(false);

        if (((playerEntity.getCredit() == null)
                && (aiEntity.getCredit() == null))
                || playerEntity.getCredit() == 0 || aiEntity.getCredit() == 0) {

            playerEntity.setCredit(BASE_CREDIT);
            aiEntity.setCredit(BASE_CREDIT);
            playerEntity.setMaxCredit(BASE_CREDIT);
        } else {
            playerEntity.setCredit(playerEntity.getCredit());
            aiEntity.setCredit(aiEntity.getCredit());
            playerEntity.setMaxCredit(playerEntity.getMaxCredit());
        }

        DB_MANAGER.save(playerEntity);
        DB_MANAGER.save(aiEntity);

        Image img =
                new Image(getClass().getClassLoader()
                        .getResource("pictures/zseton.png").toString());

        Credit01.setImage(img);
        Credit02.setImage(img);

        myCredit.setText(": " + playerEntity.getCredit());
        aiCredit.setText(": " + aiEntity.getCredit());
    }

    @FXML
    public void concedeButtonAction(ActionEvent actionEvent) throws IOException {
        AnchorPane pane =
                FXMLLoader.load(getClass().getClassLoader().
                        getResource("fxml/MainMenu.fxml"));

        Desk.getChildren().setAll(pane);
    }

    @FXML
    public void hintButtonAction(ActionEvent event) {
        HintPlayerHand();
        if(parseInt(myScore.getText()) > 21) {
            int bet = parseInt(Bets.getText());
            aiEntity.setCredit(aiEntity.getCredit() + bet);
            DB_MANAGER.save(aiEntity);
            aiCredit.setText(": " + aiEntity.getCredit());
            Bets.setText("");
            HintButton.setDisable(true);
            PassButton.setDisable(true);
            reMatch.setVisible(true);
            ConcedeButton.setDisable(false);
        }
    }

    public void letsPlay(ActionEvent event) {
        InitializePlayerHand();
        InitializeAiHand();
        StartButton.setVisible(false);
        HintButton.setDisable(false);
        ConcedeButton.setDisable(true);
        PassButton.setDisable(false);
    }

    public void PassButtonAction(ActionEvent event) {
        reMatch.setVisible(true);
        ConcedeButton.setDisable(false);
        HintButton.setDisable(true);
        PassButton.setDisable(true);
        endofgame = true;
        HintAiHand();
        aiScore.setText("" + this.gameMaster.getHandValue(this.gameMaster.getAi().getHand()));

        if(this.gameMaster.getWinner(this.gameMaster.getPlayer(), this.gameMaster.getAi()) == 1) {

            int bet = parseInt(Bets.getText());
            playerEntity.setCredit(playerEntity.getCredit() + bet);

            if (playerEntity.getMaxCredit() < playerEntity.getCredit()) {
                playerEntity.setMaxCredit(playerEntity.getCredit());
            }

            DB_MANAGER.save(playerEntity);
        } else {
            int betai = parseInt(Bets.getText());
            aiEntity.setCredit(aiEntity.getCredit() + betai);
            DB_MANAGER.save(aiEntity);
        }

        myCredit.setText(": " + playerEntity.getCredit());
        aiCredit.setText(": " + aiEntity.getCredit());

        if (playerEntity.getCredit() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("RESULT");
            alert.setHeaderText("You lost!");
            alert.setContentText("Better luck next time!:)");
            alert.showAndWait();
            HintButton.setDisable(true);
            ConcedeButton.setDisable(false);
            PassButton.setDisable(true);
            reMatch.setDisable(true);
        }

        if (aiEntity.getCredit() == 0) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("RESULT");
            alert.setHeaderText("You WON!!!");
            alert.setContentText("Your friends are so proud of you!:)");
            alert.showAndWait();
            HintButton.setDisable(true);
            ConcedeButton.setDisable(false);
            PassButton.setDisable(true);
            reMatch.setDisable(true);
        }
    }

    public static PlayerEntity getPlayerEntity(){
        return playerEntity;
    }

    public static AiEntity getAiEntity(){
        return aiEntity;
    }


    private void InitializePlayerHand() {

        if (playerPosition == 0) {
            this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

            setPlayerCardIm(playerPosition, img11);

            ++playerPosition;
        }

        if (playerPosition == 1) {
            this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

            setPlayerCardIm(playerPosition, img12);

            ++playerPosition;
        }
    }

    private void InitializeAiHand() {

        if (aiPosition == 0) {
            this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

            if(!endofgame){
                Image image1 = new Image("pictures/backGround.png");
                img01.setImage(image1);
            }
            ++aiPosition;
        }

        if (aiPosition == 1) {
            this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

            if(!endofgame){
                Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                img02.setImage(image1);
            }
            ++aiPosition;
        }
    }

    private void HintPlayerHand() {
        while (true) {

            if (playerPosition == 2) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img13);

                ++playerPosition;
                break;
            }

            if (playerPosition == 3) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img14);

                ++playerPosition;
                break;
            }

            if (playerPosition == 4) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img15);

                ++playerPosition;
                break;
            }

            if (playerPosition == 5) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img16);

                ++playerPosition;
                break;
            }

            if (playerPosition == 6) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img17);

                ++playerPosition;
                break;
            }

            if (playerPosition == 7) {
                this.gameMaster.getDealer().dealToPlayer(this.gameMaster.getPlayer(), playerPosition);

                setPlayerCardIm(playerPosition, img18);

                ++playerPosition;
                break;
            }
        }
    }

    private void HintAiHand() {

        if(endofgame){
            Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[0] + ".png").toString());
            img01.setImage(image1);

            Image image2 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[1] + ".png").toString());
            img02.setImage(image2);
        }

        if(this.gameMaster.getAi().AiTurn()){
            if(this.gameMaster.getAi().AiHint()){

                    if (aiPosition == 2) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                        img03.setImage(image1);
                        ++aiPosition;
                    }

                    if (aiPosition == 3 && this.gameMaster.getAi().AiHint()) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                        img04.setImage(image1);
                        ++aiPosition;
                    }

                    if (aiPosition == 4 && this.gameMaster.getAi().AiHint()) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                        img05.setImage(image1);
                        ++aiPosition;
                    }

                    if (aiPosition == 5 && this.gameMaster.getAi().AiHint()) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png");
                        img06.setImage(image1);
                        ++aiPosition;
                    }

                    if (aiPosition == 6 && this.gameMaster.getAi().AiHint()) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                        img07.setImage(image1);
                        ++aiPosition;
                    }

                    if (aiPosition == 7 && this.gameMaster.getAi().AiHint()) {
                        this.gameMaster.getDealer().dealToAi(this.gameMaster.getAi(), aiPosition);

                        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getAi().getHand()[aiPosition] + ".png").toString());
                        img08.setImage(image1);
                    }
                }
            }
        }

    public void Betting(ActionEvent event) {
        StartButton.setDisable(false);
        BetButton.setVisible(false);
        bettingField.setVisible(false);

        int  bet = parseInt(bettingField.getText());

        if (bet > playerEntity.getCredit() || bet > aiEntity.getCredit()) {
            StartButton.setDisable(true);
            BetButton.setVisible(true);
            bettingField.setVisible(true);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText("Too much bet!");
            alert.setContentText("Give less credit for betting!");
            alert.showAndWait();

        } else {

            this.gameMaster.getPlayer().PlusBet(bet);
            playerEntity.setCredit(playerEntity.getCredit() - bet);
            DB_MANAGER.save(playerEntity);

            this.gameMaster.getAi().plusBet(bet);
            aiEntity.setCredit(aiEntity.getCredit() - bet);
            DB_MANAGER.save(aiEntity);
            Bets.setText("" + bet * 2);

            myCredit.setText(": " + playerEntity.getCredit());
            aiCredit.setText(": " + aiEntity.getCredit());
        }

    }

    public void reMatchAction(ActionEvent event) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainFXml.fxml"));
        Desk.getChildren().setAll(pane);

    }

    private void setPlayerCardIm(int position, ImageView imageView){
        Image image1 = new Image(getClass().getClassLoader().getResource("pictures/" + this.gameMaster.getPlayer().getHand()[position] + ".png").toString());
        imageView.setImage(image1);
        myScore.setText("" + this.gameMaster.getHandValue(this.gameMaster.getPlayer().getHand()));
    }
}


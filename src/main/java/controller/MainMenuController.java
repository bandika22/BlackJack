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

import javafx.scene.control.Label;
import modell.GameMaster;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainMenuController implements Initializable {

    @FXML
    private AnchorPane MainPane;

    @FXML
    private Button ExitButton;

    @FXML
    private Button PlayButton;

    @FXML
    private Button ResetButton;

    @FXML
    private Label highScore;

    private final Integer BASE_CREDIT = 5000;

    @FXML
    public void letsStart(ActionEvent actionEvent) throws IOException {
        AnchorPane pane = FXMLLoader.load(getClass().getClassLoader().getResource("fxml/MainFXml.fxml"));
        MainPane.getChildren().setAll(pane);
    }

    public void getOff(ActionEvent event) {
        Platform.exit();
    }

    public void resetStats(ActionEvent event) {
        GameMaster gameMaster = new GameMaster();
        MainFXMLController.getPlayerEntity().setCredit(BASE_CREDIT);
        MainFXMLController.getAiEntity().setCredit(BASE_CREDIT);
    }

    public void initialize(URL location, ResourceBundle resources) {
        highScore.setText("" + MainFXMLController.playerEntity.getMaxCredit());

    }
}
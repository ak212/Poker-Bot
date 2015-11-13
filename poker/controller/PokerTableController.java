package poker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import poker.Main;

public class PokerTableController {
   private Main mainApp;

   @FXML
   private TextArea logTextArea;

   @FXML
   private TextField playerBetTextField;

   @FXML
   private Label potValueLabel, currentBetValueLabel, playerStackLabel, botStackLabel;

   @FXML
   private Button checkCallButton, betButton, minButton, halfButton, potButton, maxButton, confirmButton;

   @FXML
   private void intialize() {

   }

   @FXML
   private void betButtonClicked(ActionEvent e) {
      minButton.setVisible(true);
      halfButton.setVisible(true);
      potButton.setVisible(true);
      maxButton.setVisible(true);
      confirmButton.setVisible(true);
      playerBetTextField.setVisible(true);
   }

   @FXML
   private void confirmButtonClicked(ActionEvent e) {

   }

   public void addText(String text) {
      logTextArea.appendText(text + "\n");
   }

   public void updatePot(String text) {
      potValueLabel.setText(text);
   }

   public void updateCurrentBet(String text) {
      currentBetValueLabel.setText(text);
   }

   public void updatePlayerStack(String text) {
      playerStackLabel.setText(text);
   }

   public void updateBotStack(String text) {
      botStackLabel.setText(text);
   }

   /**
    * Is called by the main application to give a reference back to itself.
    * 
    * @param mainApp
    *           reference to main
    */
   public void setMainApp(Main mainApp) {
      this.mainApp = mainApp;
   }
}

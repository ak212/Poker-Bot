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
   private Label potValueLabel, currentBetValueLabel, stackLabel0, stackLabel1, betAmountLabel0, betAmountLabel1,
         dealerLabel0, dealerLabel1;

   @FXML
   private Button checkCallButton, betButton, minButton, halfButton, potButton, maxButton, confirmButton;

   private int betValue = 0;

   @FXML
   private void intialize() {
      // Handle Subject TextField text changes.
      playerBetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null && !newValue.matches("[0-9]+")
               || Integer.parseInt(newValue) > mainApp.poker.dealer.maxBetAmount) {
            playerBetTextField.setText(oldValue);
         }
         else {
            playerBetTextField.setText(newValue);
            betValue = Integer.parseInt(newValue);
         }
      });
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
   private void minButtonClicked(ActionEvent e) {
      playerBetTextField.setText(Integer.toString(mainApp.poker.dealer.minBetAmount));
   }

   @FXML
   private void halfButtonClicked(ActionEvent e) {
      playerBetTextField.setText(Integer.toString(mainApp.poker.dealer.halfPotBetAmount));
   }

   @FXML
   private void potButtonClicked(ActionEvent e) {
      playerBetTextField.setText(Integer.toString(mainApp.poker.dealer.potBetAmount));
   }

   @FXML
   private void maxButtonClicked(ActionEvent e) {
      playerBetTextField.setText(Integer.toString(mainApp.poker.dealer.maxBetAmount));
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

   public void updateStackZero(String text) {
      stackLabel0.setText(text);
   }

   public void updateStackOne(String text) {
      stackLabel1.setText(text);
   }

   public void updateBetAmountZero(String text) {
      betAmountLabel0.setText(text);
   }

   public void updateBetAmountOne(String text) {
      betAmountLabel1.setText(text);
   }

   public void toggleDealerZero(boolean dealer) {
      dealerLabel0.setVisible(dealer);
   }

   public void toggleDealerOne(boolean dealer) {
      dealerLabel1.setVisible(dealer);
   }

   public void enableConfirmButton() {
      confirmButton.setDisable(false);
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

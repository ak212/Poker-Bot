package poker.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.TextAlignment;
import poker.Main;
import poker.model.player.Action;
import poker.model.player.Turn;

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

   @FXML
   private ImageView dealerImage0, dealerImage1, betChipImage0, betChipImage1;

   private int betValue = 0;
   private boolean visible = false;
   private boolean turnComplete = false;

   @FXML
   private void intialize() {

   }

   @FXML
   private void betButtonClicked(ActionEvent e) {
      toggleVisable();
   }

   @FXML
   private void checkCallButtonClicked(ActionEvent e) {
      playerBetTextField.setText(Integer.toString(mainApp.poker.dealer.minAmount));
      turnComplete = true;
      confirmButton.setDisable(true);
      hide();
   }

   @FXML
   private void foldButtonClicked(ActionEvent e) {
      betValue = -1;
      turnComplete = true;
      confirmButton.setDisable(true);
      hide();
   }

   private void toggleVisable() {
      visible = !visible;
      minButton.setVisible(visible);
      halfButton.setVisible(visible);
      potButton.setVisible(visible);
      maxButton.setVisible(visible);
      confirmButton.setVisible(visible);
      playerBetTextField.setVisible(visible);
   }

   private void hide() {
      visible = false;
      minButton.setVisible(false);
      halfButton.setVisible(false);
      potButton.setVisible(false);
      maxButton.setVisible(false);
      confirmButton.setVisible(false);
      playerBetTextField.setVisible(false);
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
      confirmButton.setDisable(true);
      hide();

      turnComplete = true;
   }

   public Turn getPlayerInput() {
      while (!turnComplete) {
         try {
            Thread.sleep(100);
         }
         catch (InterruptedException e) {
            e.printStackTrace();
         }
      }
      turnComplete = false;
      
      Action action;
      int betAmount = betValue;
      
      if (betValue == mainApp.poker.dealer.minAmount) {
         action = Action.CHECKCALL;
      }
      else if (betValue > mainApp.poker.dealer.minAmount) {
         action = Action.BET;
      }
      else if (betValue == -1) {
         action = Action.FOLD;
      }
      else {
         action = Action.FOLD;
      }

      betValue = 0;
      playerBetTextField.setText(null);

      return new Turn(action, betAmount);
   }

   @FXML
   public void addListener() {
      playerBetTextField.textProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue != null && newValue.matches("[0-9]+")
               && Integer.parseInt(newValue) <= mainApp.poker.dealer.maxBetAmount) {
            playerBetTextField.setText(newValue);
            betValue = Integer.parseInt(newValue);
         }
         else if (newValue == null || newValue.equals("")) {
            playerBetTextField.setText(newValue);
            betValue = 0;
         }
         else {
            playerBetTextField.setText(oldValue);
         }
         
         if (betValue >= mainApp.poker.dealer.minAmount) {
            confirmButton.setDisable(false);
         }
         else {
            confirmButton.setDisable(true);
         }
      });
   }

   public void addText(String text) {
      logTextArea.appendText(text + "\n");
   }

   public void updatePot(String text) {
      potValueLabel.setText(text);
   }

   public void updateStackZero(String text) {
      stackLabel0.setText(text);
   }

   public void updateStackOne(String text) {
      stackLabel1.setText(text);
   }

   public void updateBetAmountZero(String text) {
      betAmountLabel0.setText(text);
      betAmountLabel0.setTextAlignment(TextAlignment.CENTER);
      betChipImage0.setVisible(text != null && !text.equals(""));
      betAmountLabel0.toFront();
   }

   public void updateBetAmountOne(String text) {
      betAmountLabel1.setText(text);
      betAmountLabel1.setTextAlignment(TextAlignment.CENTER);
      betChipImage1.setVisible(text != null && !text.equals(""));
      betAmountLabel1.toFront();
   }

   public void toggleDealerZero(boolean dealer) {
      dealerImage0.setVisible(dealer);
   }

   public void toggleDealerOne(boolean dealer) {
      dealerImage1.setVisible(dealer);
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

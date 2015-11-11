package poker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import poker.Main;

public class PokerTableController {
   private Main mainApp;

   @FXML
   private TextArea logTextArea;

   @FXML
   private Label potValueLabel, currentBetValueLabel;

   @FXML
   private Button checkCallButton;

   @FXML
   private void intialize() {

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

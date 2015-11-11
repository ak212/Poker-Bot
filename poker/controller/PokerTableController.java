package poker.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import poker.Main;

public class PokerTableController {
   private Main mainApp;

   @FXML
   private TextArea logTextArea;

   @FXML
   private Button checkCallButton;

   @FXML
   private void intialize() {
      logTextArea.textProperty().addListener((observable, oldValue, newValue) -> {
         logTextArea.setText(newValue);
      });

   }

   public void addText(String text) {
      logTextArea.appendText(text + "\n");
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

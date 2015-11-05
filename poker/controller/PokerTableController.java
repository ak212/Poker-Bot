package poker.controller;

import javafx.fxml.FXML;
import poker.Main;

public class PokerTableController {
   private Main mainApp;

   @FXML
   private void intialize() {

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

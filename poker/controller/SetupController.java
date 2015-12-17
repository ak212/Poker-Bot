package poker.controller;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import poker.Main;

public class SetupController {
   // Reference to the main application.
   private Main mainApp;
   @FXML
   private TextField handsPerLevelTextField, smallBlindTextField, stackTextField;

   private int hands = 0, smallBlind = 0, stack = 0;

   @FXML
   private void intialize() {

   }

   public void addListener() {
      stackTextField.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            int temp = 0;
            
            try {
               temp = Integer.parseInt(newValue);
            }
            catch (NumberFormatException e) {
               stackTextField.setText(oldValue);
            }
            
            if (temp >= 0 && temp <= 1000000) {
               stack = temp;
               stackTextField.setText(newValue);
            }
            else {
               stackTextField.setText(oldValue);
            }
         }
      });
      
      handsPerLevelTextField.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            int temp = 0;

            try {
               temp = Integer.parseInt(newValue);
            }
            catch (NumberFormatException e) {
               handsPerLevelTextField.setText(oldValue);
            }
            
            if (temp >= 1 && temp <= 100) {
               hands = temp;
               handsPerLevelTextField.setText(newValue);
            }
            else {
               handsPerLevelTextField.setText(oldValue);
            }
         }
      });

      smallBlindTextField.textProperty().addListener(new ChangeListener<String>() {
         @Override
         public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
            int temp = 0;

            try {
               temp = Integer.parseInt(newValue);
            }
            catch (NumberFormatException e) {
               smallBlindTextField.setText(oldValue);
            }

            if (temp >= 0 && temp <= stack / 10) {
               smallBlind = temp;
               smallBlindTextField.setText(newValue);
            }
            else {
               smallBlindTextField.setText(oldValue);
            }
         }
      });
   }

   @FXML
   private void handlePlay() {
      if (smallBlind >= stack / 100 && smallBlind <= stack / 10 && hands > 0 && hands < 100) {
         mainApp.showMainView(stack, smallBlind, hands);
      }
   }

   public void setMainApp(Main mainApp) {
      this.mainApp = mainApp;
   }
}

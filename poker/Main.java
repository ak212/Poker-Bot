package poker;

import java.io.IOException;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import poker.controller.PokerTableController;
import poker.model.Poker;

public class Main extends Application {

   private Stage primaryStage;

   private AnchorPane rootLayout;

   private PokerTableController tableController;

   @Override
   public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Poker");
      showMainView();
      Poker poker = new Poker();

      // TODO Run game and UI on separate threads
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            poker.playPoker();
         }
      });
   }

   public void showMainView() {

      try {
         // Load root layout from fxml file.
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("view/PokerTable.fxml"));
         rootLayout = (AnchorPane) loader.load();

         // Show the scene containing the root layout.
         Scene scene = new Scene(rootLayout);
         primaryStage.setScene(scene);
         primaryStage.show();

         // set controller and application information
         tableController = loader.getController();
         tableController.setMainApp(this);

      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void updateConsole(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.addText(text);
         }
      });
   }

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public static void main(String[] args) {
      launch(args);
   }
}

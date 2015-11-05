package poker;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import poker.controller.PokerTableController;

public class Main extends Application {

   private Stage primaryStage;

   private BorderPane rootLayout;

   @Override
   public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Poker");
      showMainView();
   }

   public void showMainView() {

      try {
         // Load root layout from fxml file.
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("view/PokerTable.fxml"));
         rootLayout = (BorderPane) loader.load();

         // Show the scene containing the root layout.
         Scene scene = new Scene(rootLayout);
         primaryStage.setScene(scene);
         primaryStage.show();

         // set controller and application information
         PokerTableController controller = loader.getController();
         controller.setMainApp(this);

      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public static void main(String[] args) {
      launch(args);
   }
}

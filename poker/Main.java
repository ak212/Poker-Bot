package poker;

import java.io.IOException;
import java.util.ArrayList;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import poker.controller.PokerTableController;
import poker.controller.SetupController;
import poker.model.Poker;
import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.player.PlayerStats;
import poker.model.player.Turn;

public class Main extends Application {

   private Stage primaryStage;
   private AnchorPane rootLayout;
   private PokerTableController tableController;
   public Poker poker;

   @Override
   public void start(Stage primaryStage) throws Exception {
      this.primaryStage = primaryStage;
      this.primaryStage.setTitle("Poker");
      showSetupView();

      primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
         @Override
         public void handle(WindowEvent t) {
            Platform.exit();
            System.exit(0);
         }
      });
   }

   public void startGame(int chips, int blind, int hands) {
      poker = new Poker(chips, blind, hands);
      poker.setMainApp(this);

      Task<Void> task = new Task<Void>() {
         @Override
         public Void call() {
            poker.playPoker();
            return null;
         }
      };

      new Thread(task).start();
   }
   
   public void showSetupView() {

      try {
         // Load Setup layout from fxml file.
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("view/Setup.fxml"));
         rootLayout = (AnchorPane) loader.load();

         // Show the scene containing the root layout.
         Scene scene = new Scene(rootLayout);
         primaryStage.setScene(scene);
         primaryStage.show();

         // set controller and application information
         SetupController controller = loader.getController();
         controller.addListener();
         controller.setMainApp(this);

      }
      catch (IOException e) {
         e.printStackTrace();
      }
   }

   public void showMainView(int chips, int blind, int hands) {

      try {
         // Load PokerTable layout from fxml file.
         FXMLLoader loader = new FXMLLoader();
         loader.setLocation(Main.class.getResource("view/PokerTable.fxml"));
         rootLayout = (AnchorPane) loader.load();
         rootLayout.setId("pane");

         // Show the scene containing the root layout.
         Scene scene = new Scene(rootLayout);
         scene.getStylesheets().addAll(this.getClass().getResource("view/style.css").toExternalForm());
         primaryStage.setScene(scene);
         primaryStage.show();

         // set controller and application information
         tableController = loader.getController();
         tableController.setMainApp(this);
         tableController.addListener();

         startGame(chips, blind, hands);
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

   public void updatePot(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updatePot(text);
         }
      });
   }

   public void updateStackZero(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateStackZero(text);
         }
      });
   }

   public void updateStackOne(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateStackOne(text);
         }
      });
   }

   public void updateBetAmountZero(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateBetAmountZero(text);
         }
      });
   }

   public void updateBetAmountOne(String text) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateBetAmountOne(text);
         }
      });
   }

   public void updatePlayerStats(PlayerStats stats) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updatePlayerStats(stats);
         }
      });
   }

   public void updateBotStats(PlayerStats stats) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateBotStats(stats);
         }
      });
   }

   public void toggleDealerZero(boolean dealer) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.toggleDealerZero(dealer);
         }
      });
   }

   public void toggleDealerOne(boolean dealer) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.toggleDealerOne(dealer);
         }
      });
   }

   public void getHoleCards(HoleCards cards) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateHoleCards(cards);
         }
      });
   }

   public void showBotHoleCards(HoleCards cards) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.showBotHoleCards(cards);
         }
      });
   }

   public void getCommunityCards(ArrayList<Card> cards) {
      Platform.runLater(new Runnable() {
         @Override
         public void run() {
            tableController.updateCommunityCards(cards);
         }
      });
   }

   public String getSuit(Card card) {
      String suit = "view/images/";

      switch (card.getSuit()) {
      case CLUBS:
         suit += "Clubs.png";
         break;
      case DIAMONDS:
         suit += "Diamonds.png";
         break;
      case HEARTS:
         suit += "Hearts.png";
         break;
      case SPADES:
         suit += "Spades.png";
         break;
      }

      return suit;
   }

   public Image getImage(Card card) {
      return new Image(Main.class.getResourceAsStream(getSuit(card)));
   }

   public Turn getPlayerInput() {
      return tableController.getPlayerInput();
   }

   public void disablePlayerInput() {
      tableController.disablePlayerInput();
   }

   public Stage getPrimaryStage() {
      return primaryStage;
   }

   public static void main(String[] args) {
      launch(args);
   }
}

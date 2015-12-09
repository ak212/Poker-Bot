package poker.controller;

import java.util.ArrayList;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import poker.Main;
import poker.model.cards.Card;
import poker.model.cards.HoleCards;
import poker.model.player.Action;
import poker.model.player.PlayerStats;
import poker.model.player.Profile;
import poker.model.player.Turn;

public class PokerTableController {
   private Main mainApp;

   @FXML
   private TextArea logTextArea;

   @FXML
   private TextField playerBetTextField;

   @FXML
   private Label potValueLabel, currentBetValueLabel;
   @FXML
   private Label stackLabel0, stackLabel1;
   @FXML
   private Label betAmountLabel0, betAmountLabel1;
   @FXML
   private Label cardRank0, cardRank1, cardRank2, cardRank3, cardRank4, cardRank5, cardRank6, cardRank7, cardRank8;
   @FXML
   private Label playerHandsLabel, playerLossesLabel, playerFoldsLabel, playerPreflopFoldsLabel, playerFlopFoldsLabel,
         playerTurnFoldsLabel, playerRiverFoldsLabel, playerWinsLabel, playerRaisesLabel, playerAverageWinningsLabel,
         playerWinningsLabel, playerBestHandLabel, playerBiggestWinLabel;
   @FXML
   private Label botHandsLabel, botLossesLabel, botFoldsLabel, botPreflopFoldsLabel, botFlopFoldsLabel,
         botTurnFoldsLabel, botRiverFoldsLabel, botWinsLabel, botRaisesLabel, botAverageWinningsLabel,
         botWinningsLabel, botBestHandLabel, botBiggestWinLabel;

   @FXML
   private Button checkCallButton, betButton, foldButton, minButton, halfButton, potButton, maxButton, confirmButton;

   @FXML
   private ImageView dealerImage0, dealerImage1;
   @FXML
   private ImageView betChipImage0, betChipImage1;
   @FXML
   private ImageView cardSuitSmall0, cardSuitSmall1, cardSuitSmall2, cardSuitSmall3, cardSuitSmall4, cardSuitSmall5,
         cardSuitSmall6, cardSuitSmall7, cardSuitSmall8;
   @FXML
   private ImageView cardSuitBig0, cardSuitBig1, cardSuitBig2, cardSuitBig3, cardSuitBig4, cardSuitBig5, cardSuitBig6,
         cardSuitBig7, cardSuitBig8;
   
   @FXML
   private ComboBox<Profile> profileBox;

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
      checkCallButton.setDisable(false);
      betButton.setDisable(false);
      foldButton.setDisable(false);

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

   public void disablePlayerInput() {
      checkCallButton.setDisable(true);
      betButton.setDisable(true);
      foldButton.setDisable(true);
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
         
         if (betValue >= mainApp.poker.dealer.minBetAmount) {
            confirmButton.setDisable(false);
         }
         else {
            confirmButton.setDisable(true);
         }
      });

      potValueLabel.textProperty().addListener((observable, oldValue, newValue) -> {
         if (newValue.length() <= 3) {
            potValueLabel.setFont(new Font("Arial", 18));
         }
         else if (newValue.length() > 3) {
            potValueLabel.setFont(new Font("Arial", 16));
         }
         else if (newValue.length() > 4) {
            potValueLabel.setFont(new Font("Arial", 14));
         }
      });

      profileBox.getItems().setAll(Profile.values());
      profileBox.setValue(Profile.NORMAL);
   }

   public void addText(String text) {
      logTextArea.appendText(text + "\n");
   }

   public void updatePot(String text) {
      potValueLabel.setText(text);
      potValueLabel.toFront();
   }

   public void updateStackZero(String text) {
      stackLabel0.setText(text);
   }

   public void updateStackOne(String text) {
      stackLabel1.setText(text);
   }

   public void updateBetAmountZero(String text) {
      betAmountLabel0.setText(text != null && !text.equals("0") ? text : "");
      betAmountLabel0.setTextAlignment(TextAlignment.CENTER);
      betChipImage0.setVisible(text != null && !text.equals("") && !text.equals("0"));
      betAmountLabel0.toFront();
   }

   public void updateBetAmountOne(String text) {
      betAmountLabel1.setText(text != null && !text.equals("0") ? text : "");
      betAmountLabel1.setTextAlignment(TextAlignment.CENTER);
      betChipImage1.setVisible(text != null && !text.equals("") && !text.equals("0"));
      betAmountLabel1.toFront();
   }

   public void updatePlayerStats(PlayerStats stats) {
      playerHandsLabel.setText(Integer.toString(stats.hands));
      playerLossesLabel.setText(Integer.toString(stats.losses));
      playerFoldsLabel.setText(Integer.toString(stats.folds));
      playerPreflopFoldsLabel.setText(Integer.toString(stats.preFlopFolds));
      playerFlopFoldsLabel.setText(Integer.toString(stats.flopFolds));
      playerTurnFoldsLabel.setText(Integer.toString(stats.turnFolds));
      playerRiverFoldsLabel.setText(Integer.toString(stats.riverFolds));
      playerWinsLabel.setText(Integer.toString(stats.wins));
      playerRaisesLabel.setText(Integer.toString(stats.raises));
      playerAverageWinningsLabel.setText(Double.toString(stats.averageWinningPerHand));
      playerWinningsLabel.setText(Integer.toString(stats.winnings));
      playerBestHandLabel.setText(stats.bestHand.toString());
      playerBiggestWinLabel.setText(Integer.toString(stats.biggestWin));
   }

   public void updateBotStats(PlayerStats stats) {
      botHandsLabel.setText(Integer.toString(stats.hands));
      botLossesLabel.setText(Integer.toString(stats.losses));
      botFoldsLabel.setText(Integer.toString(stats.folds));
      botPreflopFoldsLabel.setText(Integer.toString(stats.preFlopFolds));
      botFlopFoldsLabel.setText(Integer.toString(stats.flopFolds));
      botTurnFoldsLabel.setText(Integer.toString(stats.turnFolds));
      botRiverFoldsLabel.setText(Integer.toString(stats.riverFolds));
      botWinsLabel.setText(Integer.toString(stats.wins));
      botRaisesLabel.setText(Integer.toString(stats.raises));
      botAverageWinningsLabel.setText(Double.toString(stats.averageWinningPerHand));
      botWinningsLabel.setText(Integer.toString(stats.winnings));
      botBestHandLabel.setText(stats.bestHand.toString());
      botBiggestWinLabel.setText(Integer.toString(stats.biggestWin));
   }

   public void toggleDealerZero(boolean dealer) {
      dealerImage0.setVisible(dealer);
   }

   public void toggleDealerOne(boolean dealer) {
      dealerImage1.setVisible(dealer);
   }

   public void updateHoleCards(HoleCards cards) {
      cardRank0.setText(cards.getCard1().getRankString());
      cardSuitSmall0.setImage(mainApp.getImage(cards.getCard1()));
      cardSuitBig0.setImage(mainApp.getImage(cards.getCard1()));
      
      cardRank1.setText(cards.getCard2().getRankString());
      cardSuitSmall1.setImage(mainApp.getImage(cards.getCard2()));
      cardSuitBig1.setImage(mainApp.getImage(cards.getCard2()));

      cardRank7.setText(null);
      cardSuitSmall7.setImage(null);
      cardSuitBig7.setImage(null);

      cardRank8.setText(null);
      cardSuitSmall8.setImage(null);
      cardSuitBig8.setImage(null);
   }

   public void showBotHoleCards(HoleCards cards) {
      cardRank7.setText(cards.getCard1().getRankString());
      cardSuitSmall7.setImage(mainApp.getImage(cards.getCard1()));
      cardSuitBig7.setImage(mainApp.getImage(cards.getCard1()));

      cardRank8.setText(cards.getCard2().getRankString());
      cardSuitSmall8.setImage(mainApp.getImage(cards.getCard2()));
      cardSuitBig8.setImage(mainApp.getImage(cards.getCard2()));
   }

   public void updateThreeCards(ArrayList<Card> cards) {
      cardRank2.setText(cards.get(0).getRankString());
      cardSuitSmall2.setImage(mainApp.getImage(cards.get(0)));
      cardSuitBig2.setImage(mainApp.getImage(cards.get(0)));

      cardRank3.setText(cards.get(1).getRankString());
      cardSuitSmall3.setImage(mainApp.getImage(cards.get(1)));
      cardSuitBig3.setImage(mainApp.getImage(cards.get(1)));

      cardRank4.setText(cards.get(2).getRankString());
      cardSuitSmall4.setImage(mainApp.getImage(cards.get(2)));
      cardSuitBig4.setImage(mainApp.getImage(cards.get(2)));
   }

   public void updateFourCards(ArrayList<Card> cards) {
      updateThreeCards(cards);
      cardRank5.setText(cards.get(3).getRankString());
      cardSuitSmall5.setImage(mainApp.getImage(cards.get(3)));
      cardSuitBig5.setImage(mainApp.getImage(cards.get(3)));
   }

   public void updateFiveCards(ArrayList<Card> cards) {
      updateFourCards(cards);
      cardRank6.setText(cards.get(4).getRankString());
      cardSuitSmall6.setImage(mainApp.getImage(cards.get(4)));
      cardSuitBig6.setImage(mainApp.getImage(cards.get(4)));
   }

   public void updateCommunityCards(ArrayList<Card> cards) {
      switch (cards.size()) {
      case 0:
         cardRank2.setText("");
         cardSuitSmall2.setImage(null);
         cardSuitBig2.setImage(null);

         cardRank3.setText("");
         cardSuitSmall3.setImage(null);
         cardSuitBig3.setImage(null);

         cardRank4.setText("");
         cardSuitSmall4.setImage(null);
         cardSuitBig4.setImage(null);

         cardRank5.setText("");
         cardSuitSmall5.setImage(null);
         cardSuitBig5.setImage(null);

         cardRank6.setText("");
         cardSuitSmall6.setImage(null);
         cardSuitBig6.setImage(null);

         break;
      case 3:
         updateThreeCards(cards);
         break;
      case 4:
         updateFourCards(cards);
         break;
      case 5:
         updateFiveCards(cards);
         break;
      }
   }
   
   @FXML
   private void botProfileSet(ActionEvent e) {
      // Set bot to different profile
      Profile profile = profileBox.getSelectionModel().getSelectedItem();

      if (this.mainApp.poker != null) {
         this.mainApp.poker.bot.changeProfile(profile);
      }
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

# Poker-Bot

## To Do:
### Game
* know when someone is all in - create side pot if two or more other players in the hand
* Get started on the GUI

### Bot
* actions based on pot odds
* preflop and postflop decision making for raising, calling, folding
* factors to consider
* if there is a raise and how large it is
* position of the Bot
* stack size of the Bot relative to other players
* what type of hand the bot has:
* if the bot has a hand, how strong is it relative to the board (using HandStrength and  dealer.communityCards)
* ex: if the pair is on the board or in hand, number of overcards, draws (gutshot, openended, flush) -> (4 cards needed)


## Ideas:
* possibly configuration/settings file for blind amounts, starting amounts, how many hands or amount of time between upping the blinds - could be done through GUI as splash screen
* Record player actions so the bot can learn how its opponent plays and it can build a profile on them.
* Preflop - Bot uses lookup table for hole card strength and counts number of players raises to estimate player hand strength and make correct moves
* Postflop - Bot uses pot odds to make correct moves and win the hand vs the player. The bot calculates pot odds by dividing the number of chips required to call by the total number of chips in the pot. The bot will then use the calculated pot odds to determine if it will make the correct move based on the information of the player's hole cards.
* side pots: creation of a pot class, each pot would know the players contributing to it, its current size, and the most recent bet, dealer would have an arraylist of pots, betPeriod would determine the pot that is active for each player, determineWinner would loo through all pots

## Sources:
* card suit icons available on icons8.com

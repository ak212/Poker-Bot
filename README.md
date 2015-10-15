# Poker-Bot

# To Do:
* hand evaluation - most valuable hand created from 5 of the (up to) 7 cards available
* based on hand evaluation, who wins after river
* know when someone is all in - create side pot if two or more other players in the hand
* get started on planning bot

# Ideas:
* possibly configuration/settings file for blind amounts, starting amounts, how many hands or amount of time between upping the blinds
* bot(s) have a listener for an event dispatched telling them it's their turn
* this change event would also have the moves of players acting before, basically all member variables from Dealer. This would be the environment the agent takes in. The bot (agent) takes in this environment, comes up with the proper action response.

* Preflop - Bot uses lookup table for hole card strength and counts number of players raises to estimate player hand strength and make correct moves
* Postflop - Bot uses pot odds to make correct moves and win the hand vs the player. The bot calculates pot odds by dividing the number of chips required to call by the total number of chips in the pot. The bot will then use the calculated pot odds to determine if it will make the correct move based on the information of the player's hole cards.
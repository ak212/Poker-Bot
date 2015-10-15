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
* card suit icons available on icons8.com

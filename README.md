# WALLET APP

## What this application does
 The wallet app is a digital wallet that
 allows you to add your credit and debit cards digitally to a wallet
 on your phone and perform secure transactions, along with view its details as required.

## Who will use this application
This application will be used by anyone who owns credit or debit cards
and wants to avoid carrying them or losing them to misplacement or theft,
whilst being able to access all of their cards functionality and more.
The app is extremely user-friendly and easy to use and thus will be used
by a large number of people. 

## Why did I choose this project
One of the things that made me fall in love with technology and programming is the ability 
simplify and digitize processes and make life easier for all humans.
The transition from a physical bulky wallet to an app with a few touches to perform the same 
operations using an e-wallet is a great example of this, and that's what motivated me to make this project.


## User Stories
- *As a user, I want to be able to* add a new credit/debit card to my wallet
- *As a user, I want to be able to* view a list of all cards in my wallet
- *As a user, I want to be able to* perform a payment with my current card
- *As a user, I want to be able to* view all transactions on my current card
- *As a user, I want to be able to* remove a card of my choice from the wallet
- *As a user, I want to be able to* switch my current card with any other card in the wallet
- *As a user, I want to be able to* see a depiction of my current card with relevant information
- *As a user, I want to be able to* view the remaining credit on my credit cards
- *As a user, I want to be able to* view the remaining balance on my debit cards
- *As a user, I want to be able to* save all cards, their transactions, and balances in my wallet
- *As a user, I want to be able to* reload my wallet and all its cards, transactions and balances.


## Phase 4: Task 2
- Credit card from bank TD Bank belonging to Jamie S was added successfully on Thu Mar 31 17:58:55 PDT 2022
- Debit card from bank ScotiaBank belonging to Maan H was added successfully on Thu Mar 31 17:59:09 PDT 2022
- Unsuccessful : card could not be removed on Thu Mar 31 17:59:14 PDT 2022
- Current card was switched successfully on Thu Mar 31 17:59:17 PDT 2022
- Card at index 1 was removed successfully on Thu Mar 31 17:59:21 PDT 2022
- Wallet was saved on Thu Mar 31 17:59:24 PDT 2022

# Phase 4: Task 3
- I would convert the card interface into an abstract super class
- It would contain completed functionality for all common methods between CreditCard and DebitCard and abstract methods to be implemented by the sub-classes (CreditCard and DebitCard)
- I would also want to establish a bidirectional association between Wallet and CreditCard/DebitCard
- By doing so, each card itself would also have a field for the wallet it is assigned to. Thus, any changes made to a card in the wallet class would also reflect in the card class, which seems like an appropriate situation to implement a bi-directional relationship
- I would abstract repetitive/identical snippets of code(eg. styling of buttons, JFrame background and size) into functions that can be called for each JFrame component, without having to re-write any code. This would improve functionality and readability.


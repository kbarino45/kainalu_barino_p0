package dev.barino.app;

import dev.barino.models.Account;
import dev.barino.repositories.AccountRepo;
import dev.barino.repositories.AccountRepoDBImpl;

import java.util.InputMismatchException;
import java.util.Scanner;

public class Start {

    public static void main(String[] args) {
        Scanner stdIn = new Scanner(System.in);
        char response; //User types an option

        while(true) {
            do {
                System.out.println("+=====================================+");
                System.out.println("| Welcome. What would you like to do? |");
                System.out.println("+-------------------------------------+");
                System.out.println("| Options:                            |");
                System.out.println("|  L: Log into an existing account    |");
                System.out.println("|  N: Open a new account              |");
                System.out.println("|  Q: Quit                            |");
                System.out.println("+-------------------------------------+");
                System.out.print("Select: ");

                //StringIndexOutOfBoundsException; if the user presses Enter without typing
                //i.e. catches an empty input.
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                }
            } while (response != 'l' && response != 'n' && response != 'q');
            //end do (Input validation)

            if (response == 'n') {
                createNewAccount(stdIn);
            }
            else if (response == 'l') {
                loginToAccount(stdIn);
            }
            else {
                break;
            }

        } //end While (Menu)
        stdIn.close();

    }

    /**
     * Menu options for logging in to an account.
     * @param stdIn Scanner
     */
    private static void loginToAccount(Scanner stdIn) {
        char response;
        String pw;
        int accountID;

        while(true) {
            do {
                System.out.println("+=====================================+");
                System.out.println("| LOG  IN                             |");
                System.out.println("+-------------------------------------+");
                System.out.println("| Options:                            |");
                System.out.println("|  L: Log In                          |");
                System.out.println("|  Q: Go Back                         |");
                System.out.println("+-------------------------------------+");
                System.out.print("Select: ");

                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                }
            } while (response != 'l' && response != 'q');
            //end do (Input Validation)

            if (response == 'l') {
                AccountRepo ar = new AccountRepoDBImpl();
                System.out.print("Account Number: ");

                //NumberFormatException; if the user doesn't type an integer
                try {
                    accountID = Integer.parseInt(stdIn.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid account number.");
                    continue;
                }

                //Unreachable unless an integer is given
                if (ar.getAccount(accountID) != null) {
                    Account userAccount;
                    userAccount = ar.getAccount(accountID);
                    System.out.print("Password: ");
                    pw = stdIn.nextLine();
                    if (!pw.equals(userAccount.getPw())) {
                        System.out.println("Invalid password.");
                        continue;
                    } else {
                        signedIn(stdIn, accountID);
                    }
                } else {
                    System.out.println("Account not found.");
                    continue;
                } //end if-else (Password Validation)

            } //end If (Login Credentials)

            break;
        } //end While (Menu)
    }

    /**
     * Menu options for a logged-in user.
     * @param stdIn Scanner
     * @param id account_id
     */
    private static void signedIn(Scanner stdIn, int id) {
        AccountRepo ar = new AccountRepoDBImpl();
        Account user = ar.getAccount(id);
        char response;              //User input for menu options
        boolean isDeleted = false;  //Used if, under settings, the user deletes their account

        while(true) {
            do {
                System.out.println("+=====================================+");
                System.out.println("| YOUR ACCOUNT                        |");
                System.out.println("+-------------------------------------+");
                System.out.println("Owner: " + user.getFirstName() + " " + user.getLastName());
                System.out.println("Balance: $" + user.displayBalance());
                System.out.println("Account #: " + user.getAccountID());
                System.out.println("+-------------------------------------+");
                System.out.println("| Options:                            |");
                System.out.println("|  W: Withdraw      D: Deposit        |");
                System.out.println("|  S: Settings      Q: Sign Out       |");
                System.out.println("+-------------------------------------+");
                System.out.print("Select: ");

                //Doesn't accept an empty input
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                }
            } while (response != 'w' && response != 'd' && response != 's' && response != 'q');
            //end do loop (response validation)

            switch(response) {
                case 'w': withdraw(stdIn, user, ar);
                break;
                case 'd': deposit(stdIn, user, ar);
                break;
                case 's': isDeleted = goToSettings(stdIn, user, ar);
                break;
            }
            if (response == 'q') break;

            //Returns the user to the main menu if the account is deleted
            if (isDeleted) {
                break;
            }

        } //end While (Menu Options)
    }

    /**
     * Menu options for a user accessing their account settings.
     * @param stdIn Scanner
     * @param user Account (signed-in account)
     * @param ar AccountRepo
     * @return boolean (isDeleted)
     */
    private static boolean goToSettings(Scanner stdIn, Account user, AccountRepo ar) {
        char response;              //User input selection
        String firstName;           //User changes their firstname
        String lastName;            //User changes their lastname
        String pw;                  //User changes their password
        boolean isDeleted = false;  //return result

        while (true) {
            do {
                System.out.println("+=====================================+");
                System.out.println("| SETTINGS                            |");
                System.out.println("+-------------------------------------+");
                System.out.println("| Options:                            |");
                System.out.println("|  N: Change Name  P: Change password |");
                System.out.println("|  D: Delete Account                  |");
                System.out.println("|  Q: Go Back                         |");
                System.out.println("+-------------------------------------+");
                System.out.print("Select: ");

                //Doesn't accept empty inputs
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                }
            } while (response != 'n' && response != 'p' && response != 'd' && response != 'q');
            //end do-while (input validation)

            //Changes to firstname and lastname
            if (response == 'n') {
                System.out.print("First Name: ");
                do {
                    firstName = stdIn.nextLine();
                    if (firstName.length() == 0) {
                        System.out.println("Names must contain at least 1 character.");
                        System.out.print("First Name: ");
                    }
                } while (firstName.length() == 0);
                //end firstname validation

                System.out.print("Last Name: ");
                do {
                    lastName = stdIn.nextLine();
                    if (lastName.length() == 0) {
                        System.out.println("Names must contain at least 1 character.");
                        System.out.print("Last Name: ");
                    }
                } while (lastName.length() == 0);
                //end lastname validation

                user.setFirstName(firstName);
                user.setLastName(lastName);
                ar.updateAccount(user);
            } //end Change Name

            //changes to password
            if (response == 'p') {
                System.out.print("Password: ");
                do {
                    pw = stdIn.nextLine();
                    if (pw.length() < 5 || pw.length() > 20) {
                        System.out.println("Password must be 5 to 20 characters long.");
                        System.out.print("Password: ");
                    }
                } while (pw.length() < 5 || pw.length() > 20);
                //end password validation

                user.setPw(pw);
                ar.updateAccount(user);
            } //end change password

            //delete account (with zero balance)
            if (response == 'd') {
                System.out.println("+:::::::::::::::::::::::::::::::::::::+");
                System.out.println("| WARNING: This action cannot be      |");
                System.out.println("| undone. Be sure you withdraw all    |");
                System.out.println("| funds and your account is empty.    |");
                System.out.println("+:::::::::::::::::::::::::::::::::::::+");
                do {
                    System.out.print("Continue? Y/N: ");
                    try {
                        response = stdIn.nextLine().toLowerCase().charAt(0);
                    } catch (StringIndexOutOfBoundsException e) {
                        response = ' ';
                    }
                } while (response != 'y' && response != 'n');
                //end confirmation validation

                //yes/no options
                if (response == 'n') {
                    continue;
                }
                else {
                    //check for empty account
                    if (user.getBalance() > 0) {
                        System.out.println("Your account is not empty.");
                        continue;
                    }
                    else {
                        ar.deleteAccount(user.getAccountID());
                        System.out.println("Your account has been deleted.");
                        isDeleted = true;
                        break;
                    }
                } //end if-else for successful delete

            } //end Delete Option

            if (response == 'q') {
                break;
            }

        } //end while (Menu)

        return isDeleted; //result
    }

    /**
     * Deposit logic
     * @param stdIn Scanner
     * @param user Account (signed-in)
     * @param ar AccountRepo
     */
    private static void deposit(Scanner stdIn, Account user, AccountRepo ar) {
        double amount;

        //InputMismatchException; if the user doesn't input a double
        try {
            System.out.print("Deposit Amount: ");
            amount = stdIn.nextDouble();

            //User doesn't enter a negative amount
            if (amount >= 0) {
                user.setBalance(user.getBalance() + amount);
                ar.updateAccount(user);
            }
            else {
                System.out.println("Error. Choose to withdraw instead.");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid amount.");
        }
        finally {
            //consumes \n token
            stdIn.nextLine();
        } // end try-catch for deposit validation
    }

    /**
     * Withdraw logic
     * @param stdIn Scanner
     * @param user Account (signed-in)
     * @param ar AccountRepo
     */
    private static void withdraw(Scanner stdIn, Account user, AccountRepo ar) {
        double amount;

        //InputMismatch; if the user doesn't input a double
        try {
            System.out.print("Withdrawal Amount: ");
            amount = stdIn.nextDouble();

            //User must have sufficient funds
            if (user.getBalance() >= amount) {

                //Amount must be positive
                if (user.getBalance() > 0) {
                    user.setBalance(user.getBalance() - amount);
                    ar.updateAccount(user);
                }
                else {
                    System.out.println("Error. Negative amount.");
                }
            }
            else {
                System.out.println("Insufficient funds.");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid amount.");
        }
        finally {
            //consumes \n token
            stdIn.nextLine();
        } //end try-catch for amount validation
    }

    /**
     * Menu options for creating a new account.
     * @param stdIn Scanner
     */
    private static void createNewAccount(Scanner stdIn) {
        char response = ' ';    //User option input
        String pw;              //Password for new account
        String firstName;       //Firstname for new account
        String lastName;        //Lastname for new account

        do {
            System.out.println("+=====================================+");
            System.out.println("| NEW ACCOUNT                         |");
            System.out.println("+-------------------------------------+");
            System.out.println("| Options:                            |");
            System.out.println("|  N: Open a new account.             |");
            System.out.println("|  Q: Go Back.                        |");
            System.out.println("+-------------------------------------+");
            System.out.print("Select: ");

            //Doesn't accept empty strings
            try {
                response = stdIn.nextLine().toLowerCase().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                System.out.println("Choose a valid option.");
            }
        } while (response != 'n' && response != 'q');
        //end do-while (input validation)

        //Create new account steps
        if (response == 'n') {
            AccountRepo ar = new AccountRepoDBImpl();
            System.out.print("Password: ");
            do {
                pw = stdIn.nextLine();
                if (pw.length() < 5 || pw.length() > 20) {
                    System.out.println("Password must be 5 to 20 characters long.");
                    System.out.print("Password: ");
                }
            } while (pw.length() < 5 || pw.length() > 20);
            //end password validation

            System.out.print("First Name: ");
            do {
                firstName = stdIn.nextLine();
                if (firstName.length() == 0) {
                    System.out.println("Names must contain at least 1 character.");
                    System.out.print("First Name: ");
                }
            } while (firstName.length() == 0);
            //end firstname validation

            System.out.print("Last Name: ");
            do {
                lastName = stdIn.nextLine();
                if (lastName.length() == 0) {
                    System.out.println("Names must contain at least 1 character.");
                    System.out.print("Last Name: ");
                }
            } while (lastName.length() == 0);
            //end lastname validation

            //Create record receipt
            Account a = ar.addAccount(new Account(pw, firstName, lastName, 0));
            System.out.println("+=====================================+");
            System.out.println("| ACCOUNT CREATED                     |");
            System.out.println("+-------------------------------------+");
            System.out.println("Account Number: " + a.getAccountID());
            System.out.println("You will need your account number and");
            System.out.println("password to sign into your account.");

        } //end if (account creation)
    }

}

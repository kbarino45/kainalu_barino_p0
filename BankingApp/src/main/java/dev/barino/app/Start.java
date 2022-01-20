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
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                }
            } while (response != 'l' && response != 'n' && response != 'q');

            if (response == 'n') {
                createNewAccount(stdIn);
            }
            else if (response == 'l') {
                loginToAccount(stdIn);
            }
            else {
                break;
            }

        } //end While

    }

    private static void loginToAccount(Scanner stdIn) {
        char response = ' ';
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
                    continue;
                }
            } while (response != 'l' && response != 'q');

            if (response == 'l') {
                AccountRepo ar = new AccountRepoDBImpl();
                System.out.print("Account Number: ");
                try {
                    accountID = Integer.parseInt(stdIn.nextLine());
                } catch (NumberFormatException e) {
                    System.out.println("Invalid account number.");
                    continue;
                } finally {
                    response = ' ';
                }

                if (ar.getAccount(accountID) != null) {
                    Account userAccount = new Account();
                    userAccount = ar.getAccount(accountID);
                    System.out.print("Password: ");
                    pw = stdIn.nextLine();
                    if (!pw.equals(userAccount.getPw())) {
                        System.out.println("Invalid password.");
                        continue;
                    } else {
                        signedIn(stdIn, userAccount, accountID);
                    }
                } else {
                    System.out.println("Account not found.");
                    continue;
                }
            } //end If (Login Credentials)

            break;
        } //end While (Menu)
    }

    private static void signedIn(Scanner stdIn, Account user, int id) {
        char response = ' ';
        double amount = 0.0;
        AccountRepo ar = new AccountRepoDBImpl();
        user = ar.getAccount(id);
        boolean isDeleted = false;

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
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                    continue;
                }
            } while (response != 'w' && response != 'd' && response != 's' && response != 'q');
            //end While (response validation)

            if (response == 'w') {
                withdraw(stdIn, user, ar);
            }

            if (response == 'd') {
                deposit(stdIn, user, ar);
            }

            if (response == 's') {
                isDeleted = goToSettings(stdIn, user, ar);
            }

            if (response == 'q') {
                break;
            }

            if (isDeleted) {
                break;
            }

            response = ' ';
        } //end While (Menu Options)
    }

    private static boolean goToSettings(Scanner stdIn, Account user, AccountRepo ar) {
        char response = ' ';
        String firstName;
        String lastName;
        String pw;
        boolean isDeleted = false;

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
                try {
                    response = stdIn.nextLine().toLowerCase().charAt(0);
                } catch (StringIndexOutOfBoundsException e) {
                    response = ' ';
                    continue;
                }
            } while (response != 'n' && response != 'p' && response != 'd' && response != 'q');

            if (response == 'n') {
                System.out.print("First Name: ");
                do {
                    firstName = stdIn.nextLine();
                    if (firstName.length() == 0) {
                        System.out.println("Names must contain at least 1 character.");
                        System.out.print("First Name: ");
                    }
                } while (firstName.length() == 0);
                System.out.print("Last Name: ");
                do {
                    lastName = stdIn.nextLine();
                    if (lastName.length() == 0) {
                        System.out.println("Names must contain at least 1 character.");
                        System.out.print("Last Name: ");
                    }
                } while (lastName.length() == 0);
                user.setFirstName(firstName);
                user.setLastName(lastName);
                ar.updateAccount(user);
            } //end Change Name

            if (response == 'p') {
                System.out.print("Password: ");
                do {
                    pw = stdIn.nextLine();
                    if (pw.length() < 5 || pw.length() > 20) {
                        System.out.println("Password must be 5 to 20 characters long.");
                        System.out.print("Password: ");
                    }
                } while (pw.length() < 5 || pw.length() > 20);
                user.setPw(pw);
                ar.updateAccount(user);
            } //end Password option

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
                        continue;
                    }
                } while (response != 'y' && response != 'n');

                if (response == 'n') {
                    response = ' ';
                    continue;
                } else {
                    if (user.getBalance() > 0) {
                        System.out.println("Your account is not empty.");
                        response = ' ';
                        continue;
                    }
                    else {
                        ar.deleteAccount(user.getAccountID());
                        System.out.println("Your account has been deleted.");
                        isDeleted = true;
                        break;
                    }
                }

            } //end Delete Option

            if (response == 'q') {
                break;
            }

        } //end while (Menu)

        return isDeleted;

    }

    private static void deposit(Scanner stdIn, Account user, AccountRepo ar) {
        double amount;
        try {
            System.out.print("Deposit Amount: ");
            amount = stdIn.nextDouble();
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
            stdIn.nextLine();
        }
    }

    private static void withdraw(Scanner stdIn, Account user, AccountRepo ar) {
        double amount;
        try {
            System.out.print("Withdrawal Amount: ");
            amount = stdIn.nextDouble();
            if (user.getBalance() >= amount) {
                user.setBalance(user.getBalance() - amount);
                ar.updateAccount(user);
            }
            else {
                System.out.println("Insufficient funds.");
            }
        }
        catch (InputMismatchException e) {
            System.out.println("Invalid amount.");
        }
        finally {
            stdIn.nextLine();
        }
    }

    private static void createNewAccount(Scanner stdIn) {
        char response = ' ';
        String pw = "";
        String firstName = "";
        String lastName = "";

        do {
            System.out.println("+=====================================+");
            System.out.println("| NEW ACCOUNT                         |");
            System.out.println("+-------------------------------------+");
            System.out.println("| Options:                            |");
            System.out.println("|  N: Open a new account.             |");
            System.out.println("|  Q: Go Back.                        |");
            System.out.println("+-------------------------------------+");
            System.out.print("Select: ");
            try {
                response = stdIn.nextLine().toLowerCase().charAt(0);
            } catch (StringIndexOutOfBoundsException e) {
                continue;
            }
        } while (response != 'n' && response != 'q');

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
            System.out.print("First Name: ");
            do {
                firstName = stdIn.nextLine();
                if (firstName.length() == 0) {
                    System.out.println("Names must contain at least 1 character.");
                    System.out.print("First Name: ");
                }
            } while (firstName.length() == 0);
            System.out.print("Last Name: ");
            do {
                lastName = stdIn.nextLine();
                if (lastName.length() == 0) {
                    System.out.println("Names must contain at least 1 character.");
                    System.out.print("Last Name: ");
                }
            } while (lastName.length() == 0);
            Account a = ar.addAccount(new Account(pw, firstName, lastName, 0));
            System.out.println("+=====================================+");
            System.out.println("| ACCOUNT CREATED                     |");
            System.out.println("+-------------------------------------+");
            System.out.println("Account Number: " + a.getAccountID());
            System.out.println("You will need your account number and");
            System.out.println("password to sign into your account.");

        }
    }

}

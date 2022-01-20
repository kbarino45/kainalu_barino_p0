package dev.barino.repositories;
import dev.barino.models.Account;

//Implementation outline
public interface AccountRepo {

    public Account addAccount(Account a);
    public Account getAccount(int id);
    //public UserAccounts getAllAccounts();
    public Account updateAccount(Account change);
    public void deleteAccount(int id);

}

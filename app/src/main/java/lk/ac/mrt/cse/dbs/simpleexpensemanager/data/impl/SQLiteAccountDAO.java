package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

/**
 * Created by Yohan on 12/6/2015.
 */
public class SQLiteAccountDAO implements AccountDAO {
    SQLiteDatabase dataBase;
    java.io.File filename = Constants.CONTEXT.getFilesDir();

    public SQLiteAccountDAO(String dbName) {
        dataBase = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath()+"/"+dbName, null);
        dataBase.execSQL("CREATE TABLE IF NOT EXISTS Account(account_no VARCHAR(30),bank VARCHAR(50),account_holder VARCHAR(50), balance NUMERIC(10,2));");
    }

    @Override
    public List<String> getAccountNumbersList() {
        ArrayList<String> accountList = new ArrayList<String>();
        Cursor results = dataBase.rawQuery("Select account_no from Account", null);
        results.moveToFirst();
        while (!results.isAfterLast()) {
            accountList.add(results.getString(0));
            results.moveToNext();
        }
        return accountList;
    }

    @Override
    public List<Account> getAccountsList() {
        ArrayList<Account> accountList = new ArrayList<Account>();
        Cursor results = dataBase.rawQuery("Select * from Account", null);
        results.moveToFirst();
        while (!results.isAfterLast()) {
            accountList.add(new Account(results.getString(0),results.getString(1),results.getString(2),Double.parseDouble(results.getString(3))));
            results.moveToNext();
        }
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {
        Cursor results = dataBase.rawQuery("Select * from Account where account_no='" + accountNo+"'", null);
        results.moveToFirst();
        if(results.isAfterLast()){
            throw new InvalidAccountException("Invalid account no");
        }
        return new Account(results.getString(0),results.getString(1),results.getString(2),Double.parseDouble(results.getString(3)));
    }

    @Override
    public void addAccount(Account account) {
        dataBase.execSQL("INSERT INTO Account VALUES('"+account.getAccountNo()+"','"+account.getBankName()+"','"+account.getAccountHolderName()+"','"+account.getBalance()+"');");
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        dataBase.execSQL("DELETE FROM Account WHERE account_no='"+accountNo+"';");
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account=getAccount(accountNo);
        double balance=account.getBalance();
        if(ExpenseType.INCOME==expenseType){
            balance += amount;
        }
        else{
                balance -=amount;
        }
        dataBase.execSQL("UPDATE Account SET balance='"+balance+"' WHERE account_no='"+accountNo+"'");
    }
}

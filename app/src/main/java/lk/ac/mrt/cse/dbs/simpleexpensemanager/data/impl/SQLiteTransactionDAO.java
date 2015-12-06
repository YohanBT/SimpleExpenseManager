package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.Constants;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

/**
 * Created by Yohan on 12/6/2015.
 */
public class SQLiteTransactionDAO implements TransactionDAO {
    SQLiteDatabase dataBase;
    java.io.File filename = Constants.CONTEXT.getFilesDir();

    public SQLiteTransactionDAO(String dbName) {
        dataBase = SQLiteDatabase.openOrCreateDatabase(filename.getAbsolutePath()+"/"+dbName, null);
        dataBase.execSQL("CREATE TABLE IF NOT EXISTS Transactions(account_no VARCHAR(50),expense_type VARCHAR(50),amount NUMERIC(10,2), date_value Date);");
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        String expencetype;
        if(expenseType==expenseType.EXPENSE){
            expencetype="EXPENSE";
        }
        else{
            expencetype="INCOME";
        }
        dataBase.execSQL("INSERT INTO Transactions VALUES('"+accountNo+"','"+expencetype+"','"+amount+"','"+date.toString()+"');");
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        Cursor results = dataBase.rawQuery("Select * from Transactions", null);
        ExpenseType expenseType;
        results.moveToFirst();
        while (!results.isAfterLast()) {
            if(results.getString(1).toUpperCase().contains("EXPENSE")){
                expenseType=ExpenseType.EXPENSE;
            }
            else{
                expenseType=ExpenseType.INCOME;
            }
            transactionList.add(new Transaction(new Date(results.getString(3)),results.getString(0),expenseType, Double.parseDouble(results.getString(2) ) ));
            results.moveToNext();
        }
        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {
        ArrayList<Transaction> transactionList = new ArrayList<Transaction>();
        Cursor results = dataBase.rawQuery("Select * from Transactions ORDER BY date_value LIMIT "+limit,null);
        ExpenseType expenseType;
        results.moveToFirst();
        while (!results.isAfterLast()) {
            System.out.print(results.getString(1));
            System.out.println(results.getString(1).toUpperCase().contains("EXPENSE"));
            if (results.getString(1).toUpperCase().contains("EXPENSE")){
                expenseType=ExpenseType.EXPENSE;
            }
            else{
                expenseType=ExpenseType.INCOME;
            }
            transactionList.add(new Transaction(new Date(results.getString(3)),results.getString(0),expenseType, Double.parseDouble(results.getString(2) ) ));
            results.moveToNext();
        }
        return transactionList;
    }
}

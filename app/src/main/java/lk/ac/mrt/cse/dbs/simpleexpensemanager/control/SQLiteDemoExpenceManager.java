package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.control.exception.ExpenseManagerException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.SQLiteTransactionDAO;

/**
 * Created by Yohan on 12/7/2015.
 */
public class SQLiteDemoExpenceManager extends ExpenseManager{
    String dbName="1302733P";

    public SQLiteDemoExpenceManager(){
        setup();
    }

    @Override
    public void setup() {
        SQLiteAccountDAO sqliteAccountDAO=new SQLiteAccountDAO(dbName);
        setAccountsDAO(sqliteAccountDAO);

        SQLiteTransactionDAO sqliteTransactionDAO=new SQLiteTransactionDAO(dbName);
        setTransactionsDAO(sqliteTransactionDAO);
    }
}

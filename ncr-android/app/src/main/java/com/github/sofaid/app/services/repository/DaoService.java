package com.github.sofaid.app.services.repository;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

import com.github.sofaid.app.NcrApplication;
import com.github.sofaid.app.injection.ApplicationContext;
import com.github.sofaid.app.models.db.Account;
import com.github.sofaid.app.models.db.AccountDao;
import com.github.sofaid.app.models.db.DaoSession;
import com.github.sofaid.app.models.db.Share;
import com.github.sofaid.app.models.db.ShareDao;
import com.github.sofaid.app.models.db.Transaction;
import com.github.sofaid.app.models.db.TransactionDao;

import java.util.Date;

/**
 * Service to query and save all kinds of entities to database.
 * Created by robik on 6/14/17.
 */

@Singleton
public class DaoService {
    private final Context context;
    private final AccountDao accountDao;
    private final TransactionDao transactionDao;

    public ShareDao getShareDao() {
        return shareDao;
    }

    private final ShareDao shareDao;

    @Inject
    public DaoService(@ApplicationContext Context context) {
        this.context = context;
        DaoSession daoSession = NcrApplication.getInstance().getDaoSession();
        this.accountDao = daoSession.getAccountDao();
        this.transactionDao = daoSession.getTransactionDao();
        this.shareDao = daoSession.getShareDao();
    }


    /**
     * Saves {@link Account}
     *
     * @param account
     */
    public void saveAccount(Account account) {
        accountDao.save(account);
    }

    /**
     * Saves {@link Transaction}
     *
     * @param transaction
     */
    public void saveTransaction(Transaction transaction) {
        transaction.setUpdatedOn(new Date());
        transactionDao.save(transaction);
    }

    public void saveShare(Share share){
        shareDao.save(share);
    }

}

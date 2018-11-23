package com.haxi.mh.utils.dbutil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.haxi.mh.MyApplication;
import com.haxi.mh.utils.im.IMConstants;

import java.io.File;

/**
 * 加密数据库
 * Created by Han on 2018/10/18
 * Email:yin13753884368@163.com
 * CSDN:http://blog.csdn.net/yin13753884368/article
 * Github:https://github.com/yin13753884368
 */

public class DBEncrypt {
    public static DBEncrypt dBencrypt;
    private Boolean isOpen = true;

    public static DBEncrypt getInstences() {
        if (dBencrypt == null) {
            synchronized (DBEncrypt.class) {
                if (dBencrypt == null) {
                    dBencrypt = new DBEncrypt();
                }
            }
        }
        return dBencrypt;
    }

    /**
     * 如果有旧表 先加密数据库
     *
     * @param passphrase
     */
    public void encrypt(String passphrase) {
        Context context = MyApplication.getMyApplication();
        File file = new File("/data/data/" + context.getPackageName() + "/databases/" + IMConstants.DB_NAME);
        if (file.exists()) {
            if (isOpen) {
                try {
                    File newFile = File.createTempFile("sqlcipherutils", "tmp", context.getCacheDir());

                    net.sqlcipher.database.SQLiteDatabase db = net.sqlcipher.database.SQLiteDatabase.openDatabase(
                            file.getAbsolutePath(), "", null, SQLiteDatabase.OPEN_READWRITE);

                    db.rawExecSQL(String.format("ATTACH DATABASE '%s' AS encrypted KEY '%s';",
                            newFile.getAbsolutePath(), passphrase));
                    db.rawExecSQL("SELECT sqlcipher_export('encrypted')");
                    db.rawExecSQL("DETACH DATABASE encrypted;");

                    int version = db.getVersion();
                    db.close();

                    db = net.sqlcipher.database.SQLiteDatabase.openDatabase(newFile.getAbsolutePath(),
                            passphrase, null,
                            SQLiteDatabase.OPEN_READWRITE);

                    db.setVersion(version);
                    db.close();
                    file.delete();
                    newFile.renameTo(file);
                    isOpen = false;
                } catch (Exception e) {
                    isOpen = false;
                }
            }
        }
    }
}

/*
 * Copyright 2016 Thomas Hoffmann
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.xmexe.exe.crypter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.provider.BaseColumns;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Database extends SQLiteOpenHelper {

    private final static String DB_NAME = "encrypted_db";
    private final static int DB_VERSION = 1;

    static class EncryptedFilesContract implements BaseColumns {
        private final static String TABLE_NAME = "encrypted_files";
        private final static String COLUMN_FILENAME = "name";
        private final static String COLUMN_MIME = "mime";
        private final static String COLUMN_URI = "uri";
        private final static String COLUMN_SIZE = "size";
        private final static String[] ALL_COLUMNS =
                new String[]{_ID, COLUMN_FILENAME, COLUMN_MIME, COLUMN_URI, COLUMN_SIZE};
    }

    Database(final Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(final SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + EncryptedFilesContract.TABLE_NAME + " (" +
                EncryptedFilesContract._ID + " INTEGER PRIMARY KEY," +
                EncryptedFilesContract.COLUMN_FILENAME +
                " TEXT, " + EncryptedFilesContract.COLUMN_MIME + " TEXT, " +
                EncryptedFilesContract.COLUMN_URI +
                " TEXT, " + EncryptedFilesContract.COLUMN_SIZE + " INTEGER)");
    }

    /**
     * Adds the given file to the database of isEncrypted files
     *
     * @param file the isEncrypted file
     * @return the entry id
     */
    long addFile(final File file) {
      LogUtils.d("增加记录");
        ContentValues values = new ContentValues();
        values.put(EncryptedFilesContract.COLUMN_FILENAME, file.getName());
//        values.put(EncryptedFilesContract.COLUMN_MIME, file.g);
        values.put(EncryptedFilesContract.COLUMN_URI, file.getAbsolutePath());
        values.put(EncryptedFilesContract.COLUMN_SIZE, file.getTotalSpace());
        return getWritableDatabase().insert(EncryptedFilesContract.TABLE_NAME, null, values);
    }

    /**
     * Deletes a file from the database
     *
     * @param id the id of the entry to delete
     */
    void deleteFile(final long id) {
        getWritableDatabase()
                .delete(EncryptedFilesContract.TABLE_NAME, EncryptedFilesContract._ID + " = ?",
                        new String[]{String.valueOf(id)});
    }

  void deleteFile(final String path) {
    getWritableDatabase()
      .delete(EncryptedFilesContract.TABLE_NAME, EncryptedFilesContract.COLUMN_URI + " = ?",
        new String[]{String.valueOf(path)});
    LogUtils.d("删除数据");

  }

  void removeAll(){
    getWritableDatabase()
      .delete(EncryptedFilesContract.TABLE_NAME, null,null);
  }

  boolean isExist(final String path){
    SQLiteDatabase sqliteDatabase = getReadableDatabase();
    // 调用SQLiteDatabase对象的query方法进行查询，返回一个Cursor对象：由数据库查询返回的结果集对象
    // 第一个参数String：表名
    // 第二个参数String[]:要查询的列名
    // 第三个参数String：查询条件
    // 第四个参数String[]：查询条件的参数
    // 第五个参数String:对查询的结果进行分组
    // 第六个参数String：对分组的结果进行限制
    // 第七个参数String：对查询的结果进行排序
    Cursor cursor = sqliteDatabase.query(EncryptedFilesContract.TABLE_NAME, null,
        EncryptedFilesContract.COLUMN_URI + " = ?",  new String[]{path}, null, null, null);
    // 将光标移动到下一行，从而判断该结果集是否还有下一条数据，如果有则返回true，没有则返回false

      if(cursor != null && cursor.moveToFirst()){
//      id = cursor.getString(cursor.getColumnIndex("id"));
//      name = cursor.getString(cursor.getColumnIndex("name"));
        LogUtils.d("存在数据");
      return true;
    }
    return false;
  }


//    /**
//     * Gets all isEncrypted files in the database
//     *
//     * @return the list of isEncrypted files
//     */
//    List<File> getFiles() {
//        try (Cursor c = getReadableDatabase()
//                .query(EncryptedFilesContract.TABLE_NAME, EncryptedFilesContract.ALL_COLUMNS, null,
//                        null, null, null, null)) {
//            if (c != null && c.moveToFirst()) {
//                int indexId = c.getColumnIndex(EncryptedFilesContract._ID);
//                int indexName = c.getColumnIndex(EncryptedFilesContract.COLUMN_FILENAME);
//                int indexMime = c.getColumnIndex(EncryptedFilesContract.COLUMN_MIME);
//                int indexUri = c.getColumnIndex(EncryptedFilesContract.COLUMN_URI);
//                int indexSize = c.getColumnIndex(EncryptedFilesContract.COLUMN_SIZE);
//                List<File> re = new ArrayList<File>(c.getCount());
//                while (!c.isAfterLast()) {
//                    re.add(new File(c.getString(indexUri));
//                    c.moveToNext();
//                }
//                return re;
//            }
//        }
//        return new ArrayList<File>(0);
//    }

    @Override
    public void onUpgrade(final SQLiteDatabase db, int from, int to) {

    }
}

package com.licrafter.lib.db.table;

/**
 * Created by shell on 2019/5/19.
 * <p>
 * Gmail: shellljx@gmail.com
 */
public interface TableInterface {

    String getSql();

    String getTableName();

    FieldleInterface[] getFieldsInterface();
}

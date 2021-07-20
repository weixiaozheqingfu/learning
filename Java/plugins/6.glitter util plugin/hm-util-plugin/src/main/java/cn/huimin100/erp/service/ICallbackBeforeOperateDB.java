package cn.huimin100.erp.service;

import java.text.ParseException;

public interface ICallbackBeforeOperateDB<D> {

//    /**
//     * 回调对需要更新的DO数据做新增前的数据处理
//     * @param d
//     * @param <D>
//     * @return
//     */
//    <D> D beforeInsert(D d);
//
//    /**
//     * 回调对需要更新的DO数据做更新前的数据处理
//     * @param d
//     * @param <D>
//     * @return
//     */
//    <D> D beforeUpdate(D d);

    /**
     * 回调对需要更新的DO数据做新增前的数据处理
     * @param d
     * @return
     */
    default void beforeInsert(D d) throws  Exception {};

    /**
     * 回调对需要更新的DO数据做更新前的数据处理
     * @param d
     * @return
     */
    default void beforeUpdate(D d) throws ParseException {};

}

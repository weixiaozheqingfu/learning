package cn.huimin100.erp.service;

public interface ICallback4ChangeUtil<D> {

    /**
     * 回调对需要新增的DO数据做新增前的数据处理
     * @param d
     * @return
     */
    default void forInsert(D d) throws  Exception {};

    /**
     * 回调对需要更新的DO数据做更新前的数据处理
     * @param d
     * @return
     */
    default void forUpdate(D d) throws Exception {};

}

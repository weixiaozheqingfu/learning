package cn.huimin100.erp.util;

import cn.huimin100.erp.bean.UpdateMode;
import cn.huimin100.erp.dto.ModifyDTO;
import cn.huimin100.erp.service.ICallback4ChangeUtil;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.util.Iterator;
import java.util.List;

/**
 * 公共更新工具
 *
 * @author limengjun
 * @date 2021/5/13 10:51
 **/
@Log4j2
public class ChangeUtil {

    public static <V,D> D change4SinglelineTable(V vo, Class<D> doClazz, ICallback4ChangeUtil<D> callback4ChangeUtil) throws Exception {
        log.info("ChangeUtil.change4SinglelineTable,输入参数,vo:{},doClazz:{},callback4ChangeUtil:{}", JSONObject.toJSONString(vo), doClazz, callback4ChangeUtil);
        D do4update = ModifyUtil.vo2DO4Update(vo, doClazz);
        log.info("ChangeUtil.change4SinglelineTable,中间值,do4update:{}", JSONObject.toJSONString(do4update));
        if (callback4ChangeUtil !=null) {
            callback4ChangeUtil.forUpdate(do4update);
        }
        log.info("ChangeUtil.change4SinglelineTable,结果值,do4update:{}", JSONObject.toJSONString(do4update));
        return do4update;
    }

//    对应标准写法
//    SignInfoDO signInfoDO4Update = CommonModifyUtil.vo2DO4Update(signInfoEditVO, SignInfoDO.class);
//    signInfoDO4Update.setCompanyInfoId(null);
//    signInfoDO4Update.setSupplierInfoId(null);
//    signInfoDO4Update.setShopInfoId(null);
//    signInfoDO4Update.setSignStartTime(DateUtil.get0Date(signInfoDO4Update.getSignStartTime()));
//    signInfoDO4Update.setSignEndTime(DateUtil.get23Date(signInfoDO4Update.getSignEndTime()));

    public static <V,D> void change4MultilineSubtable(List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz) throws Exception {
        ChangeUtil.change4MultilineSubtable(null, vos, dos, voClazz, doClazz, null, null, null);
    }

    public static <V,D> void change4MultilineSubtable(List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz, Integer foreignKey, String foreignKeyName) throws Exception {
        ChangeUtil.change4MultilineSubtable(null, vos, dos, voClazz, doClazz, foreignKey, foreignKeyName, null);
    }

    public static <V,D> void change4MultilineSubtable(List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz, ICallback4ChangeUtil<D> callback4ChangeUtil) throws Exception {
        ChangeUtil.change4MultilineSubtable(null, vos, dos, voClazz, doClazz, null, null, callback4ChangeUtil);
    }

    public static <V,D> void change4MultilineSubtable(Integer updateMode, List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz) throws Exception {
        ChangeUtil.change4MultilineSubtable(updateMode, vos, dos, voClazz, doClazz, null, null, null);
    }

    public static <V,D> void change4MultilineSubtable(Integer updateMode, List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz, Integer foreignKey, String foreignKeyName) throws Exception {
        ChangeUtil.change4MultilineSubtable(updateMode, vos, dos, voClazz, doClazz, foreignKey, foreignKeyName, null);
    }

    public static <V,D> void change4MultilineSubtable(Integer updateMode, List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz, ICallback4ChangeUtil<D> callback4ChangeUtil) throws Exception {
        ChangeUtil.change4MultilineSubtable(updateMode, vos, dos, voClazz, doClazz, null, null, callback4ChangeUtil);
    }

    public static <V,D> void change4MultilineSubtable(Integer updateMode, List<V> vos, List<D> dos, Class<V> voClazz, Class<D> doClazz, Integer foreignKey, String foreignKeyName, ICallback4ChangeUtil<D> callback4ChangeUtil) throws Exception {
        log.info("ChangeUtil.change4MultilineSubtable方法,输入参数,updateMode:{},vos:{},dos:{},voClazz:{},doClazz:{},foreignKey:{},foreignKeyName:{},callback4ChangeUtil:{}", updateMode, JSONObject.toJSONString(vos), JSONObject.toJSONString(dos), voClazz, doClazz, foreignKey, foreignKeyName, callback4ChangeUtil);
        UpdateMode calcUpdateMode = new UpdateMode(updateMode).invoke();
        Boolean deleteFlag = calcUpdateMode.getDeleteFlag();
        Boolean updateFlag = calcUpdateMode.getUpdateFlag();
        Boolean insertFlag = calcUpdateMode.getInsertFlag();

        ModifyDTO<D, V> modifyDTO = ModifyUtil.getModifyDTO(dos, vos, doClazz, voClazz);

        String foreignKeyMethod = null;
        if (null != foreignKey && StringUtils.isNotBlank(foreignKeyName)) {
            foreignKeyMethod = "set" + foreignKeyName.substring(0, 1).toUpperCase() + foreignKeyName.substring(1);
        }
        log.info("ChangeUtil.change4MultilineSubtable方法,中间值,foreignKeyMethod:{},deleteFlag:{},updateFlag:{},insertFlag:{},modifyDTO:{}", foreignKeyMethod, deleteFlag, updateFlag, insertFlag, JSONObject.toJSONString(modifyDTO));

        if (null != modifyDTO.getDeleteIds() && modifyDTO.getDeleteIds().size() > 0) {
            // 删除
            if (deleteFlag) {
                for (Integer deleteId : modifyDTO.getDeleteIds()) {
                    Iterator<D> doIterator = dos.iterator();
                    while(doIterator.hasNext()){
                        D d = doIterator.next();
                        Method getIdMethod = d.getClass().getMethod("getId");
                        Object o = getIdMethod.invoke(d);
                        Integer id = Integer.valueOf(String.valueOf(o));
                        if(id.equals(deleteId)){
                            log.info("ChangeUtil.update4MultilineSubtable方法,中间值,deleteId:{}", deleteId);
                            doIterator.remove();
                        }
                    }
                }
            }
        }
        if (null != modifyDTO.getUpdateRecords() && modifyDTO.getUpdateRecords().size() > 0) {
            // 更新
            if (updateFlag) {
                for (D do4update : modifyDTO.getUpdateRecords()) {
                    do4update = ModifyUtil.getDO4Update(do4update);
                    if (StringUtils.isNotBlank(foreignKeyMethod)) {
                        Method setForeignKeyMethod = doClazz.getMethod(foreignKeyMethod, Integer.class);
                        setForeignKeyMethod.invoke(do4update, foreignKey);
                    }
                    if (callback4ChangeUtil !=null) {
                        callback4ChangeUtil.forUpdate(do4update);
                    }
                    log.info("ChangeUtil.change4MultilineSubtable方法,中间值,do4update:{}",  JSONObject.toJSONString(do4update));
                }
            }
        }
        if (null != modifyDTO.getInsertRecords() && modifyDTO.getInsertRecords().size() > 0) {
            // 新增
            if (insertFlag) {
                for (V vo4create : modifyDTO.getInsertRecords()) {
                    D do4create = CreateUtil.vo2DO4Create(vo4create, doClazz);
                    if (StringUtils.isNotBlank(foreignKeyMethod)) {
                        Method setForeignKeyMethod = doClazz.getMethod(foreignKeyMethod, Integer.class);
                        setForeignKeyMethod.invoke(do4create, foreignKey);
                    }
                    if (callback4ChangeUtil != null) {
                        callback4ChangeUtil.forInsert(do4create);
                    }
                    log.info("ChangeUtil.change4MultilineSubtable方法,中间值,do4create:{}", JSONObject.toJSONString(do4create));
                    dos.add(do4create);
                }
            }
        }
        log.info("ChangeUtil.change4MultilineSubtable方法,执行完毕");
    }

    // 变更提交后转json串标准做法 // 封装通用ChangeUtil.java  删除 1  更新 2  新增 4     通过传参控制是否需要触发【删除】【更新】【新增】代码块的逻辑
//    protected final void changeQualificationInfo(List<QualificationInfoDO> qualificationInfoDOS, List<QualificationInfoEditVO> qualificationInfoEditVOS, Integer signInfoId) throws Exception {
//        if (null != qualificationInfoEditVOS && qualificationInfoEditVOS.size() > 0) {
//            qualificationInfoEditVOS = qualificationInfoEditVOS.stream().filter(a -> (a.getDataSourceType().equals(QualificationEnum.DataSourceTypeEnum.DATA_SOURCE_TYPE_8.getCode()))).collect(Collectors.toList());
//        }
//        if (null == qualificationInfoEditVOS || qualificationInfoEditVOS.size() <= 0) {
//            return;
//        }
//
//        CommonModifyDTO<QualificationInfoDO, QualificationInfoEditVO> commonModifyDTO = ModifyUtil.getCommonModifyDTO(qualificationInfoDOS, qualificationInfoEditVOS, QualificationInfoDO.class, QualificationInfoEditVO.class);
//        if (null != commonModifyDTO.getInsertRecords() && commonModifyDTO.getInsertRecords().size() > 0) {
//            // 删除
//            if (null != commonModifyDTO.getDeleteIds() && commonModifyDTO.getDeleteIds().size() > 0) {
//                for (Integer deleteId : commonModifyDTO.getDeleteIds()) {
//                    Iterator<QualificationInfoDO> qualificationInfoDOIterator = qualificationInfoDOS.iterator();
//                    while(qualificationInfoDOIterator.hasNext()){
//                        QualificationInfoDO qualificationInfoDO = qualificationInfoDOIterator.next();
//                        if(qualificationInfoDO.getId().equals(deleteId)){
//                            qualificationInfoDOIterator.remove();
//                        }
//                    }
//                }
//            }
//            // 更新
//            if (null != commonModifyDTO.getUpdateRecords() && commonModifyDTO.getUpdateRecords().size() > 0) {
//                for (QualificationInfoEditVO vo4update : commonModifyDTO.getUpdateRecords()) {
//                    QualificationInfoDO do4update = ModifyUtil.vo2DO4Update(vo4update, QualificationInfoDO.class);
//                    do4update.setMainId(signInfoId);
//                    do4update.setDataSourceType(QualificationEnum.DataSourceTypeEnum.DATA_SOURCE_TYPE_3.getCode());
//                }
//            }
//            // 新增
//            for (QualificationInfoEditVO qualificationInfoEditVO : commonModifyDTO.getInsertRecords()) {
//                QualificationInfoDO qualificationInfoDO = Converter.transferObj(qualificationInfoEditVO, QualificationInfoDO.class);
//                qualificationInfoDO.setMainId(signInfoId);
//                qualificationInfoDO.setDataSourceType(QualificationEnum.DataSourceTypeEnum.DATA_SOURCE_TYPE_3.getCode());
//                qualificationInfoDOS.add(qualificationInfoDO);
//            }
//        }
//    }

    public static void main(String[] args) {
        UpdateMode calcUpdateMode4 = new UpdateMode(4).invoke();
        System.out.println(JSONObject.toJSONString(calcUpdateMode4));

        UpdateMode calcUpdateMode6 = new UpdateMode(6).invoke();
        System.out.println(JSONObject.toJSONString(calcUpdateMode6));
    }

}



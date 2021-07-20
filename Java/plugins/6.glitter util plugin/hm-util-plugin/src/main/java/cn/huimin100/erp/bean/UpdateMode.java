package cn.huimin100.erp.bean;

import cn.huimin100.erp.enums.UpdateEnums;

public class UpdateMode {

    private Integer modifyMode;
    private Boolean deleteFlag;
    private Boolean updateFlag;
    private Boolean insertFlag;

    public UpdateMode(Integer modifyMode) {
        this.modifyMode = modifyMode;
    }

    public Boolean getDeleteFlag() {
        return deleteFlag;
    }

    public Boolean getUpdateFlag() {
        return updateFlag;
    }

    public Boolean getInsertFlag() {
        return insertFlag;
    }

    public UpdateMode invoke() {
        deleteFlag = null;
        updateFlag = null;
        insertFlag = null;
        if (modifyMode == null) {
            deleteFlag = true;
            updateFlag = true;
            insertFlag = true;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_1.getCode())) {
            deleteFlag = true;
            updateFlag = false;
            insertFlag = false;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_2.getCode())) {
            deleteFlag = false;
            updateFlag = true;
            insertFlag = false;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_3.getCode())) {
            deleteFlag = true;
            updateFlag = true;
            insertFlag = false;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_4.getCode())) {
            deleteFlag = false;
            updateFlag = false;
            insertFlag = true;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_5.getCode())) {
            deleteFlag = true;
            updateFlag = false;
            insertFlag = true;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_6.getCode())) {
            deleteFlag = false;
            updateFlag = true;
            insertFlag = true;
        } else if (modifyMode.equals(UpdateEnums.ModifyModeEnum.UPDATE_MODE_7.getCode())) {
            deleteFlag = true;
            updateFlag = true;
            insertFlag = true;
        } else {
            deleteFlag = true;
            updateFlag = true;
            insertFlag = true;
        }
        return this;
    }

}

package com.web.travel.model.enums;

public enum EStatus {
    STATUS_OK(0),
    STATUS_EMPTY_FILE(1),
    STATUS_WRONG_EXT(2);

    private final int status;
    EStatus(int status){
        this.status = status;
    }

    public int getStatus(){
        return this.status;
    }
}

package com.tesu.creditgold.bean;

/**
 * Created by Administrator on 2016/12/29 0029.
 */
public class GetStoreTypeDescResponse {
    private int code;
    private String msg;
    private StoreTypeDes data;

    @Override
    public String toString() {
        return "GetStoreTypeDescResponse{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public StoreTypeDes getData() {
        return data;
    }

    public void setData(StoreTypeDes data) {
        this.data = data;
    }

    public class StoreTypeDes{
        private int store_type_id;
        private String type;
        private String bidprefix;

        @Override
        public String toString() {
            return "StoreTypeDes{" +
                    "store_type_id=" + store_type_id +
                    ", type='" + type + '\'' +
                    ", bidprefix='" + bidprefix + '\'' +
                    '}';
        }

        public int getStore_type_id() {
            return store_type_id;
        }

        public void setStore_type_id(int store_type_id) {
            this.store_type_id = store_type_id;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getBidprefix() {
            return bidprefix;
        }

        public void setBidprefix(String bidprefix) {
            this.bidprefix = bidprefix;
        }
    }
}

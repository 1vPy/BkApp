
package com.roy.bkapp.model.music.search;

import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class JsonSearchBean {

    @SerializedName("error_code")
    private Long mErrorCode;
    @SerializedName("result")
    private Result mResult;

    public Long getErrorCode() {
        return mErrorCode;
    }

    public void setErrorCode(Long errorCode) {
        mErrorCode = errorCode;
    }

    public Result getResult() {
        return mResult;
    }

    public void setResult(Result result) {
        mResult = result;
    }

}

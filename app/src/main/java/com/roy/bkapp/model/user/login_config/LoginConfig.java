
package com.roy.bkapp.model.user.login_config;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class LoginConfig {

    @SerializedName("results")
    private List<ConfigResult> mResults;

    public List<ConfigResult> getResults() {
        return mResults;
    }

    public void setResults(List<ConfigResult> results) {
        mResults = results;
    }

}

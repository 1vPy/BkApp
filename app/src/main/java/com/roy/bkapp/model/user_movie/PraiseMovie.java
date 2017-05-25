
package com.roy.bkapp.model.user_movie;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PraiseMovie {

    @SerializedName("results")
    private List<Result> mResults;

    public List<Result> getResults() {
        return mResults;
    }

    public void setResults(List<Result> results) {
        mResults = results;
    }

}

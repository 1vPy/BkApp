
package com.roy.bkapp.model.user_movie.praise;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class PraiseMovie {

    @SerializedName("results")
    private List<PraiseResult> mResults;

    public List<PraiseResult> getResults() {
        return mResults;
    }

    public void setResults(List<PraiseResult> results) {
        mResults = results;
    }

}

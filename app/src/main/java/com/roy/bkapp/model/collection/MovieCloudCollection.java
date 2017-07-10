
package com.roy.bkapp.model.collection;

import java.util.List;
import com.google.gson.annotations.SerializedName;

public class MovieCloudCollection {

    @SerializedName("results")
    private List<MovieCloudResult> mResults;

    public List<MovieCloudResult> getResults() {
        return mResults;
    }

    public void setResults(List<MovieCloudResult> results) {
        mResults = results;
    }

}

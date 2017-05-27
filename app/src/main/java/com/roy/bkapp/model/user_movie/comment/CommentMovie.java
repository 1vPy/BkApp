
package com.roy.bkapp.model.user_movie.comment;

import java.util.List;
import com.google.gson.annotations.SerializedName;

@SuppressWarnings("unused")
public class CommentMovie {

    @SerializedName("results")
    private List<CommentResult> mResults;

    public List<CommentResult> getResults() {
        return mResults;
    }

    public void setResults(List<CommentResult> results) {
        mResults = results;
    }

}

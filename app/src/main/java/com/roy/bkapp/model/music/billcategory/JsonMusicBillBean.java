
package com.roy.bkapp.model.music.billcategory;

import java.util.List;

public class JsonMusicBillBean {

    private List<Content> content;
    private Long error_code;

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public Long getError_code() {
        return error_code;
    }

    public void setError_code(Long error_code) {
        this.error_code = error_code;
    }
}

package com.geek.okweb.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Data
//@NoArgsConstructor
@AllArgsConstructor
public class TemplateData implements Serializable {
    private String type;//blogid,blogids,tags//fromid,formids,photoid,products
    private Set<String> ids = new HashSet<>();
    private Integer order;
    @JsonIgnore
    private String key;
    private Integer number;

    public TemplateData(String type, Set<String> ids, Integer order) {
        this.type = type;
        this.ids = ids;
        this.order = order;
    }

    public TemplateData(){ }

    public String getKey() {
        return this.type + this.order;
    }
    //    private Integer number = 10;
//    private Integer page = 1;
}

package com.geek.okweb.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Slf4j
public class DiffArticle implements Serializable {

    private static final long serialVersionUID = -6822773560383299035L;

    private String language;

    private String id;
}

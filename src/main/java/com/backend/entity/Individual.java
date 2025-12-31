package com.backend.entity;

import java.io.Serializable;

import lombok.Data;

@Data
public class Individual implements Serializable {

	private static final long serialVersionUID = 1L;

    /** 種コード */
    private String speciesCd;
    
    /** ID */
    private String id;

    /** 名前 */
    private String name;

    /** 雌雄 */
    private String gender;

}
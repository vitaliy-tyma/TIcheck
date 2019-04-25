package com.luxoft.falcon.model;

import lombok.*;
/* NOT USED HERE!!!!!!!!!!!!!!*/
/* Entity/Data/POJO to connect to the sources */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class ConfigData {
    private String jdbcDriver = null;
    private String jdbcUrl = null;
    private String jdbcLogin = null;
    private String jdbcPassword = null;
    private String query = null;
}

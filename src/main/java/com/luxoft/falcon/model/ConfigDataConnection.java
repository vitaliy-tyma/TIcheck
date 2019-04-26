package com.luxoft.falcon.model;

import lombok.*;
/**NOT IN USE*/
/* Entity/Data/POJO to connect to the sources */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
class ConfigDataConnection {
    private String jdbcDriver = null;
    private String jdbcUrl = null;
    private String jdbcLogin = null;
    private String jdbcPassword = null;
}

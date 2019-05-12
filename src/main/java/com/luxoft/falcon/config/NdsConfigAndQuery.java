package com.luxoft.falcon.config;

import lombok.Data;
import lombok.Getter;

/** Entity/Data/POJO to connect to the SPIDER
 * To be loaded from XML-file*/
//TODO Store all items in separate XML file and load it's content at start and on request.
@Data
public class NdsConfigAndQuery {
//TODO Use NDSlib to open compressed and enciphered NDS DB
    @Getter
    private String jdbcUrl = "jdbc:sqlite:C:\\_TEMP\\PONB2F\\001\\iDb\\ME_\\0ME_B2F1.NDS";
//    private String jdbcUrl = "jdbc:sqlite:Z:\\MGU\\ECE\\PONB1E\\003\\iDb\\ME_\\0ME_B1E3.NDS";
    @Getter
    private String jdbcLogin = "";
    @Getter
    private String jdbcPassword = "";


    @Getter
    private String query = " \n" +
            "        SELECT *\n" +
            "        FROM tmcLocationTableIdTable\n";




}

package com.luxoft.falcon.config;

import com.luxoft.falcon.config.inter.ConfigAndQueryInterface;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/** Entity/Data/POJO to connect to the SPIDER
 * To be loaded from XML-file*/
//TODO Store all items in separate XML file and load it's content at start and on request.

@Data
public final class NdsConfigAndQuery implements ConfigAndQueryInterface {
//TODO Use NDSlib to open compressed and enciphered NDS DB

    private static NdsConfigAndQuery  _instance = new NdsConfigAndQuery ();

    private NdsConfigAndQuery () {
    }

    public static synchronized NdsConfigAndQuery  getInstance() {
        return _instance;
    }

    @Getter @Setter
    private String jdbcDriver;// = "----com.mysql.jdbc.Driver";
    @Getter @Setter
    private String jdbcUrl = "jdbc:sqlite:C:\\_TEMP\\PONB2F\\001\\iDb\\ME_\\0ME_B2F1.NDS";
//    private String jdbcUrl = "jdbc:sqlite:Z:\\MGU\\ECE\\PONB1E\\003\\iDb\\ME_\\0ME_B1E3.NDS";
    @Getter @Setter
    private String jdbcLogin = "";
    @Getter @Setter
    private String jdbcPassword = "";


    @Getter @Setter
    private String queryLike = " \n" +
            "        SELECT *\n" +
            "        FROM tmcLocationTableIdTable\n";
    @Getter @Setter
    private String queryIs = " \n" +
            "        SELECT *\n" +
            "        FROM tmcLocationTableIdTable\n";


}

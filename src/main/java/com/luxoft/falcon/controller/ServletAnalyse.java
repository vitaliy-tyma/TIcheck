package com.luxoft.falcon.controller;

import com.luxoft.falcon.configuration.MainConfig;
import com.luxoft.falcon.model.Pon;
import com.luxoft.falcon.service.ServletAnalyseService;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/** Servlet is launched from web-browser and reads config from XML file */
@Slf4j
public class ServletAnalyse extends HttpServlet {

    private Pon pon;

    private static final String SOURCE_NAME = MainConfig.getSOURCE_NAME_SPIDER();

    private static final String PON_NAME_REQUEST_PARAMETER_KEY = "PON_name";
    public static final String PON_NAME_REQUEST_PARAMETER_VALUE = "PON_name";


    @Override
    public void init() throws ServletException {
        super.init();
        pon = Pon.getInstance();
    }

//    @Override
//    public void init(ServletConfig config) throws ServletException {
//        super.init(config);
////        ServletContext sc = config.getServletContext();
//
//
//    }



    @Override
    public void doGet(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {

        log.info("*** Start doGet");


        final String ponName = httpServletRequest.getParameter(PON_NAME_REQUEST_PARAMETER_KEY);
        log.info(ponName);
        httpServletRequest.setAttribute(PON_NAME_REQUEST_PARAMETER_VALUE, ponName);


        pon.setName(ponName);

        pon.setOutput("test out");
        Map <String, String> m = new HashMap<String, String>() {{
            put("Step1","Res1");
            put("Step2","Res2");
        }};

        pon.setResult(m);
        log.info(String.format("pon = %s", pon.toString()));


//////////////////////////////////////////////////////////////////////////////////////
//httpServletRequest.getRequestDispatcher("/analyse.jsp").forward(httpServletRequest, httpServletResponse);
//////////////////////////////////////////////////////////////////////////////////////


       httpServletResponse.getWriter().print("resultX");

    }





    @Override
    public void doPost(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse)
            throws ServletException, IOException {
        httpServletRequest.setCharacterEncoding("UTF-8");
        log.info("*** Start doPost");


        String action = httpServletRequest.getParameter("action");


        if ("submit".equals(action)) {
//            pon.setId(Integer.parseInt(request.getParameter("id")));
            final String ponName = httpServletRequest.getParameter(PON_NAME_REQUEST_PARAMETER_KEY);
            log.info(ponName);

            pon.setName(ponName);
//            pon.setSerial(request.getParameter("serial"));
        }
//        assert !Objects.isNull(httpServletRequest) : "Request required for greeting request";
//        assert !Objects.isNull(httpServletResponse) : "Response required for greeting request";




        log.info(pon.getName());

//        final String ponNameValue = String.format("PON value is %s", ponName);
//        log.info(ponNameValue);



        httpServletRequest.setAttribute(PON_NAME_REQUEST_PARAMETER_VALUE, pon.getName());
        httpServletRequest.getRequestDispatcher("/analyse.jsp").forward(httpServletRequest, httpServletResponse);


        ////httpServletRequest.setAttribute(PON_NAME_REQUEST_PARAMETER_VALUE, ponNameValue);
        //httpServletRequest.getRequestDispatcher
        // ("/showGreeting.jsp").
        // forward(httpServletRequest, httpServletResponse);






//        String result = ServletAnalyseService.service(SOURCE_NAME);
///**Maven's webapp code*/
//        httpServletResponse.getWriter().print(result);
    }
}
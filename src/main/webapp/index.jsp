<html>

<head>
<title>TI checklist web-interface</title>
<!--
CSS from BIRT.
It breaks mark-up - cause is not detected!!!

<link href="http://himdlxbirt01/birt/static/css/bootstrap.css?v=6" rel="stylesheet" type="text/css">
<link href="http://himdlxbirt01/birt/static/css/birtstyles.css?v=8" rel="stylesheet" type="text/css">
-->

<script src="http://himdlxbirt01/birt/static/js/jquery.js" type="text/javascript"></script>

  <style type="text/css">
   table.common {
    width: 90%; /* Table width */
    border: 1px solid black; /* Frame around table */
    margin: auto; /* Align table by the center of the page */
   }
   td {
    text-align: center; /* Align text by the center of the cell */
   }


   table.PON_form {
     border: 1px solid black; /* Frame around table */
     margin: auto; /* Align table by the center of the page */
     }
     td.left {
     text-align: right; /* Align text by the right edge of the cell */
     }
     td.center {
     text-align: center; /* Align text by the center of the cell */
     }
     td.right {
     text-align: left; /* Align text by the left edge of the cell */
     }

     button, input[type=submit] {
     width: 100%;
     color: #ffffff;
     text-shadow: 0 -1px 0 rgba(0, 0, 0, 0.25);
     background-color: #006dcc;
     border-color: rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.1) rgba(0, 0, 0, 0.25);
     background-image: linear-gradient(to bottom, #0088cc, #0044cc);
     border-radius: 4px;
     font-family: "Helvetica Neue", Helvetica, Arial, sans-serif;
     font-weight: normal;
     -webkit-appearance: button;
     border: 1px solid #bbbbbb;
     padding: 4px 14px;
     }


    body {
    min-height: 100%;
    overflow-y: scroll;
    min-width: 700px;
    background-color: #F5F6F7;
    }

     input[type=checkbox]{
     -webkit-transform: scale(2);
     }
  </style>
</head>



<body>
<table class="common">


<div class="float_container">
<tr>
<td>
<h2>TI checklist web-interface</h2>

<p>Refer to <a href="https://adc.luxoft.com/confluence/pages/viewpage.action?spaceKey=DBPROD&title=07.+TI"> DB production TI checklist</a>
</p>
</td>
</tr>
</div>







<tr>
<td>
<hr align="center" width="80%" size="2" color="#0000ff" />
</td>
</tr>



<tr>
<td>
<div class="PON_selector">


<fieldset>
<legend> Select PON</legend>




<!--***********FORM ACTION*************-->
<form action="/analyse?action=submit" method="get">





<div>
<table class="PON_form">
<tr>
<p>

<td class = "left">Search request</td>
<td class = "center">
<input type="text" name="PON_name" id="PON_name" placeholder="Enter PON or it's part"
       tabindex="1" autofocus/></td>
<td class = "right">

        <!--class="btn btn-primary fact-stat-add-ts-btn"-->






<!-********************************MAIN BUTTON*********************-->
<!--
<input type="submit" value="Analyse (form)" class = "PON_filter"
       title="Click to start TI analysis" tabindex="2">
-->

<button class = "PON_filter" type="submit"
        title="Enter PON (or it's part) and press button to start TI analisys" tabindex="2">
        Analyse (get)
</button>




        </td>
        <td>
        <input type="checkbox" name="autosearch_PON" id = "autosearch_PON" checked
               title="Auto search for PON while entering" tabindex="3"> Autocomplete</checkbox>
        </td>
</p>
</tr>
<p>
<tr>
<td class = "left">Regression check</td>
<td class = "center"><input type="text" id="prev_PON_filter" placeholder="(Optional) previous PON/part"
    tabindex="4"/></td>
<td class = "right">


<!--
<button class = "PON_filter" type="submit"
        title="Enter PON (or it's part) and press button to start TI analisys" tabindex="4">
        <strike>Analyse (js)</strike>
</button>






<button type="button" class="btn btn-primary fact-stat-add-ts-btn"
        title="Enter previous PON (or it's part) and press button to check for regression (optional)"
        tabindex="5">Select previous PON</button>
-->
</td>
        <td>
<input type="checkbox" name="autosearch_prev_PON" id="autosearch_prev_PON" checked
       title="Auto search for previous PON while entering" tabindex="6"> Autocomplete</checkbox>
        </td>
</tr>
</p>
</table>
</div>
</form>
</fieldset>
</div>








<!--
<div class="float_container">
                <div class="testcase_statistics_control">
                    <p>Search In</p>
                    <select class="dbtype_filter" id="testcase_statistics_search_db">
                        <option value="ndsreport">Production</option>
                        <option value="ndsreport_jenkins">Jenkins</option>
                    </select>
                </div>
                <div class="testcase_statistics_control">
                    <p>Testsuite name/PON/Test DB number</p>
                    <input type="text" id="tc_stat_testsuite_filter">
                </div>
                <div class="testcase_statistics_control">
                    <p>Branch</p>
                    <input type="text" id="testcase_branch_field">
                </div>
            </div>


<div class="testcase_branch_field_wrap">
                <button type="button" class="btn btn-primary fact-stat-add-ts-btn" data-group="first">
                    Add
                    testsuites to first group
                </button>
                <button type="button" class="btn btn-primary fact-stat-add-ts-btn" data-group="second">
                    Add
                    testsuites to second group
                </button>
                <div class="ajax_image_wrap">
                    <img src="../static/images/processing4.gif" class="ajax_loading" id="tc_ajax_image">
                </div>
            </div>
-->


</td>
</tr>






<tr>
<td>
<p>For testing purposes only - to be used after deploy</p>
<a href="http://172.30.136.166:889/TIcheck/">http://172.30.136.166:8889/TIcheck/</a>
</td>
</tr>


<tr>
<td>
<hr align="center" width="80%" size="2" color="#ff0000" />
</td>
</tr>
<tr>


<tr>
<td>
<p><a href="ServletLocalhost">Get data from Localhost</a></p>
</td>
</tr>

<tr>
<td>
<p><a href="ServletSpider">Get data from Spider</a></p>
</td>
</tr>
<tr>
<td>
<hr align="center" width="80%" size="2" color="#ff0000" />
</td>
</tr>




<tr>
<td>
<p><a href="ServletConfig">Show <b>spiderconnector.xml</b> - used for configuration</a><br/></p>
</td>
</tr>

<tr>
<td>
<!--
<p><a href="spiderconnector.xml"><font size=-2 color= gray>DOESN&rsquo;T WORK - Open <b>spiderconnector.xml</b> directly</font></a></p>
-->
</td>
</tr>





</table>
</body>
</html>

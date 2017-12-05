
<div class="upmc-wp">
  <div style="width:100%;"  id="data_grid">
    <div class="upmob-filter">
        <div class="dpf-line">    
            <div class="col-md-8">
                <em>${lang.mktinfoName}</em>
                <select id="mktInfoName"  style="height:24px;width:24%;-webkit-border-radius: 5px;"></select>
            </div>
            <div class="dpf-item">
                <em>${lang.platformType}</em>
            </div>
            
            <div class="dpf-item" style="padding: 0 0 0 5%; margin: 0 0 0 1%;">
                <div class="fb-actionbar" id="report_platform">
                </div>
            </div>
            
           
            
        </div>
        
         
         
        <div class="dpf-line">
            <div class="dpf-item col-md-4">
                <em><strong>*</strong> 截止日期：</em>
                <input id="d_start" readonly="" type="text" class="up-txt" style="height: 33px;width:51%;">
            </div>
            
            <div class="dpf-item col-md-2" style="margin: 0 0 0 -1.1%;">
                <div class="btn-group" style="width: 160px;">
                  <button type="button"  style="width: 150px;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  -<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" id="report_area">
                    
                  </ul>
                </div>
            </div>
            
            <div class="dpf-item col-md-2">
                <div class="btn-group" style="width: 160px;margin: 0 0 0 59%; ">
                  <button type="button"  style="width: 150px;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                  -<span class="caret"></span>
                  </button>
                  <ul class="dropdown-menu" id="report_person_group">
                  </ul>
                </div>
            </div>
            
             <div class="dpf-item col-md-2" style=" padding: 0 0 0 22.5%;">
                 <button type="button" class="btn btn-success" onClick="reportQuery()" style="width: 100px;">${lang.query} </button>
            </div>
        </div>


    <br><br><br><br><br>

        <div class="dpf-line">
            <div class="col-md-3" style="padding-left: 0px;padding-right: 10px;">
                <div  class="xytt-holder" style="border-color: #CCC;border-style: solid;border-width: 1px 1px 1px;">
                     <div title="" data-original-title="" class="box-content box-statistic " tooltip="IMP">
                     	  <span class="xy-tooltip xyt-below" style="margin-top:44px;">
                           实际曝光量除以预估曝光量
                           <b class="xytdeco"></b>
                     	  </span>
                        <h5 id="general_all_bg_pv" class="title text-error">-</h5>
                        <h5>曝光PV(完成率%)</h5>
                        <div class="text-error icon-eye-open align-right">
                            <span style="font-size:30px;float: right;top:-30px" class="glyphicon glyphicon-eye-open" aria-hidden="true"></span>
                        </div>
                     </div>
                </div>
               
            </div>
        
            <div class="col-md-3"style="padding-left: 10px;padding-right: 10px;" >
                <div class="xytt-holder" style="border-color: #CCC;border-style: solid;border-width: 1px 1px 1px;">
                    <div title="" data-original-title="" class="box-content box-statistic" tooltip="Click">
                        <span class="xy-tooltip xyt-below" style="margin-top:44px;">
                           实际点击量除以预估点击量
                           <b class="xytdeco"></b>
                        </span>
                        <h5 id="general_all_dj_pv" class="title text-error">-</h5>
                        <h5>点击PV(完成率%)</h5>
                         <div class="text-error icon-eye-open align-right">
                            <span style="font-size:30px;float: right;top:-30px" class="glyphicon glyphicon-hand-up" aria-hidden="true"></span>
                        </div>
                    </div>
                </div>
            </div>
        
            <div class="col-md-3" style="padding-left: 10px;padding-right: 10px;">
                <div class="xytt-holder" style="border-color: #CCC;border-style: solid;border-width: 1px 1px 1px;">
                    <div class="box-content box-statistic" tooltip="CTR">
                        <span class="xy-tooltip xyt-below" style="margin-top:44px;">
                           点击到达率,即广告的点击量除以浏览量
                           <b class="xytdeco"></b>
                        </span>
                        <h5 id="general_ctr" class="title text-error">-</h5>
                        <h5>CTR</h5>
                         <div class="text-error icon-eye-open align-right">
                            <span style="font-size:30px;float: right;top:-30px" class="glyphicon glyphicon-inbox" aria-hidden="true"></span>
                        </div>
                    </div>
                </div>
            </div>
        
            <div class="col-md-3" style="padding-left: 10px;padding-right: 0px;">
                <div class="xytt-holder" style="border-color: #CCC;border-style: solid;border-width: 1px 1px 1px;">
                    <div class=" box-statistic" tooltip="UV">
                    	<span class="xy-tooltip xyt-below" style="margin-top:44px;">
                           独立访问者,在特定时间周期内,访问过某网站/广告,具有唯一访问标识Cookie(MZID)的访问者
                           <b class="xytdeco"></b>
                        </span>
                        <h5 id="general_all_bg_uv" class="title text-error">-</h5>
                        <h5>点击UV</h5>
                         <div class="text-error icon-eye-open align-right">
                            <span style="font-size:30px;float: right;top:-30px" class="glyphicon glyphicon-user" aria-hidden="true"></span>
                        </div>
                    </div>
                </div>
            </div>
        </div>

</div>

<br><br><br><br><br><br>
        
<div class="pt-tabcont ptt-bigpad" data-tabvalue="0" style="display:block;">

    <div class="figbox">
        <div class="fig-hdbar">
            <span style="line-height: 42px;">媒体排名</span><span id="mediaSpan"> </span> 
            
            
            <a href="javascript:void(0)" onClick="detail(1)"><span style="line-height: 42px;float: right;">详情</span></a>
        </div>
        
      
            
        <div class="fb-fig">
            <input type="radio" name="media_chart_radio" value="bg"  />曝光
            <input type="radio" name="media_chart_radio" value="dj"  checked="checked" />点击
            <div  id="media_chart_bg" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative;overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
            <div  id="media_chart_dj" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative;overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
        </div>
    </div>
    
    <div class="figbox">
        <div class="fig-hdbar">
            <span span style="line-height: 42px;">当日小时趋势</span> <span id="hourSpan"> </span> 
            
             <a href="javascript:void(0)" onClick="detail(0)"><span style="line-height: 42px;float: right;">详情</span></a>
        </div>
       <div class="fb-fig">
            <div  id="hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
        </div>
    </div>
    
    <div class="figbox">
        <div class="fig-hdbar">
            <span span style="line-height: 42px;">点击趋势</span><span id="djSpan"> </span>
             <a href="javascript:void(0)" onClick="detail(2)"><span style="line-height: 42px;float: right;">详情</span></a>
             
        </div>
       <div class="fb-fig">
             <div  id="dj_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
        </div>
    </div>
    
    <div class="col-md-8" style="padding-left: 0px;padding-right: 10px;">
        <div class="fig-hdbar">
            <span  style="line-height: 42px;">活动数据地域分布</span> <span id="areaSpan"> </span> 
            <a href="javascript:void(0)" onClick="detail(3)"><span style="line-height: 42px;float: right;">详情</span></a>
        </div>
       <div class="fb-fig">
            <div  id="area_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
        </div>
    </div>
    
    <div class="col-md-4" style=" padding-left: 10px;padding-right: 0px;">
        <div class="fig-hdbar">
            <span  style="line-height: 42px;">人群转换</span> 
        </div>
       <div class="fb-fig">
            <div  style="width: 100%; height: 450px; cursor: default;">
                  <input type="radio" name="group_chart_radio" value="Imp" checked="checked" />Imp
                  <input type="radio" name="group_chart_radio" value="UV" />UV
                 <br><br><br>
                 <div class="row">
                     <div class="col-xs-1"></div>
                       <div class="col-xs-10">
                          <div title="" data-original-title="" class="xytt-holder" style="height: 70px;background-color:#5ab1ef; text-align:center;" tooltip="AllData" tooltip-placement="left">
                            <span class="xy-tooltip" style="margin-top:-14px;">
                            	  所有监测到的数据
                                <b class="xytdeco"></b>
                            </span>
                            <lable id="allLable" style="line-height:70px;font-size: 14px;color: #fff;">所有网民：
            
                              <span id="allp_bg_pv"></span>
                              <span id="allp_bg_uv"></span></lable>
                          </div>
                          </div>
                          <div class="col-xs-1"></div>
                    </div>
                    <div class="row">
                         <div class="col-xs-7" id="div_1_jt">
                            <div style="vertical-align:middle; " class="pull-right">
                              <img src="../css/img/jt.png" style="height: 70px;width: 60px;" align="absmiddle">
                              <div id="div_1_value" style="position: absolute; top: 40px; left: 250px;">
                              <lable id="allToStable" style="">
                              <span id="pv_TranRate"></span>
                              <span id="uv_TranRate"></span>
                              </lable></div>
                            </div>
                        </div>
                    </div>
                     <div class="row">
                       <div class="col-xs-2"></div>
                         <div class="col-xs-8">
                            <div title="" data-original-title=""  class="xytt-holder" style="height: 70px;background-color:#5ab1ef; text-align:center;" tooltip="StableCrowd" tooltip-placement="left">
                                <span class="xy-tooltip" style="margin-top:-14px;">
                            	      24小时之后仍有活跃记录的MZID
                                    <b class="xytdeco"></b>
                                </span>
                                <lable id="stableLable" style="line-height:70px;font-size: 14px;color: #fff;">稳定人群： 
                                 <span id="wdp_bg_pv"></span>
                                 <span id="wdp_bg_uv"></span>
                                </lable>
                            </div>
                          </div>
                          <div class="col-xs-2"></div>
                    </div>
            </div>
        </div>
    </div>
  </div>
 </div>
	<div id="id_selectMkt_dialog_diversion" style="font-size: 12px; font-family: Microsoft; max-height: 500px; overflow-y: auto;display: none;">
		<form name="selectMktForm"  novalidate autocomplete="off">
        	<table style="width:850px;">
           		<tr>
           			<td style="margin:0 auto;text-align:center;">从 <input id="diversion_start" readonly="" type="text" class="up-txt" style="height: 33px;width:20%;"> 至 <input id="diversion_end" readonly="" type="text" class="up-txt" style="height: 33px;width:20%;"></td>
           		</tr>
        	</table>
        </form>
	</div>    
</div>
<script type="text/javascript">
seajs.use(["modules/base/meta/generalSituat"], function(generalSituat) {
        generalSituat.showReport();
    });

   
</script>

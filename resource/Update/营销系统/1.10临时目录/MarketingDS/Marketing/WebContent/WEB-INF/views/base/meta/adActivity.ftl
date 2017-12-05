
<div class="upmc-wp">

    <div class="upmob-filter">
        <div class="dpf-line">    
           <div class="col-md-8">
                <em>${lang.mktinfoName}</em>
                <select id="mktInfoName"  style="height:24px; width: 22%;-webkit-border-radius: 5px;"  onchange="mktNameChange()"></select>
            </div>
            
            <div class="dpf-item">
                <em>${lang.platformType}</em>
            </div>
            
            <div class="dpf-item">
                <div class="fb-actionbar" id="report_platform">
                </div>
            </div>
            
        </div>
        
    </div>

    <br>
    
    <div>        
        <div class="page-tabar mkcl" style="box-sizing:content-box;">
            <div class="pt-link ptl-selected">
                <a id="page_0"   data-tabindex="0" href="javascript:void(0)" onClick="hourQuery()">分时数据</a>
            </div>
            <div class="pt-link">
                <a id="page_1"  data-tabindex="1" href="javascript:void(0)" onClick="baseQuery()">基数数据</a>
            </div>
            <div class="pt-link">
                <a id="page_2"  data-tabindex="2" href="javascript:void(0)" onClick="frQuery()">渠道效果</a>
            </div>
            <!--地图数据
            <div class="pt-link">
                <a id="page_3" data-tabindex="3" href="javascript:void(0)" onclick="query()" onClick="frQuery()" onClick="baseQuery()" onClick="hourQuery()">地域</a>
            </div>   -->
        </div>
        
        <br>
        
        <div class="pt-tabcont ptt-bigpad" data-tabvalue="0" style="display:block;">
        <!-- start ly add -->
        <div class="dpf-line">
                <div class="dpf-item col-md-4">
                    <em><strong>*</strong> 选择日期&nbsp;</em>
                    <input id="d_hourstat" readonly="" type="text" class="up-txt" style="width: 150px;height:34px;">
                </div>
                
                <div class="dpf-item col-md-3">
                    <em>媒体&nbsp;</em>
                    <div class="btn-group" style="width: 160px;">
                      <button type="button"  style="width: 102%;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      -<span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" id="report_media">
                        
                      </ul>
                    </div>
                </div>
                
                <div class="dpf-item col-md-3">
                    
                </div>
                
                 <div class="dpf-item col-md-2" style="width:33%;">
                     <button type="button"  class="btn btn-success" onClick="hourQuery()" style="width: 100px;    margin: 0 0 0 35%;">${lang.query} </button>
                     <button type="button" class="btn btn-success" onClick="hourExport()" style="width: 100px;">${lang.reportExport}</button>
                </div>
            </div>
            <br> <br> <br> <br>
       
       
        <!-- end ly add--> 
        
        
        
        
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">24小时趋势</span> 
                </div>
                <div class="fb-fig">
                
                    <input type="radio" name="hourstat_radio" value="bg"  />曝光
                    <input type="radio" name="hourstat_radio" value="dj" checked="checked" />点击     
                    <div  id="hourstat_bg_chart" style="width: 1429px; height: 450px; cursor: default;">
                    <div style="position: relative; overflow: hidden; width: 1184px; height: 350px;">
                    <div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div>
                    <canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas>
                    <canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;">
                    </canvas>
                    </div>
                    </div>
                    <div  id="hourstat_dj_chart" style="width: 1429px; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                </div>
            </div>
            
            <br>
            
            <div class="up-detable" id="hour_tblList">
                <table>
                    <thead>
                        <tr>
                            <th id="hour_tbl_sid">SID</th>
                            <th id="hour_tbl_media_name">媒体</th>
                            <th id="hour_tbl_channel">频道</th>
                            <th id="hour_tbl_ad_position">广告位</th>
                            <th id="hour_tbl_hour">Hour</th>
                            <th id="hour_tbl_bg_pv">Imp</th>
                            <th id="hour_tbl_dj_pv">Click</th>
                            <th id="hour_tbl_ctr">CTR</th>
                        </tr>
                    </thead>
                    <tbody>
                            
                    </tbody>
                </table>
                <div class="up-pager">
                    <div class="uppg">
                        <a href="javascript:void(0);" class="first-page" data-page="first">首页</a>
                        <a href="javascript:void(0);" class="uppg-prv" data-page="pre">上一页</a>
                        <a href="javascript:void(0);" class="uppg-nt" data-page="next">下一页</a>
                        <a href="javascript:void(0);" class="last-page" data-page="last">尾页</a>
                    </div>
                    <div class="pg-count">
                        共<span class="pg-no"></span>页
                    </div>
        
                </div>
                
                
                
                
            </div>
            
            
        </div>
        
        <div class="pt-tabcont ptt-bigpad" data-tabvalue="1">
        
              <br>
             
              <div class="dpf-line">
                <div class="dpf-item col-md-4">
                    <em><strong>*</strong>从&nbsp;</em>
                    <input id="d_start" readonly="" type="text" class="up-txt" style="width: 30%;    height: 34px;">
                     <em>&nbsp;到&nbsp;</em>
                    <input id="d_end" readonly="" type="text" class="up-txt" style="width: 30%;    height: 34px;">
                </div>
                
                <div class="dpf-item col-md-2" style="    margin: 0 0 0 -9%;">
                    <em>地域&nbsp;</em>
                    <div class="btn-group" >
                      <button type="button"  style="width: 177%;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      -<span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" id="report_area">
                        
                      </ul>
                    </div>
                </div>
                
                <div class="dpf-item col-md-3" style="    margin: 0 0 0 4%;">
                    <em>目标人群&nbsp;</em>
                    <div class="btn-group" style="width: 160px;">
                      <button type="button"  style="width: 150px;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      -<span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" id="report_person_group">
                      </ul>
                    </div>
                </div>
                
                 <div class="dpf-item col-md-2" style="width: 26%;">
                     <button type="button"  class="btn btn-success" onClick="baseQuery()" style="width: 100px;">${lang.query} </button>
                     <button type="button" class="btn btn-success" onClick="baseExport()" style="width: 100px;">${lang.reportExport}</button>
                </div>
              </div>
        
             <br> <br> <br>
             
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">曝光&点击</span> 
                </div>
                <div class="fb-fig">
                    <input type="radio" name="base_radio" value="bg" checked="checked" />PV
                    <input type="radio" name="base_radio" value="dj"  />UV
                    <div  id="base_bg_chart" style="width: 1429px; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                    <div  id="base_dj_chart" style="width: 1429px; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                </div>
            </div>
            
            <br>
            
            
            
            
            <div class="up-detable" id="base_list">
                <table>
                    <thead>
                        <tr>
                            <th>媒体</th>
                            <th id="base_table_Imp">Imp
                                <span style="font-size:10px;top:-5px;left:5px" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                                <span style="font-size:10px;top:5px;left:-9px" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
                            </th>
                            <th id="base_table_uv">UV
                             <span style="font-size:10px;top:-5px;left:5px" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                              <span style="font-size:10px;top:5px;left:-9px" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
                            </th>
                            <th id="base_table_click">Click
                             <span style="font-size:10px;top:-5px;left:5px" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                             <span style="font-size:10px;top:5px;left:-9px" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
                            </th>
                            <th id="base_table_clicker">Clicker
                             <span style="font-size:10px;top:-5px;left:5px" class="glyphicon glyphicon-chevron-up" aria-hidden="true"></span>
                             <span style="font-size:10px;top:5px;left:-9px" class="glyphicon glyphicon-chevron-down" aria-hidden="true"></span>
                            </th>
                        </tr>
                    </thead>
                    <tbody>
                       
                    </tbody>
                </table>
                
                <div class="up-pager">
                    <div class="uppg">
                        <a href="javascript:void(0);" class="first-page" data-page="first">首页</a>
                        <a href="javascript:void(0);" class="uppg-prv" data-page="pre">上一页</a>
                        <a href="javascript:void(0);" class="uppg-nt" data-page="next">下一页</a>
                        <a href="javascript:void(0);" class="last-page" data-page="last">尾页</a>
                    </div>
                    <div class="pg-count">
                        共<span class="pg-no"></span>页
                    </div>
        
                </div>
            </div>
           
            
        </div>
        
        
        
     <div class="pt-tabcont ptt-bigpad" data-tabvalue="2">
     
     <div class="dpf-line">
                <div class="dpf-item col-md-3" style="margin:0 0 0 0">
                    <em><strong>*</strong> 截止日期&nbsp;</em>
                    <input id="d_fr" readonly="" type="text" class="up-txt" style="    width: 52%;    height: 34px;-webkit-border-radius: 5px;">
                </div>
                
                <div class="dpf-item col-md-2" style="    margin: 0 0 0 -4%;">
                    <em>类型&nbsp</em>
                    <select id="fr_select_type" class="select_type" style="width:60%;-webkit-border-radius: 5px;;height:34px;">
                     <option value ="1">点击频次</option>
                     <option value ="0">曝光频次</option>
                     <option value ="2">着陆频次</option>
                    </select>
                    
                </div>
                
                
                <div class="dpf-item col-md-2" style="margin:0 0 0 -2%;">
                <em>最大频次</em>
                <select id="fr_select_max" class="select_count" style="width:45%;-webkit-border-radius: 5px;height:34px;">
                     <option value ="10">10</option>
                     <option value ="9">9</option>
                     <option value ="8">8</option>
                     <option value ="7">7</option>
                     <option value ="6">6</option>
                     <option value ="5">5</option>
                     <option value ="4">4</option>
                     <option value ="3">3</option>
                     <option value ="2">2</option>
                     <option value ="1">1</option>
                   
                   
                    </select>
                </div>
                
                <div class="dpf-item col-md-3" style="    margin: 0 0 0 -3%;">
                    <em>地域&nbsp</em>
                    <div class="btn-group" style="width: 160px;">
                      <button type="button"  style="width:77%" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      -<span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" id="report_province_fr">
                        
                      </ul>
                    </div>
                </div>
                
                <div class="dpf-item col-md-3" style="    margin: 0 0 0 -8%;">
                    <em>目标人群&nbsp</em>
                    <div class="btn-group" style="width: 160px;">
                      <button type="button"  style="width: 72%;" class="btn btn-default dropdown-toggle" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">
                      -<span class="caret"></span>
                      </button>
                      <ul class="dropdown-menu" id="report_person_group_fr">
                      </ul>
                    </div>
                </div>
                
                 <div class="dpf-item col-md-2" style="width: 20%; margin: 0 0 0 -12%;">
                     <button type="button"  class="btn btn-success" onClick="frQuery()" style="width: 80px;">${lang.query} </button>
                     <button type="button" class="btn btn-success" onClick="frExport()"  style="width: 80px;">${lang.reportExport}</button>
                </div>
            </div>
            <br><br><br><br>
     
            <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">点击频次</span> 
                </div>
                
                <div class="fb-fig" style="width: 100%; height: 450px;">
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_click_stack" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                     
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_click_line" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                </div>
            </div>
            
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">曝光频次</span> 
                </div>
                
                <div class="fb-fig" style="width: 100%; height: 450px;">
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_exp_stack" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                     
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_exp_line" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                </div>
            </div>
            
            
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">着陆频次</span> 
                </div>
                
                <div class="fb-fig" style="width: 100%; height: 450px;">
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_landing_stack" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                     
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="fr_landing_line" style="width: 715px; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                </div>
            </div>
            
            <!-- 媒体重合度
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">媒体重合度</span> 
                </div>
                
                <div class="fb-fig" style="width: 100%; height: 450px;">
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="main_test" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                     
                     <div class="col-md-6" style=" padding-left: 0px;padding-right: 0px;">
                            <div  id="main_test" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                     </div>
                </div>
            </div>  -->
            
             <br>
            
            <div class="up-detable" id="fr_list">
                <table >
                    <thead>
                    </thead>
                    <tbody>
                        <tr>
                            
                        </tr>
                    </tbody>
                </table>
                <div class="up-pager">
                    <div class="uppg">
                        <a href="javascript:void(0);" class="first-page" data-page="first">首页</a>
                        <a href="javascript:void(0);" class="uppg-prv" data-page="pre">上一页</a>
                        <a href="javascript:void(0);" class="uppg-nt" data-page="next">下一页</a>
                        <a href="javascript:void(0);" class="last-page" data-page="last">尾页</a>
                    </div>
                    <div class="pg-count">
                        共<span class="pg-no"></span>页
                    </div>
        
                </div>
            </div>

     </div>

    <div class="pt-tabcont ptt-bigpad" data-tabvalue="3" >
             <div class="figbox">
                <div class="fig-hdbar">
                    <span style="line-height: 42px;">活动数据地图分布</span> 
                </div>
                <div class="fb-fig">
                    <div  id="main_test" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
                </div>
            </div>
            
            <br>
            
            <div class="up-detable">
                <table>
                    <thead>
                        <tr>
                            <th>SID</th>
                            <th>媒体</th>
                            <th>频道</th>
                            <th>广告位</th>
                            <th>Hour</th>
                            <th>Imp</th>
                            <th>Click</th>
                            <th>CTR</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                        </tr>
                    </tbody>
                </table>
            </div>
            <nav style="float: right">
              <ul class="pagination">
                <li>
                  <a href="#" aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                  </a>
                </li>
                <li><a href="#">1</a></li>
                <li><a href="#">2</a></li>
                <li><a href="#">3</a></li>
                <li><a href="#">4</a></li>
                <li><a href="#">5</a></li>
                <li>
                  <a href="#" aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                  </a>
                </li>
              </ul>
            </nav>
            
    </div>
        
    </div>


</div>
<script type="text/javascript">
    
 seajs.use(["modules/base/meta/adActivity"], function(adActivity) {
        adActivity.showReport();
    });
 
   
</script>

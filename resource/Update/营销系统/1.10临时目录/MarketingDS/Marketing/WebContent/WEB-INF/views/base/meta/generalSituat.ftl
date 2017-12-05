<div class="upmc-wp">
	<div class="dp-filter">
		
		<h2 class="dpf-oph">${lang.mktName} <a href="javascript:void(0)" class="dpf-link" data-target="events" data-status="on">收起选择框</a></h2>
		<div class="dpf-opblk" data-block="events">
			
			<div class="mktabber-frame">
				<div class="tfVert-menu" id="tfVert-menu"></div>
				<div class="tfVert-cont dpf-events" id="dpf-events"></div>
			</div>
			
		</div>
		<h2 class="dpf-oph">${lang.meidaTypes} <a href="javascript:void(0)" class="dpf-link" data-target="media" data-status="on">收起选择框</a></h2>
		<div class="dpf-opblk" data-block="media">
			<div class="dpf-media" id="dpf-media">
				<div class="mms-opt">
					<div class="mmsopt-h">${lang.adTypes}</div>
					<div class="mmsopt-b" id="materialType">
						<span class="mmsopt-lineselect">全选</span>
					</div>
				</div>
				
			</div>
		</div>
		<h2 class="dpf-oph">${lang.putPlatform} <a href="javascript:void(0)" class="dpf-link" data-target="platform" data-status="on">收起选择框</a></h2>
		<div class="dpf-opblk" data-block="platform">
			<div class="dpf-media">
				<div class="mms-opt">
					<div class="mmsopt-h">${lang.detailsAdInfoPort}</div>
					<div id="portSelect" class="mmsopt-b">
						<span class="mmsopt-lineselect">${lang.selectAll}</span>
					</div>
				</div>
				<div class="mms-opt">
					<div class="mmsopt-h">${lang.adInfoPlatform}</div>
					<div id="landPlatformSelect" class="mmsopt-b">
						<span class="mmsopt-lineselect">${lang.selectAll}</span>
					</div>
				</div>
				
			</div>
		</div>
		<div class="dpf-line">
			<div class="dpf-item">
				<em style="font-size:18px;">${lang.putCycle}：</em>
				<input  id="diversion_start" type="text" class="up-txt upt-short date-from" readonly />&nbsp;&nbsp;至&nbsp;&nbsp;<input id="diversion_end" type="text" class="up-txt upt-short date-to" data-tip="true" readonly />
			</div>
		</div>
		<div class="dpf-line" style="text-align:center; padding-top:60px;">

			<a href="javascript:void(0)" class="btn-lightblue" id="mmsDoSearch" style="font-size:16px; width:160px; height:44px; line-height:44px;">查 询</a>
		</div>

	</div>
	
	<div class="mmsres-frame" id="mmsresFrame" style="display:none;">
		<div class="mmsres-top">
			<span><img src="../css/img/resicon.png" style="vertical-align:middle;"/> &nbsp; ${lang.queryResult}</span>
		
		</div>
		<div class="mmsres-cont">
			<table class="mmsres-sum">
				<tr>
					<td>
						<p id="general_all_bg_pv"></p>
					</td>
					<td>
						<p id="general_all_bg_uv"></p>
					</td>
					<td>
						<p id="general_all_dj_pv"></p>
					</td>
					<td>
						<p id="general_all_dj_uv"></p>
					</td>
					<td>
						<p id="general_ctr"></p>
					</td>
					<td>
						<p id="buy_rate"></p>
					</td>
				</tr>
			</table>
			<div class="exp-lightab" id="tabPage">
				<a href="javascript:void(0)" class="elt-tab eltt-on" data-tab="1" id="generalSituation">${lang.generalSituation}</a>
				<a href="javascript:void(0)" class="elt-tab" data-tab="2" id="diversion">${lang.diversion}</a>
				<a href="javascript:void(0)" class="elt-tab" data-tab="3" id="conversion">${lang.conversion}</a>
				<a href="javascript:void(0)" class="elt-tab" data-tab="4" id="crowd">${lang.crowd}</a>
				<a href="javascript:void(0)" class="elt-tab" data-tab="5" id="heat">${lang.heat}</a>
			
			</div>
			<div id="tableLoading" style="display:none;text-align:center;">
		        <img src="../imgs/loading.gif"/>
		    </div>
			<div class="exp-ltcont">
				<div class="exp-figbox" data-tabvalue="1" >
					<div class="expfig-sum" id="finalTime">
					</div>
					<div class="expfig-detail">
  	  					<div class="fig-hdbar">
     					   <span style="line-height: 42px;">${lang.meidaRank}</span><span id="mediaSpan"> </span> 
    					</div>
        
    					<div class="fb-fig">
      						<input type="radio" name="media_chart_radio" value="bg" onClick="changeSelect()"/>${lang.exposure}
       						<input type="radio" name="media_chart_radio" value="dj" onClick="changeSelect()" checked="checked" />${lang.click}
							<div  id="media_chart_bg" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative;overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
       						<div  id="media_chart_dj" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative;overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
    					</div>
					</div>
					<div class="expfig-detail">
						<div class="fig-hdbar">
           					<span span style="line-height: 42px;">${lang.todayHoursTrend}</span> <span id="hourSpan"> </span> 
			        	</div>
       					<div class="fb-fig">
            				<div  id="hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
        				</div>
					</div>
					<div class="expfig-detail">
						<div class="fig-hdbar">
            				<span span style="line-height: 42px;">${lang.clickTrend}</span><span id="djSpan"> </span>
        				</div>
       					<div class="fb-fig">
            				<div  id="dj_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
       					</div>
					</div>
					<div class="expfig-detail">
						<div style="padding-left: 0px;padding-right: 10px;">
					    	<div class="fig-hdbar">
					            <span  style="line-height: 42px;">${lang.activeDataRegionalDistribution}</span> <span id="areaSpan"> </span> 
					        </div>
					        <div class="fb-fig">
					            <div  id="area_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
					        </div>
					    </div>
					</div>
				</div>
				<div class="exp-figbox" data-tabvalue="2" style="display: none;">
					<div id="baseDataTable">
						<div id="baseDataTableLoading" style="display:none;text-align:center;">
					        <img src="../imgs/loading.gif"/>
					    </div>
						<div class="expfig-detail">
			            	<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.baseDataExposureClick}</span> 
			                </div>
			                <div class="fb-fig">
			                    <input type="radio" name="base_radio" value="bg" checked="checked" onClick="changeTable()"/>PV
			                    <input type="radio" name="base_radio" value="dj"  onClick="changeTable()"/>UV
			                    <div  id="base_bg_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="base_dj_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			            	</div>
						</div>
					</div>
					<div id="diversionDay">
						<div id="dayLoading" style="display:none;text-align:center;">
					        <img src="../imgs/loading.gif"/>
					    </div>
						<div class="expfig-detail">
			            	<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.diversionDay}</span> 
			                </div>
			                <div class="fb-fig">
		                		<div style="width:220px;height:35px;float:right;">${lang.AnalysisDimension}
		                			<select id="daySelect" onchange="changeDayChart(this)">
				                		<option value="dj_num_day_chart" selected="selected">${lang.clickNumber}</option>
				                		<option value="dj_person_day_chart">${lang.clickPeople}</option>
				                		<option value="bg_num_day_chart">${lang.exposureNumber}</option>
				                		<option value="bg_person_day_chart">${lang.exposurePeople}</option>
				                	</select>
		                		</div>
			                    <div  id="dj_num_day_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="dj_person_day_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="bg_num_day_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="bg_person_day_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			            	</div>
						</div>
						<div class="expfig-detail">
			            	<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.diversionHour}</span> 
			                </div>
			                <div class="fb-fig">
			                	<div style="width:220px;height:35px;float:right;">${lang.AnalysisDimension}
		                			<select id="hourSelect" onchange="changeHourChart(this)">
				                		<option value="dj_num_hour_chart" selected="selected">${lang.clickNumber}</option>
				                		<option value="dj_person_hour_chart">${lang.clickPeople}</option>
				                		<option value="bg_num_hour_chart">${lang.exposureNumber}</option>
				                		<option value="bg_person_hour_chart">${lang.exposurePeople}</option>
				                	</select>
		                		</div>
			                    <div  id="dj_num_hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="dj_person_hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="bg_num_hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                    <div  id="bg_person_hour_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			            	</div>
						</div>
					</div>					
					<div id="effectChannelTable">
						<div id="effectChannelTableLoading" style="display:none;text-align:center;">
					        <img src="../imgs/loading.gif"/>
					    </div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.effectChannelClickFrequency}</span> 
			                </div>
			                
			                <div class="fb-fig" style="width: 100%; height: 950px;">
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_click_stack" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                     <div style="width: 100%; height: 50px;"></div>
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_click_line" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                </div>
						</div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.effectChannelExposureFrequency}</span> 
			                </div>
			                <div class="fb-fig" style="width: 100%; height: 950px;">
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_exp_stack" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                     <div style="width: 100%; height: 50px;"></div>
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_exp_line" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                </div>
						</div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.effectChannelLandFrequency}</span> 
			                </div>
			                
			                <div class="fb-fig" style="width: 100%; height: 950px;">
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_landing_stack" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                     <div style="width: 100%; height: 50px;"></div>
			                     <div style=" padding-left: 0px;padding-right: 0px;">
			                            <div  id="fr_landing_line" style="width: 100%; height: 420px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
			                     </div>
			                </div>
						</div>
					</div>
					
				</div>
				<div class="exp-figbox " data-tabvalue="3" style="display: none;">
					<div id="conversionLoading" style="display:none;text-align:center;">
				        <img src="../imgs/loading.gif"/>
				    </div>
				    <div id ="conversionDiv">
				    	<div class="expfig-detail">
							<div style="padding-left: 0px;padding-right: 10px;">
						    	<div class="fig-hdbar">
						            <span  style="line-height: 42px;">${lang.ConversionRate}</span> 
						        </div>
						        <div class="fb-fig" style="height: 499.333px;">
				        			<div  id="funnel_chart" style="padding:20px;float:left;width: 49%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
				            		<div  id="overall_conversion_chart" style="float:left;width: 49%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
						        </div>
						    </div>
						</div>
						<div class="expfig-detail">
							<div style="padding-left: 0px;padding-right: 10px;">
						    	<div class="fig-hdbar">
						            <span  style="line-height: 42px;">${lang.classConversion}</span> 
						        </div>
						        <div class="fb-fig">
										<div class="expfig-detail">
									    	<div id="bg_dj_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
										</div>
										<div class="expfig-detail">
									    	<div id="dj_dd_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
										</div>
										<div class="expfig-detail">
									    	<div id="dd_xd_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
										</div>
										<div class="expfig-detail">
									    	<div id="xd_gm_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
										</div>
								</div>
						    </div>
						</div>
				    </div>
				</div>
				<div class="exp-figbox " data-tabvalue="4" style="display: none;">
					<div id="crowdLoading" style="display:none;text-align:center;">
				        <img src="../imgs/loading.gif"/>
				    </div>
				    <div id="crowdDiv">
				    	<div class="expfig-detail">
						    	<div class="fig-hdbar">
						            <span  style="line-height: 42px;">${lang.ageYear}</span> 
						        </div>
						        <div class="fb-fig" style="height: 499.333px;">
				        			<div  id="gender_chart" style="padding:20px;float:left;width: 49%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
				        			<div  id="age_chart" style="float:left;width: 49%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
						        </div>
						</div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
	           					<span span style="line-height: 42px;">${lang.mediaTrajectory}</span> <span id="hourSpan"> </span> 
				        	</div>
	       					<div class="fb-fig">
	       						<div class="expfig-detail">
	            					<div  id="media_trajectory_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
	        					</div>
	        				</div>
						</div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
	           					<span span style="line-height: 42px;">${lang.industryOrientation}</span> <span id="hourSpan"> </span> 
				        	</div>
	       					<div class="fb-fig">
	       						<div class="expfig-detail">
	            					<div  id="industry_orientation_chart" style="width: 100%; height: 450px; cursor: default;"><div style="position: relative; overflow: hidden; width: 1184px; height: 350px;"><div class="zr-element" data-zr-dom-id="bg" style="position: absolute; left: 0px; top: 0px; width: 100px; height: 350px;"></div><canvas class="zr-element" data-zr-dom-id="0" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas><canvas class="zr-element" data-zr-dom-id="_zrender_hover_" height="350" width="1184" style="position: absolute; left: 0px; top: 0px; width: 1184px; height: 350px;"></canvas></div></div>
	        					</div>
	        				</div>
						</div>
				    </div>
				</div>
				<div class="exp-figbox " data-tabvalue="5" style="display: none;">
					<div id="heatLoading" style="display:none;text-align:center;">
				        <img src="../imgs/loading.gif"/>
				    </div>
				    <div id="heatDiv">
					    <div class="expfig-detail">
							<div class="fig-hdbar">
			                    <span style="line-height: 42px;">${lang.pagePath}</span> 
			                </div>
			                <div style="width: 100%;" class="fb-fig-update">
								<div ng-app="urlApp" ng-controller="CartController">
									<table width="100%" class="up-detable" id="urlTable">
										<tr>
											<th width="60%">${lang.pageUrl}</th>
											<th width="10%">${lang.PV}</th>
											<th width="10%">${lang.UV}</th>
											<th width="10%">${lang.averageAccessTime}</th>
											<th width="10%">${lang.bounceRate}</th>
										</tr>
									</table>
								</div>
			                </div>
						</div>
						<div class="expfig-detail">
							<div class="fig-hdbar">
			                    <span style="line-height: 42px;">产品关联</span> 
			                </div>
			                <div style="width: 100%;" class="fb-fig-update">
								<div ng-app="urlApp" ng-controller="CartController">
									<table width="100%" class="up-detable" id="productTable">
										<tr>
											<th width="60%">${lang.productPage}</th>
											<th width="10%">${lang.PV}</th>
											<th width="10%">${lang.UV}</th>
											<th width="10%">${lang.averageAccessTime}</th>
											<th width="10%">${lang.bounceRate}</th>
										</tr>
									</table>
								</div>
			                </div>
						</div>
				    </div>
				</div>
			</div>
		</div>
	</div>
</div>
<script type="text/javascript">
seajs.use(["modules/base/meta/generalSituat"], function(generalSituat) {
        generalSituat.showReport();
    });

   
</script>

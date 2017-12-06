//add by zwx379555 start

var userAccessTrend={
        "0":"NEWVISITOR",
		"1":"TOTALVISITOR",
        "2":"NEWREGISTERUSER",
        "3":"NEWORDERUSER",
        "4":"LOGINUSER",
        "5":"ALLREGISTERUSER",
        "6":"TOTALREGISTERUSER",
        "7":"NEWUNSUBSCRIBEPRODUCT"
};

var userAccessDisplayTrend = new Array(
        {displayName:$.i18n.prop("video.user.access.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.access.tabletitle_7"),isAddDecimal:false,isAddPercent:false}
        );
		
var userAccessTabled={
        "0":"DTIME",
        "1":"NEWVISITOR",
		"2":"TOTALVISITOR",
        "3":"NEWREGISTERUSER",
        "4":"NEWORDERUSER",
        "5":"LOGINUSER",
        "6":"ALLREGISTERUSER",
        "7":"TOTALREGISTERUSER",
        "8":"NEWUNSUBSCRIBEPRODUCT"
};

var userAccessTablem={
        "0":"MTIME",
        "1":"NEWVISITOR",
		"2":"TOTALVISITOR",
        "3":"NEWREGISTERUSER",
        "4":"NEWORDERUSER",
        "5":"LOGINUSER",
        "6":"ALLREGISTERUSER",
        "7":"TOTALREGISTERUSER",
        "8":"NEWUNSUBSCRIBEPRODUCT"
};
var userAccessTablew={
        "0":"WTIME",
        "1":"NEWVISITOR",
		"2":"TOTALVISITOR",
        "3":"NEWREGISTERUSER",
        "4":"NEWORDERUSER",
        "5":"LOGINUSER",
        "6":"ALLREGISTERUSER",
        "7":"TOTALREGISTERUSER",
        "8":"NEWUNSUBSCRIBEPRODUCT"
};		
		
var userAccessShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWVISITOR",isAddDecimal:true,isAddPercent:false},
		 {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOGINUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWUNSUBSCRIBEPRODUCT",isAddDecimal:true,isAddPercent:false}
         );
var userAccessShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWVISITOR",isAddDecimal:true,isAddPercent:false},
		 {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOGINUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWUNSUBSCRIBEPRODUCT",isAddDecimal:true,isAddPercent:false}
         );
var userAccessShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWVISITOR",isAddDecimal:true,isAddPercent:false},
		 {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOGINUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWUNSUBSCRIBEPRODUCT",isAddDecimal:true,isAddPercent:false}
         );
		
var watchTimeAnalysisTabled={
        "0":"HTIME",
        "1":"HOURSWATCH"
};


var liveWatchTimeAnalysisTabled={
        "0":"HTIME",
        "1":"HOURSWATCH"
};






var userOrderTimeAnalysisTabled={
        "0":"HTIME",
        "1":"DAYORDERPRODUCT"
};


var userOrderTimeAnalysisShowTabled = new Array(
         {displayName:"HTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAYORDERPRODUCT",isAddDecimal:true,isAddPercent:false}
         );
         


var watchTimeAnalysisShowTabled = new Array({displayName:"HTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"HOURSWATCH",isAddDecimal:true,isAddPercent:false}
         );

         
         
var liveWatchTimeAnalysisShowTabled = new Array({displayName:"HTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"HOURSWATCH",isAddDecimal:true,isAddPercent:false}
         );         
         
         
         




var liveOperationTrend={
        "0":"PLAYTIMES",
        "1":"PLAYUSERS",
        "2":"PLAYTIME",
        "3":"PERCAPITAPLAYTIME",
        "4":"PERCAPITAPLAY",
        "5":"AVGVIDEOTIME"
};



var liveOperationTrendTitle = new Array(
        {displayName:$.i18n.prop("video.live.operation.trendtitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.operation.trendtitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.operation.trendtitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.operation.trendtitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.operation.trendtitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.operation.trendtitle_5"),isAddDecimal:false,isAddPercent:false}
        );



        
        
        
        
        
        
var liveOperationTrendTabled={
        "0":"DTIME",
        "1":"PLAYTIMES",
        "2":"PLAYUSERS",       
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};

var liveOperationTrendTablem={
        "0":"MTIME",
        "1":"PLAYTIMES",
        "2":"PLAYUSERS",       
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};
var liveOperationTrendTablew={
        "0":"WTIME",
        "1":"PLAYTIMES",
        "2":"PLAYUSERS",       
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};


var liveOperationTrendShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );
         
var liveOperationTrendShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );
         
var liveOperationTrendShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );        
        
        
        
        



var userRetentionTrend={
        "0":"NEWSUBSCRIBER",
        "1":"DAY01RETENTIONRATE",
        "2":"DAY03RETENTIONRATE",
        "3":"DAY07RETENTIONRATE",
        "4":"DAY15RETENTIONRATE",
        "5":"DAY30RETENTIONRATE",
        "6":"AVGDAY01RETENTIONRATE",
        "7":"AVGDAY03RETENTIONRATE",
        "8":"AVGDAY07RETENTIONRATE",
        "9":"AVGDAY15RETENTIONRATE",
        "10":"AVGDAY30RETENTIONRATE"
        
        /* "0":"DAY01RETENTIONRATE",
        "1":"DAY03RETENTIONRATE",
        "2":"DAY07RETENTIONRATE",
        "3":"DAY15RETENTIONRATE",
        "4":"DAY30RETENTIONRATE",
        "5":"AVGDAY01RETENTIONRATE",
        "6":"AVGDAY03RETENTIONRATE",
        "7":"AVGDAY07RETENTIONRATE",
        "8":"AVGDAY15RETENTIONRATE",
        "9":"AVGDAY30RETENTIONRATE" */
};




var userRetentionDisplayTrend = new Array(
        {displayName:$.i18n.prop("video.user.retention.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_8"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_9"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_10"),isAddDecimal:false,isAddPercent:false}
        
        /* {displayName:$.i18n.prop("video.user.retention.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_8"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_9"),isAddDecimal:false,isAddPercent:false} */
);














var userRetentionAvgTrend={
        "0":"AVGDAY01RETENTIONRATE",
        "1":"AVGDAY03RETENTIONRATE",
        "2":"AVGDAY07RETENTIONRATE",
        "3":"AVGDAY15RETENTIONRATE",
        "4":"AVGDAY30RETENTIONRATE"
};




var userRetentionAvgDisplayTrend = new Array(
        {displayName:$.i18n.prop("video.user.retention.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_8"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_9"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_10"),isAddDecimal:false,isAddPercent:false}
);





















var activityUserRetentionTrend={
        "0":"NEWSUBSCRIBER",
        "1":"DAY01RETENTIONRATE",
        "2":"DAY03RETENTIONRATE",
        "3":"DAY07RETENTIONRATE",
        "4":"DAY15RETENTIONRATE",
        "5":"DAY30RETENTIONRATE"
};




var activityUserRetentionDisplayTrend = new Array(
        {displayName:$.i18n.prop("video.user.retention.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.retention.tabletitle_5"),isAddDecimal:false,isAddPercent:false}
);



var activityUserRetentionTabled={
        "0":"DTIME",
        "1":"NEWSUBSCRIBER",
        "2":"DAY01RETENTIONRATE",
        "3":"DAY03RETENTIONRATE",
        "4":"DAY07RETENTIONRATE",
        "5":"DAY15RETENTIONRATE",
        "6":"DAY30RETENTIONRATE"
};




var activityactivityUserRetentionShowTabled = new Array( 
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY03RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY15RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY30RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
);





















var userRetentionTabled={
        "0":"DTIME",
        "1":"NEWSUBSCRIBER",
        "2":"DAY01RETENTIONRATE",
        "3":"DAY03RETENTIONRATE",
        "4":"DAY07RETENTIONRATE",
        "5":"DAY15RETENTIONRATE",
        "6":"DAY30RETENTIONRATE",
        "7":"AVGDAY01RETENTIONRATE",
        "8":"AVGDAY03RETENTIONRATE",
        "9":"AVGDAY07RETENTIONRATE",
        "10":"AVGDAY15RETENTIONRATE",
        "11":"AVGDAY30RETENTIONRATE"
        
        /* "0":"DTIME",
        "1":"DAY01RETENTIONRATE",
        "2":"DAY03RETENTIONRATE",
        "3":"DAY07RETENTIONRATE",
        "4":"DAY15RETENTIONRATE",
        "5":"DAY30RETENTIONRATE",
        "6":"AVGDAY01RETENTIONRATE",
        "7":"AVGDAY03RETENTIONRATE",
        "8":"AVGDAY07RETENTIONRATE",
        "9":"AVGDAY15RETENTIONRATE",
        "10":"AVGDAY30RETENTIONRATE" */
};



var userRetentionShowTabled = new Array( 
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY03RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY15RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY30RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGDAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGDAY03RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGDAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGDAY15RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGDAY30RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );



var projectSituationSingleTrend={
        "0":"NEWREGISTERUSER",
        "1":"TOTALREGISTERUSER",
        "2":"REGISTERUSERACTIVE",
        "3":"REGISTERUSERACTIVERATE",
		"4":"NEWVISITOR",
        "5":"TOTALVISITOR",		
        "6":"NEWCLICKNEW",
        "7":"NEWSUBSCRIBENEW",
        "8":"NEWPAYUSER",
        "9":"NEWPAYCONVERT",
        "10":"ARPUINCOMENEW"
};


var projectSituationSingleDisplayTrend = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_8"),isAddDecimal:false,isAddPercent:false},
		{displayName:$.i18n.prop("video.project.situation.single.tabletitle_9"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.situation.single.tabletitle_10"),isAddDecimal:false,isAddPercent:false}
);


var projectSituationSingleTabled={
        "0":"DTIME",
        "1":"NEWREGISTERUSER",
        "2":"TOTALREGISTERUSER",
        "3":"REGISTERUSERACTIVE",
        "4":"REGISTERUSERACTIVERATE",
		"5":"NEWVISITOR",
        "6":"TOTALVISITOR",		
        "7":"NEWCLICKNEW",
        "8":"NEWSUBSCRIBENEW",
        "9":"NEWPAYUSER",
        "10":"NEWPAYCONVERT",
        "11":"ARPUINCOMENEW"
};


var projectSituationSingleShowTabled = new Array( 
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVERATE",isAddDecimal:true,isAddPercent:false},
		 {displayName:"NEWVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},		 
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYCONVERT",isAddDecimal:true,isAddPercent:false},
         {displayName:"ARPUINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );



var userActiveTrend={
        "0":"ACTIVEUSERS",
        "1":"DAYACTIVEUSERS",
        "2":"REGISTERUSERACTIVE",
        "3":"REGISTERUSERACTIVERATE",
		"4":"WATCHUSERS",
        "5":"TOTALREGISTERUSER",
        "6":"TOTALVISITOR",
        "7":"APPDOWNLOAD"
};

var userActiveTabled={
        "0":"DTIME",
        "1":"ACTIVEUSERS",
        "2":"DAYACTIVEUSERS",
        "3":"REGISTERUSERACTIVE",
        "4":"REGISTERUSERACTIVERATE",
		"5":"WATCHUSERS",
        "6":"TOTALREGISTERUSER",
        "7":"TOTALVISITOR",
        "8":"APPDOWNLOAD"
};

var userActiveTablem={
        "0":"MTIME",
        "1":"ACTIVEUSERS",
        "2":"DAYACTIVEUSERS",
        "3":"REGISTERUSERACTIVE",
        "4":"REGISTERUSERACTIVERATE",
		"5":"WATCHUSERS",
        "6":"TOTALREGISTERUSER",
        "7":"TOTALVISITOR",
        "8":"APPDOWNLOAD"
};
var userActiveTablew={
        "0":"WTIME",
        "1":"ACTIVEUSERS",
        "2":"DAYACTIVEUSERS",
        "3":"REGISTERUSERACTIVE",
        "4":"REGISTERUSERACTIVERATE",
		"5":"WATCHUSERS",
        "6":"TOTALREGISTERUSER",
        "7":"TOTALVISITOR",
	"8":"APPDOWNLOAD"
};

var videoAppversionTabled={
        "0":"PLATFORMID",
        "1":"APPVERSIONCODE",
        "2":"APPVERNUM",
        "3":"APPVERPERCENT"
};


var userActiveShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAYACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVERATE",isAddDecimal:true,isAddPercent:false},
		 {displayName:"WATCHUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"APPDOWNLOAD",isAddDecimal:true,isAddPercent:false}
         );
         
var userActiveShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAYACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVERATE",isAddDecimal:true,isAddPercent:false},
		 {displayName:"WATCHUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"APPDOWNLOAD",isAddDecimal:true,isAddPercent:false}
         );
         
var userActiveShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAYACTIVEUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVERATE",isAddDecimal:true,isAddPercent:false},
		 {displayName:"WATCHUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALVISITOR",isAddDecimal:true,isAddPercent:false},
         {displayName:"APPDOWNLOAD",isAddDecimal:true,isAddPercent:false}
         );

var userActiveDisplayTrend = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.user.active.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.active.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.active.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.active.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
		{displayName:$.i18n.prop("video.user.active.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.active.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.user.active.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
	 {displayName:$.i18n.prop("video.user.active.tabletitle_7"),isAddDecimal:false,isAddPercent:false}
        );

var videoAppversionShowTabled = new Array(
         {displayName:"PLATFORMID",isAddDecimal:false,isAddPercent:false},
         {displayName:"APPVERSIONCODE",isAddDecimal:false,isAddPercent:false},
         {displayName:"APPVERNUM",isAddDecimal:true,isAddPercent:false},
         {displayName:"APPVERPERCENT",isAddDecimal:true,isAddPercent:false}

         );

        
        
        
var incomeAnalysisTrend={
        "0":"NEWINCOMENEW",
        "1":"NEWCLICKNEW",
        "2":"NEWSUBSCRIBENEW",
        "3":"NEWCLICKUSER",
        "4":"NEWSUBSCRIBEUSER",
        "5":"NEWPAYUSER",
        "6":"NEWPAYCONVERT",
        "7":"YEARALLINCOMENEW",
        "8":"ARPUINCOMENEW"
};        
        
        
var incomeAnalysisTabled={
        "0":"DTIME",
        "1":"NEWINCOMENEW",
        "2":"NEWCLICKNEW",
        "3":"NEWSUBSCRIBENEW",
        "4":"NEWCLICKUSER",
        "5":"NEWSUBSCRIBEUSER",
        "6":"NEWPAYUSER",
        "7":"NEWPAYCONVERT",
        "8":"YEARALLINCOMENEW",
        "9":"ARPUINCOMENEW"
};

var incomeAnalysisTablem={
        "0":"MTIME",
        "1":"NEWINCOMENEW",
        "2":"NEWCLICKNEW",
        "3":"NEWSUBSCRIBENEW",
        "4":"NEWCLICKUSER",
        "5":"NEWSUBSCRIBEUSER",
        "6":"NEWPAYUSER",
        "7":"NEWPAYCONVERT",
        "8":"YEARALLINCOMENEW",
        "9":"ARPUINCOMENEW"
};
var incomeAnalysisTablew={
        "0":"WTIME",
        "1":"NEWINCOMENEW",
        "2":"NEWCLICKNEW",
        "3":"NEWSUBSCRIBENEW",
        "4":"NEWCLICKUSER",
        "5":"NEWSUBSCRIBEUSER",
        "6":"NEWPAYUSER",
        "7":"NEWPAYCONVERT",
        "8":"YEARALLINCOMENEW",
        "9":"ARPUINCOMENEW"
};


var incomeAnalysisShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYCONVERT",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"ARPUINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
         
var incomeAnalysisShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYCONVERT",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"ARPUINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
         
var incomeAnalysisShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYCONVERT",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"ARPUINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );        
        
    
    
        
        
        
var incomeAnalysisDisplayTrend = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.income.analysis.tabletitle_8"),isAddDecimal:false,isAddPercent:false}
        );












var contentPortraitTable={
        "0":"CONTENTID",
        "1":"CONTENTNAME",
        "2":"PLAYUSERS",
        "3":"PLAYTIMES",
        "4":"PLAYTIME",
        "5":"PERCAPITAPLAYTIME",
        "6":"PERCAPITAPLAY",
        "7":"AVGVIDEOTIME"
};




var incomeRankTable={
        "0":"PRODUCTNUMBER",
        "1":"PRODUCTNAME",
        "2":"NEWCLICKINCOME",
        "3":"YEARCLICKINCOME"
};




var liveWatchTimeAnalysisTable={
        "0":"LOGICALCHANNELID",
        "1":"WATCH"
};







var productAnalysisTable={
        "0":"PRODUCTID",
        "1":"PRODUCTNAME",
        "2":"PRODUCTCATALOG",
        "3":"PRODUCTORDERTYPE",
        "4":"ALLORDERTIMES",
        "5":"NEWORDERTIMES",
        "6":"LOSEORDERTIMES",
        "7":"ALLORDERUSER",
        "8":"NEWORDERUSER",
        "9":"ORDERUSERPROPORTION",
        "10":"LOSEORDERUSER"
        
};







var orderUserAnalysisTable={
        "0":"PRODUCTID",
        "1":"PRODUCTNAME",
        "2":"PRODUCTCATALOG",
        "3":"PRODUCTORDERTYPE",
        "4":"ALLORDERUSER",
        "5":"NEWORDERUSER",
        "6":"ORDERUSERPROPORTION",
        "7":"LOSEORDERUSER"
};



var orderUserAnalysisShowTable = new Array( 
         {displayName:"PRODUCTID",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTCATALOG",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTORDERTYPE",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );














var incomeRankShowTable = new Array( 
         {displayName:"PRODUCTNUMBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARCLICKINCOME",isAddDecimal:true,isAddPercent:false}
         );


         
         
var liveWatchTimeAnalysisShowTable = new Array( 
         {displayName:"LOGICALCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"WATCH",isAddDecimal:true,isAddPercent:false}
);         
         
         
         
         
         
         
         

var productAnalysisShowTable = new Array( 
         {displayName:"PRODUCTID",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTCATALOG",isAddDecimal:true,isAddPercent:false},
         {displayName:"PRODUCTORDERTYPE",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );



         
         
var liveCooperationPortraitTable={
        "0":"COMPANYNAME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};         
         
var livecooperationPortraitShowTable = new Array( 
         {displayName:"COMPANYNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );         
         
         
         
         
         
         



var orderAnalysisTable={
        "0":"PRODUCTID",
        "1":"ALLORDER",
        "2":"NEWORDER",
        "3":"LOSEORDER"
};
var cooperationPortraitTable={
        "0":"COMPANYNAME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};
var contentPortraitTabled={
        "0":"DTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};
var channelRankTabled={
        "0":"DTIME",
        "1":"NEWREGISTERUSER",
        "2":"ACTIVEUSER",
        "3":"NEWPAYUSER",
        "4":"YEARALLINCOMENEW"
};
var channelRankTablem={
        "0":"MTIME",
        "1":"NEWREGISTERUSER",
        "2":"ACTIVEUSER",
        "3":"NEWPAYUSER",
        "4":"YEARALLINCOMENEW"
};
var channelRankTablew={
        "0":"WTIME",
        "1":"NEWREGISTERUSER",
        "2":"ACTIVEUSER",
        "3":"NEWPAYUSER",
        "4":"YEARALLINCOMENEW"
};
/* var userActiveTabled={
        "0":"HTIME",
        "1":"HOURSWATCH"
}; */





var projectChannelRankTabled={
        "0":"DTIME",
        "1":"NEWREGISTERUSER",
        "2":"ALLREGISTERUSER",
        "3":"REGISTERUSERACTIVE",
        "4":"NEWPAYUSER",
        "5":"NEWINCOMENEW",
        "6":"YEARALLINCOMENEW"
};
var projectChannelRankTablem={
        "0":"MTIME",
        "1":"NEWREGISTERUSER",
        "2":"ALLREGISTERUSER",
        "3":"REGISTERUSERACTIVE",
        "4":"NEWPAYUSER",
        "5":"NEWINCOMENEW",
        "6":"YEARALLINCOMENEW"
};
var projectChannelRankTablew={
        "0":"WTIME",
        "1":"NEWREGISTERUSER",
        "2":"ALLREGISTERUSER",
        "3":"REGISTERUSERACTIVE",
        "4":"NEWPAYUSER",
        "5":"NEWINCOMENEW",
        "6":"YEARALLINCOMENEW"
};



var liveWatchRankChannelTable={
        "0":"LOGICALCHANNELID",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};




var liveWatchRankChannelTablew={
        "0":"WTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};

var liveWatchRankChannelTablem={
        "0":"MTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};

var liveWatchRankChannelTabled={
        "0":"DTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};


var contentPortraitTablew={
        "0":"WTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};
var orderAnalysisTabled={
        "0":"DTIME",
        "1":"ALLORDER",
        "2":"NEWORDER",
        "3":"LOSEORDER"
};
var orderAnalysisTablem={
        "0":"MTIME",
        "1":"ALLORDER",
        "2":"NEWORDER",
        "3":"LOSEORDER"
};
var orderAnalysisTablew={
        "0":"WTIME",
        "1":"ALLORDER",
        "2":"NEWORDER",
        "3":"LOSEORDER"
};
var contentPortraitTablem={
        "0":"MTIME",
        "1":"PLAYUSERS",
        "2":"PLAYTIMES",
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};

var liveWatchRankChannelShowTable = new Array(
         {displayName:"LOGICALCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );


var orderAnalysisShowTable = new Array( {displayName:"PRODUCTID",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDER",isAddDecimal:true,isAddPercent:false}
         );
var cooperationPortraitShowTable = new Array( {displayName:"COMPANYNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );

var channelRankShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var channelRankShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var channelRankShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
         
         
         
         
         
         
var projectChannelRankShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var projectChannelRankShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var projectChannelRankShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
         
/* var userActiveShowTabled = new Array({displayName:"HTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"HOURSWATCH",isAddDecimal:true,isAddPercent:false}
         ); */
var orderAnalysisShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDER",isAddDecimal:true,isAddPercent:false}
         );
var orderAnalysisShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDER",isAddDecimal:true,isAddPercent:false}
         );
var orderAnalysisShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDER",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDER",isAddDecimal:true,isAddPercent:false}
         );
         
 var contentPortraitShowTable = new Array( 
         {displayName:"CONTENTID",isAddDecimal:true,isAddPercent:false},
         {displayName:"CONTENTNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );        
         
var contentPortraitShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );
var contentPortraitShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );

var contentPortraitShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );         
         





var liveWatchRankShowTablem = new Array(
         {displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );
var liveWatchRankShowTablew = new Array(
         {displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );

var liveWatchRankShowTabled = new Array(
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );             
         
         




         
         
var contentOperationTrend={
        "0":"VIDEOSNUMBER",
        "1":"PLAYTIMES",
        "2":"PLAYUSERS",       
        "3":"PLAYTIME",
        "4":"PERCAPITAPLAYTIME",
        "5":"PERCAPITAPLAY",
        "6":"AVGVIDEOTIME"
};
var incomeRankTrend={
        "0":"NEWCLICKINCOMENEW",
        "1":"YEARCLICKINCOMENEW"
};


var productOrderAnalysisTrend={
        "0":"ALLORDERTIMES",
        "1":"NEWORDERTIMES",
        "2":"LOSEORDERTIMES",
        "3":"ALLORDERUSER",
        "4":"NEWORDERUSER",
        "5":"ORDERUSERPROPORTION",
        "6":"LOSEORDERUSER"
};





var orderUserAnalysisTrend={
        "0":"ALLORDERUSER",
        "1":"NEWORDERUSER",
        "2":"ORDERUSERPROPORTION",
        "3":"LOSEORDERUSER"
};






var orderUserAnalysisTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.order.user.analysisl.table.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.order.user.analysisl.table.title_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.order.user.analysisl.table.title_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.order.user.analysisl.table.title_3"),isAddDecimal:false,isAddPercent:false}
        );
















var incomeRankTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.income.rank.detail.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.income.rank.detail.title_1"),isAddDecimal:false,isAddPercent:false}
        );





var productOrderAnalysisTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.product.order.analysisl.title_6"),isAddDecimal:false,isAddPercent:false}
        );

        
        
var productOrderAnalysisTablew={
        "0":"WTIME",
        "1":"ALLORDERTIMES",
        "2":"NEWORDERTIMES",
        "3":"LOSEORDERTIMES",
        "4":"ALLORDERUSER",
        "5":"NEWORDERUSER",
        "6":"ORDERUSERPROPORTION",
        "7":"LOSEORDERUSER"
        
};        
        
        
var productOrderAnalysisTablem={
        "0":"MTIME",
        "1":"ALLORDERTIMES",
        "2":"NEWORDERTIMES",
        "3":"LOSEORDERTIMES",
        "4":"ALLORDERUSER",
        "5":"NEWORDERUSER",
        "6":"ORDERUSERPROPORTION",
        "7":"LOSEORDERUSER"
};

var productOrderAnalysisTabled={
        "0":"DTIME",
        "1":"ALLORDERTIMES",
        "2":"NEWORDERTIMES",
        "3":"LOSEORDERTIMES",
        "4":"ALLORDERUSER",
        "5":"NEWORDERUSER",
        "6":"ORDERUSERPROPORTION",
        "7":"LOSEORDERUSER"
};        
        
        
var productOrderAnalysisShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );    

var productOrderAnalysisShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );
         
         
var productOrderAnalysisShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );        
        
    
    
    
    
    
    
var orderUserAnalysisShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );    

var orderUserAnalysisShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );
         
         
var orderUserAnalysisShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ORDERUSERPROPORTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"LOSEORDERUSER",isAddDecimal:true,isAddPercent:false}
         );        
    
    
    





    
        

var orderUserAnalysisTablew={
        "0":"WTIME",
        "1":"ALLORDERUSER",
        "2":"NEWORDERUSER",
        "3":"ORDERUSERPROPORTION",
        "4":"LOSEORDERUSER"
};        
        
        
var orderUserAnalysisTablem={
        "0":"MTIME",
        "1":"ALLORDERUSER",
        "2":"NEWORDERUSER",
        "3":"ORDERUSERPROPORTION",
        "4":"LOSEORDERUSER"
};

var orderUserAnalysisTabled={
        "0":"DTIME",
        "1":"ALLORDERUSER",
        "2":"NEWORDERUSER",
        "3":"ORDERUSERPROPORTION",
        "4":"LOSEORDERUSER"
};    















        
        
        
        
        
        
        
        

        
var incomeRankTablew={
        "0":"WTIME",
        "1":"NEWCLICKINCOMENEW",
        "2":"YEARCLICKINCOMENEW"
};        
        
        
var incomeRankTablem={
        "0":"MTIME",
        "1":"NEWCLICKINCOMENEW",
        "2":"YEARCLICKINCOMENEW"
};

var incomeRankTabled={
        "0":"DTIME",
        "1":"NEWCLICKINCOMENEW",
        "2":"YEARCLICKINCOMENEW"
};

        
var incomeRankShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );    

var incomeRankShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
         
         
var incomeRankShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARCLICKINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );



         
var liveWatchRankChannelTrend={
        "0":"PLAYUSERS",
        "1":"PLAYTIMES",
        "2":"PLAYTIME",
        "3":"PERCAPITAPLAYTIME",
        "4":"PERCAPITAPLAY",
        "5":"AVGVIDEOTIME"
};         
         
         
    

var contentPortraitTrend={
        "0":"PLAYUSERS",
        "1":"PLAYTIMES",
        "2":"PLAYTIME",
        "3":"PERCAPITAPLAYTIME",
        "4":"PERCAPITAPLAY",
        "5":"AVGVIDEOTIME"
};

var channelRankTrend={
        "0":"NEWREGISTERUSER",
        "1":"ACTIVEUSER",
        "2":"NEWPAYUSER",
        "3":"YEARALLINCOMENEW"
};


var projectChannelRankTrend={
        "0":"NEWREGISTERUSER",
        "1":"ALLREGISTERUSER",
        "2":"REGISTERUSERACTIVE",
        "3":"NEWPAYUSER",
        "4":"NEWINCOMENEW",
        "5":"YEARALLINCOMENEW"
};








var projectActivityOperationTrend={
        "0":"CLICKVOLUME",
        "1":"CONVERSIONQUANTITY",
        "2":"ACTIVEUSER",
        "3":"NEWREGISTERUSER",
        "4":"NEWORDERPACKAGEUSER",
        "5":"TOTALORDERPACKAGEUSER",
        "6":"NEWLOSEORDERUSER",
        "7":"TOTALLOSEORDERUSER",
        "8":"FREEEXPERIENCEUSER",
        "9":"REGULARORDERUSER",
        "10":"NEWINCOMENEW",
        "11":"NEWCLICKNEW",
        "12":"NEWSUBSCRIBENEW",
        "13":"YEARALLINCOMENEW"
};




var projectActivityOperationShowTabled = new Array({displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CLICKVOLUME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CONVERSIONQUANTITY",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"FREEEXPERIENCEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGULARORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var projectActivityOperationShowTablem = new Array({displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CLICKVOLUME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CONVERSIONQUANTITY",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"FREEEXPERIENCEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGULARORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );
var projectActivityOperationShowTablew = new Array({displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CLICKVOLUME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CONVERSIONQUANTITY",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALORDERPACKAGEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALLOSEORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"FREEEXPERIENCEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGULARORDERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOMENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWCLICKNEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWSUBSCRIBENEW",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOMENEW",isAddDecimal:true,isAddPercent:false}
         );         
         





var projectActivityOperationTitle = new Array(
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_6"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_7"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_8"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_9"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_10"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_11"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_12"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.activity.operation.detailtitle_13"),isAddDecimal:false,isAddPercent:false}
        ); 



        
var projectActivityOperationTabled={
        "0":"DTIME",
        "1":"CLICKVOLUME",
        "2":"CONVERSIONQUANTITY",
        "3":"ACTIVEUSER",
        "4":"NEWREGISTERUSER",
        "5":"NEWORDERPACKAGEUSER",
        "6":"TOTALORDERPACKAGEUSER",
        "7":"NEWLOSEORDERUSER",
        "8":"TOTALLOSEORDERUSER",
        "9":"FREEEXPERIENCEUSER",
        "10":"REGULARORDERUSER",
        "11":"NEWINCOMENEW",
        "12":"NEWCLICKNEW",
        "13":"NEWSUBSCRIBENEW",
        "14":"YEARALLINCOMENEW"
};
var projectActivityOperationTablem={
        "0":"MTIME",
        "1":"CLICKVOLUME",
        "2":"CONVERSIONQUANTITY",
        "3":"ACTIVEUSER",
        "4":"NEWREGISTERUSER",
        "5":"NEWORDERPACKAGEUSER",
        "6":"TOTALORDERPACKAGEUSER",
        "7":"NEWLOSEORDERUSER",
        "8":"TOTALLOSEORDERUSER",
        "9":"FREEEXPERIENCEUSER",
        "10":"REGULARORDERUSER",
        "11":"NEWINCOMENEW",
        "12":"NEWCLICKNEW",
        "13":"NEWSUBSCRIBENEW",
        "14":"YEARALLINCOMENEW"
};
var projectActivityOperationTablew={
        "0":"WTIME",
        "1":"CLICKVOLUME",
        "2":"CONVERSIONQUANTITY",
        "3":"ACTIVEUSER",
        "4":"NEWREGISTERUSER",
        "5":"NEWORDERPACKAGEUSER",
        "6":"TOTALORDERPACKAGEUSER",
        "7":"NEWLOSEORDERUSER",
        "8":"TOTALLOSEORDERUSER",
        "9":"FREEEXPERIENCEUSER",
        "10":"REGULARORDERUSER",
        "11":"NEWINCOMENEW",
        "12":"NEWCLICKNEW",
        "13":"NEWSUBSCRIBENEW",
        "14":"YEARALLINCOMENEW"
};        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        





var orderAnalysisTrend={
        "0":"ALLORDER",
        "1":"NEWORDER",
        "2":"LOSEORDER"
};



var liveWatchRankChannelTitle = new Array(
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.live.watch.rank.channel.detail.title_5"),isAddDecimal:false,isAddPercent:false}
        );



var contentPortraitTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.portrait.detail.title_5"),isAddDecimal:false,isAddPercent:false}
        );
var channelRankTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.channel.rank.detailtitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.channel.rank.detailtitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.channel.rank.detailtitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.channel.rank.detailtitle_3"),isAddDecimal:false,isAddPercent:false}
        );
        
    

    
var projectChannelRankTitle = new Array(
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.channel.rank.detailtitle_5"),isAddDecimal:false,isAddPercent:false}
        );        
        
        
        
        
var orderAnalysisTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.order.analysis.detail.title_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.order.analysis.detail.title_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.order.analysis.detail.title_2"),isAddDecimal:false,isAddPercent:false}
        );
var contentOperationTitle = new Array(
        //"Total Revenue","Partners Revenue","Paying Users","Payments","ARPU","Conversion Rate"
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_5"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.project.content.operation.trendtitle_6"),isAddDecimal:false,isAddPercent:false}
        );

var contentOperationTrendTabled={
        "0":"DTIME",
        "1":"VIDEOSNUMBER",
        "2":"PLAYTIMES",
        "3":"PLAYUSERS",        
        "4":"PLAYTIME",
        "5":"PERCAPITAPLAYTIME",
        "6":"PERCAPITAPLAY",
        "7":"AVGVIDEOTIME"
};

var contentOperationTrendTablem={
        "0":"MTIME",
        "1":"VIDEOSNUMBER",
        "2":"PLAYTIMES",
        "3":"PLAYUSERS",       
        "4":"PLAYTIME",
        "5":"PERCAPITAPLAYTIME",
        "6":"PERCAPITAPLAY",
        "7":"AVGVIDEOTIME"
};

var contentOperationTrendTablew={
        "0":"WTIME",
        "1":"VIDEOSNUMBER",
        "2":"PLAYTIMES",
        "3":"PLAYUSERS",      
        "4":"PLAYTIME",
        "5":"PERCAPITAPLAYTIME",
        "6":"PERCAPITAPLAY",
        "7":"AVGVIDEOTIME"
};

var contentOperationShowTabled = new Array( 
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"VIDEOSNUMBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );

var contentOperationShowTablem = new Array( 
         {displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"VIDEOSNUMBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );

var contentOperationShowTablew = new Array( 
         {displayName:"WTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"VIDEOSNUMBER",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIMES",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYUSERS",isAddDecimal:true,isAddPercent:false},
         {displayName:"PLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAYTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"PERCAPITAPLAY",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVIDEOTIME",isAddDecimal:true,isAddPercent:false}
         );
         
         
         
var channelRankTable={
        "0":"ACTIVITYCHANNELID",
        "1":"NEWREGISTERUSER",
        "2":"ACTIVEUSER",
        "3":"NEWPAYUSER",
        "4":"YEARALLINCOME"
};

var projectChannelRankTable={
        "0":"ACTIVITYCHANNELID",
        "1":"NEWREGISTERUSER",
        "2":"ALLREGISTERUSER",
        "3":"REGISTERUSERACTIVE",
        "4":"NEWPAYUSER",
        "5":"NEWINCOME",
        "6":"YEARALLINCOME",
        "7":"DAY01RETENTIONRATE",
        "8":"DAY07RETENTIONRATE"        
};

var projectChannelRankTableZh={
		"0":"ACTIVITYCHANNELID",
        "1":"ZH_VALUE",
        "2":"NEWREGISTERUSER",
        "3":"ALLREGISTERUSER",
        "4":"REGISTERUSERACTIVE",
        "5":"NEWPAYUSER",
        "6":"NEWINCOME",
        "7":"YEARALLINCOME",
        "8":"DAY01RETENTIONRATE",
        "9":"DAY07RETENTIONRATE"        
};

var projectChannelRankTableEn={
		"0":"ACTIVITYCHANNELID",
        "1":"EN_VALUE",
        "2":"NEWREGISTERUSER",
        "3":"ALLREGISTERUSER",
        "4":"REGISTERUSERACTIVE",
        "5":"NEWPAYUSER",
        "6":"NEWINCOME",
        "7":"YEARALLINCOME",
        "8":"DAY01RETENTIONRATE",
        "9":"DAY07RETENTIONRATE"      
};






var projectActivityOperationTable={
		"0":"ACTIVITYID",
        "1":"ACTIVITYNAME",
        "2":"CLICKVOLUME",
        "3":"KEYORDERPROMOTION",
        "4":"KEYREGISTERPROMOTION",
        "5":"ACTIVEUSER",
        "6":"NEWPAYUSER",
        "7":"NEWINCOME",
        "8":"YEARALLINCOME",
        "9":"DAY01RETENTIONRATE",
        "10":"DAY07RETENTIONRATE"     
};




var projectActivityOperationShowTable = new Array( 
		 {displayName:"ACTIVITYID",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVITYNAME",isAddDecimal:true,isAddPercent:false},
         {displayName:"CLICKVOLUME",isAddDecimal:true,isAddPercent:false},
         {displayName:"KEYORDERPROMOTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"KEYREGISTERPROMOTION",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );  


var activityOperateTable={
        "0":"ACTIVITYID",
        "1":"ACTIVEUSER",
        "2":"NEWPAYUSER",
        "3":"NEWINCOME",
        "4":"YEARALLINCOME",
        "5":"DAY01RETENTIONRATE",
        "6":"DAY07RETENTIONRATE"     
};




var activityOperateShowTable = new Array( 
         {displayName:"ACTIVITYID",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );  







var channelRankShowTable = new Array( 
         {displayName:"ACTIVITYCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ACTIVEUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false}
         );  

var projectChannelRankShowTable = new Array( 
         {displayName:"ACTIVITYCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );          
         
var projectChannelRankShowTableZh = new Array( 
		 {displayName:"ACTIVITYCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"ZH_VALUE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );         
         
var projectChannelRankShowTableEn = new Array( 
		 {displayName:"ACTIVITYCHANNELID",isAddDecimal:true,isAddPercent:false},
         {displayName:"EN_VALUE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"ALLREGISTERUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"REGISTERUSERACTIVE",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWPAYUSER",isAddDecimal:true,isAddPercent:false},
         {displayName:"NEWINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"YEARALLINCOME",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY01RETENTIONRATE",isAddDecimal:true,isAddPercent:false},
         {displayName:"DAY07RETENTIONRATE",isAddDecimal:true,isAddPercent:false}
         );          
         
         
         
var GlobalAreaSurveyTable={
        "0":"AREA",
        "1":"COUNTRY",
        "2":"PROJECTNAME",
        "3":"INCOME",
        "4":"TOTALREGISTERUSER",
        "5":"TOTALVISITOR",
        "6":"DAYACTIVEUSERS",
        "7":"DOUFLOW"
		
};


































































//start by gwx383384

var videoFlowAnalysisTrend={
        "0":"TOTALFLOW",
        "1":"VODFLOW",
        "2":"LIVETVFLOW",
        "3":"AVGVODFLOW",
        "4":"AVGLIVETVFLOW",
		"5":"DOUFLOW"
};


var videoFlowAnalysisDisplayTrend = new Array(
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_0"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_1"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_2"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_3"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_4"),isAddDecimal:false,isAddPercent:false},
        {displayName:$.i18n.prop("video.flow.analysis.tabletitle_5"),isAddDecimal:false,isAddPercent:false}
        );


var videoFlowAnalysisTabled={
        "0":"DTIME",
        "1":"TOTALFLOW",
        "2":"VODFLOW",
        "3":"LIVETVFLOW",
        "4":"AVGVODFLOW",
        "5":"AVGLIVETVFLOW",
        "6":"DOUFLOW"
};

var videoFlowAnalysisTablem={
        "0":"MTIME",
        "1":"TOTALFLOW",
        "2":"VODFLOW",
        "3":"LIVETVFLOW",
        "4":"AVGVODFLOW",
        "5":"AVGLIVETVFLOW",
        "6":"DOUFLOW"
};

var videoFlowAnalysisTabley={
        "0":"YTIME",
        "1":"TOTALFLOW",
        "2":"VODFLOW",
        "3":"LIVETVFLOW",
        "4":"AVGVODFLOW",
        "5":"AVGLIVETVFLOW",
        "6":"DOUFLOW"
};


var videoFlowAnalysisShowTabled = new Array(
         {displayName:"DTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"VODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"LIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGLIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"DOUFLOW",isAddDecimal:true,isAddPercent:false}
         );
		 
var videoFlowAnalysisShowTablem = new Array(
         {displayName:"MTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"VODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"LIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGLIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"DOUFLOW",isAddDecimal:true,isAddPercent:false}
         );	
		 
var videoFlowAnalysisShowTabley = new Array(
         {displayName:"YTIME",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"VODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"LIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGVODFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"AVGLIVETVFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"DOUFLOW",isAddDecimal:true,isAddPercent:false}
         );
		 
		 
var videoFlowAnalysisTimeAnalysisTrend={
        "0":"TOTALFLOWHOURS",
        "1":"HOURSVODWATCHFLOW",
        "2":"HOURSLIVETVWATCHFLOW",
        "3":"HOURSVODAVGFLOW",
        "4":"HOURSLIVETVAVGFLOW"
};


var flowAnalysisTimeAnalysisTabled={
        "0":"HTIME",
        "1":"TOTALFLOWHOURS",
        "2":"HOURSVODWATCHFLOW",
        "3":"HOURSLIVETVWATCHFLOW",
        "4":"HOURSVODAVGFLOW",
        "5":"HOURSLIVETVAVGFLOW"
};


var flowAnalysisTimeAnalysisShowTabled = new Array(
        {displayName:"HTIME",isAddDecimal:true,isAddPercent:false},
        {displayName:"TOTALFLOWHOURS",isAddDecimal:true,isAddPercent:false},
        {displayName:"HOURSVODWATCHFLOW",isAddDecimal:true,isAddPercent:false},
        {displayName:"HOURSLIVETVWATCHFLOW",isAddDecimal:true,isAddPercent:false},
        {displayName:"HOURSVODAVGFLOW",isAddDecimal:true,isAddPercent:false},
        {displayName:"HOURSLIVETVAVGFLOW",isAddDecimal:true,isAddPercent:false}
);



var videoFlowAnalysisDistributionAnslysisTabled={
        "0":"NETWORKTYPE",
        "1":"TOTALCONSUMEFLOW",
        "2":"FLOWPROP"
};


var videoFlowAnalysisDistributionAnslysisShowTabled = new Array(
         {displayName:"NETWORKTYPE",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALCONSUMEFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"FLOWPROP",isAddDecimal:true,isAddPercent:false}
);


var videoFlowAnalysisDistributionAnslysisPlatformidTabled={
        "0":"PLATFORMID",
        "1":"TOTALCONSUMEFLOW",
        "2":"FLOWPROP"
};


var videoFlowAnalysisDistributionAnslysisPlatformidShowTabled = new Array(
         {displayName:"PLATFORMID",isAddDecimal:true,isAddPercent:false},
         {displayName:"TOTALCONSUMEFLOW",isAddDecimal:true,isAddPercent:false},
         {displayName:"FLOWPROP",isAddDecimal:true,isAddPercent:false}
);


 

var tvodOperatorSettlementStatisticsTable={
        "0":"PRODUCTID",
        "1":"PRODUCTDESC",
        "2":"CLICKPAYTIMES",
        "3":"INCOMENEW",
        "4":"HWINCOMENEW"
};

var svodOperatorSettlementStatisticsTable={
        "0":"PRODUCTID",
        "1":"CYCLETYPE",
        "2":"PRODUCTDESC",
        "3":"SUBPAYTIMES",
        "4":"INCOMENEW",
		"5":"HWINCOMENEW"
};


var CPAChannelSettlementStatisticsTable={
        "0":"ACTIVITYCHANNELID",
        "1":"ZH_VALUE",
        "2":"CLICKVOLUME",
        "3":"KEYORDERPROMOTION"
};


var CPCChannelSettlementStatisticsTable={
        "0":"ACTIVITYCHANNELID",
        "1":"ZH_VALUE",
        "2":"CLICKVOLUME",
        "3":"KEYORDERPROMOTION"
};





var CPSvodSettlementStatisticsTable={
        "0":"CONTENTID",
        "1":"CONTENTNAME",
        "2":"COMPANYNAME",
        "3":"PRODUCTID",
		"4":"CYCLETYPE",
        "5":"ORIGINALPRICE",
        "6":"PAYUSERPLAYTIME",
        "7":"TOTALPLAYTIME",
		"8":"PLAYTIMEPROPORTION",
        "9":"SUBPAYUSER",
        "10":"INCOMENEW",
        "11":"HWINCOME"
};


var CPTvodSettlementStatisticsTable={
        "0":"CONTENTID",
        "1":"CONTENTNAME",
        "2":"COMPANYNAME",
        "3":"ORIGINALPRICE",
		"4":"CLICKPAYNUMBER",
        "5":"INCOMENEW",
        "6":"HWINCOME"
};




















 
//end by gwx383384



/*
 * 文 件 名:  ExcelUtils.java
 * 版    权:  Huawei Technologies Co., Ltd. Copyright 2009-2011,  All rights reserved
 * 描    述:  <描述>
 * 创 建 人:  w00190105
 * 创建时间:  2014-12-11
 * 修改人  :  s00359263
 */
package com.huawei.manager.mkt.util;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.slf4j.Logger;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Name;
import org.apache.poi.ss.util.CellRangeAddressList;

import com.huawei.manager.mkt.info.AdDicMapInfo;
import com.huawei.manager.mkt.info.AdExportInfo;
import com.huawei.manager.mkt.info.ExportInfo;
import com.huawei.manager.mkt.info.FreqExportInfo;
import com.huawei.manager.mkt.info.ReportExportInfo;
import com.huawei.util.LogUtil;

/**
 * <一句话功能简述>
 * <功能详细描述>
 * 
 * @author  w00190105
 * @version [Internet Business Service Platform SP V100R100, 2014-12-11]
 * @see  [相关类/方法]
 */
public class ExcelUtils
{
    /**
     * MAX_ROW_LENGTH
     */
    private static final int MAX_ROW_LENGTH = 500;
    
    /**
     * 导出长度
     */
    private static final int EXPORT_LENGTH = 34;
    
    /**
     * 单元格长度
     */
    private static final int CELL_LENGTH = 256;
    
    /**
     * 导出列表
     */
    private static final List<String> EXPORT_LIST = new ArrayList<String>();
    
    /**
     * 流量报表导出长度
     */
    private static final int REPORT_EXPORT_LENGTH = 17;
    
    /**
     * 流量报表报出列表
     */
    private static final List<String> REPORT_EXPORT_LIST = new ArrayList<String>();
    
    /**
     * 广告位模板
     */
    private static final List<String> AD_TEMPLATE_LIST = new ArrayList<String>();
    
    /**
     * 流量报表报出列表
     */
    private static final List<String> FR_EXPORT_LIST = new ArrayList<String>();
    
    /**
     * vmall着陆链接模板
     */
    private static final List<String> VMALL_TEMPLATE_LIST = new ArrayList<String>();
    
    /**
     * 荣耀官网着陆链接模板
     */
    private static final List<String> HONOR_TEMPLATE_LIST = new ArrayList<String>();
    
    /**
     * 隐藏列号
     */
    private static final List<Integer> HIDEEN_INDEX_LIST = new ArrayList<Integer>();
    
    /**
     * 必选项号
     */
    private static final List<Integer> HEAD_NEED_INDEX_LIST = new ArrayList<Integer>();
    
    /**
     * 广告位必选项号
     */
    private static final List<Integer> AD_INFO_NEED_INDEX_LIST = new ArrayList<Integer>();
    
    /**
     * vmall必选项号
     */
    private static final List<Integer> VMALL_INFO_NEED_INDEX_LIST = new ArrayList<Integer>();
    
    /**
     * hornor必选项号
     */
    private static final List<Integer> HORNOR_INFO_NEED_INDEX_LIST = new ArrayList<Integer>();
    
    /**
     * 模板隐藏行号
     */
    private static final List<Integer> TEMPLATE_HIDEN_INDEX_LIST = new ArrayList<Integer>();
    
    private static final Logger LOG = LogUtil.getInstance();
    
    public static List<String> getAdTemplateList()
    {
        return AD_TEMPLATE_LIST;
    }
    
    public static List<String> getVmallTemplateList()
    {
        return VMALL_TEMPLATE_LIST;
    }
    
    public static List<String> getHonorTemplateList()
    {
        return HONOR_TEMPLATE_LIST;
    }
    
    /**
     * 初始化导出excel文件列表头
     */
    static
    {
        FR_EXPORT_LIST.add("媒体");
        FR_EXPORT_LIST.add("1+");
        FR_EXPORT_LIST.add("2+");
        FR_EXPORT_LIST.add("3+");
        FR_EXPORT_LIST.add("4+");
        FR_EXPORT_LIST.add("5+");
        FR_EXPORT_LIST.add("6+");
        FR_EXPORT_LIST.add("7+");
        FR_EXPORT_LIST.add("8+");
        FR_EXPORT_LIST.add("9+");
        FR_EXPORT_LIST.add("10+");
        
        EXPORT_LIST.add("广告位ID");
        EXPORT_LIST.add("营销活动名称");
        EXPORT_LIST.add("网站名称");
        EXPORT_LIST.add("频道");
        EXPORT_LIST.add("广告位");
        EXPORT_LIST.add("广告素材类型");
        EXPORT_LIST.add("素材要求");
        EXPORT_LIST.add("端口所属");
        EXPORT_LIST.add("着陆平台");
        EXPORT_LIST.add("着陆页面描述");
        EXPORT_LIST.add("投放天数");
        EXPORT_LIST.add("投放日期");
        EXPORT_LIST.add("引流类型");
        EXPORT_LIST.add("预计曝光量");
        EXPORT_LIST.add("预计点击量");
        EXPORT_LIST.add("刊例价");
        EXPORT_LIST.add("净价");
        EXPORT_LIST.add("资源来源");
        EXPORT_LIST.add("是否监控曝光");
        EXPORT_LIST.add("是否监控点击");
        EXPORT_LIST.add("监控平台");
        EXPORT_LIST.add("SID");
        EXPORT_LIST.add("CPS提供商名称");
        EXPORT_LIST.add("Source");
        EXPORT_LIST.add("CID");
        EXPORT_LIST.add("渠道名称");
        EXPORT_LIST.add("Channel");
        
        EXPORT_LIST.add("着陆链接");
        EXPORT_LIST.add("BI代码");
        EXPORT_LIST.add("曝光监控代码");
        EXPORT_LIST.add("点击监控代码");
        EXPORT_LIST.add("当前状态");
        EXPORT_LIST.add("投放人");
        EXPORT_LIST.add("更新日期");
        EXPORT_LIST.add("素材状态");
        
        AD_TEMPLATE_LIST.add("广告位ID");
        AD_TEMPLATE_LIST.add("营销活动名称");
        AD_TEMPLATE_LIST.add("网站名称（下拉）");
        AD_TEMPLATE_LIST.add("频道");
        AD_TEMPLATE_LIST.add("广告位");
        AD_TEMPLATE_LIST.add("广告素材类型（下拉）");
        AD_TEMPLATE_LIST.add("素材要求");
        AD_TEMPLATE_LIST.add("端口所属（下拉）");
        AD_TEMPLATE_LIST.add("着陆平台（下拉）");
        AD_TEMPLATE_LIST.add("着陆页面描述");
        AD_TEMPLATE_LIST.add("投放天数");
        AD_TEMPLATE_LIST.add("投放日期");
        AD_TEMPLATE_LIST.add("引流类型（下拉）");
        AD_TEMPLATE_LIST.add("预计曝光量");
        AD_TEMPLATE_LIST.add("预计点击量");
        AD_TEMPLATE_LIST.add("刊例价");
        AD_TEMPLATE_LIST.add("净价");
        AD_TEMPLATE_LIST.add("资源来源（下拉）");
        AD_TEMPLATE_LIST.add("是否监控曝光（下拉）");
        AD_TEMPLATE_LIST.add("是否监控点击（下拉）");
        AD_TEMPLATE_LIST.add("监控平台（下拉）");
        AD_TEMPLATE_LIST.add("SID");
        AD_TEMPLATE_LIST.add("CPS提供商名称");
        AD_TEMPLATE_LIST.add("Source");
        AD_TEMPLATE_LIST.add("CID");//Modify by sxy 20151109 for mkt7.0.1
        AD_TEMPLATE_LIST.add("渠道名称");
        AD_TEMPLATE_LIST.add("Channel");
        AD_TEMPLATE_LIST.add("着陆链接");
        
        REPORT_EXPORT_LIST.add("活动名称");
        REPORT_EXPORT_LIST.add("网站名称");
        REPORT_EXPORT_LIST.add("频道");
        REPORT_EXPORT_LIST.add("广告位");
        REPORT_EXPORT_LIST.add("端口所属");
        REPORT_EXPORT_LIST.add("着陆平台");
        REPORT_EXPORT_LIST.add("投放日期");
        REPORT_EXPORT_LIST.add("统计周期");
        REPORT_EXPORT_LIST.add("SID");
        REPORT_EXPORT_LIST.add("CID");
        REPORT_EXPORT_LIST.add("投放域名");
        REPORT_EXPORT_LIST.add("曝光PV");
        REPORT_EXPORT_LIST.add("曝光UV");
        REPORT_EXPORT_LIST.add("点击PV");
        REPORT_EXPORT_LIST.add("点击UV");
        REPORT_EXPORT_LIST.add("广告位来源");
        REPORT_EXPORT_LIST.add("投放人");
        
        //vmall
        VMALL_TEMPLATE_LIST.add("广告位ID");
        VMALL_TEMPLATE_LIST.add("SID");
        VMALL_TEMPLATE_LIST.add("CPS提供商名称");
        VMALL_TEMPLATE_LIST.add("Source");
        VMALL_TEMPLATE_LIST.add("渠道名称");
        VMALL_TEMPLATE_LIST.add("Channel");
        VMALL_TEMPLATE_LIST.add("CID");
        VMALL_TEMPLATE_LIST.add("着陆链接");
        
        //honor
        HONOR_TEMPLATE_LIST.add("广告位ID");
        HONOR_TEMPLATE_LIST.add("着陆链接");
        
        //hidden_list
        HIDEEN_INDEX_LIST.add(0);
        HIDEEN_INDEX_LIST.add(5);
        HIDEEN_INDEX_LIST.add(6);
        HIDEEN_INDEX_LIST.add(9);
        HIDEEN_INDEX_LIST.add(10);
        HIDEEN_INDEX_LIST.add(12);
        HIDEEN_INDEX_LIST.add(13);
        HIDEEN_INDEX_LIST.add(14);
        HIDEEN_INDEX_LIST.add(15);
        HIDEEN_INDEX_LIST.add(16);
        HIDEEN_INDEX_LIST.add(18);
        HIDEEN_INDEX_LIST.add(19);
        HIDEEN_INDEX_LIST.add(20);
        HIDEEN_INDEX_LIST.add(21);
        HIDEEN_INDEX_LIST.add(22);
        HIDEEN_INDEX_LIST.add(23);
        HIDEEN_INDEX_LIST.add(24);
        HIDEEN_INDEX_LIST.add(25);
        HIDEEN_INDEX_LIST.add(26);
        HIDEEN_INDEX_LIST.add(27);
        HIDEEN_INDEX_LIST.add(28);
        HIDEEN_INDEX_LIST.add(29);
        
        HEAD_NEED_INDEX_LIST.add(1);
        HEAD_NEED_INDEX_LIST.add(2);
        HEAD_NEED_INDEX_LIST.add(3);
//        HEAD_NEED_INDEX_LIST.add(21);
//        HEAD_NEED_INDEX_LIST.add(22);
//        HEAD_NEED_INDEX_LIST.add(23);
        HEAD_NEED_INDEX_LIST.add(24);
//        HEAD_NEED_INDEX_LIST.add(25);
//        HEAD_NEED_INDEX_LIST.add(26);
        HEAD_NEED_INDEX_LIST.add(27);
        
        AD_INFO_NEED_INDEX_LIST.add(1);
        AD_INFO_NEED_INDEX_LIST.add(2);
        AD_INFO_NEED_INDEX_LIST.add(3);
        
//        VMALL_INFO_NEED_INDEX_LIST.add(21);
//        VMALL_INFO_NEED_INDEX_LIST.add(22);
//        VMALL_INFO_NEED_INDEX_LIST.add(23);
        VMALL_INFO_NEED_INDEX_LIST.add(24);
//        VMALL_INFO_NEED_INDEX_LIST.add(25);
//        VMALL_INFO_NEED_INDEX_LIST.add(26);
        
        HORNOR_INFO_NEED_INDEX_LIST.add(27);
        
        TEMPLATE_HIDEN_INDEX_LIST.add(0);
        TEMPLATE_HIDEN_INDEX_LIST.add(13);
        TEMPLATE_HIDEN_INDEX_LIST.add(14);
        TEMPLATE_HIDEN_INDEX_LIST.add(15);
        TEMPLATE_HIDEN_INDEX_LIST.add(16);
        TEMPLATE_HIDEN_INDEX_LIST.add(18);
        TEMPLATE_HIDEN_INDEX_LIST.add(19);
        TEMPLATE_HIDEN_INDEX_LIST.add(20);
    }
    
    /**
     * <将导出广告位信息列表写入Excel文件>
     * @param filePath   文件目录
     * @param list       广告位信息列表
     * @param dicMap     字典结合信息
     * @param exportType 导出方式
     * @see [类、类#方法、类#成员]
     */
    public static void writeAdExportFile(String filePath, List<AdExportInfo> list, AdDicMapInfo dicMap, int exportType)
    {
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("广告位模板");
        HSSFSheet hiddenSheet = workbook.createSheet("hidden");
        
        CellStyle style = getHeadCellStyle(workbook);
        
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < EXPORT_LENGTH; i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(EXPORT_LIST.get(i));
            headCell.setCellStyle(style);
            if (exportType == 0 && HIDEEN_INDEX_LIST.contains(i))
            {
                sheet.setColumnWidth(i, 0);
            }
            else
            {
                sheet.setColumnWidth(i, 20 * CELL_LENGTH);
            }
        }
        
        //生成下拉框
        dropDownList(dicMap, workbook, sheet, hiddenSheet);
        
        if (null != list && !list.isEmpty())
        {
            //插入表格内容
            for (int i = 0; i < list.size(); i++)
            {
                AdExportInfo info = list.get(i);
                //写行信息
                writeAdExportRowInfo(info, sheet, i + 1);
            }
        }
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
        
    }
    
    /**
     * <写广告位导出行信息>
     * @param info       广告位信息
     * @param sheet      sheet页
     * @param rowIndex   文件行号
     * @see [类、类#方法、类#成员]
     */
    private static void writeAdExportRowInfo(AdExportInfo info, HSSFSheet sheet, int rowIndex)
    {
        //创建行
        HSSFRow cotentRow = sheet.getRow(rowIndex);
        if (null == cotentRow)
        {
            cotentRow = sheet.createRow(rowIndex);
        }
        
        //写入值
        for (int i = 0; i < EXPORT_LENGTH; i++)
        {
            HSSFCell headCell = cotentRow.createCell(i);
            headCell.setCellValue(getAdCellStringValue(info, i));
        }
    }
    
    /**
     * <获取对应单元格信息>
     * @param info    广告位导出信息
     * @param index   单元格对应的位置
     * @return        单元格对应的字符
     * @see [类、类#方法、类#成员]
     */
    private static String getAdCellStringValue(AdExportInfo info, int index)
    {
        String value = null;
        
        switch (index)
        {
            case 0:
                value = info.getAdInfoId();
                break;
            case 1:
                value = info.getMktName();
                break;
            case 2:
                value = info.getAdInfoWebName();
                break;
            case 3:
                value = info.getAdInfoChannel();
                break;
            case 4:
                value = info.getAdInfoPosition();
                break;
            case 5:
                value = info.getMaterialType();
                break;
            case 6:
                value = info.getMaterialDesc();
                break;
            case 7:
                value = info.getAdInfoPort();
                break;
            case 8:
                value = info.getAdInfoPlatform();
                break;
            case 9:
                value = info.getAdInfoPlatformDesc();
                break;
            case 10:
                value = info.getAdInfoDeliveryDays();
                break;
            case 11:
                value = info.getAdInfoDeliveryTimes();
                break;
            case 12:
                value = info.getAdInfoFlowType();
                break;
            case 13:
                value = info.getExpAmount();
                break;
            case 14:
                value = info.getClickAmount();
                break;
            case 15:
                value = info.getPublishPrice();
                break;
            case 16:
                value = info.getNetPrice();
                break;
            case 17:
                value = info.getAdInfoResource();
                break;
            case 18:
                value = info.getIsExposure();
                break;
            case 19:
                value = info.getIsClick();
                break;
            case 20:
                value = info.getMonitorPlatform();
                break;
            case 21:
                value = info.getSid();
                break;
            case 22:
                value = info.getCpsName();
                break;
            case 23:
                value = info.getSource();
                break;
            case 24:
                value = info.getCid();
                break;
            case 25:
                value = info.getChannelName();
                break;
            case 26:
                value = info.getChannel();
                break;
            case 27:
                value = info.getLandUrl();
                break;
                
            case 28:
                value = info.getBiCode();
                break;
                
            case 29:
                value = info.getMonitorExposureUrl();
                break;
            case 30:
                value = info.getMonitorClickUrl();
                break;
            case 31:
                value = info.getAdInfoState();
                break;
            case 32:
                value = info.getOperator();
                break;
            case 33:
                value = info.getUpdateTime();
                break;
            case 34:
                value = info.getMaterialState();
                break;
            default:
                break;
        }
        return value;
    }
    
    /**
     * <写广告位模板>
     * @param filePath           文件目录
     * @param dicMap             字典信息
     * @see [类、类#方法、类#成员]
     */
    public static void writeAdInfoTemplateFile(String filePath, AdDicMapInfo dicMap)
    {
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("广告位模板");
        HSSFSheet hiddenSheet = workbook.createSheet("hidden");
        
        //设置单元格宽度
        setSheetColumnWidth(sheet, false);
        
        setHeadRowStyle(workbook, sheet, AD_TEMPLATE_LIST.size());
        
        setContentRowStyle(workbook, sheet, AD_TEMPLATE_LIST.size());
        
        dropDownList(dicMap, workbook, sheet, hiddenSheet);
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
    }
    
    /**
     * <设置单元格宽度>
     * @param sheet      excel文件sheet页对象
     * @param result     是否是模板导出信息
     * @see [类、类#方法、类#成员]
     */
    private static void setSheetColumnWidth(HSSFSheet sheet, boolean result)
    {
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        int size = AD_TEMPLATE_LIST.size();
        for (int i = 0; i < size; i++)
        {
            
            if (TEMPLATE_HIDEN_INDEX_LIST.contains(i))
            {
                sheet.setColumnWidth(i, 0);
            }
            else if (i == (size - 1))
            {
                //着陆链接单元格变长
                sheet.setColumnWidth(i, 60 * CELL_LENGTH);
            }
            else
            {
                sheet.setColumnWidth(i, 20 * CELL_LENGTH);
            }
            
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(AD_TEMPLATE_LIST.get(i));
        }
        
        if (!result)
        {
            return;
        }
        
        HSSFCell headCell = headRow.createCell(AD_TEMPLATE_LIST.size());
        sheet.setColumnWidth(AD_TEMPLATE_LIST.size(), 20 * CELL_LENGTH);
        headCell.setCellValue("操作结果");
    }
    
    /**
     * <设置内容行单元格格式>
     * @param workbook    工作薄
     * @param sheet       sheet页对象
     * @param size        最大长度
     * @see [类、类#方法、类#成员]
     */
    private static void setContentRowStyle(HSSFWorkbook workbook, HSSFSheet sheet, int size)
    {
        //黄色
        CellStyle yellowStyle = getCellStyle(workbook, IndexedColors.LIGHT_YELLOW.getIndex());
        
        //绿色
        CellStyle greenStyle = getCellStyle(workbook, IndexedColors.LIGHT_GREEN.getIndex());
        
        //蓝色
        CellStyle blueStyle = getCellStyle(workbook, IndexedColors.LIGHT_TURQUOISE.getIndex());
        
        //白色
        CellStyle whiteStyle = getCellStyle(workbook, IndexedColors.WHITE.getIndex());
        
        for (int i = 1; i < MAX_ROW_LENGTH; i++)
        {
            HSSFRow row = sheet.getRow(i);
            if (null == row)
            {
                row = sheet.createRow(i);
            }
            
            for (int j = 0; j < size; j++)
            {
                HSSFCell cell = row.getCell(j);
                if (null == cell)
                {
                    cell = row.createCell(j);
                }
                
                if (AD_INFO_NEED_INDEX_LIST.contains(j))
                {
                    cell.setCellStyle(yellowStyle);
                }
                else if (VMALL_INFO_NEED_INDEX_LIST.contains(j))
                {
                    cell.setCellStyle(greenStyle);
                    
                }
                else if (HORNOR_INFO_NEED_INDEX_LIST.contains(j))
                {
                    cell.setCellStyle(blueStyle);
                }
                else
                {
                    cell.setCellStyle(whiteStyle);
                }
            }
        }
        
    }
    
    /**
     * <设置首行单元格格式>
     * @param workbook    工作薄
     * @param sheet       sheet页对象
     * @param size        首行最大长度
     * @see [类、类#方法、类#成员]
     */
    private static void setHeadRowStyle(HSSFWorkbook workbook, HSSFSheet sheet, int size)
    {
        HSSFRow headRow = sheet.getRow(0);
        if (null == headRow)
        {
            headRow = sheet.createRow(0);
        }
        
        //红色
        CellStyle redStyle = getCellStyle(workbook, IndexedColors.RED.getIndex());
        
        //默认颜色
        CellStyle defaultStyle = getCellStyle(workbook, IndexedColors.AQUA.getIndex());
        
        for (int i = 0; i < size; i++)
        {
            
            HSSFCell headCell = headRow.getCell(i);
            if (null == headCell)
            {
                headCell = headRow.createCell(i);
            }
            
            if (HEAD_NEED_INDEX_LIST.contains(i))
            {
                headCell.setCellStyle(redStyle);
            }
            else
            {
                headCell.setCellStyle(defaultStyle);
            }
        }
        
    }
    
    /**
     * <获取单元格样式>
     * @param workbook    工作薄
     * @param color       背景色
     * @return            单元格样式
     * @see [类、类#方法、类#成员]
     */
    private static CellStyle getCellStyle(HSSFWorkbook workbook, short color)
    {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(color);
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        style.setBorderBottom(CellStyle.BORDER_THIN); //下边框
        style.setBorderLeft(CellStyle.BORDER_THIN);//左边框
        style.setBorderTop(CellStyle.BORDER_THIN);//上边框
        style.setBorderRight(CellStyle.BORDER_THIN);//右边框
        //style.setWrapText(true);
        return style;
    }
    
    /**
     * <写Excel文件导出第一行>
     * @param workbook    excel文件工作薄
     * @param dicMap      字典信息
     * @return            excel文件对象
     * @see [类、类#方法、类#成员]
     */
    public static HSSFSheet wirteFileHeadRow(HSSFWorkbook workbook, AdDicMapInfo dicMap)
    {
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("广告位模板");
        HSSFSheet hiddenSheet = workbook.createSheet("hidden");
        
        setSheetColumnWidth(sheet, true);
        
        setHeadRowStyle(workbook, sheet, AD_TEMPLATE_LIST.size() + 1);
        
        setContentRowStyle(workbook, sheet, AD_TEMPLATE_LIST.size() + 1);
        
        //添加操作结果信息
        dropDownList(dicMap, workbook, sheet, hiddenSheet);
        
        return sheet;
    }
    
    /**
     * <添加下拉列表>
     * @param dicMap        字典信息
     * @param workbook      工作薄
     * @param sheet         显示sheet页对象
     * @param hiddenSheet   隐藏sheet页对象
     * @see [类、类#方法、类#成员]
     */
    private static void dropDownList(AdDicMapInfo dicMap, HSSFWorkbook workbook, HSSFSheet sheet, HSSFSheet hiddenSheet)
    {
        //网站名称
        String[] webNameArray = getArray(dicMap.getWebMap());
        setDropDownList(workbook, sheet, hiddenSheet, webNameArray, "webNameHideen", "A", 0, 2);
        
        //素材类型
        String[] materiaArray = getArray(dicMap.getMateriaMap());
        setDropDownList(workbook, sheet, hiddenSheet, materiaArray, "materiaHideen", "B", 1, 5);
        
        //端口所属（下拉）
        String[] portArray = getArray(dicMap.getPortMap());
        setDropDownList(workbook, sheet, hiddenSheet, portArray, "portHideen", "C", 2, 7);
        
        //着陆平台（下拉）
        String[] platformArray = getArray(dicMap.getPlatformMap());
        setDropDownList(workbook, sheet, hiddenSheet, platformArray, "platformHideen", "D", 3, 8);
        
        //引流类型（下拉）
        String[] flowArray = getArray(dicMap.getFlowMap());
        setDropDownList(workbook, sheet, hiddenSheet, flowArray, "flowHideen", "E", 4, 12);
        
        //资源来源（下拉）  
        String[] resourceArray = getArray(dicMap.getResourceMap());
        setDropDownList(workbook, sheet, hiddenSheet, resourceArray, "resourceHideen", "F", 5, 17);
        
        //是否监控曝光（下拉）
        String[] exposureArray = getArray(dicMap.getBooleanMap());
        setDropDownList(workbook, sheet, hiddenSheet, exposureArray, "exposureHideen", "G", 6, 18);
        
        // 是否监控点击（下拉）  
        String[] clickArray = getArray(dicMap.getBooleanMap());
        setDropDownList(workbook, sheet, hiddenSheet, clickArray, "clickHideen", "H", 7, 19);
        
        //监控平台（下拉）
        String[] monitorArray = getArray(dicMap.getMonitorPlatformMap());
        setDropDownList(workbook, sheet, hiddenSheet, monitorArray, "monitorHideen", "I", 8, 20);
        
        workbook.setSheetHidden(1, true);
    }
    
    /**
     * <设置下拉菜单>
     * @param workbook              工作薄
     * @param sheet                 当前sheet页对象
     * @param hiddenSheet           隐藏sheet页对象
     * @param array                 下拉菜单内容数组
     * @param listFormula           下拉列表公式
     * @param hidenColName          隐藏列名称
     * @param hideColIndex          隐藏下拉列索引 在隐藏页的那一列
     * @param dropColIndex          下拉列索引 在当前页那一列
     * @see [类、类#方法、类#成员]
     */
    private static void setDropDownList(HSSFWorkbook workbook, HSSFSheet sheet, HSSFSheet hiddenSheet, String[] array,
        String listFormula, String hidenColName, int hideColIndex, int dropColIndex)
    {
        //网站名称隐藏
        for (int i = 0, length = array.length; i < length; i++)
        {
            String name = array[i];
            HSSFRow row = hiddenSheet.getRow(i);//第i行
            if (null == row)
            {
                row = hiddenSheet.createRow(i);
            }
            HSSFCell cell = row.createCell(hideColIndex);
            cell.setCellValue(name);
        }
        
        Name nameCell = workbook.createName();
        nameCell.setNameName(listFormula);
        nameCell.setRefersToFormula("hidden!$" + hidenColName + "$1:$" + hidenColName + "$" + array.length);
        DVConstraint dvConstraint = DVConstraint.createFormulaListConstraint(listFormula);
        
        for (int i = 1; i < MAX_ROW_LENGTH; i++)
        {
            //网站名称下拉
            CellRangeAddressList cellRangeAddressList = new CellRangeAddressList(i, i, dropColIndex, dropColIndex);
            HSSFDataValidation validation = new HSSFDataValidation(cellRangeAddressList, dvConstraint);
            sheet.addValidationData(validation);
            validation.setShowErrorBox(false);
        }
    }
    
    /**
     * <将集合中的key值转换为数组>
     * @param map        集合
     * @return           key值根据中文排序后的数组
     * @see [类、类#方法、类#成员]
     */
    private static String[] getArray(Map<String, ?> map)
    {
        List<String> list = new ArrayList<String>();
        
        if (null == map || map.isEmpty())
        {
            return new String[0];
        }
        
        Iterator<String> iterator = map.keySet().iterator();
        
        while (iterator.hasNext())
        {
            String key = (String)iterator.next();
            list.add(key);
        }
        
        //根据中文进行排序
        Collections.sort(list, Collator.getInstance(Locale.CHINA));
        
        return list.toArray(new String[list.size()]);
    }
    
    /**
     * <获取EXCEL第一行格式>
     * @param workbook    EXCEL文件工作薄
     * @return            格式
     * @see [类、类#方法、类#成员]
     */
    private static CellStyle getHeadCellStyle(HSSFWorkbook workbook)
    {
        CellStyle style = workbook.createCellStyle();
        style.setFillForegroundColor(IndexedColors.AQUA.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        return style;
    }
    
    /**
     * <写流量报表导出文件>
     * @param filePath       文件路径
     * @param list           报表信息列表
     * @see [类、类#方法、类#成员]
     */
    public static void writeReportExportFile(String filePath, List<ReportExportInfo> list)
    {
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("流量报表导出");
        
        //        CellStyle style = getHeadCellStyle(workbook);
        //白色
        CellStyle whiteStyle = getCellStyle(workbook, IndexedColors.WHITE.getIndex());
        //默认颜色
        CellStyle style = getCellStyle(workbook, IndexedColors.AQUA.getIndex());
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < REPORT_EXPORT_LENGTH; i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(REPORT_EXPORT_LIST.get(i));
            headCell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * CELL_LENGTH);
        }
        
        if (null != list && !list.isEmpty())
        {
            //插入表格内容
            for (int i = 0; i < list.size(); i++)
            {
                ReportExportInfo info = list.get(i);
                writeReportRowInfo(sheet, i + 1, info, whiteStyle);
            }
            
        }
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
        
    }
    
    /**
     * <写流量报表行信息>
     * @param sheet          Excel文件sheet页对象
     * @param rowIndex       行号
     * @param info           报表流量信息
     * @param whiteStyle 
     * @see [类、类#方法、类#成员]
     */
    private static void writeReportRowInfo(HSSFSheet sheet, int rowIndex, ReportExportInfo info, CellStyle whiteStyle)
    {
        HSSFRow cotentRow = sheet.createRow(rowIndex);
        
        for (int i = 0; i < REPORT_EXPORT_LENGTH; i++)
        {
            HSSFCell cell = cotentRow.createCell(i);
            cell.setCellStyle(whiteStyle);
            cell.setCellValue(getReportCellStringValue(info, i));
        }
    }
    
    /**
     * <获取流量报表单元格信息>
     * @param info    流量报表导出信息
     * @param index   序号
     * @return        导出单元格信息
     * @see [类、类#方法、类#成员]
     */
    private static String getReportCellStringValue(ReportExportInfo info, int index)
    {
        //参数验证
        if (null == info)
        {
            return null;
        }
        
        String value = null;
        
        switch (index)
        {
            
            case 0:
                value = info.getMktLandInfoActName();
                break;
            case 1:
                value = info.getMktLandInfoWebName();
                break;
            case 2:
                value = info.getMktLandInfoChannel();
                break;
            case 3:
                value = info.getMktLandInfoAdPosition();
                break;
            case 4:
                value = info.getMktLandInfoPort();
                break;
            case 5:
                value = info.getPlatform();
                break;
            case 6:
                value = info.getMktLandInfoUseDate();
                break;
            case 7:
                value = info.getReportDate();
                break;
            case 8:
                value = info.getMktLandInfoSID();
                break;
            case 9:
                value = info.getMktLandInfoCID();
                break;
            case 10:
                value = info.getDnsname();
                break;
            case 11:
                value = info.getBgPv();
                break;
            case 12:
                value = info.getBgUv();
                break;
            case 13:
                value = info.getDjPv();
                break;
            case 14:
                value = info.getDjUv();
                break;
            case 15:
                value = info.getResource();
                break;
            case 16:
                value = info.getOperator();
                break;
                
            default:
                break;
        }
        return value;
    }
    
    /**
     * <写着陆链接模板文件>
     * @param filePath    文件目录
     * @param isVmall     是否是vmall着陆链接
     * @see [类、类#方法、类#成员]
     */
    public static void writeLandInfoTemplateFile(String filePath, boolean isVmall)
    {
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        if (isVmall)
        {
            writeVmallLandTempLateHeadRow(workbook);
        }
        else
        {
            writeHonorLandTempLateHeadRow(workbook);
        }
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
        
    }
    
    /**
     * <写VMall着陆链接首行>
     * @param workbook          Excel工作薄
     * @see [类、类#方法、类#成员]
     */
    private static void writeVmallLandTempLateHeadRow(HSSFWorkbook workbook)
    {
        //创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("着陆链接(VMALL)模板");
        CellStyle style = getHeadCellStyle(workbook);
        
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < VMALL_TEMPLATE_LIST.size(); i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(VMALL_TEMPLATE_LIST.get(i));
            headCell.setCellStyle(style);
        }
    }
    
    /**
     * <写荣耀官网着陆链接首行>
     * @param workbook          Excel工作薄
     * @see [类、类#方法、类#成员]
     */
    private static void writeHonorLandTempLateHeadRow(HSSFWorkbook workbook)
    {
        HSSFSheet sheet = workbook.createSheet("着陆链接(官网)模板");
        CellStyle style = getHeadCellStyle(workbook);
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        for (int i = 0; i < HONOR_TEMPLATE_LIST.size(); i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(HONOR_TEMPLATE_LIST.get(i));
            headCell.setCellStyle(style);
        }
    }
    
    /**
     * <获取单元格的值>
     * @param cell   对应的单元格
     * @return        单元格的数值
     * @see [类、类#方法、类#成员]
     */
    public static String getCellValue(HSSFCell cell)
    {
        
        if (null == cell)
        {
            return null;
        }
        
        String value = null;
        
        int cellType = cell.getCellType();
        switch (cellType)
        {
            case HSSFCell.CELL_TYPE_BOOLEAN:
                value = String.valueOf(cell.getBooleanCellValue()).trim();
                break;
            case HSSFCell.CELL_TYPE_NUMERIC:
                value = StringUtils.formateNumber(cell.getNumericCellValue());
                break;
            default:
                value = String.valueOf(cell.getStringCellValue()).trim();
        }
        
        if (0 == value.trim().length())
        {
            value = null;
        }
        
        return value;
    }
    
    /**
    * <生成导出excel文件>
    * @param filePath          动态分配的文件路径
    * @param texportList       导出信息列表
    * @param tColName          字段中文名称
    * @param tCyName           成员名称
    * @param tfileName         sheet页名称
    * @see [类、类#方法、类#成员]
    */
    public static void writeReportExportFile(String filePath, List<ExportInfo> texportList, List<String> tColName,
        List<String> tCyName, String tfileName)
    {
        
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet(tfileName);
        
        //白色
        CellStyle whiteStyle = getCellStyle(workbook, IndexedColors.WHITE.getIndex());
        //默认颜色
        CellStyle style = getCellStyle(workbook, IndexedColors.AQUA.getIndex());
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        if (null == texportList || tColName.isEmpty())
        {
            return;
        }
        for (int i = 0; i < tColName.size(); i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(tColName.get(i));
            headCell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * CELL_LENGTH);
        }
        
        if (!texportList.isEmpty())
        {
            //插入表格内容
            for (int i = 0; i < texportList.size(); i++)
            {
                ExportInfo info = texportList.get(i);
                writeReportRowInfo(sheet, i + 1, info, tCyName, whiteStyle);
            }
            
        }
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
        
    }
    
    /**
     * <写入excel文件>
     * @param sheet       创建工作表对象
     * @param rowIndex    当前行数
     * @param info        当前导出内容
     * @param tCyName     成员名称
     * @param whiteStyle  白色 
     * @see [类、类#方法、类#成员]
     */
    private static void writeReportRowInfo(HSSFSheet sheet, int rowIndex, ExportInfo info, List<String> tCyName,
        CellStyle whiteStyle)
    {
        
        HSSFRow cotentRow = sheet.createRow(rowIndex);
        
        for (int i = 0; i < tCyName.size(); i++)
        {
            HSSFCell cell = cotentRow.createCell(i);
            cell.setCellStyle(whiteStyle);
            String tEnName = tCyName.get(i);
            cell.setCellValue(getReportCellStringValue(info, tEnName));
        }
        
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param info
     * @param tEnName
     * @return
     * @see [类、类#方法、类#成员]
     */
    private static String getReportCellStringValue(ExportInfo info, String tEnName)
    {
        
        for (Field field : info.getClass().getDeclaredFields())
        {
            
            if (tEnName.equals(field.getName()))
            {
                String upperName = "get" + tEnName.substring(0, 1).toUpperCase(Locale.US) + tEnName.substring(1);
                try
                {
                    Method method = info.getClass().getDeclaredMethod(upperName, null);
                    return (String)method.invoke(info, null);
                }
                catch (SecurityException e)
                {
                    LOG.error("getReportCellStringValue error! exception is {SecurityException}");
                    break;
                }
                catch (NoSuchMethodException e)
                {
                    LOG.error("getReportCellStringValue error! exception is {NoSuchMethodException}");
                    break;
                }
                catch (InvocationTargetException e)
                {
                    LOG.error("getReportCellStringValue error! exception is {InvocationTargetException}");
                    break;
                }
                catch (IllegalArgumentException e)
                {
                    LOG.error("getReportCellStringValue error! exception is {IllegalArgumentException}");
                    break;
                }
                catch (IllegalAccessException e)
                {
                    LOG.error("getReportCellStringValue error! exception is {IllegalAccessException}");
                    break;
                }
                
            }
            
        }
        
        return "";
        
    }
    
    /**
     * <一句话功能简述>
     * @param filePath         路径
     * @param tranRs           转换数据
     * @param reportSelectMax  最大选择
     * @see [类、类#方法、类#成员]
     */
    public static void writeFrExportFile(String filePath, List<FreqExportInfo> tranRs, int reportSelectMax)
    {
        
        // 先创建工作簿对象 2003
        HSSFWorkbook workbook = new HSSFWorkbook();
        
        // 创建工作表对象并命名  
        HSSFSheet sheet = workbook.createSheet("渠道效果报表导出");
        
        //白色
        CellStyle whiteStyle = getCellStyle(workbook, IndexedColors.WHITE.getIndex());
        //默认颜色
        CellStyle style = getCellStyle(workbook, IndexedColors.AQUA.getIndex());
        //插入表头信息
        HSSFRow headRow = sheet.createRow(0);
        
        for (int i = 0; i <= reportSelectMax; i++)
        {
            HSSFCell headCell = headRow.createCell(i);
            headCell.setCellValue(FR_EXPORT_LIST.get(i));
            headCell.setCellStyle(style);
            sheet.setColumnWidth(i, 20 * CELL_LENGTH);
        }
        
        if (null != tranRs && !tranRs.isEmpty())
        {
            //插入表格内容
            for (int i = 0; i < tranRs.size(); i++)
            {
                FreqExportInfo info = tranRs.get(i);
                writeReportRowInfo(sheet, i + 1, info, whiteStyle, reportSelectMax);
            }
            
        }
        
        // 生成文件  
        FileUtils.writeFile(filePath, workbook);
        
    }
    
    /**
     * <一句话功能简述>
     * <功能详细描述>
     * @param sheet
     * @param rowIndex
     * @param info
     * @param whiteStyle
     * @param reportSelectMax
     * @see [类、类#方法、类#成员]
     */
    private static void writeReportRowInfo(HSSFSheet sheet, int rowIndex, FreqExportInfo info, CellStyle whiteStyle,
        int reportSelectMax)
    {
        HSSFRow cotentRow = sheet.createRow(rowIndex);
        
        HSSFCell cell = cotentRow.createCell(0);
        cell.setCellStyle(whiteStyle);
        cell.setCellValue(info.getMediaName());
        
        for (int i = 0; i < reportSelectMax; i++)
        {
            cell = cotentRow.createCell(i + 1);
            cell.setCellStyle(whiteStyle);
            cell.setCellValue(info.getUserList().get(i));
        }
        
    }
    
}

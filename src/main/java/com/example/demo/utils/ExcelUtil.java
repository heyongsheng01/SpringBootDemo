package com.example.demo.utils;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import jxl.Workbook;
import jxl.format.*;
import jxl.format.Alignment;
import jxl.format.Border;
import jxl.format.BorderLineStyle;
import jxl.format.Colour;
import jxl.format.VerticalAlignment;
import jxl.write.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 导入导出Excel工具类
 */
public class ExcelUtil {

    /**
     * 导出Excel（导出到本地）
     *
     * @param cols     列的集合
     * @param dataList 每一行的数据
     * @param title    标题
     * @param out      导出流
     * @throws ExcelException
     */
    public static void listToExcel(String[] cols, List<String[]> dataList, String title, OutputStream out)
            throws ExcelException {
        if (dataList.size() == 0 || dataList == null) {
            throw new ExcelException("数据源中没有任何数据");
        }
        // 创建工作簿并发送到OutputStream指定的地方
        WritableWorkbook wwb;
        try {
            wwb = Workbook.createWorkbook(out);
            // 因为2003的Excel一个工作表最多可以有65536条记录
            int sheetSize = 65530;
            // 所以如果记录太多，需要放到多个工作表中，其实就是个分页的过程
            // 1.计算一共有多少个工作表
            double sheetNum = Math.ceil(dataList.size() / new Integer(sheetSize).doubleValue());
            // 2.创建相应的工作表，并向其中填充数据
            for (int i = 0; i < sheetNum; i++) {
                // 如果只有一个工作表的情况
                if (1 == sheetNum) {
                    WritableSheet sheet = wwb.createSheet(title, i);
                    fillSheet(sheet, cols, dataList, title, 0, dataList.size() - 1);
                    // 有多个工作表的情况
                } else {
                    WritableSheet sheet = wwb.createSheet(title + (i + 1), i);
                    // 获取开始索引和结束索引
                    int firstIndex = i * sheetSize;
                    int lastIndex = (i + 1) * sheetSize - 1 > dataList.size() - 1 ? dataList.size() - 1
                            : (i + 1) * sheetSize - 1;
                    // 填充工作表
                    fillSheet(sheet, cols, dataList, title, firstIndex, lastIndex);
                }
            }
            wwb.write();
            wwb.close();
        } catch (Exception e) {
            e.printStackTrace();
            // 如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;
                // 否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }
    }

    /**
     * 导出Excel（导出到浏览器）
     *
     * @param cols     列的集合
     * @param dataList 每一行的数据
     * @param title    标题
     * @param response 使用response可以导出到浏览器
     * @param request  使用request可以设置文件名不乱码
     * @throws ExcelException
     */
    public static void listToExcel(String[] cols, List<String[]> dataList, String title, HttpServletResponse response,
                                   HttpServletRequest request) throws ExcelException {

        // 创建工作簿并发送到浏览器
        try {
            // 当前时间字符串
            String dateStr = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
            // 文件名（标题+当前时间）
            String titleStr = title + "_" + dateStr;
            // 针对IE或者以IE为内核的浏览器
            String userAgent = request.getHeader("User-Agent");
            if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
                titleStr = URLEncoder.encode(titleStr, "UTF-8");
            } else {
                // 非IE浏览器的处理
                titleStr = new String(titleStr.getBytes("UTF-8"), "ISO-8859-1");
            }

            // 设置response头信息
            response.reset();
            // 改成输出excel文件
            response.setContentType("application/vnd.ms-excel");
            // 这里设置一下让浏览器弹出下载提示框，而不是直接在浏览器中打开
            response.setHeader("Content-disposition", "attachment; filename=" + titleStr + ".xls");
            // 获取导出流
            OutputStream out = response.getOutputStream();
            listToExcel(cols, dataList, title, out);
        } catch (Exception e) {
            e.printStackTrace();
            // 如果是ExcelException，则直接抛出
            if (e instanceof ExcelException) {
                throw (ExcelException) e;
                // 否则将其它异常包装成ExcelException再抛出
            } else {
                throw new ExcelException("导出Excel失败");
            }
        }
    }

	/*
     * <------辅助的私有方法------>
	 */

    /**
     * 设置工作表自动列宽
     *
     * @param ws        工作表
     * @param extraWith 额外宽度
     */
    private static void setColumnAutoSize(WritableSheet ws, int extraWith) {
        // 获取本列的最宽单元格的宽度
        for (int i = 0; i < ws.getColumns(); i++) {
            int colWith = 0;
            for (int j = 0; j < ws.getRows(); j++) {
                String content = ws.getCell(i, j).getContents().toString();
                int cellWith = content.getBytes().length; // 一个中文占2个字节
                if (colWith < cellWith) {
                    colWith = cellWith;
                }
            }
            // 设置单元格的宽度为最宽宽度+额外宽度
            ws.setColumnView(i, colWith + extraWith);
        }
    }

    /**
     * 向工作表中填充数据
     *
     * @param sheet      工作表
     * @param cols       列的集合
     * @param dataList   每一行的数据
     * @param title      标题
     * @param firstIndex 开始索引
     * @param lastIndex  结束索引
     * @throws Exception
     */
    private static void fillSheet(WritableSheet sheet, String[] cols, List<String[]> dataList, String title,
                                  int firstIndex, int lastIndex) throws Exception {

        // 标题单元格定义
        WritableFont wf = new WritableFont(WritableFont.ARIAL, 16, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK); // 定义格式、字体、粗体、斜体、下划线、颜色
        WritableCellFormat wcf = new WritableCellFormat(wf);
        wcf.setAlignment(Alignment.CENTRE); // 平行居中
        wcf.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
        wcf.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK); // 边框样式

        // 导航单元格定义
        WritableFont wfNav = new WritableFont(WritableFont.ARIAL, 12, WritableFont.BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcfc = new WritableCellFormat(wfNav);
        wcfc.setAlignment(Alignment.CENTRE); // 平行居中
        wcfc.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
        wcfc.setWrap(true); // 设置自动换行
        wcfc.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK); // 边框样式

        // 内容单元格定义
        WritableFont wfontent = new WritableFont(WritableFont.ARIAL, 10, WritableFont.NO_BOLD, false,
                UnderlineStyle.NO_UNDERLINE, Colour.BLACK);
        WritableCellFormat wcfe = new WritableCellFormat(wfontent);
        wcfe.setAlignment(Alignment.CENTRE); // 平行居中
        wcfe.setVerticalAlignment(VerticalAlignment.CENTRE); // 垂直居中
        wcfe.setWrap(true); // 设置自动换行
        wcfe.setBorder(Border.ALL, BorderLineStyle.THIN, Colour.BLACK); // 边框样式

        int rowIndex = 0;
        sheet.setRowView(rowIndex, 999, false);// 设置标题行高
        // 填充标题
        sheet.addCell(new Label(0, rowIndex, title, wcf));
        sheet.mergeCells(0, rowIndex, cols.length - 1, rowIndex); // 合并标题所占单元格
        rowIndex++;

        sheet.setRowView(rowIndex, 666, false);// 设置导航的行高
        // 填充导航
        for (int i = 0; i < cols.length; i++) {
            Label label = new Label(i, rowIndex, cols[i], wcfc);
            sheet.addCell(label);
        }

        // 填充内容
        for (int index = firstIndex; index <= lastIndex; index++) {
            rowIndex++;
            sheet.setRowView(rowIndex, 555, false); // 设置内容的行高
            // 获取单个对象
            String[] item = dataList.get(index);
            for (int i = 0; i < item.length; i++) {
                Label label = new Label(i, rowIndex, item[i], wcfe);
                sheet.addCell(label);
            }
        }
        // 设置自动列宽
        setColumnAutoSize(sheet, 6);
    }
}
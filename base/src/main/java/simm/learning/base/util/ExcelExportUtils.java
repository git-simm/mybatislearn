package simm.learning.base.util;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletResponse;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 导出Excel文件（导出“XLSX”格式，支持大数据量导出 @see org.apache.poi.ss.SpreadsheetVersion）
 */
public class ExcelExportUtils {

    private static Logger log = LoggerFactory.getLogger(ExcelExportUtils.class);
    private static final String DEFAULT_URL_ENCODING = "UTF-8";
    /**
     * 工作薄对象
     */
    private HSSFWorkbook wb;

    /**
     * 工作表对象
     */
    private Sheet sheet;

    /**
     * 样式列表
     */
    private Map<String, CellStyle> styles;

    /**
     * 当前行号
     */
    private int rownum;

    /**
     * 注解列表（Object[]{ ExcelField, Field/Method }）
     */
    List<Object[]> annotationList = new ArrayList<>();

    /**
     * 构造函数
     *
     * @param title
     *            表格标题，传“空值”，表示无标题
     * @param cls
     *            实体对象，通过annotation.ExportField获取标题
     */
    public ExcelExportUtils(String title, Class<?> cls) {
        this(title, cls, 1);
    }

    /**
     * 构造函数
     *
     * @param title
     *            表格标题，传“空值”，表示无标题
     * @param cls
     *            实体对象，通过annotation.ExportField获取标题
     * @param type
     *            导出类型（1:导出数据；2：导出模板）
     * @param groups
     *            导入分组
     */
    public ExcelExportUtils(String title, Class<?> cls, int type, int... groups) {
        getAnnotationField(cls, type, groups);// Get annotation field
        fieldSorting();// Field sorting
        List<String> headerList = initializeHeaderList(type);// Initialize
        initializeTitle(title, headerList);
        initializeHeader(title, headerList);
    }

    /**
     * 根据注解生成表格头
     *
     * @Description
     * @param type
     * @return
     * @return List<String>
     * @author yuxiaoyu
     * @date 2016年4月5日 下午5:05:42
     */
    private List<String> initializeHeaderList(int type) {
        List<String> headerList = new ArrayList<>();
        for (Object[] os : annotationList) {
            String t = ((ExcelField) os[0]).title();
            // 如果是导出，则去掉注释
            if (type == 1) {
                String[] ss = StringUtils.split(t, "**", 2);
                if (ss.length == 2) {
                    t = ss[0];
                }
            }
            headerList.add(t);
        }
        return headerList;
    }

    /**
     * 列排序
     *
     * @Description
     * @return void
     * @author yuxiaoyu
     * @date 2016年4月5日 下午5:06:07
     */
    private void fieldSorting() {
        Collections.sort(annotationList, new Comparator<Object[]>() {
            public int compare(Object[] o1, Object[] o2) {
                return new Integer(((ExcelField) o1[0]).sort()).compareTo(new Integer(((ExcelField) o2[0]).sort()));
            }
        });
    }

    /**
     * 获取ExcelField注解的成员便量
     * @author yuxiaoyu
     * @date 2016年4月5日 下午5:07:19
     */
    private void getAnnotationField(Class<?> cls, int type, int... groups) {
        Field[] fs = cls.getDeclaredFields();
        for (Field f : fs) {
            ExcelField ef = f.getAnnotation(ExcelField.class);
            addExcelField(type, f, ef, groups);
        }
    }

    private void addExcelField(int type, Field f, ExcelField ef, int[] groups) {
        if (ef != null && (ef.type() == 0 || ef.type() == type)) {
            if (groups != null && groups.length > 0) {
                boolean inGroup = false;
                for (int g : groups) {
                    if (inGroup) {
                        break;
                    }
                    for (int efg : ef.groups()) {
                        if (g == efg) {
                            inGroup = true;
                            annotationList.add(new Object[] { ef, f });
                            break;
                        }
                    }
                }
            } else {
                annotationList.add(new Object[] { ef, f });
            }
        }
    }

    /**
     * 构造函数
     *
     * @param title
     *            表格标题，传“空值”，表示无标题
     * @param headers
     *            表头数组
     */
    public ExcelExportUtils(String title, String[] headers) {
        initializeTitle(title, Arrays.asList(headers));
        initializeHeader(title, Arrays.asList(headers));
    }

    /**
     * 构造函数
     *
     * @param title
     *            表格标题，传“空值”，表示无标题
     * @param headerList
     *            表头列表
     */
    public ExcelExportUtils(String title, List<String> headerList) {
        initializeTitle(title, headerList);
        initializeHeader(title, headerList);
    }

    /**
     * 初始化函数
     *
     * @param title
     *            表格标题，传“空值”，表示无标题
     * @param headerList
     *            表头列表
     */
    private void initializeHeader(String title, List<String> headerList) {
        // Create header
        if (headerList == null) {
            throw new RuntimeException("headerList not null!");
        }
        Row headerRow = sheet.createRow(rownum++);
        headerRow.setHeightInPoints(16);
        for (int i = 0; i < headerList.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellStyle(styles.get("header"));
            String[] ss = StringUtils.split(headerList.get(i), "**", 2);
            if (ss.length == 2) {
                cell.setCellValue(ss[0]);
                Comment comment = this.sheet.createDrawingPatriarch().createCellComment(new XSSFClientAnchor(0, 0, 0, 0, (short) 3, 3, (short) 5, 6));
                comment.setString(new XSSFRichTextString(ss[1]));
                cell.setCellComment(comment);
            } else {
                cell.setCellValue(headerList.get(i));
            }
            sheet.autoSizeColumn(i);
        }
        for (int i = 0; i < headerList.size(); i++) {
            int colWidth = sheet.getColumnWidth(i) * 2;
            sheet.setColumnWidth(i, colWidth < 3000 ? 3000 : colWidth);
        }
        log.debug("Initialize success.");
    }

    private void initializeTitle(String title, List<String> headerList) {
        this.wb = new HSSFWorkbook();
        this.sheet = wb.createSheet("Export");
        this.styles = createStyles(wb);
        // Create title
        if (StringUtils.isNotBlank(title)) {
            Row titleRow = sheet.createRow(rownum++);
            titleRow.setHeightInPoints(30);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellStyle(styles.get("title"));
            titleCell.setCellValue(title);
            sheet.addMergedRegion(new CellRangeAddress(titleRow.getRowNum(), titleRow.getRowNum(), titleRow.getRowNum(), headerList.size() - 1));
        }
    }

    /**
     * 创建表格样式
     *
     * @param wb
     *            工作薄对象
     * @return 样式列表
     */
    private Map<String, CellStyle> createStyles(Workbook wb) {
        Map<String, CellStyle> styles = new HashMap<String, CellStyle>();

        CellStyle style = wb.createCellStyle();
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        Font titleFont = wb.createFont();
        titleFont.setFontName("Arial");
        titleFont.setFontHeightInPoints((short) 16);
        titleFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(titleFont);
        styles.put("title", style);

        style = wb.createCellStyle();
        style.setVerticalAlignment(CellStyle.VERTICAL_CENTER);
        style.setBorderRight(CellStyle.BORDER_THIN);
        style.setRightBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderLeft(CellStyle.BORDER_THIN);
        style.setLeftBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderTop(CellStyle.BORDER_THIN);
        style.setTopBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setBorderBottom(CellStyle.BORDER_THIN);
        style.setBottomBorderColor(IndexedColors.GREY_50_PERCENT.getIndex());
        Font dataFont = wb.createFont();
        dataFont.setFontName("Arial");
        dataFont.setFontHeightInPoints((short) 10);
        style.setFont(dataFont);
        styles.put("data", style);

        DataFormat format = wb.createDataFormat();
        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setDataFormat(format.getFormat("yyyy-MM-dd"));
        styles.put("defaultDate", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_LEFT);
        styles.put("data1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        styles.put("data2", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_RIGHT);
        styles.put("data3", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        style.setAlignment(CellStyle.ALIGN_CENTER);
        Font headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        style.setFont(headerFont);
        styles.put("header1", style);

        style = wb.createCellStyle();
        style.cloneStyleFrom(styles.get("data"));
        // style.setWrapText(true);
        style.setAlignment(CellStyle.ALIGN_CENTER);
        style.setFillForegroundColor(IndexedColors.GREY_50_PERCENT.getIndex());
        style.setFillPattern(CellStyle.SOLID_FOREGROUND);
        headerFont = wb.createFont();
        headerFont.setFontName("Arial");
        headerFont.setFontHeightInPoints((short) 10);
        headerFont.setBoldweight(Font.BOLDWEIGHT_BOLD);
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        style.setFont(headerFont);
        styles.put("header", style);

        return styles;
    }

    /**
     * 添加一行
     *
     * @return 行对象
     */
    public Row addRow() {
        return sheet.createRow(rownum++);
    }

    /**
     * 添加一个单元格，指定样式
     * @author yuxiaoyu
     * @date 2016年4月5日 下午2:54:52
     */
    public Cell addCell(Row row, int column, String val, String styleType) {
        Cell cell = row.createCell(column);
        CellStyle style = styles.get(styleType);
        if (val == null) {
            cell.setCellValue(StringUtils.EMPTY);
        } else if (val instanceof String) {
            cell.setCellValue((String) val);
        }
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 添加一个单元格
     *
     * @param row
     *            添加的行
     * @param column
     *            添加列号
     * @param val
     *            添加值
     * @return 单元格对象
     */
    public Cell addCell(Row row, int column, Object val) {
        return this.addCell(row, column, val, 0, Class.class, null);
    }

    /**
     * 添加一个单元格
     *
     * @param row
     *            添加的行
     * @param column
     *            添加列号
     * @param val
     *            添加值
     * @param align
     *            对齐方式（1：靠左；2：居中；3：靠右）
     * @return 单元格对象
     */
    public Cell addCell(Row row, int column, Object val, int align, Class<?> fieldType, String category) {
        Cell cell = row.createCell(column);
        CellStyle style = styles.get("data" + (align >= 1 && align <= 3 ? align : ""));
        try {
            if (val == null) {
                cell.setCellValue("");
            } else if (fieldType == List.class) {
                cell.setCellValue("");
                //TODO：设置字典项的值
            } else if(fieldType == Date.class){
                String dateFormart=new SimpleDateFormat(category).format((Date)val);
                cell.setCellValue(dateFormart);
            } else if (val instanceof String) {
                cell.setCellValue((String) val);
            }else if (val instanceof Byte) {
                /*Bug1627 jiangkewen 20171208 begin*/
                cell.setCellValue((Byte) val);
                /*Bug1627 jiangkewen 20171208 end*/
            } else if (val instanceof Integer) {
                cell.setCellValue((Integer) val);
            } else if (val instanceof Long) {
                cell.setCellValue((Long) val);
            } else if (val instanceof Double) {
                cell.setCellValue((Double) val);
            } else if (val instanceof Float) {
                cell.setCellValue((Float) val);
            } else if (val instanceof Date) {
                /*Bug1627 jiangkewen 20171208 begin*/
                style = styles.get("defaultDate");
                /*Bug1627 jiangkewen 20171208 end*/
                cell.setCellValue((Date) val);
            } else {
                if (fieldType != Class.class) {
                    cell.setCellValue((String) fieldType.getMethod("setValue", Object.class).invoke(null, val));
                } else {
                    cell.setCellValue((String) Class
                            .forName(
                                    this.getClass().getName()
                                            .replaceAll(this.getClass().getSimpleName(), "fieldtype." + val.getClass().getSimpleName() + "Type"))
                            .getMethod("setValue", Object.class).invoke(null, val));
                }
            }
        } catch (Exception ex) {
            log.info("Set cell value [" + row.getRowNum() + "," + column + "] error: " + ex.toString());
            cell.setCellValue(val.toString());
        }
        cell.setCellStyle(style);
        return cell;
    }

    /**
     * 添加数据（通过annotation.ExportField添加数据）
     *
     * @return list 数据列表
     */
    public ExcelExportUtils setDataList(JSONArray list) {
        for (JSONObject e : list.toJavaList(JSONObject.class)) {
            int colunm = 0;
            Row row = this.addRow();
            String category = "";
            StringBuilder sb = new StringBuilder();
            for (Object[] os : annotationList) {
                ExcelField ef = (ExcelField) os[0];
                Object val = null;
                // Get entity value
                try {
                    category = ef.value();
                    if(ef.fieldType() == Integer.class){
                        val = e.getInteger(category);
                    }else if(ef.fieldType() == Float.class){
                        val = e.getFloat(category);
                    }else if(ef.fieldType() == BigDecimal.class){
                        val = e.getBigDecimal(category);
                    }else if(ef.fieldType() == Byte.class){
                        val = e.getByte(category);
                    }else if(ef.fieldType() == Long.class){
                        val = e.getLong(category);
                    }else if(ef.fieldType() == Double.class){
                        val = e.getDouble(category);
                    }else if(ef.fieldType() == Boolean.class){
                        val = e.getBoolean(category);
                    }else if(ef.fieldType() == Date.class){
                        val = e.getDate(category);
                    } else {
                        val = e.getString(category);
                    }
                    // If is dict, get dict label
					/*if (StringUtils.isNotBlank(ef.dictType())) {
						val = DictUtils.getDictLabel(val == null ? "" : val.toString(), ef.dictType(), "");
					}*/
                } catch (Exception ex) {
                    // Failure to ignore
                    log.info(ex.toString());
                    val = "";
                }
                this.addCell(row, colunm++, val, ef.align(), ef.fieldType(), category);
                sb.append(val + ", ");
            }
            log.debug("Write success: [" + row.getRowNum() + "] " + sb.toString());
        }
        return this;
    }

    /**
     * 输出数据流
     *
     * @param os
     *            输出数据流
     */
    public ExcelExportUtils write(OutputStream os) throws IOException {
        wb.write(os);
        return this;
    }

    /**
     * 输出到客户端
     *
     * @param fileName
     *            输出文件名
     */
    public ExcelExportUtils write(HttpServletResponse response, String fileName) throws IOException {
        response.reset();
        response.setContentType("application/octet-stream; charset=utf-8");
        response.setHeader("Access-Control-Allow-Origin","*");
        response.setHeader("Access-Control-Expose-Headers","Content-Disposition");
        response.setHeader("Content-Disposition", "attachment; filename=" + URLEncoder.encode(fileName, DEFAULT_URL_ENCODING) + ".xls");
        write(response.getOutputStream());
        return this;
    }

    /**
     * 输出到文件
     */
    public ExcelExportUtils writeFile(String name) throws FileNotFoundException, IOException {
        FileOutputStream os = new FileOutputStream(name);
        this.write(os);
        return this;
    }

    /**
     * 清理临时文件
     */
    public ExcelExportUtils dispose() {
//		wb.dispose();
        return this;
    }
}
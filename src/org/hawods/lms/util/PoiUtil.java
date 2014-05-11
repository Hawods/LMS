package org.hawods.lms.util;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.hawods.lms.model.LmsVo;
import org.hawods.lms.view.Column;

/**
 * 
 * 导入导出工具类
 * <p>
 * 可以导入导出excel格式文件
 * <p>
 * 
 * @author hawods
 * @version 1.0
 * @since 1.0
 */
public final class PoiUtil {
	public static Logger logger = LogManager.getLogger();

	/**
	 * 导出
	 * @param path 用户选择的导出路径
	 * @param dataList 导出数据
	 * @return 实际导出的完整路径
	 */
	public static String exportExcel(String path, List<LmsVo> dataList) {
		if (!path.endsWith(".xls")) {
			path += ".xls";
		}
		Workbook workbook = new HSSFWorkbook();
		CreationHelper creationHelper = workbook.getCreationHelper();
		CellStyle numberStyle = workbook.createCellStyle();
		numberStyle.setDataFormat(creationHelper.createDataFormat().getFormat(
				"0.00"));
		Sheet sheet = workbook.createSheet();
		Row row = sheet.createRow(0);
		for (int i = 0; i < Column.values().length; i++) {
			row.createCell(i).setCellValue(Column.values()[i].toString());
		}
		LmsVo vo = null;
		for (int i = 1; i <= dataList.size(); i++) {
			row = sheet.createRow(i);
			vo = dataList.get(i - 1);
			for (int j = 0; j < Column.values().length; j++) {
				Object obj = getDataValue(vo, j);
				Cell cell = row.createCell(j);
				if (obj != null && obj.getClass() == Double.class) {
					cell.setCellStyle(numberStyle);
					cell.setCellType(Cell.CELL_TYPE_NUMERIC);
					cell.setCellValue(Double.parseDouble(obj.toString()));
				} else {
					cell.setCellValue(obj == null ? "" : obj.toString());
				}
			}
		}
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(path);
			workbook.write(fileOut);
			logger.info("文件导出成功");
			return path;
		} catch (IOException e) {
			logger.error("文件导出失败", e);
		} finally {
			try {
				if (fileOut != null) {
					fileOut.close();
				}
			} catch (IOException e) {
			}
		}
		return null;
	}

	private static Object getDataValue(LmsVo vo, int column) {
		switch (Column.values()[column]) {
		case 收货方:
			return vo.getReceiver();
		case 编号:
			return vo.getId();
		case 货物名称:
			return vo.getName();
		case 电话号码:
			return vo.getPhone();
		case 现付:
			return vo.getNowPay();
		case 提付:
			return vo.getTakePay();
		case 回付:
			return vo.getBackPay();
		case 贷款:
			return vo.getLoan();
		case 送货费:
			return vo.getDeliverCharge();
		case 中转费:
			return vo.getTransferCharge();
		case 发货方:
			return vo.getShipper();
		case 签字:
			return vo.getSign();
		case 备注:
			return vo.getState();
		case 创建日期:
			return vo.getCreateTime();
		case 回单带回日期:
			return vo.getBackTime();
		}
		return null;
	}

	/**
	 * 导入，暂未实现
	 * @return
	 */
	public static boolean importExcel() {
		return false;
	}
}

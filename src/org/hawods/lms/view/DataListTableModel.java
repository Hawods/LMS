/*
 * 文件名：DataListTableModel.java
 * 版权：Copyright 2006-2014 Huawei Tech. Co. Ltd. All Rights Reserved. 
 * 描述： DataListTableModel.java
 * 修改人：panzili
 * 修改时间：2014-5-9
 * 修改内容：新增
 */
package org.hawods.lms.view;

import java.sql.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.hawods.lms.model.LmsVo;

public class DataListTableModel extends AbstractTableModel {
	private static final long serialVersionUID = 6755785009248218829L;
	private Logger logger = LogManager.getLogger();

	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

	private List<LmsVo> dataList;

	public void setDataList(List<LmsVo> dataList) {
		this.dataList = dataList;
		resetSum(false);
		fireTableDataChanged();
	}

	public List<LmsVo> getDataList() {
		return dataList;
	}

	public LmsVo getRow(int row) {
		return dataList.get(row);
	}

	public LmsVo addRow(int row, LmsVo vo) {
		if (dataList == null) {
			dataList = new ArrayList<LmsVo>();
		}
		dataList.add(row, vo);
		resetSum(true);
		fireTableRowsInserted(row, row);
		return vo;
	}

	public String[] deleteRow(int... rows) {
		String[] idArr = new String[rows.length];
		for (int i = rows.length - 1; i >= 0; i--) {
			idArr[i] = dataList.remove(i).getId();
		}
		fireTableRowsDeleted(rows[0], rows[rows.length - 1]);
		resetSum(true);
		return idArr;
	}

	private void resetSum(boolean clear) {
		if (clear) {
			dataList.remove(dataList.size() - 1);
			dataList.remove(dataList.size() - 1);
		}
		LmsVo sumVo = new LmsVo();
		LmsVo backVo = new LmsVo();
		double nowPay = 0.0;
		double takePay = 0.0;
		double backPay = 0.0;
		double loan = 0.0;
		double deliverCharge = 0.0;
		double transferCharge = 0.0;
		for (LmsVo item : dataList) {
			if (item.getNowPay() != null) {
				nowPay += item.getNowPay();
			}
			if (item.getTakePay() != null) {
				takePay += item.getTakePay();
			}
			if (item.getBackPay() != null) {
				backPay += item.getBackPay();
			}
			if (item.getLoan() != null) {
				loan += item.getLoan();
			}
			if (item.getDeliverCharge() != null) {
				deliverCharge += item.getDeliverCharge();
			}
			if (item.getTransferCharge() != null) {
				transferCharge += item.getTransferCharge();
			}
		}
		sumVo.setName("合计");
		sumVo.setNowPay(nowPay);
		sumVo.setTakePay(takePay);
		sumVo.setBackPay(backPay);
		sumVo.setLoan(loan);
		sumVo.setDeliverCharge(deliverCharge);
		sumVo.setTransferCharge(transferCharge);
		backVo.setName("应返");
		backVo.setNowPay(takePay + loan - deliverCharge);
		dataList.add(sumVo);
		dataList.add(backVo);
	}

	@Override
	public String getColumnName(int column) {
		return Column.values()[column].name();
	}

	@Override
	public int getRowCount() {
		return dataList == null ? 0 : dataList.size();
	}

	@Override
	public int getColumnCount() {
		return Column.values().length;
	}

	@Override
	public Object getValueAt(int row, int column) {
		LmsVo vo = dataList.get(row);
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

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public Class getColumnClass(int column) {
		switch (Column.values()[column]) {
		case 收货方:
		case 编号:
		case 货物名称:
		case 电话号码:
		case 发货方:
		case 签字:
		case 备注:
			return String.class;
		case 现付:
		case 提付:
		case 回付:
		case 贷款:
		case 送货费:
		case 中转费:
			return Double.class;
		case 创建日期:
		case 回单带回日期:
			return Date.class;
		default:
			return null;
		}
	}

	@Override
	public boolean isCellEditable(int row, int col) {
		return row < dataList.size() - 2 && Column.values()[col] != Column.编号;
	}

	@Override
	public void setValueAt(Object aValue, int row, int column) {
		LmsVo vo = dataList.get(row);
		boolean flag = false;
		try {
			switch (Column.values()[column]) {
			case 收货方:
				flag = vo.setReceiver(aValue.toString());
				break;
			case 编号:
				vo.setId(aValue.toString());
				break;
			case 货物名称:
				flag = vo.setName(aValue.toString());
				break;
			case 电话号码:
				flag = vo.setPhone(aValue.toString());
				break;
			case 现付:
				flag = vo.setNowPay(Double.parseDouble(aValue.toString()));
				break;
			case 提付:
				flag = vo.setTakePay(Double.parseDouble(aValue.toString()));
				break;
			case 回付:
				flag = vo.setBackPay(Double.parseDouble(aValue.toString()));
				break;
			case 贷款:
				flag = vo.setLoan(Double.parseDouble(aValue.toString()));
				break;
			case 送货费:
				flag = vo
						.setDeliverCharge(Double.parseDouble(aValue.toString()));
				break;
			case 中转费:
				flag = vo.setTransferCharge(Double.parseDouble(aValue
						.toString()));
				break;
			case 发货方:
				flag = vo.setShipper(aValue.toString());
				break;
			case 签字:
				flag = vo.setSign(aValue.toString());
				break;
			case 备注:
				flag = vo.setState(aValue.toString());
				break;
			case 创建日期:
				flag = vo.setCreateTime(new Date(dateFormat.parse(
						aValue.toString()).getTime()));
				break;
			case 回单带回日期:
				flag = vo.setBackTime(new Date(dateFormat.parse(
						aValue.toString()).getTime()));
				break;
			}
			if (flag) {
				resetSum(true);
				fireTableCellUpdated(row, column);
				fireTableRowsUpdated(row, dataList.size() - 1);
			}
		} catch (NumberFormatException e) {
			logger.warn(e);
		} catch (ParseException e1) {
			logger.warn(e1);
		}
	}
}

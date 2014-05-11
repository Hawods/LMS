package org.hawods.lms.model;

import java.sql.Date;

public class LmsVo {
	private String id;
	private String name;
	private String phone;
	private Date startTime;
	private Date endTime;
	private Date createTime;
	private Date backTime;
	private String startStation = "";
	private String endStation = "";
	private String shipper = "";
	private String receiver = "";
	private Double nowPay;
	private Double takePay;
	private Double backPay;
	private Double loan;
	private Double deliverCharge;
	private Double transferCharge;
	private String sign;
	private String state;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public boolean setName(String name) {
		if (name != null && !name.equals(this.name)) {
			this.name = name;
			return true;
		}
		return false;
	}

	public String getPhone() {
		return phone;
	}

	public boolean setPhone(String phone) {
		if (phone != null && !phone.equals(this.phone)) {
			this.phone = phone;
			return true;
		}
		return false;
	}

	public String getStartStation() {
		return startStation;
	}

	public void setStartStation(String startStation) {
		this.startStation = startStation;
	}

	public String getEndStation() {
		return endStation;
	}

	public void setEndStation(String endStation) {
		this.endStation = endStation;
	}

	public String getShipper() {
		return shipper;
	}

	public boolean setShipper(String shipper) {
		if (shipper != null && !shipper.equals(this.shipper)) {
			this.shipper = shipper;
			return true;
		}
		return false;
	}

	public String getReceiver() {
		return receiver;
	}

	public boolean setReceiver(String receiver) {
		if (receiver != null && !receiver.equals(this.receiver)) {
			this.receiver = receiver;
			return true;
		}
		return false;
	}

	public Double getNowPay() {
		return nowPay;
	}

	public boolean setNowPay(Double nowPay) {
		if (nowPay != null && !nowPay.equals(this.nowPay)) {
			this.nowPay = nowPay;
			return true;
		}
		return false;
	}

	public Double getTakePay() {
		return takePay;
	}

	public boolean setTakePay(Double takePay) {
		if (takePay != null && !takePay.equals(this.takePay)) {
			this.takePay = takePay;
			return true;
		}
		return false;
	}

	public Double getBackPay() {
		return backPay;
	}

	public boolean setBackPay(Double backPay) {
		if (backPay != null && !backPay.equals(this.backPay)) {
			this.backPay = backPay;
			return true;
		}
		return false;
	}

	public Double getLoan() {
		return loan;
	}

	public boolean setLoan(Double loan) {
		if (loan != null && !loan.equals(this.loan)) {
			this.loan = loan;
			return true;
		}
		return false;
	}

	public Double getDeliverCharge() {
		return deliverCharge;
	}

	public boolean setDeliverCharge(Double deliverCharge) {
		if (deliverCharge != null && !deliverCharge.equals(this.deliverCharge)) {
			this.deliverCharge = deliverCharge;
			return true;
		}
		return false;
	}

	public Double getTransferCharge() {
		return transferCharge;
	}

	public boolean setTransferCharge(Double transferCharge) {
		if (transferCharge != null
				&& !transferCharge.equals(this.transferCharge)) {
			this.transferCharge = transferCharge;
			return true;
		}
		return false;
	}

	public String getSign() {
		return sign;
	}

	public boolean setSign(String sign) {
		if (sign != null && !sign.equals(this.sign)) {
			this.sign = sign;
			return true;
		}
		return false;
	}

	public String getState() {
		return state;
	}

	public boolean setState(String state) {
		if (state != null && !state.equals(this.state)) {
			this.state = state;
			return true;
		}
		return false;
	}

	public Date getStartTime() {
		return startTime;
	}

	public void setStartTime(Date startTime) {
		this.startTime = startTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public boolean setCreateTime(Date createTime) {
		if (createTime != null && !createTime.equals(this.createTime)) {
			this.createTime = createTime;
			return true;
		}
		return false;
	}

	public Date getBackTime() {
		return backTime;
	}

	public boolean setBackTime(Date backTime) {
		if (backTime != null && !backTime.equals(this.backTime)) {
			this.backTime = backTime;
			return true;
		}
		return false;
	}

}

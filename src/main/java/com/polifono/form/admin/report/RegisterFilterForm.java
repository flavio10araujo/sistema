package com.polifono.form.admin.report;

import java.util.Date;

import com.polifono.util.DateUtil;

public class RegisterFilterForm {

	private Date dateBegin;
	private Date dateEnd;
	
	public Date getDateBegin() {
		return dateBegin;
	}
	
	public void setDateBegin(Date dateBegin) {
		this.dateBegin = dateBegin;
	}
	
	public String getDateBeginStr() {
		return DateUtil.formatDate(this.dateBegin);
	}

	public void setDateBeginStr(String dateBeginStr) {
		try {
			this.dateBegin = DateUtil.parseDateYearMonthDayFormat(dateBeginStr);
		}
		catch (Exception e) {
			this.dateBegin = null;
		}
	}
	
	public Date getDateEnd() {
		return dateEnd;
	}
	
	public void setDateEnd(Date dateEnd) {
		this.dateEnd = dateEnd;
	}
	
	public String getDateEndStr() {
		return DateUtil.formatDate(this.dateEnd);
	}

	public void setDateEndStr(String dateEndStr) {
		try {
			this.dateEnd = DateUtil.parseDateYearMonthDayFormat(dateEndStr);
		}
		catch (Exception e) {
			this.dateEnd = null;
		}
	}
}
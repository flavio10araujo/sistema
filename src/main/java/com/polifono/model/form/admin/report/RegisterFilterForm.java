package com.polifono.model.form.admin.report;

import java.util.Date;

import com.polifono.common.util.DateUtil;

import lombok.Data;

@Data
public class RegisterFilterForm {

    private Date dateBegin;
    private Date dateEnd;

    public String getDateBeginStr() {
        return DateUtil.formatDate(this.dateBegin);
    }

    public String getDateEndStr() {
        return DateUtil.formatDate(this.dateEnd);
    }
}

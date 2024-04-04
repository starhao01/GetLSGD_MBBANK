package com.viettelpost.core.controller.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class AccList {
    public String acctNo;
    public String acctAlias;
    public String acctNm;
    public String acctTypCd;
    public String ccyCd;
    public String custId;
    public String hostCustId;
    public String inactiveSts;
    public String orgUnitCd;
    public String isCrdt;
    public String isDebit;
    public String isInq;
    public String currentBalance;
    public String isSync;
    public String category;
    public String productType;
}

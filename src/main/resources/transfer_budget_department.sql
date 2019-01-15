SELECT
    tc.COMPANY_NAME,
    tc.COMPANY_CODE,
    aoe.ORG_NAME DEPARTMENT_NAME,
    aoe.ORG_CODE,
    pa.BUDGET_ACCOUNT_NAME,
    pa.PROJECT_NUMBER,
    pa.BUDGET_YEAR,
    pa.PROJECT_NAME,
    CASE
        WHEN COALESCE(CHAR(TPBH.APPROVED_BY_USER_ID),'')= ''
        THEN
            CASE
                WHEN COALESCE(CWH.APPROVER_NAME,'')=''
                THEN
                    CASE
                        WHEN COALESCE(CWH2.APPROVER_NAME,'')=''
                        THEN AU.USER_NAME
                        ELSE CWH2.APPROVER_NAME
                    END
                ELSE CWH.APPROVER_NAME
            END
        ELSE '已结束'
    END BMS_CURRENT_PERSON,
    pa.APPROVED_BUDGET_AMOUNT,
    pbz.SUBMIT_HT_AMOUNT,
    pbz.DOCUMENT_CODE,
    pbz.DOCUMENT_DESCRIPTION,
    pbz.HT_CURRENT_PERSON,
    pbz.HT_AMOUNT,
    pbz.HT_PROFIT_FROM ,
    pbz.HT_PROFIT_TO,
    pbz.HT_SUPPLIER,
    pbz.FROM_DEPT,
    pbz.DOCUMENT_CREATE_BY,
    pbz.BZ_DOCUMENT_NUMBER,
    pbz.BZ_DOCUMENT_CREATE_BY,
    pbz.BZ_CURRENT_PERSON,
    pbz.BZ_AMOUNT,
    pbz.ORDER_NUMBER,
    pbz.TOTAL_ORDER_AMOUNT,
    pbz.TOTAL_NOT_IN_ACCOUNT,
    pbz.ORDER_BILL_AMOUNT
FROM
    (
        SELECT
            a.COMPANY_NATURE_LOOKUP_CODE,
            a.SET_OF_BOOKS_ID,
            a.COMPANY_ID,
            a.DEPARTMENT_ID,
            a.PROJECT_ID,
            a.BUDGET_TYPE_LOOKUP_CODE ,
            a.BUDGET_YEAR,
            c.BUDGET_ACCOUNT_ID,
            MAX(c.BUDGET_ACCOUNT_NAME) BUDGET_ACCOUNT_NAME,
            MAX(c.BUDGET_ACCOUNT_CODE) BUDGET_ACCOUNT_NUMBER,
            MAX(d.PROJECT_NAME)        PROJECT_NAME,
            MAX(d.PROJECT_NUMBER)      PROJECT_NUMBER,
            ROUND(SUM(
                CASE
                    WHEN COALESCE(a.APPROVED_BUDGET_AMOUNT, 0) = 0
                    THEN MIN(COALESCE(a.INPROCESS_BUDGET_AMOUNT, 0), COALESCE(a.OLD_AMOUNT, 0))
                    ELSE a.APPROVED_BUDGET_AMOUNT
                END ),2) APPROVED_BUDGET_AMOUNT
        FROM
            tbm_budget_project_summary a ,
            TBM_DIMVAL_SUB_COMBINATIONS b ,
            TBM_BUDGET_ACCOUNTS c ,
            TBM_PROJECTS d
        WHERE
            1=1
        AND a.DIMVAL_SUB_COMBINATION_ID=b.DIMVAL_SUB_COMBINATION_ID
        AND b.BUDGET_ACCOUNT_ID=c.BUDGET_ACCOUNT_ID
        AND a.PROJECT_ID=d.PROJECT_ID
        AND a.BUDGET_TYPE_LOOKUP_CODE = 'OPEX'
        AND a.DEPARTMENT_ID = ${department_id}
        AND a.BUDGET_YEAR=${budget_year}
        GROUP BY
            a.COMPANY_NATURE_LOOKUP_CODE,
            a.SET_OF_BOOKS_ID,
            a.COMPANY_ID,
            a.DEPARTMENT_ID,
            a.PROJECT_ID,
            a.BUDGET_TYPE_LOOKUP_CODE,
            a.BUDGET_YEAR,
            c.BUDGET_ACCOUNT_ID )pa
INNER JOIN
    ARCH_ORG_EXT aoe
ON
    aoe.ORG_EXT_ID = pa.DEPARTMENT_ID
LEFT JOIN
    (
        SELECT
            pht.BUDGET_PROJECT_NUMBER,
            pht.BUDGET_ACCOUNT_CODE BUDGET_ACCOUNT_NUMBER,
            pht.DEPARTMENT_CODE,
            pht.SUBMIT_HT_AMOUNT,
            pht.DOCUMENT_CODE,
            pht.DOCUMENT_DESCRIPTION,
            pht.HT_CURRENT_PERSON,
            pht.HT_AMOUNT,
            pht.HT_PROFIT_FROM ,
            pht.HT_PROFIT_TO,
            pht.HT_SUPPLIER,
            pht.FROM_DEPT,
            pht.DOCUMENT_CREATE_BY,
            htbz.BZ_DOCUMENT_NUMBER,
            htbz.BZ_DOCUMENT_CREATE_BY,
            htbz.BZ_CURRENT_PERSON,
            htbz.BZ_AMOUNT,
            htdd.ORDER_NUMBER,
            htdd.TOTAL_ORDER_AMOUNT,
            htdd.TOTAL_NOT_IN_ACCOUNT,
            htdd.ORDER_BILL_AMOUNT
        FROM
            (
                SELECT
                    tba.COMPANY_ID,
                    tba.DEPARTMENT_ID,
                    tba.PROJECT_ID ,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER,
                    CASE
                        WHEN COALESCE(CHAR(tba.DOCUMENT_CODE),'') = ''
                        THEN tba.DOCUMENT_NUMBER
                        ELSE tba.DOCUMENT_CODE
                    END                                    DOCUMENT_CODE ,
                    MAX(tba.DOCUMENT_DESCRIPTION)                             DOCUMENT_DESCRIPTION ,
                    MAX(bpi.CURRENT_PERSON)                                    HT_CURRENT_PERSON,
                    SUM(tba.RESERVED_BUDGET_AMOUNT+tba.OCCUPIED_BUDGET_AMOUNT) HT_AMOUNT ,
                    ROUND(SUM(tba.RESERVED_BUDGET_AMOUNT+tba.OCCUPIED_BUDGET_AMOUNT+
                    tba.RESERVED_BUDGET_TAX_AMOUNT+ tba.OCCUPIED_BUDGET_TAX_AMOUNT),2)
                                            SUBMIT_HT_AMOUNT ,
                    MAX(hpa.HT_PROFIT_FROM) HT_PROFIT_FROM ,
                    MAX(hpa.HT_PROFIT_TO)   HT_PROFIT_TO ,
                    MAX(hpa.HT_SUPPLIER)    HT_SUPPLIER,
                    MAX(hpa.HT_DEPT)        FROM_DEPT,
                    MAX(hpa.HT_PERSON)      DOCUMENT_CREATE_BY,
                    MAX(aoe.ORG_CODE)       DEPARTMENT_CODE,
                    MAX(tp.PROJECT_NUMBER)  BUDGET_PROJECT_NUMBER,
                    bac.BUDGET_ACCOUNT_CODE,
                    bac.BUDGET_ACCOUNT_ID
                FROM
                    TBM_BUDGET_ACTUAL_SUMMARY tba
                INNER JOIN
                    TBM_PROJECTS tp
                ON
                    tp.PROJECT_ID=tba.PROJECT_ID
                INNER JOIN
                    ARCH_ORG_EXT aoe
                ON
                    aoe.ORG_EXT_ID = tba.DEPARTMENT_ID
                LEFT JOIN
                    TBM_BILL_PERSON_INFO bpi
                ON
                    bpi.BILL_NUM=tba.DOCUMENT_NUMBER
                AND bpi.IMPORT_SYSTEM='HT'
                LEFT JOIN
                    (
                        SELECT
                            bzhb.HT_PROFIT_FROM,
                            bzhb.HT_PROFIT_TO,
                            bzhb.HT_SUPPLIER,
                            bzhb.HT_DEPT,
                            bzhb.HT_PERSON,
                            bzhb.ACTUAL_SUMMARY_ID
                        FROM
                            TBM_BUDGET_ACTUAL_SUMMARY_HBJSONDATA bzhb )hpa
                ON
                    tba.SUMMARY_ID = hpa.ACTUAL_SUMMARY_ID
                INNER JOIN
                    (
                        SELECT
                            sub.DIMVAL_SUB_COMBINATION_ID,
                            ba.BUDGET_ACCOUNT_CODE,
                            ba.BUDGET_ACCOUNT_ID
                        FROM
                            TBM_DIMVAL_SUB_COMBINATIONS sub
                        INNER JOIN
                            TBM_BUDGET_ACCOUNTS ba
                        ON
                            ba.BUDGET_ACCOUNT_ID=sub.BUDGET_ACCOUNT_ID) bac
                ON
                    bac.DIMVAL_SUB_COMBINATION_ID=tba.DIMVAL_SUB_COMBINATION_ID
                WHERE
                    1=1
                AND SUBSTR(tba.DOCUMENT_TYPE_CODE,1,3)='HT_'
                AND tba.budget_year=${budget_year}
                GROUP BY
                    tba.COMPANY_ID,
                    tba.DEPARTMENT_ID,
                    tba.PROJECT_ID,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER,
                    tba.DOCUMENT_CODE,
                    bac.BUDGET_ACCOUNT_ID,
                    bac.BUDGET_ACCOUNT_CODE )pht
        LEFT JOIN
            (
                SELECT
                    tba.DEPARTMENT_ID,
                    tba.PROJECT_ID,
                    bac.BUDGET_ACCOUNT_ID,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER BZ_DOCUMENT_NUMBER,
                    tba.PRE_DOCUMENT_NUMBER,
                    tba.PRE_DOCUMENT_TYPE_CODE ,
                    MAX(bpi.CURRENT_PERSON)                                    BZ_CURRENT_PERSON,
                    SUM(tba.RESERVED_BUDGET_AMOUNT+tba.OCCUPIED_BUDGET_AMOUNT) BZ_AMOUNT,
                    MAX(tba.DOCUMENT_CREATE_BY)                                BZ_DOCUMENT_CREATE_BY
                    ,
                    MAX(tbash.BZ_ORDER) BZ_ORDER
                FROM
                    TBM_BUDGET_ACTUAL_SUMMARY tba
                LEFT JOIN
                    TBM_BUDGET_ACTUAL_SUMMARY_HBJSONDATA tbash
                ON
                    tba.SUMMARY_ID = tbash.ACTUAL_SUMMARY_ID
                LEFT JOIN
                    TBM_BILL_PERSON_INFO bpi
                ON
                    bpi.BILL_NUM=tba.DOCUMENT_NUMBER
                AND bpi.IMPORT_SYSTEM='BZ'
                INNER JOIN
                    (
                        SELECT
                            sub.DIMVAL_SUB_COMBINATION_ID,
                            ba.BUDGET_ACCOUNT_ID
                        FROM
                            TBM_DIMVAL_SUB_COMBINATIONS sub
                        INNER JOIN
                            TBM_BUDGET_ACCOUNTS ba
                        ON
                            ba.BUDGET_ACCOUNT_ID=sub.BUDGET_ACCOUNT_ID) bac
                ON
                    bac.DIMVAL_SUB_COMBINATION_ID=tba.DIMVAL_SUB_COMBINATION_ID
                WHERE
                    1=1
                AND tba.DOCUMENT_TYPE_CODE IN('BZ_YHTBZ',
                                              'BZ_YHTJT',
                                              'BZ_YHTDT',
                                              'ERP_YZXZC',
                                              'ERP_YYFFP',
                                              'ERP_YSGPZ',
                                              'ERP_YNBWL')
                AND tba.BUDGET_YEAR=${budget_year}
                AND (
                        tba.RESERVED_BUDGET_AMOUNT<>0
                    OR  tba.OCCUPIED_BUDGET_AMOUNT<>0)
                GROUP BY
                    tba.DEPARTMENT_ID,
                    tba.PROJECT_ID ,
                    bac.BUDGET_ACCOUNT_ID,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER,
                    PRE_DOCUMENT_NUMBER,
                    PRE_DOCUMENT_TYPE_CODE )htbz
        ON
            htbz.BUDGET_ACCOUNT_ID=pht.BUDGET_ACCOUNT_ID
        AND pht.DEPARTMENT_ID = htbz.DEPARTMENT_ID
        AND pht.PROJECT_ID = htbz.PROJECT_ID
        AND pht.DOCUMENT_TYPE_CODE = htbz.PRE_DOCUMENT_TYPE_CODE
        AND pht.DOCUMENT_CODE = htbz.PRE_DOCUMENT_NUMBER
        LEFT JOIN
            (
                SELECT
                    coa.ORDER_NUM ORDER_NUMBER,
                    coa.TOTAL_ORDER_AMOUNT,
                    coa.TOTAL_NOT_IN_ACCOUNT,
                    coa.ORDER_BILL_AMOUNT
                FROM
                    TBM_SOA_PO_ORDER_EB_INF_PKG_SRV coa
                WHERE
                    coa. ORDER_NUM IS NOT NULL )htdd
        ON
            htdd.ORDER_NUMBER=htbz.BZ_ORDER
        UNION
        SELECT
            wht.BUDGET_PROJECT_NUMBER,
            wht.BUDGET_ACCOUNT_NUMBER,
            wht.DEPARTMENT_CODE,
            CAST(NULL AS DECIMAL(16,2)) SUBMIT_HT_AMOUNT,
            ''                          DOCUMENT_CODE,
            ''                          DOCUMENT_DESCRIPTION,
            ''                          HT_CURRENT_PERSON,
            CAST(NULL AS DECIMAL(16,2)) HT_AMOUNT,
            CASE '2016-01-02'
                WHEN '2016-01-01'
                THEN CURRENT DATE
                ELSE NULL
            END HT_PROFIT_FROM ,
            CASE '2016-01-02'
                WHEN '2016-01-01'
                THEN CURRENT DATE
                ELSE NULL
            END HT_PROFIT_TO,
            ''  HT_SUPPLIER,
            ''  FROM_DEPT,
            ''  DOCUMENT_CREATE_BY,
            wht.BZ_DOCUMENT_NUMBER,
            wht.BZ_DOCUMENT_CREATE_BY,
            wht.BZ_CURRENT_PERSON,
            wht.BZ_AMOUNT,
            htdd.ORDER_NUMBER,
            htdd.TOTAL_ORDER_AMOUNT,
            htdd.TOTAL_NOT_IN_ACCOUNT,
            htdd.ORDER_BILL_AMOUNT
        FROM
            (
                SELECT
                    tba.COMPANY_ID,
                    aoe.ORG_CODE            DEPARTMENT_CODE,
                    tp.PROJECT_NUMBER       BUDGET_PROJECT_NUMBER,
                    bac.BUDGET_ACCOUNT_CODE BUDGET_ACCOUNT_NUMBER,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER BZ_DOCUMENT_NUMBER,
                    tba.PRE_DOCUMENT_NUMBER,
                    tba.PRE_DOCUMENT_TYPE_CODE,
                    MAX(bpi.CURRENT_PERSON)                                    BZ_CURRENT_PERSON,
                    SUM(tba.RESERVED_BUDGET_AMOUNT+tba.OCCUPIED_BUDGET_AMOUNT) BZ_AMOUNT,
                    MAX(tba.DOCUMENT_CREATE_BY)                                BZ_DOCUMENT_CREATE_BY
                    ,
                    MAX(tbash.BZ_ORDER) BZ_ORDER
                FROM
                    TBM_BUDGET_ACTUAL_SUMMARY tba
                INNER JOIN
                    TBM_PROJECTS tp
                ON
                    tp.PROJECT_ID=tba.PROJECT_ID
                INNER JOIN
                    ARCH_ORG_EXT aoe
                ON
                    aoe.ORG_EXT_ID = tba.DEPARTMENT_ID
                LEFT JOIN
                    TBM_BUDGET_ACTUAL_SUMMARY_HBJSONDATA tbash
                ON
                    tba.SUMMARY_ID = tbash.ACTUAL_SUMMARY_ID
                LEFT JOIN
                    TBM_BILL_PERSON_INFO bpi
                ON
                    bpi.BILL_NUM=tba.DOCUMENT_NUMBER
                AND bpi.IMPORT_SYSTEM='BZ'
                INNER JOIN
                    (
                        SELECT
                            sub.DIMVAL_SUB_COMBINATION_ID,
                            ba.BUDGET_ACCOUNT_CODE
                        FROM
                            TBM_DIMVAL_SUB_COMBINATIONS sub
                        INNER JOIN
                            TBM_BUDGET_ACCOUNTS ba
                        ON
                            ba.BUDGET_ACCOUNT_ID=sub.BUDGET_ACCOUNT_ID) bac
                ON
                    bac.DIMVAL_SUB_COMBINATION_ID=tba.DIMVAL_SUB_COMBINATION_ID
                WHERE
                    1=1
                AND tba.DOCUMENT_TYPE_CODE IN('BZ_WHTBZ',
                                              'BZ_WHTJT',
                                              'BZ_WHTDT',
                                              'ERP_ZXZC',
                                              'ERP_YFFP',
                                              'ERP_SGPZ',
                                              'ERP_NBWL')
                AND tba.BUDGET_YEAR=${budget_year}
                AND (
                        tba.RESERVED_BUDGET_AMOUNT<>0
                    OR  tba.OCCUPIED_BUDGET_AMOUNT<>0)
                GROUP BY
                    tba.COMPANY_ID,
                    aoe.ORG_CODE,
                    tp.PROJECT_NUMBER,
                    bac.BUDGET_ACCOUNT_CODE,
                    tba.DOCUMENT_TYPE_CODE,
                    tba.DOCUMENT_NUMBER ,
                    PRE_DOCUMENT_NUMBER,
                    PRE_DOCUMENT_TYPE_CODE )wht
        LEFT JOIN
            (
                SELECT
                    coa.ORDER_NUM ORDER_NUMBER,
                    coa.TOTAL_ORDER_AMOUNT,
                    coa.TOTAL_NOT_IN_ACCOUNT,
                    coa.ORDER_BILL_AMOUNT
                FROM
                    TBM_SOA_PO_ORDER_EB_INF_PKG_SRV coa
                WHERE
                    coa. ORDER_NUM IS NOT NULL )htdd
        ON
            htdd.ORDER_NUMBER=wht.BZ_ORDER )pbz
ON
    aoe.ORG_CODE=pbz.DEPARTMENT_CODE
AND pa.PROJECT_NUMBER=pbz.BUDGET_PROJECT_NUMBER
AND pa.BUDGET_ACCOUNT_NUMBER=pbz.BUDGET_ACCOUNT_NUMBER
INNER JOIN
    TBM_COMPANIES tc
ON
    tc.COMPANY_ID = pa.COMPANY_ID
LEFT JOIN
    TBM_PROJECT_BUDGET_HEADERS TPBH
ON
    PA.PROJECT_ID=TPBH.PROJECT_ID
AND PA.DEPARTMENT_ID=TPBH.DEPARTMENT_ID
LEFT JOIN
    CJBPM_WORKFLOW_HISTORY CWH
ON
    CWH.BUSINESS_ID=CHAR(PA.PROJECT_ID)
AND CWH.TASK_TYPE='waitfordeal'
LEFT JOIN
    CJBPM_WORKFLOW_HISTORY CWH2
ON
    CWH2.BUSINESS_ID=CHAR(TPBH.PROJECT_BUDGET_HEADER_ID)
AND CWH2.TASK_TYPE='waitfordeal'
LEFT JOIN
    ARCH_USER_EXT AUE
ON
    AUE.USER_EXT_ID=TPBH.LAST_UPDATE_BY
LEFT JOIN
    ARCH_USER AU
ON
    AU.USER_ID=AUE.USER_ID
WHERE
    1=1
AND TPBH.LATEST_VERSION_FLAG='Y'
ORDER BY
    pa.COMPANY_ID,
    pa.DEPARTMENT_ID,
    pa.BUDGET_ACCOUNT_ID,
    pa.PROJECT_ID,
    pbz.DOCUMENT_CODE,
	pbz.ORDER_NUMBER DESC
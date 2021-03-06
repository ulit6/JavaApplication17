USE IMPORTER;
DROP PROCEDURE IF EXISTS LNPRH_ADD;
DELIMITER //

CREATE PROCEDURE LNPRH_ADD(IN AWTCH INT ,IN AWPRH  DECIMAL(3,2),IN AONFZ TINYINT  ,IN ACGEN TIMESTAMP )
MODIFIES SQL DATA
BEGIN

    INSERT INTO LNPRH(WTCH,WPRH,ONFZ,CGEN,DPIM) VALUES (AWTCH,AWPRH,AONFZ,ACGEN,CURRENT_TIMESTAMP); 

END;


DROP PROCEDURE IF EXISTS LNPRH_EDT;
CREATE PROCEDURE LNPRH_EDT(IN AWTCH INT)
MODIFIES SQL DATA
BEGIN
    UPDATE LNPRH SET DKIM=CURRENT_TIMESTAMP WHERE WTCH=AWTCH;
END;
//
DELIMITER ;
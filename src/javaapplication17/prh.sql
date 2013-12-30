USE IMPORTER;
DROP TABLE IF EXISTS LNPRH;
CREATE TABLE LNPRH
(
  WTCH SMALLINT NOT NULL PRIMARY KEY,
  WPRH  DECIMAL(3,2) NOT NULL,
  NTYP CHAR(3)   character set utf8 NOT NULL DEFAULT 'PRH',
  ONFZ TINYINT NOT NULL,
  CGEN TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  DPIM TIMESTAMP NOT NULL DEFAULT '0000-00-00 00:00:00',
  DKIM TIMESTAMP DEFAULT '0000-00-00 00:00:00'
) ENGINE=INNODB;

DROP TABLE IF EXISTS LJDMR;

CREATE TABLE  LJDMR
(
 KDJM TINYINT NOT NULL PRIMARY KEY,
 NZJM VARCHAR(4) CHARACTER SET UTF8 NOT NULL,
 STJM CHAR(1) CHARACTER SET UTF8 NOT NULL
) ENGINE=INNODB;

DROP TABLE IF EXISTS LGRSU;

CREATE  TABLE LGRSU
(
 KGRS SMALLINT NOT NULL PRIMARY KEY,
 NGRS VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 KDJM TINYINT NOT NULL,
 SGRS CHAR(1) CHARACTER SET UTF8 NOT NULL
) ENGINE=INNODB;

ALTER TABLE LGRSU ADD CONSTRAINT FOREIGN KEY(KDJM) REFERENCES LJDMR(KDJM) ON DELETE RESTRICT
ON UPDATE RESTRICT ;

DROP TABLE IF EXISTS LHGRS ;
CREATE  TABLE LHGRS
(
 IHGS INT NOT NULL PRIMARY KEY AUTO_INCREMENT,
 KGRS SMALLINT NOT NULL ,
 NGRS VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 KDJM TINYINT NOT NULL,
 SGRS CHAR(1) CHARACTER SET UTF8 NOT NULL,
 WTCH SMALLINT NOT NULL
)ENGINE=INNODB;

ALTER TABLE LHGRS ADD CONSTRAINT FOREIGN KEY(KDJM) REFERENCES LJDMR(KDJM) ON DELETE RESTRICT
ON UPDATE RESTRICT ;
ALTER TABLE LHGRS ADD CONSTRAINT FOREIGN KEY(WTCH) REFERENCES LNPRH(WTCH) ON DELETE RESTRICT
ON UPDATE RESTRICT ;
ALTER TABLE LHGRS ADD CONSTRAINT FOREIGN KEY(KGRS) REFERENCES LGRSU(KGRS) ON DELETE RESTRICT
ON UPDATE RESTRICT ;

DROP TABLE IF EXISTS LPRHA;
CREATE TABLE LPRHA
(
 IDPR INT NOT NULL  PRIMARY KEY AUTO_INCREMENT,
 WTCH SMALLINT NOT NULL,
 KPRH CHAR(6) CHARACTER SET UTF8 NOT NULL,
 KEAN CHAR(14) CHARACTER SET UTF8,
 SPRH CHAR(9) CHARACTER SET UTF8 ,
 KGRS SMALLINT NOT NULL ,
 NPRH VARCHAR(90)  CHARACTER SET UTF8 NOT NULL,
 PPRH VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 DPRH VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 OPRH VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 POPR VARCHAR(90) CHARACTER SET UTF8 NOT NULL,
 IPRH DECIMAL(20,5) NOT NULL,
 LPRH DECIMAL(20,5) NOT NULL,
 STPR CHAR(1) CHARACTER SET UTF8  NOT NULL
) ENGINE=INNODB;

DROP TABLE IF EXISTS TRPRH;
CREATE TABLE TRPRH
(
  IDTR INT NOT NULL  PRIMARY KEY AUTO_INCREMENT,
  IDPR INT NOT NULL  ,
  KKAT CHAR(2) CHARACTER SET UTF8 NOT NULL,
  DTPO DATE NOT NULL,
  DTKO DATE ,
  TBAZ DECIMAL(10,4) NOT NULL,
  LJRO DECIMAL(10.4) NOT NULL
) ENGINE=INNODB;


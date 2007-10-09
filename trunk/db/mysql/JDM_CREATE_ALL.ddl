 

create 	table JDM_DOWNLOAD(
	ID varchar(255) not null primary key, 
	PRIORITY varchar(255) not null, 
	STATUS varchar(255) not null, 
	MD5 varchar(255) , 
	WORK_DIR varchar(255) not null, 
	TARGET_FILE varchar(255) not null, 
	SIZE numeric(20, 0) ,
	REQUEST_TIME DATETIME not null,
	COMMENCEMENT_TIME DATETIME,
	COMPLETION_TIME DATETIME ,
	PROTOCOL varchar(255)	
);

create 	table JDM_DOWNLOAD_URLS(
	DOWNLOAD_ID varchar(255) not null, 
	URL varchar(255) not null
);

create 	table JDM_CHUNK(
	ID int not null , 
	DOWNLOAD_ID varchar(255) not null, 
	STATUS varchar(255) not null, 
	FILE_PATH varchar(255) not null, 
	SIZE numeric(20, 0) not null,
	BEGIN_RANGE numeric(20, 0) ,
	END_RANGE numeric(20, 0) 
);



alter table JDM_CHUNK add constraint FKCHNKID_DNLD_ID foreign key (DOWNLOAD_ID) references JDM_DOWNLAOD(ID);
alter table JDM_DOWNLOAD_URLS add constraint FKURLID_DNLD_ID foreign key (DOWNLOAD_ID) references JDM_DOWNLAOD(ID);

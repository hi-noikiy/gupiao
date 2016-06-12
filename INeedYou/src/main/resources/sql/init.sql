-- 购买记录
create table buy_record(
	id INT AUTO_INCREMENT,
	user_id VARCHAR(50),
	pal_type char(10),
	direction char(10),
	amount numeric(16,4),
	price numeric(16,4),
	op_time timestamp,
	PRIMARY KEY ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 交易记录
create table transaction_record(
	id INT AUTO_INCREMENT,
	direction char(10),
	amount numeric(16,4),
	price numeric(16,4),
	op_time timestamp,
	pal_type char(10),
	good_type char(10),
	PRIMARY KEY ( id )
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 委托记录
create table client_record(
	id INT AUTO_INCREMENT,
	client varchar(100),
	direction char(10),
	amount numeric(16,4),
	price numeric(16,4),
	op_time timestamp,
	pal_type char(10),
	good_type char(10),
	PRIMARY KEY ( id )
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- 用户表
create table orguser(
	id INT AUTO_INCREMENT,
	username varchar(50),
	password varchar(50),
	PRIMARY KEY ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 账户
create table account(
	id INT AUTO_INCREMENT,
	username varchar(50),
	total numeric(16,4),
	pal_type char(10),
	PRIMARY KEY ( id )
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- 字典
create table dictionary(
	code char(10),
	name varchar(50),
	isuse boolean
) ENGINE=MyISAM DEFAULT CHARSET=utf8;


-- 初始化字典数据
insert into dictionary(code,name,isuse) values('ytb','以太币',true);
insert into dictionary(code,name,isuse) values('btb','比特币',true);
insert into dictionary(code,name,isuse) values('in','买入',true);
insert into dictionary(code,name,isuse) values('out','卖出',true);
insert into dictionary(code,name,isuse) values('btc','比特币中国',true);

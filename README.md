# Jinter
Jinter is a project that used to free your hand when you want to put the json string data into the Db . 

we can easily use Jinter like this : 
```  
  Jinter jinter = new Jinter(); // default Jinter will search Jinter.properties or application.properties in the classPath. but if you want to add your own config file you can do using another constructor with parameters

  jinter.goFetch(jsonStr);
```

then the jsonStr data will automaticly put into Db.

we need to configure our dataSource into Jinter . it would be something like this : 
```
#mysql
jdbcUrl = jdbc:mysql://localhost/forum?autoReconnect=true&relaxAutoCommit=true
user = root
password =123456
driverClass=com.mysql.jdbc.Driver

#oracle
#jdbcUrl = jdbc:oracle:thin:@10.60.17.241:1521:dg1
#user = oyjr_ic_uat
#password =oyjr_ic_uat
#driverClass=oracle.jdbc.OracleDriver
```

the Json String standard are : 
```
   {
    "tableName": "Jinter", // tell Jinter what's the name that you want to create in the Db. 
    "jsonDataType": [      // tell Jinter what are the type's of these Columns.
        {
            "isNullable": false, // so we know this column can not be set to null 
            "columnName": "idJ",  // so we know this column's name is id
            "columnType": "int", // so we know this column's type is int
            "columnLength": 11,  // so we know this column's length is 11
            "isPrimaryKey": 1    // so we know this column is a primary key. 
            "fkTableAndColumn":"test1;id;idJ" // so Jinter know the reference table is test1 and reference column is id to Jid. if you there are more than one column , we can use "," to seperate them .    
        },
        {
            "isNullable": true,
            "columnName": "name",
            "columnType": "varchar",
            "columnLength": 256,
            "isPrimaryKey": 0
        },
        {
            "isNullable": true,
            "columnName": "time",
            "columnType": "datetime",
            "columnLength": 0,
            "isPrimaryKey": 0
        },
        {
            "isNullable": true,
            "columnName": "remark",
            "columnType": "varchar",
            "columnLength": 256,
            "isPrimaryKey": 0
        }
    ],
    "jsonDataVal": [      // tell Jinter the columns values
        [
            {
                "id": 1
            },
            {
                "name": "xiaoming"
            },
            {
                "time": "2016-07-09 00:00:00"
            },
            {
                "remark": "hello test"
            }
        ],
        [
            {
                "id": 2
            },
            {
                "name": "xiaoming"
            },
            {
                "time": "2016-07-10 00:00:00"
            },
            {
                "remark": "ello test"
            }
        ]
    ]
}
```
if you want to try it on . 
here is what you need to do .
```
  git clone https://github.com/xiaomingfuckeasylife/Jinter.git
  
  mvn package
  
  cd Jinter/target/  cp Jinter.jar /yourProjectDirectory
  
```
now . Jinter support Mysql and Oracle.

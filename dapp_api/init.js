// const sqlite3 = require('sqlite3').verbose()
// var db = new sqlite3.Database(
//     './sqlite3/api.db', 
//     sqlite3.OPEN_READWRITE, 
//     function (err) {
//         if (err) {
//             return console.log(err.message)
//         }
//         console.log('connect database successfully')
//     }
// )

// db.run('CREATE TABLE t_expect(game_id int, num varchar(10), start_time varchar(20), end_time varchar(20))', function (err) {
//     if (err) {
//         return console.log(err)
//     }
//     console.log('create table user')
// })

// db.run('INSERT INTO t_expect(game_id, num, start_time, end_time) VALUES(?)', ['jack_tony'], function (err) {
//     if (err) {
//         return console.log('insert data error: ', err.message)
//     }
//     console.log('insert data: ', this)
// })


// db.all("select * from t_expect limit 2",function(err,row) {
//     console.log(row);
// })


let sqlite3 = require('sqlite3').verbose()
const db = new sqlite3.Database('./sqlite3/sqlite.db');


class SQLite{

    async hasTable(query) {
        return new Promise((resolve,reject) => {
            db.get(query, [], (err, row) =>{
                if(err) reject(err.message)
                else resolve(row == undefined ? false:true);
            })
        });
    }

    async run(query) {
        return new Promise(function(resolve, reject) {
            db.run(query, function(err)  {
                if(err) reject(err.message)
                else    resolve(true)
            })
        })
    }

    async get(query, params) {
        return new Promise(function(resolve, reject) {
            db.get(query, params, function(err, row)  {
                if(err) reject("Read error: " + err.message)
                else {
                    resolve(row)
                }
            })
        }) 
    }
    

    async all(query, params) {
        return new Promise(function(resolve, reject) {
            if(params == undefined) params=[]
     
            db.all(query, params, function(err, rows)  {
                if(err) reject("Read error: " + err.message)
                else {
                    resolve(rows)
                }
            })
        }) 
    }

    async close() {
        return new Promise(function(resolve, reject) {
            db.close()
            resolve(true)
        }) 
    }
    

}

(async ()=>{

    let sqlite = new SQLite();


    //判断表是否存在(没有则创建)
    let query = `SELECT name FROM sqlite_master WHERE type='table' AND name='t_expect'`;
    let result = await sqlite.hasTable(query);
    if(result == false) {
        query = 'CREATE TABLE t_expect(game_id int, num varchar(10), start_time varchar(20), end_time varchar(20))';
        var r = await sqlite.run(query)
        if(r) console.log("table created")
    }

    // query = `INSERT INTO t_expect(game_id, num, start_time, end_time) VALUES(2,1,'1','1')`;
    // result = await sqlite.run(query)
    // console.log(result);

    query = `select * from t_expect order by game_id desc limit 3`;
    result = await sqlite.all(query);
    console.log(result);



})();




// 增:


// 删:
// var del=db.prepare("DELETE from human where name =?");  
// del.run('小白1');  
// del.finalize();

//改:
// var r = db.prepare("UPDATE human set name =? where id =2");  
// r.run("小白22222");  
// r.finalize();

// 查 指定字段
// db.each("SELECT id, name,age FROM human", function(err, row) {
//     console.log(`${row.id} 姓名:${row.name} 年龄:${row.age}`);
//   });

// 查 所有字段
// db.all("select * from t_expect",function(err,row){
//     console.log(JSON.stringify(row));
// })

// 查 按条件
// db.each("SELECT id, name,age FROM human where name=?",'小白2', function(err, row) {
//     console.log(`${row.id} 姓名:${row.name} 年龄:${row.age}`);
//   });
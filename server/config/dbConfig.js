
/**
 * DB 연결을 시도하는 모듈
 * createConnection : 시작하자마자 DB랑 연동, 끝까지 연결되는 문제가 있음 
 * ->(데이터 불일치 상황이 어쩌면 발생할 수도???)
 * createPool : 필요할 때만 DB랑 연동, 비동기처리 방식이라서 끊는 것도 원하는 시점에 끊는 게 가능
 */
var mysql = require('mysql');

const pool = mysql.createPool({
    host: '10.1.188.54',
    user: 'root',
    password : 'wlshvkr!1',
    database : 'sw_test',
    charset : 'utf8'
});

module.exports = pool;
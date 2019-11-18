var express = require('express');
var router = express.Router();
var pool = require('../config/dbConfig');

/* POST home page. */
router.get('/', function (req, res, next) {
  /**
   * getConnection으로 DB연결
   * 연결에 실패하면 err를 던지고
   * 성공하면 그 아래에 있는 query문을 실행한다.
   * (변수 넣을 때 query문 주의 해야됨!)
   * release로 연결을 끊을 때는 query 함수 안에서 끊어주면 된다. (비동기처리라서 알아서 끊김)
   */
  pool.getConnection((err, conn) => {
      if (err) throw err;
      const query = "SELECT CID, CNAME FROM hairclient";
      conn.query(query, (err, row) => {
          conn.release();
          if (err) throw err;
          res.send(row);
      })
  });
});

router.post('/login', (req, res) => {
  const { id, password } = req.body;
  //Create Connection
  /**
   * 
   */
  pool.getConnection((err, conn) => {
      if (err) throw err;
      const query = "SELECT CID, CPassword FROM hairclient WHERE CID = ? AND CPassword = ?";
      conn.query(query, {id, password}, (err, row) => {
          if (err) throw err;
          console.log(row.id);
          res.send(row);
      })
  });
})

module.exports = router;

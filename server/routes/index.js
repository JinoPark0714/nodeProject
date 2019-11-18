var express = require('express');
var router = express.Router();
var pool = require('../config/dbConfig');

/* GET home page. */
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

/**
 * 로그인을 할 떄 ID와 Password를 확인하는 곳
 * /login으로 url을 받아오면 여기서 데이터를 모두 받게 된다.
 * 받아온 데이터는 SQL WHERE문에 데이터가 들어가게 되고
 * 모두 일치하면 로그인에 성공했다는 응답을 다시 보내준다.
 *  */
router.post('/login', (req, res) => {
    var result = {};
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        /**
         * Grade = 0 -> 미용실 손님
         * Grade = 1 -> 미용사
         * Grade = 2 -> 사업자
         */
        const query = `SELECT CID, CPassword, CName, CPhoneNum, Grade
        FROM Hairclient H
        WHERE (CID = ? AND CPassword = ?)`;
        conn.query(query, [myRequest.CID, myRequest.CPassword], (err, row) => {
            if (err) throw err;

            if (row.length !== 0) {
                result["success"] = 1;
                result["Grade"] = row[0].Grade;
                res.send(result);
            } else {
                result["success"] = 0;
                result["err"] = "incorrect ID or Password"; //오류가 발생한 원인을 알려주는 용도
                res.send(result);
            }
        })
    });
})

/**
 * 회원가입을 했을 때 데이터가 들어오는 곳
 * 1차 통과(비밀번호 일치 유무)를 하고 왔으면 여기서 ID 중복유무 검사를 확인한다.
 * 만약 ID가 중복된 상태로 회원가입이 끝나면 개체 무결성 제약조건을 위반하므로
 * 중복된다고 응답을 보내야 한다.
 * 중복이 안된다면 INSERT문으로 DB에 저장하고 회원가입에 성공했다는 응답을 보낸다.
 * `INSERT INTO HAIRCLIENT VALUES("CID", "CPASSWORD", "CNAME", "CPHONENM", GRADE)`
 */
router.post('/makeaccount', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        if (myRequest.length !== 0) { //예외처리 확인
            const duplicate = `INSERT INTO HAIRCLIENT VALUES('`
                + myRequest.CID + `','`
                + myRequest.CPassword + `','`
                + myRequest.CName + `','`
                + myRequest.CPhoneNum + `',0)`;
            conn.query(duplicate, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, 0], (err, row) => {
                console.log(req.body);
                if (err) {
                    console.log("이런... 오류가 있어요");
                    res.send(myRequest);
                }
                else if (row.length !== 0) {
                    console.log("중복이 안되네? 회원가입 가능해!");
                }
                else if (row.length === 0) {
                    console.log("앗... 또 다른 오류가 있어요");
                }
            })
        }
    });
});

/**
 * 미용사 회원가입 처리
 * 미용사는 Grade값이 1이므로 마지막 파라미터에는 1이 들어와야 함
 * 미용사가 회원가입 시, CID, CPassword, CName, CPhoneNum, Grade값이 DB에 등록된다.
 * 미용사는 미용사 정보가 따로 있으므로 query문을 한번 더 쓴다.
 * 제대로 들어오는지 확인하기 위해서는 length !== 0으로 확인을 한다.
 * 
 */
router.post('/makehairdresser', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err)
            console.log("으흠! 문제가 있어요!");
        else if (myRequest.length !== 0) { //예외처리 확인
            const duplicate = `INSERT INTO HAIRCLIENT VALUES('`
                + myRequest.CID + `','`
                + myRequest.CPassword + `','`
                + myRequest.CName + `','`
                + myRequest.CPhoneNum + `', 1)`;

            const insertHairInfo = `INSERT INTO Hairdresserinfo values('` + myRequest.license + `','` + myRequest.CID + `')`;
            conn.query(duplicate, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, 1], (err, row) => {
                console.log(req.body);
                if (err)
                    console.log("makeHairdresser : 앗! 오류가 있어요 err");
                else if (row.length !== 0) {
                    console.log("");
                    res.send(myRequest);
                }
                else if (row.length === 0){
                    console.log("makeHairdresser : 앗! 오류가 있어요! row.length")
                    //res.send(myRequest);
                }
                else{
                    console.log("makeHairdresser : 중복이 안되네? 회원가입 가능해!");
                    
                }
            });
            conn.query(insertHairInfo, [myRequest.license, myRequest.CID], (err, row) => {
                if (err)
                    console.log("앗! 오류가 있어요 err");
                else if (row.length !== 0) {
                    console.log('Result : ' + myRequest.license + '   ' + myRequest.CID);

                }
                else { }
            });
        }

    })
})



/**
 * ID 찾기를 누를 시, req에서 넘겨 들어온CName, CPhoneNum값과 DB에 있는 CName, CPhoneNum을 비교한다.
 * 2개의 값이 모두 일치하면 ID값을 받아오는 query문을 넣어 다시 결과값에 있는 ID
 */
router.post('/findid', (req, res) => {
    res.writeHead(200, { 'Content-Type': 'text/html;charset=utf-8' });
    const myRequest = req.body;

    console.log("사람 이름이 안나와 : " + req.body.CName);
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const duplicate = `SELECT CID, CName, CPhoneNum FROM HairClient WHERE (CName = ?) AND (CPhoneNum = ?)`;
        conn.query(duplicate, [myRequest.CName, myRequest.CPhoneNum], (err, row) => {
            if (err) throw err;
            if (row.length !== 0) {
                console.log("중복이 있네? 이러면 회원가입 못해!");
            }
            else {
                console.log("중복이 안되네? 회원가입 가능해!");
                res.send(myRequest);
            }
        })
    });
});

module.exports = router;

var express = require('express');
var router = express.Router();
var pool = require('../config/dbConfig');

var cookie = {
    CID: "",
    CPassword: ""
};

/**
 * Table Info
 * 
 * 1.HairClient
 * 2.ClientClass
 * 3.HairDresserInfo
 * 4.BusinessInfo
 * 5.HairRoom
 * 
 * 
 */
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
            if (row.length !== 0) { //로그인 성공
                cookie.CID = row.CID;
                cookie.CPassword = row.CPassword;
                result["success"] = 1;
                result["Grade"] = row[0].Grade;
                res.send(result);
            } else { //로그인 실패
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
        const reqCondition = ((myRequest.CID !== undefined) && (myRequest.CPassword !== undefined) && (myRequest.CName !== undefined) && (myRequest.CPhoneNum !== undefined));
        if (reqCondition) { //값을 제대로 입력하였을 때
            /**
             * 값을 제대로 입력하였으면 IF문 내부가 실행이 된다. 당연하지 
             */
            const checkAccount = `SELECT * FROM HairClient WHERE CID = '` + myRequest.CID + `'`; //SELECT 문으로 CID 무결성 검사
            conn.query(checkAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum], (err, row) => {
                console.log(row);
                if (row.length !== 0)
                    res.send({ "result": "ID가 중복됩니다." })
                else {
                    console.log('회원가입 가능');
                    const makeAccount = `INSERT INTO HAIRCLIENT VALUES('`
                        + myRequest.CID + `','`
                        + myRequest.CPassword + `','`
                        + myRequest.CName + `','`
                        + myRequest.CPhoneNum + `',0)`;
                    conn.query(makeAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, 0], (err, row) => {
                        if (err) throw err;
                        console.log('일반손님 회원가입 완료!');
                    });
                    res.send({ "result": "회원가입 성공" });
                }
            })
        }
        else {
            console.log("이런! 실패했어요 makeAccount");
        }
    });
});

/**
 * 미용사 회원가입 처리
 * 미용사는 Grade값이 1이므로 마지막 파라미터에는 1이 들어와야 함
 * 미용사가 회원가입 시, CID, CPassword, CName, CPhoneNum, Grade값이 DB에 등록된다.
 * 미용사는 미용사 정보가 따로 있으므로 query문을 한번 더 쓴다.
 * 제대로 들어오는지 확인하기 위해서는 length !== 0으로 확인을 한다.
 */
router.post('/makehairdresser', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const reqCondition = ((myRequest.CID !== undefined) && (myRequest.CPassword !== undefined) && (myRequest.CName !== undefined) && (myRequest.CPhoneNum !== undefined) && (myRequest.license !== undefined));
        if (reqCondition) { //값을 제대로 입력하였을 때
            /**
             * 값을 제대로 입력하였으면 IF문 내부가 실행이 된다. 당연하지 
             */
            const checkAccount = `SELECT * FROM HairClient WHERE CID = '` + myRequest.CID + `'`; //SELECT 문으로 CID 무결성 검사
            conn.query(checkAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum], (err, row) => {
                //console.log(row);
                if (row.length !== 0)
                    res.send({ "result": "ID가 중복됩니다." })
                else {
                    console.log('회원가입 가능');
                    console.log(row);
                    //회원 정보 입력
                    const makeAccount = `INSERT INTO HAIRCLIENT VALUES('`
                        + myRequest.CID + `','`
                        + myRequest.CPassword + `','`
                        + myRequest.CName + `','`
                        + myRequest.CPhoneNum + `', 1)`;
                    conn.query(makeAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, 1], (err, row) => {
                        if (err) throw err;
                        console.log('미용사 회원가입 완료!');
                    });
                    //미용사정보 (자격증번호, CID) 입력
                    const makeHairDresserInfo = `INSERT INTO HAIRDresserInfo VALUES('`
                        + myRequest.license + `', '`
                        + myRequest.CID + `')`;
                    conn.query(makeHairDresserInfo, [myRequest.license, myRequest.CID], (err, row) => {
                        if (err) throw err;
                        console.log('미용사 회원가입 완료!');
                    })
                    res.send({ "result": "회원가입 성공" });
                }
            })
        }
        else {
            console.log("이런! 실패했어요 makeHairDresser");
        }
    });
})

router.post('/makemanager', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const reqCondition = ((myRequest.CID !== undefined) && (myRequest.CPassword !== undefined) && (myRequest.CName !== undefined) && (myRequest.CPhoneNum !== undefined) && (myRequest.mBusinessNum !== undefined));
        if (reqCondition) {//값을 제대로 입력 했을 때
            /**
             * 값을 제대로 입력했으면 이제부터 데이터 삽입을 해야지?
             */
            const checkAccount = `SELECT * FROM HairClient WHERE CID = '` + myRequest.CID + `'`;
            conn.query(checkAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, myRequest.mBusinessNum, myRequest.license, 2], (err, row) => {
                if (row.length !== 0)
                    res.send({ "result": "ID가 중복됩니다." })
                else {
                    console.log('회원가입 가능');
                    console.log(row);
                    //회원 정보 입력
                    const makeAccount = `INSERT INTO HAIRCLIENT VALUES('`
                        + myRequest.CID + `','`
                        + myRequest.CPassword + `','`
                        + myRequest.CName + `','`
                        + myRequest.CPhoneNum + `', 2)`;
                    conn.query(makeAccount, [myRequest.CID, myRequest.CPassword, myRequest.CName, myRequest.CPhoneNum, 1], (err, row) => {
                        if (err) throw err;
                        console.log('사업자 회원가입 완료!');
                    });
                    const makeManagerInfo = `INSERT INTO businessinfo VALUES('`
                        + myRequest.mBusinessNum + `', '`
                        + myRequest.CID + `', '`
                        + myRequest.license + `')`;
                    //사업자 번호 입력
                    conn.query(makeManagerInfo, [myRequest.mBusinessNum, myRequest.CID, myRequest.license], (err, row) => {
                        if (err) throw err;
                    });
                    res.send({ "result": "회원가입 성공" });
                }
            });
        }
        else {
            console.log("이런! 실패했어요 makeManager");
        }
    });
});


/**
 * 1. 받아온 CName, CPhoneNum값을 SELECT로 조회 시도
 * 2. 결과가 나오면 CID 값을 response를 통해 클라로 던져준다.
 */
router.post('/findid', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const reqCondition = ((myRequest.CName !== undefined) && (myRequest.CPhoneNum !== undefined));
        if (reqCondition) { //값을 제대로 입력했을 때
            const findID = `SELECT * FROM HairClient WHERE CName = ? AND CPhoneNum = ?`;
            conn.query(findID, [myRequest.CName, myRequest.CPhoneNum], (err, row) => {
                if (row.length !== 0) {
                    /**
                     * ID가 존재한다는 뜻
                     */
                    console.log('ID 찾기에 성공했습니다.');
                    console.log(row[0].CID);
                    res.send({ "CID": row[0].CID });
                }
                else {
                    console.log('ID 찾기에 실패했습니다.');

                }
            })
        }
        else {
            console.log("이런! 실패했어요 findid");
        }
    });
});

/**
 * 1. 받아온 CID, CName, CPhoneNum값을 SELECT로 조회
 * 2. 결과가 나오면 PW 값을 response를 통해 클라로 던져준다. (이후 재설정 방식으로 변경해야 함)
 */
router.post('/findPW', (req, res) => {
    var myRequest = req.body;
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const reqCondition = ((myRequest.CID !== undefined) && (myRequest.CName !== undefined) && (myRequest.CPhoneNum !== undefined));
        if (reqCondition) { //값을 제대로 입력했다면
            const findPW = `SELECT * FROM HairClient WHERE CID = ? AND CName = ? AND CPhoneNum = ?`
            conn.query(findPW, [myRequest.CID, myRequest.CName, myRequest.CPhoneNum], (err, row) => {
                if (row.length !== 0) {
                    console.log('PW 찾기에 성공했습니다.');
                    res.send({ "CPassword": row[0].CPassword });
                }
                else {
                    console.log("실패했습니다.");
                }
            });
        }
        else {
            console.log("이런! 실패했어요 findPW");
        }
    })
});

/**
 * 미용실 정보를 DB에 삽입한다.
 * 이미 미용실 정보가 삽입되어 있다면 INSERT가 실행되어선 안된다
 * 
 * 완료 버튼을 눌렀을 때
 * 1. DB에서 SELECT문을 이용하여 해당 CID를 참조하여 미용실 정보를 조회한다.
 * 2. 미용실 정보가 없으면 INSERT INTO 문을 실행한다.
 * 3. 미용실 정보가 있으면 ALTER문을 실행하여 정보를 변경한다.
 */
router.post('/setHairRoomInfo', (req, res) => {
    var myRequest = req.body;
    console.log('======setHairRoomInfo========');
    console.log(myRequest);
    console.log('=============================');
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const reqCondition = ((myRequest.hairRoomName !== undefined) && (myRequest.hairRoomCallNum !== undefined) && (myRequest.hairRoomAddress !== undefined) && (myRequest.dresserNum !== undefined) && (myRequest.CID !== undefined));
        console.log('reqCondition : ' + reqCondition);
        if (reqCondition) {
            console.log('등록이 가능합니다. DB에서 데이터 유무를 확인합니다.');
            const checkHairRoomInfo = `SELECT * FROM HairRoom WHERE CID = '` + myRequest.CID + `'`;
            conn.query(checkHairRoomInfo, [myRequest.hairRoomName, myRequest.hairRoomCallNum, myRequest.hairRoomAddress, myRequest.dresserNum, myRequest.CID], (err, row) => {
                console.log('length : ' + row.length);
                if (err) console.log("Error! Disconnect");
                else if (row.length !== 0) {
                    //이미 데이터가 있기 때문에 변경을 해야 한다.
                    console.log(row);
                    console.log('row.length : ' + row.length);
                    console.log("앗! 삽입 못합니다. 혹시 변경을 해야 하지 않아요?");
                    res.send({ "result": "변경이 필요합니다" });
                    const updateHairRoomName = `
                        UPDATE HairRoom 
                        SET HairRoomName = '` + myRequest.hairRoomName + `'
                        WHERE CID = '` + myRequest.CID + `'`;
                    conn.query(updateHairRoomName, [myRequest.CID], (err, row)=>{
                        if(err) throw err;
                        console.log("HairRoomName 변경 완료");
                    });
                    const updateHairRoomCallNum = `
                        UPDATE HairRoom 
                        SET HairRoomCallNum = '` + myRequest.hairRoomCallNum + `'
                        WHERE CID = '` + myRequest.CID + `'`;
                    conn.query(updateHairRoomCallNum, [myRequest.CID], (err, row)=>{
                        if(err) throw err;
                        console.log("HairRoomCallNum 변경 완료");
                    });
                    const updateHairRoomAddress = `
                        UPDATE HairRoom 
                        SET HairRoomAddress = '` + myRequest.hairRoomAddress + `'
                        WHERE CID = '` + myRequest.CID + `'`;
                    conn.query(updateHairRoomAddress, [myRequest.CID], (err, row)=>{
                        if(err) throw err;
                        console.log("HairRoomAddress 변경 완료");
                    });
                    
                    const updateHairRoomDresserNum = `
                        UPDATE HairRoom 
                        SET DresserNum = ` + myRequest.dresserNum + `
                        WHERE CID = '` + myRequest.CID + `'`;
                    conn.query(updateHairRoomDresserNum, [myRequest.CID], (err, row)=>{
                        if(err) throw err;
                        console.log("DresserNum 변경 완료");
                    });
                }
                else {
                    //최초 설정 시, DB에 미용실 정보가 삽입된다.
                    console.log("아무것두 안나오면 데이터를 삽입!");
                    console.log(row);
                    const InsertHairRoomInfo = `INSERT INTO HairRoom values('`
                        + myRequest.hairRoomName + `','`
                        + myRequest.hairRoomCallNum + `','`
                        + myRequest.hairRoomAddress + `','`
                        + myRequest.dresserNum + `','`
                        + myRequest.CID + `')`;

                    conn.query(InsertHairRoomInfo, [myRequest.hairRoomName, myRequest.hairRoomCallNum, myRequest.hairRoomAddress, myRequest.dresserNum, myRequest.CID], (err, row) => {
                        if (err) throw err;
                        else {
                            res.send({ "result": "삽입 완료!" });
                            console.log('HairRoom 삽입 완료!');
                        }
                    })
                }
            })
        } else {
            console.log("이런 실패했어요! setHairRoomInfo");
        }
    });
})


/**
 * 사업장 설정에 들어가는 순간 미용실 데이터를 즉시 가져와서 클라이언트에 뿌려준다.
 */
router.post('/showHairRoomInfo', (req, res) => {
    var myRequest = req.body;
    console.log(myRequest);
    pool.getConnection((err, conn) => {
        if (err) throw err;
        const checkHairRoomInfo = `SELECT * FROM HairRoom WHERE CID = '` + myRequest.CID + `'`;
        conn.query(checkHairRoomInfo, [myRequest.CID], (err, row) => {
            if (err) console.log("문제가 발생했어요!");
            else if (row.length !== 0) {
                // DB에 데이터가 있다는 뜻
                console.log(row);
                res.send(row);
            }
            else {
                // DB에 데이터가 없다는 뜻
                res.send(row);
                console.log(row);
            }

        })
    });
})

module.exports = router;

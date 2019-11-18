
var createError = require('http-errors');
var express = require('express');
var path = require('path');
var cookieParser = require('cookie-parser');
var logger = require('morgan');
var bodyParser = require('body-parser');
//Cross Origin
var cors = require('cors');

//라우터 불러오기
var indexRouter = require('./routes/index');
var usersRouter = require('./routes/users');
var postsRouter = require('./routes/posts');

var app = express();

// view engine setup
app.set('views', path.join(__dirname, 'views'));
app.set('view engine', 'ejs');

app.use(express.static(path.join(__dirname, 'public')));
app.use(cors());
app.use(logger('dev'));
app.use(express.json());
app.use(express.urlencoded({ extended: true }));
//app.use(bodyParser.text({defaultCharset : 'utf-8'}));
app.use(bodyParser.json());
app.use(cookieParser());


// 라우터 등록(미들웨어 등록)
app.use('/', indexRouter);
app.use('/users', usersRouter);
app.use('/posts', postsRouter);

// 404 잡아줌 (NOT FOUND)
app.use(function(req, res, next) {
  next(createError(404));
});

// 에러 표출
app.use(function(err, req, res, next) {
  // 오류 정보가 담김
  res.locals.message = err.message;
  res.locals.error = req.app.get('env') === 'development' ? err : {};

  // 오류 페이지 표시
  res.status(err.status || 500);
  res.render('error');
});

module.exports = app;

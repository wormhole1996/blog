<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1">
    <link rel="stylesheet" href="/static/custom/css/article.css"/>
    <link rel="stylesheet" href="/static/editor.md/css/editormd.min.css"/>
    <link rel="stylesheet" href="/static/editor.md/css/editormd.preview.min.css"/>
    <link rel="stylesheet" href="/static/bootstrap/css/bootstrap.min.css">
    <script src="/static/jquery/jquery.min.js"></script>
    <script src="/static/bootstrap/js/bootstrap.min.js"></script>
    <script src="/static/editor.md/lib/marked.min.js"></script>
    <script src="/static/editor.md/lib/prettify.min.js"></script>
    <script src="/static/editor.md/editormd.min.js"></script>
    <script src="/static/knockout/knockout-3.4.2.js"></script>
    <script src="/static/custom/js/article.js"></script>
    <title>溢栈</title>
</head>
<body>
<div class="container">
    <div class="row">
        <div class="col-md-3">
            <div class="side" data-bind="with:sideInfo">
                <img src="" id="head" class="rounded-circle" data-bind="attr:{src:headUrl}">
                <div class="nickname" id="nickname" data-bind="text:nickname"></div>
                <hr/>
                <div class="signature" id="signature" data-bind="text:signature"></div>
                <div class="menu">
                    <a class="item select btn" href="/">
                        首页
                    </a>
                    <a class="item btn">
                        归档
                    </a>
                    <a class="item btn">
                        分类
                    </a>
                    <a class="item btn">
                        与我聊聊
                    </a>
                    <a class="item btn">
                        留言板
                    </a>
                    <a class="item btn">
                        友情链接
                    </a>
                </div>
            </div>
        </div>
        <div class="col-md-9">
            <div class="body">
                <div class="article" data-bind="with:article">
                    <div class="title" data-bind="text:title"></div>
                    <div class="info">
                        <div class="row">
                            <div class="col-sm-4">
                                <span id="date" data-bind="text:date"></span>
                            </div>
                            <div class="col-sm-4">
                                作者：<span id="author" data-bind="text:nickname"></span>
                            </div>
                            <div class="col-sm-4">
                                分类：<span id="category" data-bind="text:categoryName"></span>
                            </div>
                        </div>
                    </div>
                    <div id="editormd-view" class="content">
                    </div>
                </div>
            </div>
            <div class="padding">
            </div>
        </div>
    </div>
</div>
<footer>copyright &copy; 2018 by 溢栈</footer>
</body>
</html>
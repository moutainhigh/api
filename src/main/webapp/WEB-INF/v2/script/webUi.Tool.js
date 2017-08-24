var Tools = function (argument) {
    this.argument = argument;
};
// 图片预先加载
var img = new Image();
img.src="../image/loading.png";
Tools.prototype = {
    option: {
        body: '',
        child: '',
        alertChild: ''
    },
    // 获取设备详情
    device: function () {
        var doc = window.document,
            Isdevice = [],
            ua = window.navigator && window.navigator.userAgent || '';
        var ipad = !!ua.match(/(iPad).*OS\s([\d_]+)/),
            ipod = !!ua.match(/(iPod)(.*OS\s([\d_]+))?/),
            iphone = !ipad && !!ua.match(/(iPhone\sOS)\s([\d_]+)/);

        Isdevice.device = {
            /**
             * 是否移动终端
             * @return {Boolean}
             */
            isMobile: !!ua.match(/AppleWebKit.*Mobile.*/) || 'ontouchstart' in doc.documentElement,
            /**
             * 是否IOS终端
             * @returns {boolean}
             */
            isIOS: !!ua.match(/\(i[^;]+;( U;)? CPU.+Mac OS X/),
            /**
             * 是否Android终端
             * @returns {boolean}
             */
            isAndroid: !!ua.match(/(Android);?[\s\/]+([\d.]+)?/),
            /**
             * 是否ipad终端
             * @returns {boolean}
             */
            isIpad: ipad,
            /**
             * 是否ipod终端
             * @returns {boolean}
             */
            isIpod: ipod,
            /**
             * 是否iphone终端
             * @returns {boolean}
             */
            isIphone: iphone,
            /**
             * 是否webview
             * @returns {boolean}
             */
            isWebView: (iphone || ipad || ipod) && !!ua.match(/.*AppleWebKit(?!.*Safari)/i),
            /**
             * 是否微信端
             * @returns {boolean}
             */
            isWeixin: ua.indexOf('MicroMessenger') > -1,
            /**
             * 是否火狐浏览器
             */
            isMozilla: /firefox/.test(navigator.userAgent.toLowerCase()),
            /**
             * 设备像素比
             */
            pixelRatio: window.devicePixelRatio || 1
        }
        return Isdevice
    },
    // 获取url传值
    getURLParams: function (name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)", "i");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) {
            return unescape(r[2]);
        }
        return null;
    },
    // YDUI 的REm布局 ui图量的PX/100 = rem 比如100/100 = 1rem;
    RemYdui: function () {
        /* 设计图文档宽度 */
        var docWidth = 750;

        var doc = window.document,
            docEl = doc.documentElement,
            resizeEvt = 'orientationchange' in window ? 'orientationchange' : 'resize';

        var recalc = (function refreshRem() {
            var clientWidth = docEl.getBoundingClientRect().width;

            /* 8.55：小于320px不再缩小，11.2：大于420px不再放大 */
            docEl.style.fontSize = Math.max(Math.min(20 * (clientWidth / docWidth), 11.2), 8.55) * 5 + 'px';

            return refreshRem;
        })();

        /* 添加倍屏标识，安卓倍屏为1 */
        docEl.setAttribute('data-dpr', window.navigator.appVersion.match(/iphone/gi) ? window.devicePixelRatio : 1);

        if (/iP(hone|od|ad)/.test(window.navigator.userAgent)) {
            /* 添加IOS标识 */
            doc.documentElement.classList.add('ios');
            /* IOS8以上给html添加hairline样式，以便特殊处理 */
            if (parseInt(window.navigator.appVersion.match(/OS (\d+)_(\d+)_?(\d+)?/)[1], 10) >= 8)
                doc.documentElement.classList.add('hairline');
        }

        if (!doc.addEventListener) return;
        window.addEventListener(resizeEvt, recalc, false);
        doc.addEventListener('DOMContentLoaded', recalc, false);
    },
    // 正在加载的弹窗
    loading: function (options) {
        if (this.option.child === '') {
            // es5 拼接方法
            // 	   var $dom = '<div class="Mloadings">'+
            // '<img class="MloadingImg" src="../images/loading.png"/>'
            // +'<p class="MCon">' +options.containr+'</p>'
            // +'</div>';
            // es6拼接方法
            var $dom = `<div class="Mloadings">
        <img class="MloadingImg" src="../image/loading.png"/>
        <p class="MCon">${options.containr}</p>
      </div>`
            var body = document.getElementsByTagName('body')[0];
            var div = document.createElement('div');
            div.innerHTML = $dom;
            div.className = 'MloadingBg';
            body.appendChild(div);
            if (options.timer) {
                setTimeout(function () {
                    body.removeChild(div);
                }, options.timer)
            }
            return this.option.body = body, this.option.child = div;
        }
    },
    // 关闭正在加载的弹窗
    closeLoading: function () {
        document.getElementsByTagName('body')[0].removeChild(this.option.child);
        return this.option.body = '', this.option.child = '';
    },
    alert: function (options) {
        if (this.option.alertChild === '') {
            var $dom = '<div class="m-alert ani">'
                + '<div class="alert-say">' + options.containr + '</div>'
                + '<div class="alert-button" id="sub">' + '确定' + '</div>';
            var body = document.getElementsByTagName('body')[0];
            var div = document.createElement('div');
            div.innerHTML = $dom;
            div.className = "alertBg";
            body.appendChild(div);
            var sub = document.getElementById('sub');
            sub.onclick = this.closeAlert;
            return this.option.alertChild = div;
        }
        else {
            return
        }
    },
    closeAlert: function () {
        document.getElementsByTagName('body')[0].removeChild(NewTool.option.alertChild);
        return NewTool.option.alertChild = '';
    },
    buttonAlert: function (options) {
        var $dom = `<div class="m-alert ani">
	  	<div class="alert-say">${options.containr}</div>
	  	<div class="alert-button">
        <div class="subButton" id="baSub" >确定</div>
        <div class="resButton" id="baRes">取消</div>
	  	</div>`;
        var body = document.getElementsByTagName('body')[0];
        var div = document.createElement('div');
        div.innerHTML = $dom;
        div.className = "alertBg";
        body.appendChild(div);
        var baSub = document.getElementById('baSub');
        var baRes = document.getElementById('baRes');
        if (options.sub) {
            baSub.onclick = options.sub;
        }
        if (options.res) {
            baRes.onclick = options.res;
        }
        else {
            baRes.onclick = function () {
                body.removeChild(div);
            }
        }


    },
    timeALL: function (options) {
        var timestamp = options.timestamp;
        var d = new Date(timestamp * 1000);    //根据时间戳生成的时间对象
        var year = d.getFullYear();
        var Month = d.getMonth() + 1;
        if (Month.toString().length < 2) {
            Month = '0' + Month;
        }
        var datas = d.getDate();
        if (datas.toString().length < 2) {
            datas = '0' + datas;
        }
        var hour = d.getHours();
        if (hour.toString().length < 2) {
            hour = '0' + hour;
        }
        var minute = d.getMinutes();
        if (minute.toString().length < 2) {
            minute = '0' + minute;
        }
        var times = year + '-' + Month + '-' + datas + ' ' + hour + ':' + minute;
        return times;
    },
    timeData: function (options) {
        var timestamp = options.timestamp;
        var d = new Date(timestamp * 1000);    //根据时间戳生成的时间对象
        var year = d.getFullYear();
        var Month = d.getMonth() + 1;
        if (Month.toString().length < 2) {
            Month = '0' + Month;
        }
        var datas = d.getDate();
        if (datas.toString().length < 2) {
            datas = '0' + datas;
        }
        var hour = d.getHours();
        if (hour.toString().length < 2) {
            hour = '0' + hour;
        }
        var minute = d.getMinutes();
        if (minute.toString().length < 2) {
            minute = '0' + minute;
        }
        var years = year + '-' + Month + '-' + datas;
        return years;
    },
    timehour: function (options) {
        var timestamp = options.timestamp;
        var d = new Date(timestamp * 1000);    //根据时间戳生成的时间对象
        var year = d.getFullYear();
        var Month = d.getMonth() + 1;
        if (Month.toString().length < 2) {
            Month = '0' + Month;
        }
        var datas = d.getDate();
        if (datas.toString().length < 2) {
            datas = '0' + datas;
        }
        var hour = d.getHours();
        if (hour.toString().length < 2) {
            hour = '0' + hour;
        }
        var minute = d.getMinutes();
        if (minute.toString().length < 2) {
            minute = '0' + minute;
        }
        var hours = hour + ':' + minute;
        return hours;
    }
}
var NewTool = new Tools();
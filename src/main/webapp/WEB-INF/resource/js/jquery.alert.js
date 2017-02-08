/**
 * Created by hctym on 2017/2/7.
 * 实例：
 * jalert.show("弹出框");
 */
;(function($,window){

     var jQ_alert = {
          bg:$("<div>").css({
              'position': 'fixed',
              'background': '#000',
              'opacity': '.5',
              'color':'#FFF',
              'text-align': 'center',
              'vertical-align': 'middle',
              'z-index':'10000',
              'font-size':'16px',
              'display':'none'
          }).attr("id","wwt-dialog"),
         bgmc:$("<div>").css({
             'height:':'100%',
             'padding':'12px 40px'
         }),
         content:$("<span>"),
         init:function(){
              if(!$("#wwt-dialog")[0]){
                     $(document.body).append(this.bg.append(this.bgmc.append(this.content)));
              }
             return this;
         },
         show:function(msg){
             this.init().content.text(msg);
             var top = ($(window).height()-this.bg.height())/2-20,
                 left = ($(window).width()-this.bg.width())/2;
             this.bg.css({
                 'top':top,
                 'left':left
             }).fadeIn();
             /*this.bg.fadeIn().animate({
                 'top':top,
                 'left':left
             });*/
             var that = this;
             var time = setTimeout(function(){
                 that.bg.fadeOut();
                 /*that.bg.fadeOut().animate({
                     'top':Math.random()*1000,
                     'left':Math.random()*1000
                 })*/
                 clearTimeout(time);
             },1000)
         }
     };
     window.jalert = jQ_alert;
})($,window);

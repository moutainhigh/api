/**
 * Created by hctym on 2017/2/22.
 */
;(function($,window){
    var jQ_select = {
         main:$("<div>").css({
             'display':'none'
         }).attr("id","wwt-select-main"),
        shape:$("<div>").css({
            'position': 'fixed',
            'top': '0px',
            'left': '0px',
            'width': '100%',
            'height': '100%',
            'background': '#000',
            'z-index': '888',
            'opacity': '0.2'
        }).attr("class","wwt-select-shape"),
        core:{
           bg:$("<div>").css({
               'position': 'fixed',
               'width': '100%',
               'bottom':'-200px',
               'left':'0',
               'right':'0',
               'background': '#FFF',
               'box-shadow': '0 0 8px rgba(0,0,0,.3)',
               'z-index':'100000',
               'font-size':'.8em',
               '-webkit-transition':'bottom 0.5s',
               'transition':'bottom 0.5s '
           }),
           content:$("<div>").css({
               'text-align': 'center',
               'margin':'0 auto',
               'overflow-y':' scroll',
               'overflow-x': 'hidden',
               'max-height':'200px'
           }),
            ul:$("<ul>").css({
                'list-style':' none',
                'padding':'0 20px'
            }).attr("id","wwt-select-ul")
        },
        init:function(){//初始化
          if(!$("#wwt-select-main")[0]){
              $(document.body).append(this.main
                  .append(this.shape)
                  .append(this.core.bg.append(this.core.content.append(this.core.ul)))
              );
              var that = this;
              this.shape.on("click",function(e){
            	  that.hide();
              });
          }
          this.core.ul.empty();//清空li
          return this;
        },
        add:function(obj){//添加内容 obj={msg:'',id:''};
        	var that = this;
        	var listyle;
        	var select = this.operateObj.defaultsel;
        	if(select == obj.id){
        		listyle = {
        				'padding':'8px 0',
                        'border-bottom':'1px solid #EEE',
                        'color':'#fc324a'	
        		};
        	}else{
        		listyle = {
        				'padding':'8px 0',
                        'border-bottom':'1px solid #EEE'
        		};
        	}
            this.core.ul.append($("<li>").css(listyle).append($("<span>").text(obj.msg)).attr("data-id",obj.id).on("click",function(){
            	$(that.operateObj.curObj).text($(this).text()).attr("data-id",$(this).attr("data-id"));
            	if(obj.exec){
            		obj.exec();
            	}
            	that.hide();
            }));
            return this;
        },
        operateObj:{//需要操作的元素
        	curObj:'',
        	defaultsel:''//选中的元素
        },//当前操作对象
        show:function(){//展示效果
            this.main.show();
            var that = this;
            	that.core.bg.css({
                    'bottom':'0'
                });	
        },
        hide:function(){//隐藏效果
        	var that = this;
        	that.core.bg.css({
                'bottom':'-200px'
            });
        	var t = setTimeout(function(){
        		 that.main.hide();
        		 clearTimeout(t);
        	},300);
        }
    };

    window.jselect = jQ_select;//暴露
})($,window);
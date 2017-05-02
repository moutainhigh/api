package com.zhsj.api.tag;


import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;

import org.springframework.beans.factory.annotation.Autowired;

public class HasUrlPermissionTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private String link;
	
	@Autowired
	public int doStartTag() throws JspException {       
	            // 输出标签中的内容  
	            return EVAL_BODY_INCLUDE;  
	  
	        // 跳过标签中的内容  
//	        return SKIP_BODY;    
	   }    

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}
    
}

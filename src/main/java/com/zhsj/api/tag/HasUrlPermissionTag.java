package com.zhsj.api.tag;

import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.TagSupport;
import org.springframework.beans.factory.annotation.Autowired;
import com.zhsj.api.service.PermissionService;
import com.zhsj.api.util.SpringBeanUtil;

public class HasUrlPermissionTag extends TagSupport {
	private static final long serialVersionUID = 1L;

	private String link;
	private String auth;
	
	
	@Autowired
	public int doStartTag() throws JspException {  
		PermissionService permissionService = (PermissionService) SpringBeanUtil.getBean("permissionService");
		boolean flag = permissionService.hasPermission(this.getLink(),this.getAuth());
		if(flag){
			// 输出标签中的内容  
            return EVAL_BODY_INCLUDE;  
		}else {
			 // 跳过标签中的内容  
	        return SKIP_BODY;  
		}
	}


	public String getLink() {
		return link;
	}
	public void setLink(String link) {
		this.link = link;
	}
	public String getAuth() {
		return auth;
	}
	public void setAuth(String auth) {
		this.auth = auth;
	}    
	
	
    
}

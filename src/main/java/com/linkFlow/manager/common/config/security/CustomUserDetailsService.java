package com.linkFlow.manager.common.config.security;

import com.linkFlow.manager.common.config.ConfigDataManager;
import com.linkFlow.manager.common.model.ReturnCode;
import com.linkFlow.manager.common.model.define.OperatorState;
import com.linkFlow.manager.common.model.define.OperatorType;
import com.linkFlow.manager.common.model.vo.OperatorVO;
import com.linkFlow.manager.common.service.OperatorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CustomUserDetailsService implements UserDetailsService
{
	@Autowired
    BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private OperatorService operatorService;
    @Autowired
    private ConfigDataManager configDataManager;

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
    	CustomUserDetails userDetails = new CustomUserDetails();
    	try
    	{
    		List<GrantedAuthority> authorities = new ArrayList<>();

			OperatorVO user = operatorService.selectById(username);
			if(user == null)
			{
				logger.error("invalid login request : " + username);
				throw new DisabledException("Join a non- identity.");
			}

			if(user.getOpState() == OperatorState.BLOCK)
			{
				logger.error("blocked.  account: " + username);
				throw new DisabledException(ReturnCode.ACCOUNT_BLOCKED.getMessage());
			}

			if (user.getOpType().equals(OperatorType.ADMIN))
				authorities.add(new SimpleGrantedAuthority(SecurityRoleDef.RL_SYSTEM_ADMIN));
			else if (user.getOpType().equals(OperatorType.MASTER))
				authorities.add(new SimpleGrantedAuthority(SecurityRoleDef.RL_MASTER));

    		userDetails.setUsername(username);
    		userDetails.setPassword(user.getOpPwd());
    		userDetails.setAuthorities(authorities);
			userDetails.setUserObject(user);
		}
    	catch (Exception e)
    	{
    		logger.error(e.toString());
    		throw e;
		}
    	return userDetails;
    }
}
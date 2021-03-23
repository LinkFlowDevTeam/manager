package com.linkFlow.manager.common.config.security;

import com.linkFlow.manager.common.model.vo.OperatorVO;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class CustomUserDetails implements UserDetails
{
	private static final long serialVersionUID = 1L;
	private String username;
    private String password;
    private List<GrantedAuthority> authorities;
    private String apiPassword;

    private OperatorVO userObject;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities()
    {
        return authorities;
    }

    public void setAuthorities(List<GrantedAuthority> authorities)
    {
        this.authorities = authorities;
    }

    @Override
    public String getPassword()
    {
        return password;
    }

    @Override
    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }

    public String getApiPassword()
    {
        return apiPassword;
    }

    public void setApiPassword(String apiPassword)
    {
        this.apiPassword = apiPassword;
    }

    @Override
    public boolean isAccountNonExpired()
    {
        return true;
    }

    @Override
    public boolean isAccountNonLocked()
    {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired()
    {
        return true;
    }

    @Override
    public boolean isEnabled()
    {
        return true;
    }

    public OperatorVO getUserObject()
    {
        return userObject;
    }

    public void setUserObject(OperatorVO userObject)
    {
        this.userObject = userObject;
    }
}

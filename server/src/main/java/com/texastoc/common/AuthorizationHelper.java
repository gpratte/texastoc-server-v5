package com.texastoc.common;

import com.texastoc.module.player.model.Role;
import org.springframework.stereotype.Component;

@Component
public class AuthorizationHelper {

  public boolean isLoggedInUserHaveRole(Role.Type roleType) {
    //;;!!
//    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//    Collection<? extends GrantedAuthority> grantedAuthorities = authentication.getAuthorities();
//    for (GrantedAuthority grantedAuthority : grantedAuthorities) {
//      if (("ROLE_" + roleType.name()).equals(grantedAuthority.toString())) {
//        return true;
//      }
//    }

    return false;
  }

  public String getLoggedInUserEmail() {
    //;;!!
//    Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
//    if (principal instanceof UserDetails) {
//      return ((UserDetails) principal).getUsername();
//    }
    return null;
  }

}

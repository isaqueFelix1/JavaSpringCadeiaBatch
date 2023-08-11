package br.com.ibm.cadeiabatch.auth;

import br.com.ibm.cadeiabatch.entity.Empresa;
import br.com.ibm.cadeiabatch.entity.Usuario;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
public class MyUserPrincipal implements UserDetails {

    private static final long serialVersionUID = -8489053074208206273L;

    private Usuario user;

    public MyUserPrincipal(Usuario user){
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public String getPassword() {
        return user.getSenha();
    }

    @Override
    public String getUsername() {
        return user.getUsuario();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public List<Empresa> getEmpresas(){
        return user.getEmpresas();
    }
}

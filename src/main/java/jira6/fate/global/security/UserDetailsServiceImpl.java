package jira6.fate.global.security;

import jira6.fate.domain.user.entity.User;
import jira6.fate.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userRepository.findByUserName(username).orElseThrow( () -> new UsernameNotFoundException("아이디, 비밀번호를 확인해주세요"));

        return new UserDetailsImpl(user);
    }

}

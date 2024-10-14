package ru.gb.timesheet.security;

//@Component
//@RequiredArgsConstructor
//public class MyCustomUserDetailsService implements UserDetailsService {
//
//  // {SpringSecurity}
//  // ... @ UserDetailsService userDetailsService
//  // User[admin] --login--> [UserDetails userDetailsService.loadUserByUsername(login)]
//  // [UserDetails -> SecurityContext]
//
//  // В нашем случае юзеры хранятся в БД в таблице UserRepository
//  // Строго говоря, в этой реализации UserDetailsService можно загружать данные о пользователе из любого источника:
//  // внешний auth-service, ldap-сервер, ...
//  private final UserRepository userRepository;
//  private final UserRoleRepository userRoleRepository;
//
//  @Override
//  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
//    User user = userRepository.findByLogin(username)
//      .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
//
//    List<SimpleGrantedAuthority> userRoles = userRoleRepository.findByUserId(user.getId()).stream()
//      .map(it -> new SimpleGrantedAuthority(it.getRoleName()))
//      .toList();
//    return new org.springframework.security.core.userdetails.User(
//      user.getLogin(),
//      user.getPassword(),
//      userRoles
//    );
//  }
//
//}

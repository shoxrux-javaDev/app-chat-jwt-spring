package uz.spring.appownjwtpractice.service;


import org.springframework.context.annotation.Lazy;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import uz.spring.appownjwtpractice.dto.LoginDto;
import uz.spring.appownjwtpractice.dto.RegisterDto;
import uz.spring.appownjwtpractice.dto.Response;
import uz.spring.appownjwtpractice.entity.User;
import uz.spring.appownjwtpractice.enums.PermissionEnum;
import uz.spring.appownjwtpractice.enums.RoleName;
import uz.spring.appownjwtpractice.repository.RoleRepository;
import uz.spring.appownjwtpractice.repository.UserRepository;
import uz.spring.appownjwtpractice.security.JwtProvider;

import javax.transaction.Transactional;
import java.time.Instant;
import java.util.*;

@Service
@Transactional(rollbackOn = {NullPointerException.class})
public class RegisterService implements UserDetailsService {

    final UserRepository userRepository;
    final PasswordEncoder encoder;
    final RoleRepository roleRepository;
    final JavaMailSender mailSender;
    final JwtProvider jwtProvider;
    final AuthenticationManager authenticationManager;

    public RegisterService(UserRepository userRepository, PasswordEncoder encoder,
                           RoleRepository roleRepository, @Lazy JavaMailSender mailSender,
                           JwtProvider jwtProvider, @Lazy AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.encoder = encoder;
        this.roleRepository = roleRepository;
        this.mailSender = mailSender;
        this.jwtProvider = jwtProvider;
        this.authenticationManager = authenticationManager;
    }

    public Response userRegister(RegisterDto registerDto) {
        boolean trueOrFalse = userRepository.existsByEmail(registerDto.getEmail());
        if (trueOrFalse) return new Response("bunday email tarmoqda mavjud", false);
        User user = new User();
        user.setFirstname(registerDto.getFirstname());
        user.setLastname(registerDto.getLastname());
        user.setEmail(registerDto.getEmail());
        user.setPassword(encoder.encode(registerDto.getPassword()));
        user.setRoleList(Collections.singleton(roleRepository.findByRoleName(RoleName.USER)));
        if (user.getEmail().equals("shoxruxsindarov1996@gmail.com")) {
            Set<PermissionEnum> adminPermission = new HashSet<>(Arrays.asList(
                    PermissionEnum.ADD_MESSAGE, PermissionEnum.EDIT_MESSAGE, PermissionEnum.GET_MESSAGE,
                    PermissionEnum.DELETE_MY_MESSAGE, PermissionEnum.GET_OWN_INFORMATION,
                    PermissionEnum.EDIT_OWN_INFORMATION, PermissionEnum.GET_ONLINE_USERS
            ));
            user.setPermissions(adminPermission);
        }
        Set<PermissionEnum> permissionEnums = new HashSet<>(Arrays.asList(
                PermissionEnum.ADD_MESSAGE, PermissionEnum.EDIT_MESSAGE, PermissionEnum.GET_MESSAGE,
                PermissionEnum.DELETE_MY_MESSAGE, PermissionEnum.GET_OWN_INFORMATION,
                PermissionEnum.EDIT_OWN_INFORMATION
        ));
        user.setPermissions(permissionEnums);
        user.setEmailCode(UUID.randomUUID().toString());
        user.setPhoneNumber(registerDto.getPhoneNumber());
        sendEmail(user.getEmail(), user.getEmailCode());
        userRepository.save(user);
        return new Response("Mufoqiyatli ro'yhatdan o'ttingiz emailingiz tasdqiqlash havolasi jo'natildi", true);
    }

    private void sendEmail(String sendingEmail, String emailCode) {
        try {
            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setFrom("shoxruxsindarov@gmail.com");
            mailMessage.setTo(sendingEmail);
            mailMessage.setSubject("Account ni tasdiqlang");
            mailMessage.setText("http://localhost:8080/api/user/verifyEmail?emailCode=" + emailCode + "&email=" + sendingEmail);
            mailSender.send(mailMessage);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public Response verifyEmail(String email, String emailCode) {
        Optional<User> userOption = userRepository.findByEmailAndEmailCode(email, emailCode);
        if (userOption.isEmpty()) return new Response("accaount allaqachon faolashtirilgan", false);
        User user = userOption.get();
        user.setEnabled(true);
        user.setEmailCode(null);
        user.setActive(true);
        userRepository.save(user);
        return new Response("Account tasdiqlandi", true);
    }


    public Response userLogin(LoginDto loginDto) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                    loginDto.getEmail(),
                    loginDto.getPassword()));
            User user = (User) authentication.getPrincipal();
            String token = jwtProvider.generateToken(loginDto.getEmail(), user.getPermissions());
            return new Response("Token", true, token);
        } catch (BadCredentialsException badCredentialsException) {
            return new Response("parol yo login hato", false);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username + "topilmadi"));
    }

}

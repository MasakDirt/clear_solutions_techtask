package tech.task.clearsolutions;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.util.Assert;
import tech.task.clearsolutions.component.AuthTokenFilter;
import tech.task.clearsolutions.component.EntryPointJwt;
import tech.task.clearsolutions.component.JwtUtils;
import tech.task.clearsolutions.config.ApplicationConfig;
import tech.task.clearsolutions.config.SecurityConfig;
import tech.task.clearsolutions.controller.AuthController;
import tech.task.clearsolutions.controller.LogoutController;
import tech.task.clearsolutions.controller.UserController;
import tech.task.clearsolutions.exception.GlobalExceptionHandler;
import tech.task.clearsolutions.mapper.UserMapper;
import tech.task.clearsolutions.repository.RefreshTokenRepository;
import tech.task.clearsolutions.repository.RoleRepository;
import tech.task.clearsolutions.repository.TokenBlacklistRepository;
import tech.task.clearsolutions.repository.UserRepository;
import tech.task.clearsolutions.service.RefreshTokenService;
import tech.task.clearsolutions.service.RoleService;
import tech.task.clearsolutions.service.TokenBlacklistService;
import tech.task.clearsolutions.service.UserService;
import tech.task.clearsolutions.service.auth.AuthUserService;
import tech.task.clearsolutions.service.userdetails.CustomUserDetailsService;

@SpringBootTest
@ActiveProfiles("test")
class ClearSolutionsApplicationTests {
	private final UserService userService;
	private final UserRepository userRepository;
	private final GlobalExceptionHandler globalExceptionHandler;
	private final UserController userController;
	private final UserMapper userMapper;
	private final RefreshTokenRepository refreshTokenRepository;
	private final RoleRepository roleRepository;
	private final TokenBlacklistRepository tokenBlacklistRepository;
	private final RefreshTokenService refreshTokenService;
	private final RoleService roleService;
	private final TokenBlacklistService tokenBlacklistService;
	private final AuthUserService authUserService;
	private final CustomUserDetailsService customUserDetailsService;
	private final AuthController authController;
	private final LogoutController logoutController;
	private final SecurityConfig securityConfig;
	private final ApplicationConfig applicationConfig;
	private final AuthTokenFilter authTokenFilter;
	private final EntryPointJwt entryPointJwt;
	private final JwtUtils jwtUtils;

	@Autowired
	public ClearSolutionsApplicationTests(UserService userService, UserRepository userRepository,
										  GlobalExceptionHandler globalExceptionHandler,
										  UserController userController, UserMapper userMapper,
										  RefreshTokenRepository refreshTokenRepository,
										  RoleRepository roleRepository,
										  TokenBlacklistRepository tokenBlacklistRepository,
										  RefreshTokenService refreshTokenService,
										  RoleService roleService,
										  TokenBlacklistService tokenBlacklistService,
										  AuthUserService authUserService,
										  CustomUserDetailsService customUserDetailsService,
										  AuthController authController,
										  LogoutController logoutController,
										  SecurityConfig securityConfig,
										  ApplicationConfig applicationConfig,
										  AuthTokenFilter authTokenFilter,
										  EntryPointJwt entryPointJwt,
										  JwtUtils jwtUtils) {
		this.userService = userService;
		this.userRepository = userRepository;
		this.globalExceptionHandler = globalExceptionHandler;
		this.userController = userController;
		this.userMapper = userMapper;
		this.refreshTokenRepository = refreshTokenRepository;
		this.roleRepository = roleRepository;
		this.tokenBlacklistRepository = tokenBlacklistRepository;
		this.refreshTokenService = refreshTokenService;
		this.roleService = roleService;
		this.tokenBlacklistService = tokenBlacklistService;
		this.authUserService = authUserService;
		this.customUserDetailsService = customUserDetailsService;
		this.authController = authController;
		this.logoutController = logoutController;
		this.securityConfig = securityConfig;
		this.applicationConfig = applicationConfig;
		this.authTokenFilter = authTokenFilter;
		this.entryPointJwt = entryPointJwt;
		this.jwtUtils = jwtUtils;
	}

	@Test
	void contextLoads() {
		Assert.notNull(userService, "userService is null");
		Assert.notNull(userRepository, "userRepository is null");
		Assert.notNull(globalExceptionHandler, "globalExceptionHandler is null");
		Assert.notNull(userController, "userController is null");
		Assert.notNull(userMapper, "userMapper is null");
		Assert.notNull(refreshTokenRepository, "refreshTokenRepository is null");
		Assert.notNull(roleRepository, "roleRepository is null");
		Assert.notNull(tokenBlacklistRepository, "tokenBlacklistRepository is null");
		Assert.notNull(refreshTokenService, "refreshTokenService is null");
		Assert.notNull(roleService, "roleService is null");
		Assert.notNull(tokenBlacklistService, "tokenBlacklistService is null");
		Assert.notNull(authUserService, "authUserService is null");
		Assert.notNull(customUserDetailsService, "customUserDetailsService is null");
		Assert.notNull(authController, "authController is null");
		Assert.notNull(logoutController, "logoutController is null");
		Assert.notNull(securityConfig, "securityConfig is null");
		Assert.notNull(applicationConfig, "applicationConfig is null");
		Assert.notNull(authTokenFilter, "authTokenFilter is null");
		Assert.notNull(entryPointJwt, "entryPointJwt is null");
		Assert.notNull(jwtUtils, "jwtUtils is null");
	}

}

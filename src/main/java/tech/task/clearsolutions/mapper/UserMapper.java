package tech.task.clearsolutions.mapper;

import org.mapstruct.*;
import tech.task.clearsolutions.domain.User;
import tech.task.clearsolutions.dto.UserCreateRequest;
import tech.task.clearsolutions.dto.UserResponse;
import tech.task.clearsolutions.dto.UserTokenResponse;
import tech.task.clearsolutions.dto.UserUpdateRequest;

import java.util.List;

@Mapper(componentModel = "spring",
        nullValueMapMappingStrategy = NullValueMappingStrategy.RETURN_NULL)
public interface UserMapper {

    User getDomainFromCreateRequest(UserCreateRequest userCreateRequest);

    @Mappings({
            @Mapping(target = "city", expression = "java(user.getAddress() != null ? user.getAddress().getCity() : null)"),
            @Mapping(target = "street", expression = "java(user.getAddress() != null ? user.getAddress().getStreet() : null)"),
            @Mapping(target = "number", expression = "java(user.getAddress() != null ? user.getAddress().getNumber() : null)"),
            @Mapping(target = "apartmentNumber", expression = "java(user.getAddress() != null ? user.getAddress().getApartmentNumber() : null)")
    })
    UserResponse getResponseFromDomain(User user);

    @Mapping(target = "password", source = "newPassword")
    User getDomainFromUpdateRequest(UserUpdateRequest userUpdateRequest);

    List<UserResponse> getResponseListFromDomain(List<User> userList);

    @Mapping(target = "refreshToken", expression = "java(user.getRefreshToken().getToken())")
    UserTokenResponse getTokenResponseFromDomain(User user, String accessToken);

}

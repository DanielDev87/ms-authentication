package co.com.bancolombia.r2dbc.mapper;

import co.com.bancolombia.model.user.User;
import co.com.bancolombia.r2dbc.data.UserData;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring")
public interface UserMapper {
    User toDomain(UserData userData);
    UserData toData(User user);
}

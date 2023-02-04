package ch.zhaw.card2brain.objectmapper;

import ch.zhaw.card2brain.TestData.TestDataGenerator;
import ch.zhaw.card2brain.dto.LoginDto;
import ch.zhaw.card2brain.model.User;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class LoginMapperTest {


    @Test
    void toUser() {
        //arrange
        LoginMapper loginMapper = new LoginMapper();
        User user = TestDataGenerator.GET_DEFAULT_USER();
        LoginDto loginDto = new LoginDto(user.getMailAddress(), user.getPassword());

        //act
        User mappedUser = loginMapper.toUser(loginDto);
        //assert
        Assertions.assertEquals(user.getMailAddress(), mappedUser.getMailAddress());
        Assertions.assertEquals(user.getPassword(), mappedUser.getPassword());

    }
}

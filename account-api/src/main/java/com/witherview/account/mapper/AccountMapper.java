package com.witherview.account.mapper;

import com.witherview.account.dto.AccountDTO;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.mysql.entity.User;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountDTO.ResponseLogin toResponseLogin(User user);
    AccountDTO.ResponseRegister toRegister(User user);
    AccountDTO.ResponseMyInfo toResponseMyInfo(User user);
    AccountDTO.UpdateMyInfoDTO toUpdateMyInfo(User user);
    AccountDTO.UploadProfileDTO toUploadProfile(User user);
    AccountDTO.StudyRoomDTO[] toResponseDtoArray(List<StudyRoom> studyRoomList);

}

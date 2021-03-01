package com.witherview.utils;

import com.witherview.account.AccountDTO;
import com.witherview.database.entity.StudyRoom;
import com.witherview.database.entity.User;
import com.witherview.groupPractice.GroupStudy.GroupStudyDTO;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

import java.util.List;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountDTO.ResponseRegister toRegister(User user);
    AccountDTO.ResponseMyInfo toResponseMyInfo(User user);
    AccountDTO.UpdateMyInfoDTO toUpdateMyInfo(User user);
    AccountDTO.UploadProfileDTO toUploadProfile(User user);
    AccountDTO.StudyRoomDTO[] toResponseDtoArray(List<StudyRoom> studyRoomList);

}

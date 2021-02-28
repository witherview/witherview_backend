package com.witherview.utils;

import com.witherview.account.AccountDTO;
import com.witherview.database.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedSourcePolicy = ReportingPolicy.IGNORE)
public interface AccountMapper {

    AccountDTO.ResponseRegister toRegister(User user);
    AccountDTO.ResponseMyInfo toResponseMyInfo(User user);
    AccountDTO.UpdateMyInfoDTO toUpdateMyInfo(User user);
    AccountDTO.UploadProfileDTO toUploadProfile(User user);

}

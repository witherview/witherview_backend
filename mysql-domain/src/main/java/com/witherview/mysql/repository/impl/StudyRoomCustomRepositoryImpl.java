package com.witherview.mysql.repository.impl;

import static com.witherview.mysql.entity.QStudyRoom.studyRoom;
import static com.witherview.mysql.entity.QStudyRoomParticipant.studyRoomParticipant;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.witherview.mysql.entity.StudyRoom;
import com.witherview.mysql.repository.StudyRoomCustomRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class StudyRoomCustomRepositoryImpl implements StudyRoomCustomRepository {
  private final JPAQueryFactory queryFactory;

  @Override
  public List<StudyRoom> findRooms(String userId, String industry, String job, String keyword, Long lastId, int pageSize) {

    return queryFactory
        .selectDistinct(studyRoom)
        .from(studyRoomParticipant)
        .join(studyRoomParticipant.studyRoom, studyRoom)
        .on(studyRoomParticipant.user.id.notEqualsIgnoreCase(userId))
        .where(findKeyword(keyword), eqIndustry(industry), eqJob(job), gtCurrentId(lastId), availableRooms())
        .limit(pageSize)
        .fetch();
  }

  private BooleanExpression availableRooms() {
    return studyRoom.maxUserCnt.subtract(studyRoom.nowUserCnt).goe(1);
  }

  private BooleanExpression gtCurrentId(Long lastId) {
    if(lastId == null) return null;
    return studyRoom.id.gt(lastId);
  }

  private BooleanExpression eqIndustry(String industry) {
    if(StringUtils.isEmpty(industry) || industry.equals(" ")) return null;
    return studyRoom.industry.equalsIgnoreCase(industry);
  }

  private BooleanExpression eqJob(String job) {
    if(StringUtils.isEmpty(job) || job.equals(" ")) return null;
    return studyRoom.job.equalsIgnoreCase(job);
  }

  private BooleanExpression findKeyword(String keyword) {
    if(StringUtils.isEmpty(keyword) || keyword.equals(" ")) return null;
    return studyRoom.title.containsIgnoreCase(keyword).or(studyRoom.description.containsIgnoreCase(keyword));
  }
}

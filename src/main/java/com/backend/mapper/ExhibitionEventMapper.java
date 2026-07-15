package com.backend.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.backend.entity.ExhibitionEventEntity;

@Mapper
public interface ExhibitionEventMapper {

    List<ExhibitionEventEntity> findAll();

    List<ExhibitionEventEntity> findUpcomingPublished(@Param("limit") Integer limit);

    ExhibitionEventEntity findByEventId(@Param("eventId") Long eventId);

    int insert(ExhibitionEventEntity event);

    int update(@Param("eventId") Long eventId, @Param("event") ExhibitionEventEntity event);

    int delete(@Param("eventId") Long eventId);
}

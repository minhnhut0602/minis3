package com.pksprojects.minis3.repositories;

import com.pksprojects.minis3.models.metadata.MetaData;
import com.pksprojects.minis3.models.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.jpa.repository.Temporal;
import org.springframework.stereotype.Repository;

import javax.persistence.TemporalType;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Created by PKS on 4/8/17.
 */
@Repository
public interface MetaDataRepository extends JpaRepository<MetaData, String>{

    public List<MetaData> findAllByUserOrderByTimeCreatedDesc(User user, Pageable pageable);

    public List<OnlyNameAndUserId> findAllByTimeCreatedBetweenOrderByUserAsc(LocalDateTime start, LocalDateTime end);

    public MetaData findOneByUserAndName(User user, String name);

}

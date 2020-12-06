package com.chiropoint.backend.domain.repositories;

import com.chiropoint.backend.domain.models.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

import java.util.List;

@NoRepositoryBean
public interface StatisticRepository<T extends Statistic> extends JpaRepository<T, Integer> {

    List<T> findAllByOfficeIdAndCategory(Integer officeId, String category);

    List<T> findAllByOfficeId(Integer officeId);

}

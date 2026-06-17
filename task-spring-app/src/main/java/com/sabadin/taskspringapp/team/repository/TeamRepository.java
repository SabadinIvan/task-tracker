package com.sabadin.taskspringapp.team.repository;

import com.sabadin.taskspringapp.team.model.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TeamRepository extends JpaRepository<Team, Long> {
}

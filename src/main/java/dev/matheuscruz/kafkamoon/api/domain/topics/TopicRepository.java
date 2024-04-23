package dev.matheuscruz.kafkamoon.api.domain.topics;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface TopicRepository extends JpaRepository<Topic, String>, PagingAndSortingRepository<Topic, String> {
}

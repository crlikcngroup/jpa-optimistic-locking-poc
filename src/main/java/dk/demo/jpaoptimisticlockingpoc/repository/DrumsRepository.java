package dk.demo.jpaoptimisticlockingpoc.repository;

import dk.demo.jpaoptimisticlockingpoc.entity.Drums;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DrumsRepository extends JpaRepository<Drums, Long> {
}

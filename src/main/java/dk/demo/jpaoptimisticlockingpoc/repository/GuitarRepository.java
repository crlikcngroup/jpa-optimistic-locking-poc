package dk.demo.jpaoptimisticlockingpoc.repository;

import dk.demo.jpaoptimisticlockingpoc.entity.Guitar;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GuitarRepository extends JpaRepository<Guitar, Long> {
}

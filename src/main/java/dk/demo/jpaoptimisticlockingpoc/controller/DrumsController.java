package dk.demo.jpaoptimisticlockingpoc.controller;

import dk.demo.jpaoptimisticlockingpoc.converter.DrumsToDtoConverter;
import dk.demo.jpaoptimisticlockingpoc.converter.DtoToDrumsConverter;
import dk.demo.jpaoptimisticlockingpoc.dto.DrumsDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Drums;
import dk.demo.jpaoptimisticlockingpoc.repository.DrumsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
@RequestMapping(DrumsController.ENDPOINT)
public class DrumsController extends AbstractController<Drums, DrumsDto> {

    public static final String ENDPOINT = "/drums";

    final private DrumsRepository drumsRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public DrumsController(final DrumsRepository drumsRepository,
                           final DrumsToDtoConverter toDtoConverter,
                           final DtoToDrumsConverter fromDtoConverter) {
        this.drumsRepository = drumsRepository;

        setToDtoConverter(toDtoConverter);
        setFromDtoConverter(fromDtoConverter);
    }

    @PutMapping("/{id}")
    public DrumsDto update(@PathVariable(name = "id") final long id,
                           @RequestBody final DrumsDto dto) {
        // Entity must be detached
        Drums drums = new Drums();

        getFromDtoConverter().convert(dto, drums);
        final Drums savedEntity = drumsRepository.save(drums);

        return getToDtoConverter().convert(savedEntity);
    }

    @PutMapping("/detach/{id}")
    public DrumsDto updateLoadAndDetach(@PathVariable(name = "id") final long id,
                                        @RequestBody final DrumsDto dto) {
        final Drums entity = getOne(id);

        // This needs to be manually called for locking to work
        entityManager.detach(entity);

        getFromDtoConverter().convert(dto, entity);
        final Drums savedEntity = drumsRepository.save(entity);

        return getToDtoConverter().convert(savedEntity);
    }

    @PutMapping("/attached/{id}")
    public DrumsDto updateAttached(@PathVariable(name = "id") final long id,
                                   @RequestBody final DrumsDto dto) {
        final Drums entity = getOne(id);

        // Entity is still attached. No lock exception will be thrown.
        getFromDtoConverter().convert(dto, entity);
        final Drums savedEntity = drumsRepository.save(entity);

        return getToDtoConverter().convert(savedEntity);
    }

    @Override
    public Drums getOne(final long id) {
        return drumsRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public List<Drums> getAll() {
        return drumsRepository.findAll();
    }
}

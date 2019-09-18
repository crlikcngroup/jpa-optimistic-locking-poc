package dk.demo.jpaoptimisticlockingpoc.controller;

import dk.demo.jpaoptimisticlockingpoc.converter.DtoToGuitarConverter;
import dk.demo.jpaoptimisticlockingpoc.converter.GuitarToDtoConverter;
import dk.demo.jpaoptimisticlockingpoc.dto.GuitarDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Guitar;
import dk.demo.jpaoptimisticlockingpoc.repository.GuitarRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(GuitarController.ENDPOINT)
public class GuitarController extends AbstractController<Guitar, GuitarDto> {

    public static final String ENDPOINT = "/guitars";

    final private GuitarRepository guitarRepository;

    @Autowired
    public GuitarController(final GuitarRepository guitarRepository,
                            final GuitarToDtoConverter toDtoConverter,
                            final DtoToGuitarConverter fromDtoConverter) {
        this.guitarRepository = guitarRepository;

        setToDtoConverter(toDtoConverter);
        setFromDtoConverter(fromDtoConverter);
    }

    @Override
    public Guitar getOne(final long id) {
        return guitarRepository.findById(id).orElseThrow(() -> new RuntimeException("Entity not found"));
    }

    @Override
    public List<Guitar> getAll() {
        return guitarRepository.findAll();
    }
}

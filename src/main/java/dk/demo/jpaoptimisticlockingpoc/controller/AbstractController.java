package dk.demo.jpaoptimisticlockingpoc.controller;

import dk.demo.jpaoptimisticlockingpoc.converter.AbstractFromDtoConverter;
import dk.demo.jpaoptimisticlockingpoc.converter.AbstractToDtoConverter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Collectors;

abstract public class AbstractController <ENTITY, DTO>{

    private AbstractToDtoConverter<ENTITY, DTO> toDtoConverter;
    private AbstractFromDtoConverter<DTO, ENTITY> fromDtoConverter;

    protected void setToDtoConverter(final AbstractToDtoConverter<ENTITY, DTO> converter) {
        this.toDtoConverter = converter;
    }

    protected AbstractToDtoConverter<ENTITY, DTO> getToDtoConverter() {
        return toDtoConverter;
    }

    protected void setFromDtoConverter(final AbstractFromDtoConverter<DTO, ENTITY> converter) {
        this.fromDtoConverter = converter;
    }

    protected AbstractFromDtoConverter<DTO, ENTITY> getFromDtoConverter() {
        return fromDtoConverter;
    }

    @GetMapping("/{id}")
    public DTO detail(@PathVariable(name = "id") final long id) {
        final ENTITY entity = getOne(id);

        return toDtoConverter.convert(entity);
    }

    @GetMapping("")
    public List<DTO> list() {
        final List<ENTITY> entities = getAll();

        return entities.stream()
                .map(toDtoConverter::convert)
                .collect(Collectors.toList());
    }

    abstract public ENTITY getOne(final long id);

    abstract public List<ENTITY> getAll();
}

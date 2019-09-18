package dk.demo.jpaoptimisticlockingpoc.converter;

import dk.demo.jpaoptimisticlockingpoc.dto.DrumsDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Drums;
import org.springframework.stereotype.Service;

@Service
public class DtoToDrumsConverter extends AbstractFromDtoConverter<DrumsDto, Drums> {
    public void convert(final DrumsDto dto, final Drums entity) {
        entity.setId(dto.getId());
        entity.setColor(dto.getColor());
        entity.setDecibels(dto.getDecibels());
        entity.setManufacturer(dto.getManufacturer());
        entity.setVersion(dto.getVersion());
    }
}

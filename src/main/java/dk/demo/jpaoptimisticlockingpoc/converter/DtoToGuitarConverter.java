package dk.demo.jpaoptimisticlockingpoc.converter;

import dk.demo.jpaoptimisticlockingpoc.dto.GuitarDto;
import dk.demo.jpaoptimisticlockingpoc.entity.Guitar;
import org.springframework.stereotype.Service;

@Service
public class DtoToGuitarConverter extends AbstractFromDtoConverter<GuitarDto, Guitar>{
    public void convert(final GuitarDto dto, final Guitar entity) {
        entity.setId(dto.getId());
        entity.setYear(dto.getYear());
        entity.setColor(dto.getColor());
        entity.setType(dto.getType());
        entity.setVersion(dto.getVersion());
    }
}
